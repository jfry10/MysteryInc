package clueless;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import clueless.Network.ChatMessage;
import clueless.Network.RegisterName;
import clueless.Network.UpdateNames;
import clueless.Network.BeginGame;
import clueless.Network.BeginTurn;
import clueless.Network.DealCard;
import clueless.Network.EndTurn;
import clueless.Network.GetSuspects;
import clueless.Network.SetSuspect;
import clueless.Network.MoveToken;
import clueless.Network.PlayerTurn;
import clueless.Network.RegisterRequest;
import clueless.Network.RegisterResponse;
import clueless.Network.SuggestionDisprove;
import clueless.Network.SuggestionAsk;
import clueless.Network.SuspectResponse;
import clueless.Network.EndSuggestion;
import clueless.Network.DisplayGUI;

public class GameExecutive
{
	Server server;
	
	Suggestion currentSuggestion;
	PlayerInfo playerMakingSuggestion;

	LinkedHashMap<String, Integer> suspectConnectionMap;
	LinkedHashMap<Integer, PlayerInfo> playerInfoMap;
	
	ArrayList<Player> players;
	
	CardDeck cardDeck;
	CaseFile caseFile;
	
	ArrayList<Integer> forfeitPlayerList;
	
	Location[][] gameBoard;
	
	

	public GameExecutive() throws IOException {

		initSuspectConnectionMap();
		
		Kryo kryo = new Kryo();
		kryo.setReferences(true);
		KryoSerialization serialization = new KryoSerialization(kryo);

		server = new Server(13684, 2048, serialization) {
			
			protected Connection newConnection ()
			{
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new CluelessConnection();
			}
		};

		
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				// We know all connections for this server are actually ChatConnections.
				CluelessConnection conn = (CluelessConnection)c;
				
				Integer playerID = conn.getID();

				if (object instanceof RegisterName) {
					// Ignore the object if a client has already registered a name. This is
					// impossible with our client, but a hacker could send messages at any time.
					if (conn.playerName != null) return;
					// Ignore the object if the name is invalid.
					String name = ((RegisterName)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					// Store the name on the connection.
					conn.playerName = name;
					// Send a "connected" message to everyone except the new client.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = name + " connected.";
					server.sendToAllExceptTCP(conn.getID(), chatMessage);
					// Send everyone a new list of connection names.
					updateNames();
					return;
				}

				if (object instanceof ChatMessage)
				{
					// Ignore the object if a client tries to chat before registering a name.
					if (conn.playerName == null) return;
					ChatMessage chatMessage = (ChatMessage)object;
					// Ignore the object if the chat message is invalid.
					String message = chatMessage.text;
					if (message == null) return;
					message = message.trim();
					if (message.length() == 0) return;
					// Prepend the connection's name and send to everyone.
					chatMessage.text = conn.playerName + ": " + message;
					server.sendToAllTCP(chatMessage);
					return;
				}
				
				// The Client sends us a Suggestion, pass on to other clients
				if (object instanceof Suggestion)
				{
					currentSuggestion = (Suggestion)object;
					playerMakingSuggestion = playerInfoMap.get(conn.getID());
					server.sendToTCP(playerMakingSuggestion.playerToLeft.playerId, currentSuggestion);
				}
				
				if (object instanceof SuggestionDisprove) {
					if(((SuggestionDisprove)object).card == null) {
						// cannot disprove -- see if next player can disprove
						if(playerInfoMap.get(playerID).playerToLeft.equals(playerMakingSuggestion)) {
							// no one disproved, player can make an accusation
							server.sendToTCP(playerMakingSuggestion.playerId, new Accusation());
							playerMakingSuggestion = null;
							currentSuggestion = null;
						} else {
							server.sendToTCP(playerInfoMap.get(playerID).playerToLeft.playerId, currentSuggestion);
						}
					} else {
						server.sendToTCP(playerMakingSuggestion.playerId, object);
						server.sendToTCP(playerMakingSuggestion.playerId, new EndTurn());
						playerMakingSuggestion = null;
						currentSuggestion = null;
					}
				}
				
				// The Client sends us an Accusation, pass on to other clients
				if (object instanceof Accusation)
				{
					PlayerInfo currentPlayer = playerInfoMap.get(playerID);
					
					StringBuilder accusationSB = new StringBuilder();
					accusationSB.append(currentPlayer.suspectName);
					accusationSB.append(" has made the accusation: ");
					accusationSB.append(new ChatMessage(((Accusation)object).toString()));
					
					if(caseFile.isAccusationValid(((Accusation)object).room,
							((Accusation)object).weapon,
							((Accusation)object).suspect)) {
						// A WINRAR IS YOU!!1!1!!!
						StringBuilder messageSB = new StringBuilder();
						messageSB.append(currentPlayer.suspectName);
						messageSB.append("'s accusation of ");
						messageSB.append(((Accusation)object).toString());
						messageSB.append(" was correct! ");
						messageSB.append(currentPlayer.suspectName);
						messageSB.append(" has won the game! ");
						server.sendToAllTCP(new ChatMessage(messageSB.toString()));
						endGame();
					} else {
						// bzzzzzzt. nope.
						StringBuilder messageSB = new StringBuilder();
						messageSB.append(currentPlayer.suspectName);
						messageSB.append("'s accusation of ");
						messageSB.append(((Accusation)object).toString());
						messageSB.append(" was incorrect. ");
						messageSB.append(currentPlayer.suspectName);
						messageSB.append("'s future turns are now forfeit.");
						server.sendToAllTCP(new ChatMessage(messageSB.toString()));
						
						// you get added to THE LIST
						forfeitPlayerList.add(playerID);
						
						// next player is up
						prodNextPlayer(currentPlayer);
					}
					
				}
				
				// The Client sends us a Card, determine what to do with it
				if (object instanceof Card)
				{
					Card card = (Card)object;
					if (card.getName() == "null");
				}
				
				// The Client sends us a String, determine what to do with it
				if (object instanceof String)
				{
					String s = (String)object;

					return;
				}

                if(object instanceof RegisterRequest)
				{
	        		RegisterResponse regResponse = new RegisterResponse();
	        		regResponse.clientId = conn.getID();
	        		regResponse.suspectNames = getAvailableSuspects();
	        		server.sendToTCP(conn.getID(), regResponse);
	        	}
                
                if(object instanceof GetSuspects) {
                	SuspectResponse response = new SuspectResponse();
                	response.suspectNames = getAvailableSuspects();
                	server.sendToTCP(conn.getID(), response);
                }
                
                if(object instanceof SetSuspect) {
                	String suspectName = ((SetSuspect) object).selectedSuspect;
                	suspectConnectionMap.put(suspectName, playerID);
                	server.sendToTCP(playerID, new ChatMessage("You are now " + suspectName + "!"));
                }

                /*
				if (object instanceof SuspectRequest)
				{
	        		String requestedSuspect = ((SuspectRequest)object).requestedSuspect;
	        		SuspectResponse response = new SuspectResponse();
	        		if(suspectConnectionMap.get(requestedSuspect) == null) {
	        			suspectConnectionMap.put(requestedSuspect, conn.getID());
	        			response.success = true;
	        			response.selectedSuspectName = requestedSuspect;
	        		} else {
	        			response.success = false;
	        			response.suspectNames = getAvailableSuspects();
	        		}
	        		server.sendToTCP(conn.getID(), response);
	        	}
	        	*/

				if (object instanceof BeginGame)
				{
					int playerCounter = 0;
					for(String suspect : suspectConnectionMap.keySet()) {
						if(suspectConnectionMap.get(suspect) != null) {
							playerCounter++;
						}
					}
					
					if(playerCounter > 2) {
						startGame();
					} else {
						server.sendToTCP(playerID, new ChatMessage("Not enough players to start game. You need " + (3 - playerCounter) + " more player(s)."));
					}
	        	}

				if (object instanceof EndTurn) {
	        		
	        	}

				if (object instanceof MoveToken) {
					int direction = ((MoveToken)object).direction;
					PlayerInfo playerInfo = playerInfoMap.get(playerID);
	        		switch(direction) {
	        		case Constants.DIR_UP:
	        			Gameboard.moveUp(gameBoard, playerInfo.player);
	        			break;
	        		case Constants.DIR_DOWN:
	        			Gameboard.moveDown(gameBoard, playerInfo.player);
	        			break;
	        		case Constants.DIR_LEFT:
	        			Gameboard.moveLeft(gameBoard, playerInfo.player);
	        			break;
	        		case Constants.DIR_RIGHT:
	        			Gameboard.moveRight(gameBoard, playerInfo.player);
	        			break;
	        		case Constants.DIR_PASSAGE:
	        			Gameboard.takePassage(gameBoard, playerInfo.player);
	        			break;
	        		}

	        	    if(playerInfo.player.positionOnBoard instanceof Room) {
	        	    	// Player has entered room, is allowed to make a suggestion
	        	    	server.sendToTCP(playerInfo.playerId, new SuggestionAsk());
	        	    } else {
	        	    	server.sendToTCP(playerInfo.playerId, new EndTurn());
	        	    	
	        	    	PlayerInfo playerLeftInfo = playerInfoMap.get(playerInfo.playerToLeft.playerId);
	        	    	Location playerLocation = playerLeftInfo.player.positionOnBoard;
	        	    	PlayerTurn playerTurn = new PlayerTurn();
	        	    	if(playerLocation.hasUp()) {
	        	    		playerTurn.up = true;
	        	    	}
	        	    	if(playerLocation.hasDown()) {
	        	    		playerTurn.down = true;
	        	    	}
	        	    	if(playerLocation.hasLeft()) {
	        	    		playerTurn.left = true;
	        	    	}
	        	    	if(playerLocation.hasRight()) {
	        	    		playerTurn.right = true;
	        	    	}
	        	    	if(playerLocation instanceof Room && ((Room) playerLocation).hasSecretPassage()) {
	        	    		playerTurn.passage = true;
	        	    	} 
	        	    	
	        	    	server.sendToTCP(playerLeftInfo.playerId, playerTurn);
	        	    }
	        	}
			}

			
			public void disconnected (Connection c) {
				CluelessConnection connection = (CluelessConnection)c;
				if (connection.playerName != null) {
					// Announce to everyone that someone (with a registered name) has left.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = connection.playerName + " disconnected.";
					server.sendToAllTCP(chatMessage);
					updateNames();
				}
			}
		});
		server.bind(Network.port);
		server.start();

		// Open a window to provide an easy way to stop the server.
		JFrame frame = new JFrame("Clueless Server");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed (WindowEvent evt) {
				server.stop();
			}
		});
		
		frame.getContentPane().add(new JLabel("Close to stop the Clueless server."));
		frame.setSize(320, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	void initSuspectConnectionMap() {
		suspectConnectionMap = new LinkedHashMap<String, Integer>(Constants.SUSPECTS.length);
		for(int i=0; i<Constants.SUSPECTS.length; i++) {
			suspectConnectionMap.put(Constants.SUSPECTS[i], null);
		}
	}

	void startGame() {
				
		initializePlayerInfoObjects();
		
		gameBoard = Gameboard.createNewBoard(players.toArray(new Player[players.size()]));
		
		//server.sendToAllTCP(generateGUIDisplayObject());
		/*
		GUIDisplay guiDisplay = generateGUIDisplayObject();
		
		for(Integer connId : playerInfoMap.keySet()) {
			try {
				server.sendToTCP(connId, guiDisplay);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		*/
		
		Player[] playerArray = getPlayerArrayForGUIDisplay();
		server.sendToAllTCP(new DisplayGUI(playerArray));
		
		forfeitPlayerList = new ArrayList<Integer>();
		
		distributeCards();
		
		Integer[] playerInfoKeys = playerInfoMap.keySet().toArray(new Integer[playerInfoMap.size()]);
		
		PlayerInfo firstPlayer = playerInfoMap.get(playerInfoKeys[0]);
		Location firstPlayerLocation = firstPlayer.player.positionOnBoard;
    	PlayerTurn playerTurn = new PlayerTurn();
    	if(firstPlayerLocation.hasUp()) {
    		playerTurn.up = true;
    	}
    	if(firstPlayerLocation.hasDown()) {
    		playerTurn.down = true;
    	}
    	if(firstPlayerLocation.hasLeft()) {
    		playerTurn.left = true;
    	}
    	if(firstPlayerLocation.hasRight()) {
    		playerTurn.right = true;
    	}
    	if(firstPlayerLocation instanceof Room && ((Room) firstPlayerLocation).hasSecretPassage()) {
    		playerTurn.passage = true;
    	}
		
		server.sendToTCP(playerInfoKeys[0], playerTurn);
	}
	
	// Creates a singly linked list of player info objects that are linked by 
	// the player "on the left" of the current player so that we can iterate
	// through the players in order for dealing and working through suggestions
	void initializePlayerInfoObjects() {
		playerInfoMap = new LinkedHashMap<Integer, PlayerInfo>();
		players = new ArrayList<Player>();
		
		PlayerInfo first = null;
		PlayerInfo previous = null;
		PlayerInfo current = null;
		for(int i=0; i<Constants.SUSPECTS.length; i++) {
			Integer playerId = suspectConnectionMap.get(Constants.SUSPECTS[i]);
			if(playerId != null) {
				current = new PlayerInfo();
				current.playerId = playerId;
				current.suspectName = Constants.SUSPECTS[i];
				current.player = new Player(Constants.SUSPECTS[i]);
				players.add(current.player);
				
				if(previous != null) {
					previous.playerToLeft = current;
				}
				previous = current;
				if(first == null) {
					first = current;
				}
				
				playerInfoMap.put(playerId, current);
			}
		}
		current.playerToLeft = first;
	}

	
	void distributeCards()
	{
		cardDeck = new CardDeck();
		caseFile = new CaseFile();
		
		caseFile.addCard(cardDeck.drawRandomCard(Constants.WEAPON_CARD));
		caseFile.addCard(cardDeck.drawRandomCard(Constants.SUSPECT_CARD));
		caseFile.addCard(cardDeck.drawRandomCard(Constants.ROOM_CARD));
		
		cardDeck.randomizeRemaining();
		
		Card nextCard = null;
		
		// Get connection Id of first player
		Integer[] connIds =  playerInfoMap.keySet().toArray(new Integer[playerInfoMap.size()]);
		// get player info object for first player
		PlayerInfo currentPlayer = playerInfoMap.get(connIds[0]);
		
		// deal cards around the horn
		while((nextCard = cardDeck.drawCard()) != null) {
			DealCard dealCard = new DealCard();
			dealCard.card = nextCard;
			server.sendToTCP(currentPlayer.playerId, dealCard);
			currentPlayer = currentPlayer.playerToLeft;
		}
	
	}
	
	GUIDisplay generateGUIDisplayObject() {
		
		Player[] playerArray = new Player[6];
		
		for(Player player : players) {
			switch(player.suspectName) {
			case Constants.MISS_SCARLET_STR:
				playerArray[Constants.MISS_SCARLET] = player;
				break;
			case Constants.COL_MUSTARD_STR:
				playerArray[Constants.COL_MUSTARD] = player;
				break;
			case Constants.MRS_WHITE_STR:
				playerArray[Constants.MRS_WHITE] = player;
				break;
			case Constants.MR_GREEN_STR:
				playerArray[Constants.MR_GREEN] = player;
				break;
			case Constants.MRS_PEACOCK_STR:
				playerArray[Constants.MRS_PEACOCK] = player;
				break;
			case Constants.PROF_PLUM_STR:
				playerArray[Constants.PROF_PLUM] = player;
				break;
			}
		}
		
		GUIDisplay guiDisplay = new GUIDisplay(playerArray);
		
		return guiDisplay;
	}
	
	Player[] getPlayerArrayForGUIDisplay() {
		Player[] playerArray = new Player[6];
		
		for(Player player : players) {
			switch(player.suspectName) {
			case Constants.MISS_SCARLET_STR:
				playerArray[Constants.MISS_SCARLET] = player;
				break;
			case Constants.COL_MUSTARD_STR:
				playerArray[Constants.COL_MUSTARD] = player;
				break;
			case Constants.MRS_WHITE_STR:
				playerArray[Constants.MRS_WHITE] = player;
				break;
			case Constants.MR_GREEN_STR:
				playerArray[Constants.MR_GREEN] = player;
				break;
			case Constants.MRS_PEACOCK_STR:
				playerArray[Constants.MRS_PEACOCK] = player;
				break;
			case Constants.PROF_PLUM_STR:
				playerArray[Constants.PROF_PLUM] = player;
				break;
			}
		}
		
		return playerArray;
	}
	
	String[] getAvailableSuspects() {
		ArrayList<String> suspectList = new ArrayList<String>();
		
		for(String suspect : suspectConnectionMap.keySet()) {
			if(suspectConnectionMap.get(suspect) == null) {
				suspectList.add(suspect);
			}
		}
		
		return suspectList.toArray(new String[suspectList.size()]);
	}

	void updateNames () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList names = new ArrayList(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			CluelessConnection connection = (CluelessConnection)connections[i];
			names.add(connection.playerName);
		}
		// Send the names to everyone.
		UpdateNames updateNames = new UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAllTCP(updateNames);
	}
	
	void prodNextPlayer(PlayerInfo currentPlayer) {
		Integer nextPlayerId = currentPlayer.playerToLeft.playerId;
		while(forfeitPlayerList.contains(nextPlayerId)) {
			PlayerInfo nextPlayer = playerInfoMap.get(nextPlayerId);
			String message = nextPlayer.suspectName + " forfeits their turn.";
			server.sendToAllTCP(new ChatMessage(message));
			nextPlayerId = nextPlayer.playerToLeft.playerId;
		}
		server.sendToTCP(nextPlayerId, new BeginTurn());
	}
	
	void endGame() {
		
	}
	
	// This holds per connection state.
	static class CluelessConnection extends Connection {
		public String playerName;
	}

	public static void main (String[] args) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		new GameExecutive();
	}
}

class PlayerInfo {
	public String suspectName;
	public Integer playerId;
	public PlayerInfo playerToLeft;
	public Player player;
	
	@Override
	public boolean equals(Object obj) {
		return playerId.equals(((PlayerInfo)obj).playerId);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Suspect Name = " + suspectName + "\n");
		sb.append("playerId = " + playerId + "\n");
		sb.append("Player to left = " + playerToLeft.suspectName + "\n");
		return sb.toString();
	}
}

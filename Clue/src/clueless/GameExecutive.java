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
	
	Player[] players;
	
	CardDeck cardDeck;
	CaseFile caseFile;
	
	ArrayList<Integer> forfeitPlayerList;
	
	Gameboard gameBoard;
	
	

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
				
				// The Client sends us a Suggestion, pass on to the first client
				if (object instanceof Suggestion)
				{
					currentSuggestion = (Suggestion)object;
					playerMakingSuggestion = playerInfoMap.get(conn.getID());
					
					// First, move the Suggestion player to the room
					gameBoard.suspectMove(currentSuggestion.suspect, currentSuggestion.room);
					
					// Now, send out the updated board
	        	    		server.sendToAllTCP(new DisplayGUI(gameBoard));

	        	    		// The player on the left gets the first guess
					server.sendToTCP(playerMakingSuggestion.playerToLeft.playerId, currentSuggestion);
					return;
				}

				// We receive the (next) suggestionDisprove
				if (object instanceof SuggestionDisprove)
				{
					// Sent by client, means that the player could not disprove
					if(((SuggestionDisprove)object).card == null)
					{
						// cannot disprove -- see if next player can disprove
						if(playerInfoMap.get(playerID).playerToLeft.equals(playerMakingSuggestion))
						{
							// no one disproved, player can make an accusation
							server.sendToTCP(playerMakingSuggestion.playerId, new Accusation());
							playerMakingSuggestion = null;
							currentSuggestion = null;
						}
						else
						{
							server.sendToTCP(playerInfoMap.get(playerID).playerToLeft.playerId, currentSuggestion);
						}
					} 
					// SuggestionDisprove returned a card, send it to the playerMakingSuggestion
					else 
					{
						server.sendToTCP(playerMakingSuggestion.playerId, object);
						server.sendToTCP(playerMakingSuggestion.playerId, new EndTurn());
						playerMakingSuggestion = null;
						currentSuggestion = null;
					}
					return;
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
							((Accusation)object).suspect))
					{
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
					}
					else
					{
						// bzzzzzzt. nope.
						// accusation is wrong. Ban the player
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
					return;
				}

                if(object instanceof RegisterRequest)
				{
		        		RegisterResponse regResponse = new RegisterResponse();
		        		regResponse.clientId = conn.getID();
		        		regResponse.suspectNames = getAvailableSuspects();
		        		server.sendToTCP(conn.getID(), regResponse);
		        		return;
		        	}
                
                if(object instanceof GetSuspects)
                {
	                	SuspectResponse response = new SuspectResponse();
	                	response.suspectNames = getAvailableSuspects();
	                	server.sendToTCP(conn.getID(), response);
	                	return;
                }
                
                if(object instanceof SetSuspect)
                {
	                	String suspectName = ((SetSuspect) object).selectedSuspect;
					// Store the name on the connection.
					conn.playerName = suspectName;
	                	suspectConnectionMap.put(suspectName, playerID);
	                	server.sendToTCP(playerID, new ChatMessage("You are now " + suspectName + "!"));
	                	return;
                }

				if (object instanceof BeginGame)
				{
					int playerCounter = 0;
					for(String suspect : suspectConnectionMap.keySet())
					{
						if(suspectConnectionMap.get(suspect) != null)
						{
							playerCounter++;
						}
					}

					if(playerCounter > 2) // at least 3 players
					{
						server.sendToAllTCP(new BeginGame());
						startGame();
					}
					else 
					{
						server.sendToTCP(playerID, new ChatMessage("Not enough players to start game. You need " + (3 - playerCounter) + " more player(s)."));
					}
					return;
				}

				if (object instanceof EndTurn)
				{
					PlayerInfo currentPlayer = playerInfoMap.get(playerID);
		    	    		// end current player's turn
		    	    		server.sendToTCP(currentPlayer.playerId, new EndTurn());

		    	    		// and start the next player's turn
					prodNextPlayer(currentPlayer);

		        	    	return;
				}

				if (object instanceof MoveToken) 
				{
					int direction = ((MoveToken)object).direction;
					PlayerInfo currentPlayer = playerInfoMap.get(playerID);
		        		switch(direction)
		        		{
			        		case Constants.DIR_UP:
			        			gameBoard.moveUp(currentPlayer.player);
			        			break;
			        		case Constants.DIR_DOWN:
			        			gameBoard.moveDown(currentPlayer.player);
			        			break;
			        		case Constants.DIR_LEFT:
			        			gameBoard.moveLeft(currentPlayer.player);
			        			break;
			        		case Constants.DIR_RIGHT:
			        			gameBoard.moveRight(currentPlayer.player);
			        			break;
			        		case Constants.DIR_PASSAGE:
			        			gameBoard.takePassage(currentPlayer.player);
			        			break;
		        		}

		        		// Player can make a Suggestion if they recently moved to a room
		        	    if(currentPlayer.player.positionOnBoard instanceof Room)
		        	    {
			        	    	// Player has entered room, is allowed to make a suggestion
			        	    	server.sendToTCP(currentPlayer.playerId, new SuggestionAsk());
		        	    }
		        	    else
		        	    {
		        	    		// end current player's turn
		        	    		server.sendToTCP(currentPlayer.playerId, new EndTurn());
	        	    	
		        	    		// and start the next player's turn
		        	    		prodNextPlayer(currentPlayer);
		        	    }

		        	    // Update the GameBoard after every move
		        	    server.sendToAllTCP(new DisplayGUI(gameBoard));
		        	    return;
		        	}
			}

			public void disconnected (Connection c)
			{
				CluelessConnection connection = (CluelessConnection)c;
				if (connection.playerName != null)
				{
					// Announce to everyone that someone (with a registered name) has left.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = connection.playerName + " disconnected.";
					server.sendToAllTCP(chatMessage);
				}
				return;
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

	void initSuspectConnectionMap()
	{
		suspectConnectionMap = new LinkedHashMap<String, Integer>(Constants.SUSPECTS.length);
		for(int i=0; i<Constants.SUSPECTS.length; i++)
		{
			suspectConnectionMap.put(Constants.SUSPECTS[i], null);
		}
	}

	void startGame()
	{
		// set up the game with PlayerInfo objects, used for directionality/turns
		initializePlayerInfoObjects();
		
		// create a new GameBoard for the game
		gameBoard = new Gameboard();
		gameBoard.createNewBoard(players);

        // send the players to the clients so they can update the GUIDiplay / gameboard
		server.sendToAllTCP(new DisplayGUI(gameBoard));
		
		forfeitPlayerList = new ArrayList<Integer>();
		
		distributeCards(); // Create the caseFile and distribute all remaining cards
		
		Integer[] playerInfoKeys = playerInfoMap.keySet().toArray(new Integer[playerInfoMap.size()]);
		
		PlayerInfo firstPlayer = playerInfoMap.get(playerInfoKeys[0]);
		Location firstPlayerLocation = firstPlayer.player.positionOnBoard;
		server.sendToAllTCP(new ChatMessage(firstPlayer.suspectName + " has the first move"));

		// assign moves for playerTurn
	    	PlayerTurn playerTurn = new PlayerTurn();
	    	playerTurn.turn = true;
	    	playerTurn.up = 	firstPlayerLocation.hasUp() && gameBoard.moveValidUp(firstPlayer.player);
	    	playerTurn.down = firstPlayerLocation.hasDown() && gameBoard.moveValidDown(firstPlayer.player);
	    	playerTurn.left = firstPlayerLocation.hasLeft() && gameBoard.moveValidLeft(firstPlayer.player);
	    	playerTurn.right = firstPlayerLocation.hasRight() && gameBoard.moveValidRight(firstPlayer.player);
	    	playerTurn.passage = (firstPlayerLocation instanceof Room && ((Room) firstPlayerLocation).hasSecretPassage());
			
		server.sendToTCP(playerInfoKeys[0], playerTurn);
		
		// send turn = false to other players
		playerTurn.turn = false;
		server.sendToAllExceptTCP(playerInfoKeys[0], playerTurn);
	}
	
	// Creates a singly linked list of player info objects that are linked by 
	// the player "on the left" of the current player so that we can iterate
	// through the players in order for dealing and working through suggestions
	void initializePlayerInfoObjects()
	{
		playerInfoMap = new LinkedHashMap<Integer, PlayerInfo>();
		players = new Player[Constants.SUSPECTS.length];

		PlayerInfo first = null;
		PlayerInfo previous = null;
		PlayerInfo current = null;
		for(int i=0; i<Constants.SUSPECTS.length; i++)
		{
			Integer playerId = suspectConnectionMap.get(Constants.SUSPECTS[i]);
			current = new PlayerInfo();
			current.suspectName = Constants.SUSPECTS[i];
			current.player = new Player(Constants.SUSPECTS[i]);

			// We only care about setting playerToLeft to the real players
			if(playerId != null)
			{
				current.playerId = playerId;
				
				if(previous != null) // assign the last player
				{
					previous.playerToLeft = current; // to the left of the current
				}
				previous = current; // now the current is on the left

				if(first == null) // if this is the first player ... 
				{
					first = current;
				}

				// add this real player to our playerInfoMap
				playerInfoMap.put(playerId, current);
			}
			
			// add the player to the array
			players[i] = current.player;
		}
		
		previous.playerToLeft = first; // ... then assign to the left of the last player
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
		while((nextCard = cardDeck.drawCard()) != null)
		{
			DealCard dealCard = new DealCard();
			dealCard.card = nextCard;
			server.sendToTCP(currentPlayer.playerId, dealCard);
			currentPlayer = currentPlayer.playerToLeft;
		}
	
	}

	// Return a list of the remaining suspects that are available for the user to select
	String[] getAvailableSuspects()
	{
		ArrayList<String> suspectList = new ArrayList<String>();
		
		for(String suspect : suspectConnectionMap.keySet())
		{
			if(suspectConnectionMap.get(suspect) == null)
			{
				suspectList.add(suspect);
			}
		}
		
		return suspectList.toArray(new String[suspectList.size()]);
	}

	// Move to the next player
	void prodNextPlayer(PlayerInfo currentPlayer)
	{
		Integer nextPlayerId = currentPlayer.playerToLeft.playerId;
		while(forfeitPlayerList.contains(nextPlayerId))
		{
			PlayerInfo nextPlayer = playerInfoMap.get(nextPlayerId);
			String message = nextPlayer.suspectName + " forfeits their turn.";
			server.sendToAllTCP(new ChatMessage(message));
			nextPlayerId = nextPlayer.playerToLeft.playerId;
		}

		// grab the next PlayerInfo
		PlayerInfo nextPlayer = playerInfoMap.get(nextPlayerId);
		Location playerLocation = nextPlayer.player.positionOnBoard;
		server.sendToAllTCP(new ChatMessage(nextPlayer.suspectName + " has the next move"));
		
		// assign moves for playerTurn
	    	PlayerTurn playerTurn = new PlayerTurn();
	    	playerTurn.turn = true;
	    	playerTurn.up = 	playerLocation.hasUp() && gameBoard.moveValidUp(nextPlayer.player);
	    	playerTurn.down = playerLocation.hasDown() && gameBoard.moveValidDown(nextPlayer.player);
	    	playerTurn.left = playerLocation.hasLeft() && gameBoard.moveValidLeft(nextPlayer.player);
	    	playerTurn.right = playerLocation.hasRight() && gameBoard.moveValidRight(nextPlayer.player);
	    	playerTurn.passage = (playerLocation instanceof Room && ((Room) playerLocation).hasSecretPassage());
		
		server.sendToTCP(nextPlayerId, playerTurn);
		
		// send turn = false to other players
		playerTurn.turn = false;
		server.sendToAllExceptTCP(nextPlayerId, playerTurn);
	}
	
	void endGame()
	{
		// do something crazy
	}
	
	// This holds per connection state.
	static class CluelessConnection extends Connection {
		public String playerName;
	}

	public static void main (String[] args) throws IOException {
		Log.set(Log.LEVEL_ERROR);
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

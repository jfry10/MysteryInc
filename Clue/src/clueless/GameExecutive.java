package clueless;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import clueless.Network.ChatMessage;
import clueless.Network.RegisterName;
import clueless.Network.UpdateNames;
import clueless.Network.BeginGame;
import clueless.Network.BeginPlay;
import clueless.Network.DealCard;
import clueless.Network.EndTurn;
import clueless.Network.MoveToken;
import clueless.Network.RegisterRequest;
import clueless.Network.RegisterResponse;
import clueless.Network.SuspectRequest;
import clueless.Network.SuspectResponse;

public class GameExecutive
{
	Server server;
	int playerID;
	Suggestion currentSuggestion;
	Accusation currentAccusation;
	HashMap<String, Integer> connectionMap;
	HashMap<String, Integer> suspectConnectionMap;
	CardDeck cardDeck;
	CaseFile caseFile;
	ArrayList<String> forfeitPlayerList;

	public GameExecutive() throws IOException {

		initSuspectConnectionMap();

		server = new Server() {
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
					playerID = conn.getID();
					currentSuggestion = (Suggestion)object;
					HandleSuggestion();
				}
				
				// The Client sends us an Accusation, pass on to other clients
				if (object instanceof Accusation)
				{
					playerID = conn.getID();
					currentAccusation = (Accusation)object;
					HandleAccusation();
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
					if (s == "no disprove suggestion")
					{
						HandleSuggestion();
					}
					return;
				}

                if(object instanceof RegisterRequest)
				{
	        		RegisterResponse regResponse = new RegisterResponse();
	        		regResponse.clientId = conn.getID();
	        		regResponse.suspectNames = getAvailableSuspects();
	        		server.sendToTCP(conn.getID(), regResponse);
	        	}

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

				if (object instanceof BeginGame)
				{
	        		startGame();
	        	}

				if (object instanceof EndTurn) {
	        		
	        	}

				if (object instanceof MoveToken) {
	        		
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
		suspectConnectionMap = new HashMap<String, Integer>(Constants.SUSPECTS.length);
		for(int i=0; i<Constants.SUSPECTS.length; i++) {
			suspectConnectionMap.put(Constants.SUSPECTS[i], null);
		}
	}

	void startGame() {
		distributeCards();
		
		server.sendToTCP(suspectConnectionMap.get(Constants.SUSPECTS[Constants.MISS_SCARLET]), new BeginPlay());
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
		
		String[] suspectArray = 
				suspectConnectionMap.keySet().toArray(new String[suspectConnectionMap.keySet().size()]);

		for(int i=0; (nextCard = cardDeck.drawCard()) != null && i<suspectArray.length; i++) {
			Integer connId = suspectConnectionMap.get(suspectArray[i]);
			if(connId != null) {
				DealCard dealCard = new DealCard();
				dealCard.card = nextCard;
				server.sendToTCP(connId, dealCard);
			}
			if(i == suspectArray.length-1) {
				i = -1;
			}
		}
		
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
	
	void HandleSuggestion()
	{
		// We might need to send this to all players
		Connection[] connections = server.getConnections();
		for (int index = 0; index < connections.length; index++)
		{
			// Don't send it back to the player making the suggestion
			if (connections[index].getID() != playerID)
			{
				server.sendToTCP(connections[index].getID(), currentSuggestion);
			}
		}
		return; 
	}
	
	void HandleAccusation()
	{
		return;
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

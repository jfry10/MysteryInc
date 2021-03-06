package clueless;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import clueless.Network.ChatMessage;
import clueless.Network.DealCard;
import clueless.Network.DetectiveInfo;
import clueless.Network.EndTurn;
import clueless.Network.DisplayGUI;
import clueless.Network.EndGame;
import clueless.Network.MoveToken;
import clueless.Network.PlayerTurn;
import clueless.Network.RegisterName;
import clueless.Network.SuspectResponse;
import clueless.Network.UpdateNames;
import clueless.Network.ValidMove;
import clueless.Network.BeginGame;
import clueless.Network.GetSuspects;
import clueless.Network.SetSuspect;
import clueless.Network.SuggestionAsk;
import clueless.Network.SuggestionDisprove;;

public class CluelessClient
{
	// We can re-purpose the GameFrame to be our GUI
	// OR we can use the GameFrame inside the GUI as a debug/message log
	GameFrame GameFrame;
	Client client;
	Player player;
	String name;

    public CluelessClient () {}

	public CluelessClient (String ipAddress)
	{
		Kryo kryo = new Kryo();
		kryo.setReferences(true);
		KryoSerialization serialization = new KryoSerialization(kryo);
		
		client = new Client(16384, 2048, serialization);
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
			}

			public void received (Connection connection, Object object)
			{
                // Received a notification to select a suspect
                if(object instanceof SuspectResponse)
                {
                		String[] availableSuspectNames = ((SuspectResponse)object).suspectNames;
                		selectSuspectName(availableSuspectNames);
                		return;
                }

                // Left in here so we can receive server messages
				if (object instanceof ChatMessage)
				{
					ChatMessage chatMessage = (ChatMessage)object;
					GameFrame.addMessage(chatMessage.text);
					return;
				}

				// The Server deals us a new Card, add it to the playerHand
				if (object instanceof DealCard)
				{
					DealCard newCard = (DealCard)object;
					player.addCardToHand(newCard.card);
					// update GameFrame's detectiveNotes
					GameFrame.detectiveNotes.setText(player.suspectName + "'s " + player.getDetectiveNotes().toString());
					return;
				}

				// Server has sent us the latest gameboard, use it to update
				// our GameFrame display
				if (object instanceof DisplayGUI)
				{
					DisplayGUI dg = (DisplayGUI)object;
					GUIDisplay gui = new GUIDisplay(dg.gameboard);
					GameFrame.updateGameboard(gui);
					// Update the player location, used for suggestions
					for (int i = 0; i < Constants.SUSPECTS.length; i++)
					{
						if (player.suspectName.equals(dg.gameboard.playerlist[i].suspectName))
						{
							player.positionOnBoard = dg.gameboard.playerlist[i].positionOnBoard;
						}
					}
					return;
				}

				// Received a BeginGame object, so hide the current "Start Game" button
				if (object instanceof BeginGame)
				{
					GameFrame.actionButton.setVisible(false);
					return;
				}

				// Received a PlayerTurn object. Two possible outcomes:
				// 1) Clients turn to play. Only display the valid move buttons
				// 2) Not our turn. Hide all buttons
				if (object instanceof PlayerTurn)
				{
					PlayerTurn pt = (PlayerTurn)object;
					GameFrame.accusButton.setEnabled(pt.turn); // only available during turn
					if (pt.turn == true)
					{
						GameFrame.addMessage("It is now your turn");

        					// Show only the move buttons that are valid
        					GameFrame.moveUpButton.setVisible(pt.up);
        					GameFrame.moveRightButton.setVisible(pt.right);
        					GameFrame.moveDownButton.setVisible(pt.down);
        					GameFrame.moveLeftButton.setVisible(pt.left);
        					GameFrame.takePassageButton.setVisible(pt.passage);
        					GameFrame.suggestButton.setVisible(false); // wait until server enables this

        					// unique case, player cannot make any movements. Give them
        					// the option to end turn (or make accusation, should be true)
        					if (!pt.up && !pt.right && !pt.down && !pt.left && !pt.passage)
        					{					
        						// Make this button an EndTurn button
        						GameFrame.actionButton.setText("End Turn");
        						GameFrame.actionButton.setVisible(true);
        					}
					}
					else // not your turn
					{
						HideButtons();
					}
					return;
				}
				
				// If we receive this, the user has an opportunity to make a suggestion
				if (object instanceof SuggestionAsk)
				{
					GameFrame.suggestButton.setVisible(true);
					GameFrame.actionButton.setVisible(true);
					// Make this button an EndTurn button
					GameFrame.actionButton.setText("End Turn");
					return;
				}

                // The Server sends us a card indicating that our suggestion was incorrect
                if (object instanceof SuggestionDisprove)
                {
                		SuggestionDisprove sd = (SuggestionDisprove)object;
                		player.updateDetectiveNotes(sd.card);
    					// update GameFrame's detectiveNotes
    					GameFrame.detectiveNotes.setText(player.suspectName + "'s " + player.getDetectiveNotes().toString());
    					// now print a message so the player knows why they were disproved
    					GameFrame.addMessage("Your suggestion was incorrect!\n" + sd.card.getName() + " is not in the Case File!");
                		return;
                }

				// The Server sends us a Suggestion to disprove
                if (object instanceof Suggestion)
                {
                	    Suggestion sug = (Suggestion)object;

					boolean disprove = player.canDisprove(sug.room, sug.weapon, sug.suspect);
	
					// if we can't disprove, then send a message back
					if (disprove == false)
					{
						// send any random new card. It will initialize to "null"
						connection.sendTCP(new SuggestionDisprove(new RoomCard()));
					}
					else
					{
						Card disproveCard = player.getDisproveCard(sug.room, sug.weapon, sug.suspect);
						connection.sendTCP(new SuggestionDisprove(disproveCard));
					}
					return;
                }
                
                // We received an Accusation object! Dang.. Remember your suggestion so you can 
                // make an Accusation during your next turn!
                if (object instanceof Accusation)
                {
                		GameFrame.addMessage("Congratulations! Your suggestion was correct!");
                		GameFrame.addMessage("Remember it so you can make an accusation next turn");
                		return;
                }
				
				// We receive an EndTurn object, disable and hide unusable buttons
				if (object instanceof EndTurn)
				{
					HideButtons();
					return;
				}
				
				// We receive an EndGame object, let the clients decide if they want to restart
				if (object instanceof EndGame)
				{
					// Hide all the buttons, then display the restart button
					HideButtons();
					GameFrame.actionButton.setText("Restart Game");
					GameFrame.actionButton.setVisible(true);
					GameFrame.accusButton.setEnabled(false);
					return;
				}
			}

			public void disconnected (Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run () {
						// Closing the frame calls the close listener which will stop the client's update thread.
						GameFrame.dispose();
					}
				});
			}
		});


		// All the ugly Swing stuff is hidden in GameFrame so it doesn't clutter the KryoNet example code.
		GameFrame = new GameFrame();
		
		// This listener is called when the Up button is clicked.
		GameFrame.moveUpListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_UP;
				HideButtons(); // hide the other 
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Right button is clicked.
		GameFrame.moveRightListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_RIGHT;
				HideButtons(); // hide the other 
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Down button is clicked.
		GameFrame.moveDownListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_DOWN;
				HideButtons(); // hide the other 
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Left button is clicked.
		GameFrame.moveLeftListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_LEFT;
				HideButtons(); // hide the other 
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Passage button is clicked.
		GameFrame.takePassageListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_PASSAGE;
				HideButtons(); // hide the other 
				client.sendTCP(token);
			}
		});
		
		///Accusation Listener
		GameFrame.accusationListener(new Runnable() {
			public void run () {
				JFrame frame = new AccusationGUI(client);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		//Suggestion Listener
		GameFrame.suggestionListener(new Runnable() {
			public void run () {
				JFrame frame = new SuggestionGUI(client, player.positionOnBoard);
				frame.setVisible(true);	
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		// This listener is called when the Action button is clicked
		GameFrame.actionButtonListener(new Runnable() {
			
			@Override
			public void run() {
				
				String buttonText = GameFrame.actionButton.getText();
				// Send an object based on the button pressed
				if (buttonText.equals("Start Game"))
				{
					client.sendTCP(new BeginGame());
				}
				else if (buttonText.equals("End Turn"))
				{
					client.sendTCP(new EndTurn());
				}
				else if (buttonText.equals("Restart Game"))
				{
					// Dream system, allow the users to start a new game. 
					// I imagine this would be similar to calling StartGame, however, we need the
					// clients to delete all their data and stuff could get messy.
					// We might not want to worry about that for this presentation
					//client.sendTCP(new BeginGame()); // ? Restart Game object?
				}
				// hide the action button. We will not use this again till prompted
				GameFrame.actionButton.setVisible(false);
			}
		});

		// This listener is called when the chat window is closed.
		GameFrame.setCloseListener(new Runnable() {
			public void run () {
				client.stop();
			}
		});
		GameFrame.setVisible(true);

		// We'll do the connect on a new thread so the GameFrame can show a progress bar.
		// Connecting to localhost is usually so fast you won't see the progress bar.
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(10000, ipAddress, Network.port);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("woke up!");
			e.printStackTrace();
		}

		// Once a client has connected, send a request to get available suspects
		client.sendTCP(new GetSuspects());
	}
	
	// This pop-up will allow the user to select a Suspect name to use
	void selectSuspectName(String[] suspectNames) {

		JFrame suspectJFrame = new JFrame();
		
		JRadioButton[] suspectButton = new JRadioButton[suspectNames.length];
		for(int i=0; i<suspectNames.length; i++) {
			suspectButton[i] = new JRadioButton(suspectNames[i]);
		}
		
		ButtonGroup suspectButtonGroup = new ButtonGroup();
		for(JRadioButton button : suspectButton) {
			suspectButtonGroup.add(button);
		}
		
		suspectJFrame.setSize(200,400);
		Container cont = suspectJFrame.getContentPane();
		
		cont.setLayout(new GridLayout(0, 1));
		cont.add(new JLabel("Select your suspect name:"));
		
		for(JRadioButton button : suspectButton) {
			cont.add(button);
		}
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean selected = false;
				for(JRadioButton button : suspectButton) {
					if(button.isSelected())
					{
						selected = true;
						player = new Player(button.getText());
                			client.sendTCP(new SetSuspect(player.suspectName));
                			// Update your detectiveNotes
                			GameFrame.detectiveNotes.setText(player.suspectName + "'s " + player.getDetectiveNotes().toString());
						break;
					}
				}
				
				if(selected)
				{
					suspectJFrame.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You must select a suspect name", "Select a suspect name", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		cont.add(submitButton);
		suspectJFrame.setVisible(true);
		suspectJFrame.setLocationRelativeTo(null);
	}

	// Marks all buttons (except accuse) as Hidden
	// and disables the accuse button
	void HideButtons()
	{
		GameFrame.moveUpButton.setVisible(false);
		GameFrame.moveRightButton.setVisible(false);
		GameFrame.moveDownButton.setVisible(false);
		GameFrame.moveLeftButton.setVisible(false);
		GameFrame.takePassageButton.setVisible(false);
		GameFrame.suggestButton.setVisible(false);
		GameFrame.actionButton.setVisible(false);
	}

	static private class GameFrame extends JFrame implements ActionListener
	{
		GUIDisplay guiDisplay = new GUIDisplay();
		JPanel gameboardPanel;
		JPanel optionsPanel;
		JTextArea detectiveNotes;
		JTextArea serverMessages;
		JButton moveUpButton;
		JButton moveRightButton;
		JButton moveDownButton;
		JButton moveLeftButton;
		JButton takePassageButton;
		JButton suggestButton;
		JButton accusButton;
		JButton actionButton;

		public GameFrame ()
		{
			super("Clueless");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setSize(1000, 820);
			setLocationRelativeTo(null);

			Container contentPane = getContentPane();
			contentPane.setLayout(null); // absolute positioning

			//////Game Board Screen//////
			contentPane.add(guiDisplay);
			// The Gameboard in the top left
			guiDisplay.setBounds(10, 10, 590, 540);

			//////Detective Notes////////
			JPanel detectiveNotesPanel = new JPanel(new BorderLayout());
			detectiveNotesPanel.add(new JScrollPane(detectiveNotes = new JTextArea()), BorderLayout.CENTER);
			detectiveNotes.setText(new Player().getDetectiveNotes().toString()); // just a plain notepad for now
			detectiveNotes.setEditable(false);
			contentPane.add(detectiveNotesPanel);
			// The Detective Notes in the top right
			detectiveNotesPanel.setBounds(610, 10, 380, 540);

			//////Game options/Menu//////
			optionsPanel = new JPanel(new GridLayout(1,3));
			
			// first column is Movement directions
			{
				JPanel movementPanel = new JPanel(new BorderLayout());
				movementPanel.add(moveUpButton = new JButton("Up"), BorderLayout.NORTH);
				movementPanel.add(moveRightButton = new JButton("Right"), BorderLayout.EAST);
				movementPanel.add(moveDownButton = new JButton("Down"), BorderLayout.SOUTH);
				movementPanel.add(moveLeftButton = new JButton("Left"), BorderLayout.WEST);
				movementPanel.add(takePassageButton = new JButton("Passage"), BorderLayout.CENTER);
				optionsPanel.add(movementPanel);

				// Hide all buttons to start
				moveUpButton.setVisible(false);
				moveRightButton.setVisible(false);
				moveDownButton.setVisible(false);
				moveLeftButton.setVisible(false);
				takePassageButton.setVisible(false);
			}

			// middle column is the Start / Restart Game button
			{
				actionButton = new JButton("Start Game");
				optionsPanel.add(actionButton);
			}

			// third column is Suggestion and Accusation buttons
			{
				JPanel sugAccPanel = new JPanel(new BorderLayout());
				sugAccPanel.add(suggestButton = new JButton("Make Suggestion"), BorderLayout.NORTH);
				sugAccPanel.add(accusButton = new JButton("Make Accusation"), BorderLayout.SOUTH);
				optionsPanel.add(sugAccPanel);
				
				// Hide all buttons to start
				suggestButton.setVisible(false);
				accusButton.setEnabled(false);
			}

			contentPane.add(optionsPanel);
			// The Game Options box is in the center
			optionsPanel.setBounds(10, 560, 980, 90);


			//////Server Messages////////
			JPanel serverMessagesPanel = new JPanel(new BorderLayout());
			JLabel serverMessagesLabel = new JLabel("Game Messages"); 
			serverMessagesPanel.add(serverMessagesLabel, BorderLayout.NORTH);
			serverMessagesPanel.add(new JScrollPane(serverMessages = new JTextArea()));
			serverMessages.setText("Welcome to Clueless!\n");
			serverMessages.setEditable(false);
			serverMessages.setAutoscrolls(true);
			contentPane.add(serverMessagesPanel);
			// The Server message box is in the bottom
			serverMessagesPanel.setBounds(10, 660, 980, 130);

		}

		public void updateGameboard(GUIDisplay gui)
		{
			Container contentPane = getContentPane();
			contentPane.setLayout(null); // absolute positioning
			contentPane.remove(guiDisplay);

			//////Game Board Screen//////
			guiDisplay = gui;
			contentPane.add(guiDisplay);
			// The Gameboard in the top left
			guiDisplay.setBounds(10, 10, 590, 540);
		}

		public void moveUpListener (final Runnable listener) {
			moveUpButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					listener.run(); // call so we can send the move token
				}
			});
		}
		
		public void moveRightListener (final Runnable listener) {
			moveRightButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					listener.run(); // call so we can send the move token
				}
			});
		}
		
		public void moveDownListener (final Runnable listener) {
			moveDownButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					listener.run(); // call so we can send the move token
				}
			});
		}
		
		public void moveLeftListener (final Runnable listener) {
			moveLeftButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					listener.run(); // call so we can send the move token
				}
			});
		}
		
		public void takePassageListener (final Runnable listener) {
			takePassageButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					listener.run(); // call so we can send the move token
				}
			});
		}
		
		///Accusation listener
		public void accusationListener (final Runnable listener) {
			accusButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					listener.run(); // call so we can send the move token
				}
			});
		}
				
		//Suggestion
		public void suggestionListener (final Runnable listener) {
			suggestButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					listener.run(); // call so we can send the move token
				}
			});
		}

		public void actionButtonListener (final Runnable listener) {
			actionButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					listener.run();
				}
			});
		}

		public void setCloseListener (final Runnable listener)
		{
			addWindowListener(new WindowAdapter() {
				public void windowClosed (WindowEvent evt) {
					listener.run();
				}

				public void windowActivated (WindowEvent evt) {
					serverMessages.requestFocus();
				}
			});
		}

		public void addMessage (final String message) {
			EventQueue.invokeLater(new Runnable() {
				public void run () {
					String current = serverMessages.getText();
					current += "\n\n" + message;
					serverMessages.setText(current);
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// don't know if this even does anything
		}
	}

	public static void main (String[] args)
	{
		//String ipAddress = args[0];
		Log.set(Log.LEVEL_DEBUG);
		//new CluelessClient(ipAddress);
		new CluelessClient("72.205.5.54");
		//new CluelessClient("68.98.230.119");
		//new CluelessClient("localhost");
	}
}

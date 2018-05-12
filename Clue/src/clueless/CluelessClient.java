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


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import clueless.Network.ChatMessage;
import clueless.Network.DetectiveInfo;
import clueless.Network.MoveToken;
import clueless.Network.PlayerTurn;
import clueless.Network.RegisterName;
import clueless.Network.SuspectResponse;
import clueless.Network.UpdateNames;
import clueless.Network.ValidMove;
import clueless.Network.BeginGame;
import clueless.Network.GetSuspects;
import clueless.Network.SetSuspect;


public class CluelessClient
{
	// We can re-purpose the GameFrame to be our GUI
	// OR we can use the GameFrame inside the GUI as a debug/message log
	GameFrame GameFrame;
	Client client;
	Player player;
	String name;
	boolean turn;

    public CluelessClient () {}

	public CluelessClient (String ipAddress)
	{
		client = new Client();
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
				// We can re-purpose this to any Game object.
				// if we receive an object, do something with it
				if (object instanceof UpdateNames)
				{
					UpdateNames updateNames = (UpdateNames)object;
					GameFrame.setNames(updateNames.names);
					return;
				}
				
				if (object instanceof GUIDisplay)
				{
					GUIDisplay gui = (GUIDisplay)object;
					GameFrame.updateGameboard(gui);
				}
				
				if (object instanceof PlayerTurn)
				{
					PlayerTurn pt = (PlayerTurn)object;
					turn = pt.turn;
					GameFrame.setEnabled(turn);
					if (turn == true)
					{
            				JOptionPane.showMessageDialog(
            					null, 
            					"It is now your turn", 
            					"Player Turn", 
                          	JOptionPane.INFORMATION_MESSAGE);
					}
					return;
				}

                // Previously included, left in here so we can receive global messages
				if (object instanceof ChatMessage)
				{
					ChatMessage chatMessage = (ChatMessage)object;
					GameFrame.addMessage(chatMessage.text);
					return;
				}

				// The Server sends us a new Card
				if (object instanceof Card)
				{
					Card newCard = (Card)object;
					player.addCardToHand(newCard);
					return;
				}

				// The Server sends us a Suggestion to disprove
                if (object instanceof Suggestion)
                {
                	    Suggestion sug = (Suggestion)object;
                	    String no = "no disprove suggestion";

					boolean disprove = player.canDisprove(sug.room, sug.weapon, sug.suspect);
	
					// if we can't disprove, then send a message back
					if (disprove == false)
					{
						connection.sendTCP(no);
					}
					else
					{
						Card disproveCard = player.getDisproveCard(sug.room, sug.weapon, sug.suspect);
						connection.sendTCP(disproveCard);
					}
					return;
                }

                // The Server sends us a Note / private message
                if (object instanceof DetectiveInfo)
                {
                		DetectiveInfo pm = (DetectiveInfo)object;
                		player.updateDetectiveNotes(pm.type, pm.name);
                		return;
                }
                
                // The Server sends us an update on our move request
                if (object instanceof ValidMove)
                {
                		ValidMove vm = (ValidMove)object;
                		if (vm.valid == false)
                		{
                			JOptionPane.showMessageDialog(
                					null, 
                					"Unable to make the provided move. Please select another location.", 
                					"Invalid Move", 
                              	JOptionPane.WARNING_MESSAGE);
                		}
                		return;
                }
                
                if(object instanceof SuspectResponse) {
                		String[] availableSuspectNames = ((SuspectResponse)object).suspectNames;
                		selectSuspectName(availableSuspectNames);
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

		// Request the user's name.
		String input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to Clueless Server", JOptionPane.QUESTION_MESSAGE, null,
			null, "Player1");
		if (input == null || input.trim().length() == 0) System.exit(1);
		name = input.trim();
		
		
		

		// All the ugly Swing stuff is hidden in GameFrame so it doesn't clutter the KryoNet example code.
		GameFrame = new GameFrame(ipAddress);
		
		// This listener is called when the Up button is clicked.
		GameFrame.moveUpListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_UP;
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Right button is clicked.
		GameFrame.moveRightListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_RIGHT;
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Down button is clicked.
		GameFrame.moveDownListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_DOWN;
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Left button is clicked.
		GameFrame.moveLeftListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_LEFT;
				client.sendTCP(token);
			}
		});
		
		// This listener is called when the Passage button is clicked.
		GameFrame.takePassageListener(new Runnable() {
			public void run () {
				MoveToken token = new MoveToken();
				token.direction = Constants.DIR_PASSAGE;
				client.sendTCP(token);
			}
		});
		
		///Accusation Listener
				GameFrame.accusationListener(new Runnable() {
					public void run () {
						JFrame frame = new AccusationGui(client);
						frame.setVisible(true);
					}
				});
				
				//Suggestion Listener
				GameFrame.suggestionListener(new Runnable() {
					public void run () {
						JFrame frame = new main(client);
				        frame.setVisible(true);	
					}
				});
		// This listener is called when the Start Game button is clicked
		GameFrame.startGameListener(new Runnable() {
			
			@Override
			public void run() {
				client.sendTCP(new BeginGame());
			}
		});

		// This listener is called when the send button is clicked.
		/*GameFrame.setSendListener(new Runnable() {
			public void run () {
				ChatMessage chatMessage = new ChatMessage();
				chatMessage.text = GameFrame.getSendText();
				client.sendTCP(chatMessage);
			}
		});
		// This listener is called when the chat window is closed.
		GameFrame.setCloseListener(new Runnable() {
			public void run () {
				client.stop();
			}
		});*/
		GameFrame.setVisible(true);

		// We'll do the connect on a new thread so the GameFrame can show a progress bar.
		// Connecting to localhost is usually so fast you won't see the progress bar.
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, ipAddress, Network.port);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println("woke up!");
			e.printStackTrace();
		}
		
		client.sendTCP(new GetSuspects());
	}
	
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
				
				for(JRadioButton button : suspectButton) {
					if(button.isSelected()) {
						player = new Player(button.getText());
                		client.sendTCP(new SetSuspect(player.suspectName));
						break;
					}
				}
				
				if(player.suspectName != null) {
					suspectJFrame.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "You must select a suspect name", "Select a suspect name", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		cont.add(submitButton);
		suspectJFrame.setVisible(true);
	}

	// Handle the Suggestion button, pressed by the JFrame
	void MakeSuggestion(RoomCard room, WeaponCard weapon, SuspectCard suspect)
	{
		Suggestion sug = new Suggestion(room, weapon, suspect);
		client.sendTCP(sug);
	}

	// Handle the Accusation button, pressed by the JFrame
	void MakeAccusation(RoomCard room, WeaponCard weapon, SuspectCard suspect)
	{
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(
				null, 
				"You only get one accusation! If you are incorrect, you forfeit the rest of your turns. Are you sure you want to submit?", 
				"Making Accusation", 
				dialogButton);
		if(dialogResult == JOptionPane.YES_OPTION)
		{ 
			Accusation acc = new Accusation(room, weapon, suspect);
			client.sendTCP(acc);
		}
	}

	// Handle the move request, pressed by the JFrame
	void Move(Location location)
	{
		client.sendTCP(location);
	}

	static private class GameFrame extends JFrame implements ActionListener
	{
		Dimension d;
		GUIDisplay guiDisplay = new GUIDisplay();
		JProgressBar progressBar;
		JList messageList;
		JPanel gameboardPanel;
		JTextArea detectiveNotes;
		JTextField serverMessages;
		JTextField sendText;
		JButton moveUpButton;
		JButton moveRightButton;
		JButton moveDownButton;
		JButton moveLeftButton;
		JButton takePassageButton;
		JButton sendButton;
		JButton suggestButton;
		JButton accusButton;
		JButton restartButton;
		JList nameList;

		public GameFrame (String host)
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
			detectiveNotes.setText(new Player().getDetectiveNotes().toString());
			detectiveNotes.setEditable(false);
			contentPane.add(detectiveNotesPanel);
			// The Detective Notes in the top right
			detectiveNotesPanel.setBounds(610, 10, 380, 540);

			//////Game options/Menu//////
			JPanel optionsPanel = new JPanel(new GridLayout(1,3));
			
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
				/*moveUpButton.setVisible(false);
				moveRightButton.setVisible(false);
				moveDownButton.setVisible(false);
				moveLeftButton.setVisible(false);
				takePassageButton.setVisible(false);*/
			}

			// middle column is the Start / Restart Game button
			{
				restartButton = new JButton("Start Game");
				//restartButton.setActionCommand("Restart Game");
				optionsPanel.add(restartButton);
			}

			// third column is Suggestion and Accusation buttons
			{
				JPanel sugAccPanel = new JPanel(new BorderLayout());
				sugAccPanel.add(suggestButton = new JButton("Make Suggestion"), BorderLayout.NORTH);
				sugAccPanel.add(accusButton = new JButton("Make Accusation"), BorderLayout.SOUTH);
				optionsPanel.add(sugAccPanel);
				
				// Hide all buttons to start
				/*suggestButton.setVisible(false);
				accusButton.setVisible(false);*/
			}

			contentPane.add(optionsPanel);
			// The Game Options box is in the bottom
			optionsPanel.setBounds(10, 560, 980, 90);
			
			//suggestButton.setActionCommand("Make Suggestion Please");
			//optionPanel.add(suggestButton);
			//suggestButton.addActionListener(this);
			
			//accusButton.setActionCommand("Make Accusation Please");
			//optionPanel.add(accusButton);
			
			//restartButton = new JButton("Restart Game");
			//restartButton.setActionCommand("Restart Game");
			//optionPanel.add(restartButton);
			// The Buttons / Action grouping is in the center


			//////Server Messages////////
			JPanel serverMessagesPanel = new JPanel(new BorderLayout());
			JLabel serverMessagesLabel = new JLabel("Game Messages"); 
			serverMessagesPanel.add(serverMessagesLabel, BorderLayout.NORTH);
			serverMessagesPanel.add(new JScrollPane(serverMessages = new JTextField()));
			serverMessages.setText("Welcome to Clueless!\n\n");
			serverMessages.setEditable(false);
			contentPane.add(serverMessagesPanel);
			// The Server message box is in the bottom
			serverMessagesPanel.setBounds(10, 660, 980, 130);
			
			// Hide this for now
//			{
//				JPanel panel = new JPanel(new BorderLayout());
//				contentPane.add(panel, "progress");
//				panel.add(new JLabel("Connecting to " + host + "..."));
//				{
//					panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
//					progressBar.setIndeterminate(true);
//				}
//			}
//			{
//				JPanel panel = new JPanel(new BorderLayout());
//				contentPane.add(panel, "chat");
//				{
//					JPanel topPanel = new JPanel(new GridLayout(1, 1));
//					panel.add(topPanel, BorderLayout.EAST);
//			
//					{
//						topPanel.add(new JScrollPane(messageList = new JList()));
//						messageList.setModel(new DefaultListModel());
//					}
//					{
//						topPanel.add(new JScrollPane(nameList = new JList()));
//						nameList.setModel(new DefaultListModel());
//						
//					}
//					
//					
//					DefaultListSelectionModel disableSelections = new DefaultListSelectionModel() {
//						public void setSelectionInterval (int index0, int index1) {
//						}
//					};
//					
//					messageList.setSelectionModel(disableSelections);
//					nameList.setSelectionModel(disableSelections);
//					
//				}
				
//				//////Game Board Screen//////
//				{
//					JPanel leftPanel = new JPanel(new GridLayout(1,1));
//					JLabel label = new JLabel("Game Board");
//					label.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 15));
//					panel.add(leftPanel, BorderLayout.WEST);
//					leftPanel.add(label);
//				}

//				{
//					JPanel bottomPanel = new JPanel(new GridBagLayout());
//					panel.add(bottomPanel, BorderLayout.SOUTH);
//					bottomPanel.add(sendText = new JTextField(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
//						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//					bottomPanel.add(sendButton = new JButton("Send"), new GridBagConstraints(1, 0, 1, 1, 0, 0,
//						GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));
//				}
				
//				//////Game options/Menu ///////
//				{
//					JPanel optionPanel = new JPanel (new GridLayout(3,1));
//					panel.add(optionPanel, BorderLayout.NORTH);
//					
//					suggestButton = new JButton("Make Suggestion");
//					suggestButton.setActionCommand("Make Suggestion Please");
//					optionPanel.add(suggestButton);
//					suggestButton.addActionListener(this);
//					
//					accusButton = new JButton("Make Accusation");
//					accusButton.setActionCommand("Make Accusation Please");
//					optionPanel.add(accusButton);
//					
//					restartButton = new JButton("Restart Game");
//					restartButton.setActionCommand("Restart Game");
//					optionPanel.add(restartButton);
//				}


			/*sendText.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					sendButton.doClick();
				}
			});*/
		}

		public void updateGameboard(GUIDisplay gui) {
			gameboardPanel.removeAll();
			gameboardPanel.add(gui);
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
		
		///Accusation linstener
				public void accusationListener (final Runnable listener) {
					accusButton.addActionListener(new ActionListener() {
						public void actionPerformed (ActionEvent evt) {
							listener.run(); // call so we can send the move token
						}
					});
				}
				
				//Suggestion
				public void suggestionListener (final Runnable listener) {
					accusButton.addActionListener(new ActionListener() {
						public void actionPerformed (ActionEvent evt) {
							listener.run(); // call so we can send the move token
						}
					});
				}
		public void startGameListener (final Runnable listener) {
			restartButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					listener.run();
				}
			});
		}


		/*public void setSendListener (final Runnable listener) {
			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					if (getSendText().length() == 0) return;
					listener.run();
					sendText.setText("");
					sendText.requestFocus();
				}
			});
		}*/

		/*public void setCloseListener (final Runnable listener) {
			addWindowListener(new WindowAdapter() {
				public void windowClosed (WindowEvent evt) {
					listener.run();
				}

				public void windowActivated (WindowEvent evt) {
					sendText.requestFocus();
				}
			});
		}*/

		/*public String getSendText () {
			return sendText.getText().trim();
		}*/

		public void setNames (final String[] names) {
			// This listener is run on the client's update thread, which was started by client.start().
			// We must be careful to only interact with Swing components on the Swing event thread.
			EventQueue.invokeLater(new Runnable() {
				public void run () {
					//cardLayout.show(getContentPane(), "chat");
					DefaultListModel model = (DefaultListModel)nameList.getModel();
					model.removeAllElements();
					for (String name : names)
						model.addElement(name);
				}
			});
		}

		public void addMessage (final String message) {
			EventQueue.invokeLater(new Runnable() {
				public void run () {
					DefaultListModel model = (DefaultListModel)messageList.getModel();
					model.addElement(message);
					messageList.ensureIndexIsVisible(model.size() - 1);
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

	public static void main (String[] args)
	{
		String ipAddress = args[0];
		Log.set(Log.LEVEL_DEBUG);
		new CluelessClient(ipAddress);
		//new CluelessClient("localhost");
	}
}

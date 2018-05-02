package clueless;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import clueless.Network.ChatMessage;
import clueless.Network.DetectiveInfo;
import clueless.Network.PlayerTurn;
import clueless.Network.RegisterName;
import clueless.Network.UpdateNames;
import clueless.Network.ValidMove;

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
		// This listener is called when the send button is clicked.
		GameFrame.setSendListener(new Runnable() {
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
		});
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

	static private class GameFrame extends JFrame
	{
		CardLayout cardLayout;
		JProgressBar progressBar;
		JList messageList;
		JTextField sendText;
		JButton sendButton;
		JList nameList;

		public GameFrame (String host)
		{
			super("Clueless");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setSize(640, 1000);
			setLocationRelativeTo(null);

			Container contentPane = getContentPane();
			cardLayout = new CardLayout();
			contentPane.setLayout(cardLayout);
			{
				JPanel panel = new JPanel(new BorderLayout());
				contentPane.add(panel, "progress");
				panel.add(new JLabel("Connecting to " + host + "..."));
				{
					panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
					progressBar.setIndeterminate(true);
				}
			}
			{
				JPanel panel = new JPanel(new BorderLayout());
				contentPane.add(panel, "chat");
				{
					JPanel topPanel = new JPanel(new GridLayout(1, 2));
					panel.add(topPanel);
					{
						topPanel.add(new JScrollPane(messageList = new JList()));
						messageList.setModel(new DefaultListModel());
					}
					{
						topPanel.add(new JScrollPane(nameList = new JList()));
						nameList.setModel(new DefaultListModel());
					}
					DefaultListSelectionModel disableSelections = new DefaultListSelectionModel() {
						public void setSelectionInterval (int index0, int index1) {
						}
					};
					messageList.setSelectionModel(disableSelections);
					nameList.setSelectionModel(disableSelections);
				}
				{
					JPanel bottomPanel = new JPanel(new GridBagLayout());
					panel.add(bottomPanel, BorderLayout.SOUTH);
					bottomPanel.add(sendText = new JTextField(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					bottomPanel.add(sendButton = new JButton("Send"), new GridBagConstraints(1, 0, 1, 1, 0, 0,
						GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));
				}
			}

			sendText.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					sendButton.doClick();
				}
			});
		}

		public void setSendListener (final Runnable listener) {
			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent evt) {
					if (getSendText().length() == 0) return;
					listener.run();
					sendText.setText("");
					sendText.requestFocus();
				}
			});
		}

		public void setCloseListener (final Runnable listener) {
			addWindowListener(new WindowAdapter() {
				public void windowClosed (WindowEvent evt) {
					listener.run();
				}

				public void windowActivated (WindowEvent evt) {
					sendText.requestFocus();
				}
			});
		}

		public String getSendText () {
			return sendText.getText().trim();
		}

		public void setNames (final String[] names) {
			// This listener is run on the client's update thread, which was started by client.start().
			// We must be careful to only interact with Swing components on the Swing event thread.
			EventQueue.invokeLater(new Runnable() {
				public void run () {
					cardLayout.show(getContentPane(), "chat");
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
	}

	public static void main (String[] args)
	{
		//String ipAddress = args[0];
		Log.set(Log.LEVEL_DEBUG);
		//new CluelessClient(ipAddress);
		new CluelessClient("localhost");
	}
}

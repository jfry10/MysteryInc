package clueless;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.border.Border;

import com.esotericsoftware.kryonet.Client;

import clueless.Network.EndTurn;

public class SuggestionGUI extends JFrame implements ActionListener
{
    
    private JRadioButton knifeButton, revolverButton, ropeButton, candlestickButton, leadPipeButton, wrenchButton;
    private JRadioButton studyButton, hallButton, diningButton, loungeButton, libraryButton, billiardButton, conservButton, kitchenButton, ballroomButton;
    private JRadioButton scarletButton, mustardButton, whiteButton, greenButton, peacockButton, plumButton;
    protected JButton submit;

    JFrame jFrame;

	public SuggestionGUI(Client client)
	{
		jFrame = this;
		setTitle("Make Suggestion");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //JPanel panel = new SuggestionBox(c);
        //JPanel panel = new JPanel(new GridLayout(0, 1));
        //this.setLayout(new GridLayout(0, 1));
        //this.add(panel);
        
        //public SuggestionBox(Client client)
        //{
        knifeButton = new JRadioButton("Knife");
        revolverButton = new JRadioButton("Revolver");
        ropeButton = new JRadioButton("Rope");
        candlestickButton = new JRadioButton("Candlestick");
        leadPipeButton = new JRadioButton("Lead Pipe");
        wrenchButton = new JRadioButton("Wrench");
        
        String weapon;;
        Card room = null;
        Card person = null;

        ButtonGroup weaponsGroup = new ButtonGroup();
        weaponsGroup.add(knifeButton);
        weaponsGroup.add(revolverButton);
        weaponsGroup.add(ropeButton);
        weaponsGroup.add(candlestickButton);
        weaponsGroup.add(leadPipeButton);
        weaponsGroup.add(wrenchButton);

        Border buttonBorder = BorderFactory.createEtchedBorder();
        buttonBorder = BorderFactory.createTitledBorder(buttonBorder, "Weapon");

        JPanel weaponPanel = new JPanel();
        weaponPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        weaponPanel.add(knifeButton);
        weaponPanel.add(revolverButton);
        weaponPanel.add(ropeButton);
        weaponPanel.add(candlestickButton);
        weaponPanel.add(leadPipeButton);
        weaponPanel.add(wrenchButton);
        weaponPanel.setBorder(buttonBorder);

        knifeButton.addActionListener(this);
        weaponPanel.add(knifeButton);
        revolverButton.addActionListener(this);
        weaponPanel.add(revolverButton);
        ropeButton.addActionListener(this);
        weaponPanel.add(ropeButton);
        candlestickButton.addActionListener(this);
        weaponPanel.add(candlestickButton);
        leadPipeButton.addActionListener(this);
        weaponPanel.add(leadPipeButton);
        wrenchButton.addActionListener(this);
        weaponPanel.add(wrenchButton);


        studyButton = new JRadioButton("Study");
        hallButton = new JRadioButton("Hall");
        diningButton = new JRadioButton("Dining Room");
        loungeButton = new JRadioButton("Lounge");
        libraryButton = new JRadioButton("Library");
        billiardButton = new JRadioButton("Billiard Room");
        conservButton = new JRadioButton("Conservatory");
        kitchenButton = new JRadioButton("Kitchen");
        ballroomButton = new JRadioButton("Ballroom");

        ButtonGroup roomsGroup = new ButtonGroup();
        roomsGroup.add(studyButton);
        roomsGroup.add(hallButton);
        roomsGroup.add(diningButton);
        roomsGroup.add(loungeButton);
        roomsGroup.add(libraryButton);
        roomsGroup.add(billiardButton);
        roomsGroup.add(conservButton);
        roomsGroup.add(kitchenButton);
        roomsGroup.add(ballroomButton);

        Border butBorder = BorderFactory.createEtchedBorder();
        butBorder = BorderFactory.createTitledBorder(butBorder, "Room");

        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        roomPanel.add(studyButton);
        roomPanel.add(hallButton);
        roomPanel.add(diningButton);
        roomPanel.add(loungeButton);
        roomPanel.add(libraryButton);
        roomPanel.add(billiardButton);
        roomPanel.add(conservButton);
        roomPanel.add(kitchenButton);
        roomPanel.add(ballroomButton);
        roomPanel.setBorder(butBorder);

        studyButton.addActionListener(this);
        roomPanel.add(studyButton);
        hallButton.addActionListener(this);
        roomPanel.add(hallButton);
        diningButton.addActionListener(this);
        roomPanel.add(diningButton);
        loungeButton.addActionListener(this);
        roomPanel.add(loungeButton);
        libraryButton.addActionListener(this);
        roomPanel.add(libraryButton);
        billiardButton.addActionListener(this);
        roomPanel.add(billiardButton);
        conservButton.addActionListener(this);
        roomPanel.add(conservButton);
        kitchenButton.addActionListener(this);
        roomPanel.add(kitchenButton);
        ballroomButton.addActionListener(this);
        roomPanel.add(ballroomButton);


        scarletButton = new JRadioButton("Miss Scarlet");
        mustardButton = new JRadioButton("Colonel Mustard");
        whiteButton = new JRadioButton("Mrs. White");
        greenButton = new JRadioButton("Mr. Green");
        peacockButton = new JRadioButton("Mrs. Peacock");
        plumButton = new JRadioButton("Professor Plum");

        ButtonGroup personGroup = new ButtonGroup();
        personGroup.add(scarletButton);
        personGroup.add(mustardButton);
        personGroup.add(whiteButton);
        personGroup.add(greenButton);
        personGroup.add(peacockButton);
        personGroup.add(plumButton);

        Border personBorder = BorderFactory.createEtchedBorder();
        personBorder = BorderFactory.createTitledBorder(personBorder, "Suspect");

        JPanel suspectPanel = new JPanel();
        suspectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        suspectPanel.add(scarletButton);
        suspectPanel.add(mustardButton);
        suspectPanel.add(whiteButton);
        suspectPanel.add(greenButton);
        suspectPanel.add(peacockButton);
        suspectPanel.add(plumButton);
        suspectPanel.setBorder(personBorder);

        scarletButton.addActionListener(this);
        suspectPanel.add(scarletButton);
        mustardButton.addActionListener(this);
        suspectPanel.add(mustardButton);
        whiteButton.addActionListener(this);
        suspectPanel.add(whiteButton);
        greenButton.addActionListener(this);
        suspectPanel.add(greenButton);
        peacockButton.addActionListener(this);
        suspectPanel.add(peacockButton);
        plumButton.addActionListener(this);
        suspectPanel.add(plumButton);


        submit = new JButton("Submit");
        submit.setVerticalTextPosition(AbstractButton.CENTER);
        submit.setHorizontalTextPosition(AbstractButton.LEADING);
        submit.setMnemonic(KeyEvent.VK_D);

        // Display in the same order as detectiveNotes
        this.setLayout(new GridLayout(4,1));
        this.add(suspectPanel);
        this.add(weaponPanel);
        this.add(roomPanel);
        this.add(submit);
        
        submit.addActionListener (new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
				String r;
				String w;
				String s;
				WeaponCard wc = new WeaponCard();
				RoomCard rc = new RoomCard();
				SuspectCard sc = new SuspectCard();
	        		
				for(Enumeration<AbstractButton> buttons= weaponsGroup.getElements(); buttons.hasMoreElements();)  
				{
					Object wbutton = buttons.nextElement();
	        			 
	        			if(((AbstractButton) wbutton).isSelected())
	        			{
	        				w = ((AbstractButton) wbutton).getText();
	        				wc.setName(w);	 
	        			}	 
				}
	        		 
				for(Enumeration<AbstractButton> buttons= roomsGroup.getElements(); buttons.hasMoreElements();)  
	        		{
					Object rbutton = buttons.nextElement();
	        			 
	        			if(((AbstractButton) rbutton).isSelected())
	        			{
	        				r = ((AbstractButton) rbutton).getText();
	        				rc.setName(r);	 
	        			}	 
	        		}
	        		 
				for(Enumeration<AbstractButton> buttons= personGroup.getElements(); buttons.hasMoreElements();)  
	        		{
					Object rbutton = buttons.nextElement();
	        			 
	        			if(((AbstractButton) rbutton).isSelected())
	        			{
	        				s = ((AbstractButton) rbutton).getText();
	        				sc.setName(s);	 
	        			}	 
	        		}
	        		
	        		// Player can submit a suggestion if available
        			Suggestion sug = new Suggestion(rc, wc, sc);
        			client.sendTCP(sug);
				// now endTurn
				client.sendTCP(new EndTurn());
        			jFrame.dispose();
	        	}
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}

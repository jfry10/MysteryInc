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

public class AccusationGUI extends JFrame implements ActionListener
{
    
    private JRadioButton knifeButton, pistolButton, ropeButton, candlestickButton, leadPipeButton, wrenchButton;
    private JRadioButton studyButton, hallButton, diningButton, loungeButton, libraryButton, billiardButton, conservButton, kitchenButton, ballroomButton;
    private JRadioButton scarletButton, mustardButton, whiteButton, greenButton, peacockButton, plumButton;
    protected JButton submit;
    
    JFrame jFrame;

	public AccusationGUI(Client client)
	{
		jFrame = this;
		setTitle("Make Accusation");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        knifeButton = new JRadioButton(Constants.WEAPONS[Constants.KNIFE]);
        pistolButton = new JRadioButton(Constants.WEAPONS[Constants.PISTOL]);
        ropeButton = new JRadioButton(Constants.WEAPONS[Constants.ROPE]);
        candlestickButton = new JRadioButton(Constants.WEAPONS[Constants.CANDLESTICK]);
        leadPipeButton = new JRadioButton(Constants.WEAPONS[Constants.LEAD_PIPE]);
        wrenchButton = new JRadioButton(Constants.WEAPONS[Constants.WRENCH]);

        ButtonGroup weaponGroup = new ButtonGroup();
        weaponGroup.add(knifeButton);
        weaponGroup.add(pistolButton);
        weaponGroup.add(ropeButton);
        weaponGroup.add(candlestickButton);
        weaponGroup.add(leadPipeButton);
        weaponGroup.add(wrenchButton);

        Border weaponBorder = BorderFactory.createEtchedBorder();
        weaponBorder = BorderFactory.createTitledBorder(weaponBorder, "Weapon");

        JPanel weaponPanel = new JPanel();
        weaponPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        weaponPanel.add(knifeButton);
        weaponPanel.add(pistolButton);
        weaponPanel.add(ropeButton);
        weaponPanel.add(candlestickButton);
        weaponPanel.add(leadPipeButton);
        weaponPanel.add(wrenchButton);
        weaponPanel.setBorder(weaponBorder);

        knifeButton.addActionListener(this);
        weaponPanel.add(knifeButton);
        pistolButton.addActionListener(this);
        weaponPanel.add(pistolButton);
        ropeButton.addActionListener(this);
        weaponPanel.add(ropeButton);
        candlestickButton.addActionListener(this);
        weaponPanel.add(candlestickButton);
        leadPipeButton.addActionListener(this);
        weaponPanel.add(leadPipeButton);
        wrenchButton.addActionListener(this);
        weaponPanel.add(wrenchButton);


        studyButton = new JRadioButton(Constants.ROOMS[Constants.STUDY]);
        hallButton = new JRadioButton(Constants.ROOMS[Constants.HALL]);
        diningButton = new JRadioButton(Constants.ROOMS[Constants.DINING_ROOM]);
        loungeButton = new JRadioButton(Constants.ROOMS[Constants.LOUNGE]);
        libraryButton = new JRadioButton(Constants.ROOMS[Constants.LIBRARY]);
        billiardButton = new JRadioButton(Constants.ROOMS[Constants.BILLIARD_ROOM]);
        conservButton = new JRadioButton(Constants.ROOMS[Constants.CONSERVATORY]);
        kitchenButton = new JRadioButton(Constants.ROOMS[Constants.KITCHEN]);
        ballroomButton = new JRadioButton(Constants.ROOMS[Constants.BALL_ROOM]);

        ButtonGroup roomGroup = new ButtonGroup();
        roomGroup.add(studyButton);
        roomGroup.add(hallButton);
        roomGroup.add(diningButton);
        roomGroup.add(loungeButton);
        roomGroup.add(libraryButton);
        roomGroup.add(billiardButton);
        roomGroup.add(conservButton);
        roomGroup.add(kitchenButton);
        roomGroup.add(ballroomButton);

        Border roomBorder = BorderFactory.createEtchedBorder();
        roomBorder = BorderFactory.createTitledBorder(roomBorder, "Room");

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
        roomPanel.setBorder(roomBorder);

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


        scarletButton = new JRadioButton(Constants.SUSPECTS[Constants.MISS_SCARLET]);
        mustardButton = new JRadioButton(Constants.SUSPECTS[Constants.COL_MUSTARD]);
        whiteButton = new JRadioButton(Constants.SUSPECTS[Constants.MRS_WHITE]);
        greenButton = new JRadioButton(Constants.SUSPECTS[Constants.MR_GREEN]);
        peacockButton = new JRadioButton(Constants.SUSPECTS[Constants.MRS_PEACOCK]);
        plumButton = new JRadioButton(Constants.SUSPECTS[Constants.PROF_PLUM]);

        ButtonGroup suspectGroup = new ButtonGroup();
        suspectGroup.add(scarletButton);
        suspectGroup.add(mustardButton);
        suspectGroup.add(whiteButton);
        suspectGroup.add(greenButton);
        suspectGroup.add(peacockButton);
        suspectGroup.add(plumButton);

        Border suspectBorder = BorderFactory.createEtchedBorder();
        suspectBorder = BorderFactory.createTitledBorder(suspectBorder, "Suspect");

        JPanel suspectPanel = new JPanel();
        suspectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        suspectPanel.add(scarletButton);
        suspectPanel.add(mustardButton);
        suspectPanel.add(whiteButton);
        suspectPanel.add(greenButton);
        suspectPanel.add(peacockButton);
        suspectPanel.add(plumButton);
        suspectPanel.setBorder(suspectBorder);

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


        submit = new JButton("\"You only get one accusation! If you are incorrect, you forfeit the rest of your turns.\n\nSubmit");
        submit.setVerticalTextPosition(AbstractButton.CENTER);
        submit.setHorizontalTextPosition(AbstractButton.LEADING);
        submit.setMnemonic(KeyEvent.VK_D);

        // Display in same order as detectiveNotes
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
	        		
				for(Enumeration<AbstractButton> buttons= weaponGroup.getElements(); buttons.hasMoreElements();)  
				{
					Object wbutton = buttons.nextElement();
	        			 
	        			if(((AbstractButton) wbutton).isSelected())
	        			{
	        				w = ((AbstractButton) wbutton).getText();
	        				wc.setName(w);	 
	        			}	 
				}
	        		 
				for(Enumeration<AbstractButton> buttons= roomGroup.getElements(); buttons.hasMoreElements();)  
	        		{
					Object rbutton = buttons.nextElement();
	        			 
	        			if(((AbstractButton) rbutton).isSelected())
	        			{
	        				r = ((AbstractButton) rbutton).getText();
	        				rc.setName(r);	 
	        			}	 
	        		}
	        		 
				for(Enumeration<AbstractButton> buttons= suspectGroup.getElements(); buttons.hasMoreElements();)  
	        		{
					Object rbutton = buttons.nextElement();
	        			 
	        			if(((AbstractButton) rbutton).isSelected())
	        			{
	        				s = ((AbstractButton) rbutton).getText();
	        				sc.setName(s);	 
	        			}	 
	        		}
	        		
//	        		// Warn the Player that they only get one accusation
//	        		int dialogButton = JOptionPane.YES_NO_OPTION;
//	        		int dialogResult = JOptionPane.showConfirmDialog(
//	        				null, 
//	        				"You only get one accusation! If you are incorrect, you forfeit the rest of your turns. Are you sure you want to submit?", 
//	        				"Making Accusation", 
//	        				dialogButton);
//	        		if(dialogResult == JOptionPane.YES_OPTION)
//	        		{ 
	        			Accusation acc = new Accusation(rc, wc, sc);
	        			client.sendTCP(acc);
    					// now endTurn
    					client.sendTCP(new EndTurn());
	        			jFrame.dispose();
	        		//}
	        	}
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}

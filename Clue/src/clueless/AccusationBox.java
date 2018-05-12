package clueless;

import javax.swing.*;
import javax.swing.border.Border;

import com.esotericsoftware.kryonet.Client;

import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

public class AccusationBox extends JPanel implements ActionListener {
    private JRadioButton knifeButton, revolverButton, ropeButton, candlestickButton, leadPipeButton, wrenchButton;
    private JRadioButton studyButton, hallButton, diningButton, loungeButton, libraryButton, billiardButton, conservButton, kitchenButton, ballroomButton;
    private JRadioButton scarletButton, mustardButton, whiteButton, greenButton, peacockButton, plumButton;
    private Client client;
    protected JButton submit;
    
    public AccusationBox(Client c) {
    		client  = c;
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

        JPanel buttonPannel = new JPanel();
        buttonPannel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPannel.add(knifeButton);
        buttonPannel.add(revolverButton);
        buttonPannel.add(ropeButton);
        buttonPannel.add(candlestickButton);
        buttonPannel.add(leadPipeButton);
        buttonPannel.add(wrenchButton);
        buttonPannel.setBorder(buttonBorder);

        knifeButton.addActionListener(this);
        buttonPannel.add(knifeButton);
        revolverButton.addActionListener(this);
        buttonPannel.add(revolverButton);
        ropeButton.addActionListener(this);
        buttonPannel.add(ropeButton);
        candlestickButton.addActionListener(this);
        buttonPannel.add(candlestickButton);
        leadPipeButton.addActionListener(this);
        buttonPannel.add(leadPipeButton);
        wrenchButton.addActionListener(this);
        buttonPannel.add(wrenchButton);


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

        JPanel butPanel = new JPanel();
        butPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        butPanel.add(studyButton);
        butPanel.add(hallButton);
        butPanel.add(diningButton);
        butPanel.add(loungeButton);
        butPanel.add(libraryButton);
        butPanel.add(billiardButton);
        butPanel.add(conservButton);
        butPanel.add(kitchenButton);
        butPanel.add(ballroomButton);
        butPanel.setBorder(butBorder);

        studyButton.addActionListener(this);
        butPanel.add(studyButton);
        hallButton.addActionListener(this);
        butPanel.add(hallButton);
        diningButton.addActionListener(this);
        butPanel.add(diningButton);
        loungeButton.addActionListener(this);
        butPanel.add(loungeButton);
        libraryButton.addActionListener(this);
        butPanel.add(libraryButton);
        billiardButton.addActionListener(this);
        butPanel.add(billiardButton);
        conservButton.addActionListener(this);
        butPanel.add(conservButton);
        kitchenButton.addActionListener(this);
        butPanel.add(kitchenButton);
        ballroomButton.addActionListener(this);
        butPanel.add(ballroomButton);


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

        JPanel personPanel = new JPanel();
        personPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        personPanel.add(scarletButton);
        personPanel.add(mustardButton);
        personPanel.add(whiteButton);
        personPanel.add(greenButton);
        personPanel.add(peacockButton);
        personPanel.add(plumButton);
        personPanel.setBorder(personBorder);

        scarletButton.addActionListener(this);
        personPanel.add(scarletButton);
        mustardButton.addActionListener(this);
        personPanel.add(mustardButton);
        whiteButton.addActionListener(this);
        personPanel.add(whiteButton);
        greenButton.addActionListener(this);
        personPanel.add(greenButton);
        peacockButton.addActionListener(this);
        personPanel.add(peacockButton);
        plumButton.addActionListener(this);
        personPanel.add(plumButton);


        submit = new JButton("Submit");
        submit.setVerticalTextPosition(AbstractButton.CENTER);
        submit.setHorizontalTextPosition(AbstractButton.LEADING);
        submit.setMnemonic(KeyEvent.VK_D);
        /*submit.addActionListener(this);*/

        this.setLayout(new GridLayout(4,1));
        this.add(buttonPannel);
        this.add(butPanel);
        this.add(personPanel);
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
        		 
        		 for(Enumeration<AbstractButton> buttons= roomsGroup.getElements(); buttons.hasMoreElements();)  
        		 {
        			 Object rbutton = buttons.nextElement();
        			 
        			 if(((AbstractButton) rbutton).isSelected())
        			 {
        				 s = ((AbstractButton) rbutton).getText();
        				 sc.setName(s);	 
        			 }	 
        		 }
        		
        		 /////
        		 int dialogButton = JOptionPane.YES_NO_OPTION;
        			int dialogResult = JOptionPane.showConfirmDialog(
        					null, 
        					"You only get one accusation! If you are incorrect, you forfeit the rest of your turns. Are you sure you want to submit?", 
        					"Making Accusation", 
        					dialogButton);
        			if(dialogResult == JOptionPane.YES_OPTION)
        			{ 
        				Accusation acc = new Accusation(rc, wc, sc);
        				client.sendTCP(acc);
        			}
        		 
        		 
        		 
        		 
        		 
        		/*ButtonModel wbutton = weaponsGroup.getSelection();	
        		weaponsGroup.getElements().getActionCommand();
        		 System.out.println(wbutton);*/
        	}
        	
        	
        });
        
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 System.out.println(e);
		
	}
	
	public void getWeapon()
	{
		boolean isWeapon = false;
		int i = 0;
		while (isWeapon == false)
		{
			
		}
		
	}

}
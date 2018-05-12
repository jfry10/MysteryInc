package clueless;

import javax.swing.*;

import com.esotericsoftware.kryonet.Client;

public class AccusationGui extends JFrame {
	
	public AccusationGui(Client c) {
		setTitle("Make Accusation");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new AccusationBox(c);

        this.add(panel);
	}

}

package clueless;

import javax.swing.*;

import com.esotericsoftware.kryonet.Client;



public class main extends JFrame {
    public main(Client c) {
        setTitle("Make Suggestion");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new box(c);

        this.add(panel);

    }

}






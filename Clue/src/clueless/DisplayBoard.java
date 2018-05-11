package clueless;

import java.awt.*;  
import javax.swing.*;  
import java.awt.event.*;  

public class DisplayBoard {
    public DisplayBoard() {
        JFrame frame = new JFrame("Display Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JPanel panel = (JPanel)frame.getContentPane();
 
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("C:/Users/robert.spinosa/.vscode/workspace/GameBoard/ImageOfBoard.PNG"));// your image here
        panel.add(label);
 
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main (String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DisplayBoard();
            }
        });
    }
}
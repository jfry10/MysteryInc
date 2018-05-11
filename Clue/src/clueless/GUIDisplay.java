package clueless;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIDisplay extends JPanel{

	public void paint(Graphics g){

		 Image image = new ImageIcon(this.getClass().getResource("ImageOfBoard.png")).getImage();
            g.drawImage(image, 0, 0, this);

            g.setColor(Color.RED);
            g.fillOval(138, 116, 20, 20);   //x value for col 1 is 138
                                            //y value for row 1 is 117
            g.setColor(Color.GREEN);
            g.fillOval(138, 260, 20, 20);   //y value for row 3 is 260

            g.setColor(Color.BLUE);
            g.fillOval(138, 189, 20, 20);   //y value for row 2 is 189

            g.setColor(Color.YELLOW);
            g.fillOval(138, 332, 20, 20);   //y value for row 4 is 332

            g.setColor(Color.PINK);
            g.fillOval(138, 404, 20, 20);   //y value for row 5 is 404

            g.setColor(Color.BLACK);        //x value for col 2 is
            g.fillOval(210, 116, 20, 20);

            g.setColor(Color.GRAY);         //x value for cole 3 is
            g.fillOval(282, 116, 20, 20);
            g.setColor(Color.CYAN);         //x value for cole 4 is
            g.fillOval(354, 116, 20, 20);
            g.setColor(Color.magenta);      //x value for cole 5 is
            g.fillOval(426, 116, 20, 20);
        
	}
	
	public static void main(String[] args){
        JFrame frame= new JFrame("GameboardGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
        frame.getContentPane().add(new GUIDisplay());
        frame.setLocationRelativeTo(null);
        frame.setSize(601, 575);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);		
    }
    
    private static int getXforLocation(){
        return 1;
    }

    private static int getYforLocation(){
        return 1;
    }
}
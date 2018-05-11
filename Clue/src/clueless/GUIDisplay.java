package clueless;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIDisplay extends JPanel{

	private int ROW_1 = 116;
	private int ROW_2 = 188;
	private int ROW_3 = 260;
	private int ROW_4 = 332;
	private int ROW_5 = 404;

	private int COL_1 = 138;
	private int COL_2 = 210;
	private int COL_3 = 282;
	private int COL_4 = 354;
	private int COL_5 = 426;

	private Color scarlet = new Color(220,20,60);
	private Color green = new Color(0,100,0);
	private Color mustard = new Color(218,165,32);
	private Color white = new Color(192,192,192);
	private Color peacock = new Color(30,144,255);
	private Color plum = new Color(75,0,130);

	public void paint(Graphics g){

		 Image image = new ImageIcon(this.getClass().getResource("ImageOfBoard.png")).getImage();
            g.drawImage(image, 0, 0, this);

            
            //paintStudy(suspectColor);
            g.setColor(Color.RED);
            g.fillOval(138, 116, 20, 20);   //x value for col 1 is 138
                                            //y value for row 1 is 116
            g.setColor(Color.GREEN);
            g.fillOval(138, 260, 20, 20);   //y value for row 3 is 260

            g.setColor(Color.BLUE);
            g.fillOval(138, 188, 20, 20);   //y value for row 2 is 188

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
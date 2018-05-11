package clueless;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIDisplay extends JPanel{
	
	Player[] playerlist = null;
	int x0 = 0;
	int y0 = 0;
	int x1 = 0;
	int y1 = 0;
	int x2 = 0;
	int y2 = 0;
	int x3 = 0;
	int y3 = 0;
	int x4 = 0;
	int y4 = 0;
	int x5 = 0;
	int y5 = 0;

	private Color scarlet = new Color(220,20,60);
	private Color green   = new Color(0,100,0);
	private Color mustard = new Color(218,165,32);
	private Color white   = new Color(192,192,192);
	private Color peacock = new Color(30,144,255);
	private Color plum    = new Color(75,0,130);

	public GUIDisplay(Player[] players) {
		playerlist = players;
		setLocations();
	}
	
	public GUIDisplay(){
		
	}

	public void paint(Graphics g)
	{
		Image image = new ImageIcon(this.getClass().getResource("ImageOfBoard.png")).getImage();
		g.drawImage(image, 0, 0, this);
    
		if (playerlist != null)
		{
	        // Get Player 1 color
			if (playerlist[0] != null)
			{
				g.setColor(getPlayersColor(playerlist[0]));
				g.fillOval(x0, y0, 20, 20);
			}
            
	        // Get Player 2 color
			if (playerlist[1] != null)
			{
				g.setColor(getPlayersColor(playerlist[1]));
				g.fillOval(x1, y1, 20, 20);
			}
            
	        // Get Player 3 color
			if (playerlist[2] != null)
			{
				g.setColor(getPlayersColor(playerlist[2]));
				g.fillOval(x2, y2, 20, 20);
			}
            
	        // Get Player 4 color
			if (playerlist[3] != null)
			{
				g.setColor(getPlayersColor(playerlist[3]));
				g.fillOval(x3, y3, 20, 20);
			}
            
	        // Get Player 5 color
			if (playerlist[4] != null)
			{
				g.setColor(getPlayersColor(playerlist[4]));
				g.fillOval(x4, y4, 20, 20);
			}
            
	        // Get Player 6 color
			if (playerlist[5] != null)
			{
				g.setColor(getPlayersColor(playerlist[5]));
				g.fillOval(x5, y5, 20, 20);
			}
		}
		else {
            g.setColor(Color.GREEN);
            g.fillOval(138, 116, 20, 20);}//x value for col 1 is 138
            /*                               //y value for row 1 is 117
             * 
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
            */
        
	}
	
	private Color getPlayersColor(Player player)
	{
		String name = player.suspectName;
		Color result = null;
		if (name == Constants.MISS_SCARLET_STR)
		{
			result = scarlet;
		}
		if (name == Constants.COL_MUSTARD_STR)
		{
			result = mustard;
		}
		if (name == Constants.MRS_WHITE_STR)
		{
			result = white;
		}
		if (name == Constants.MR_GREEN_STR)
		{
			result = green;
		}
		if (name == Constants.MRS_PEACOCK_STR)
		{
			result = peacock;
		}
		if (name == Constants.PROF_PLUM_STR)
		{
			result = plum;
		}
		return result;
	}

	public static void main(String[] args){
		Player player1 = new Player(Constants.MISS_SCARLET_STR);
	    	Player player2 = new Player(Constants.COL_MUSTARD_STR);
	    	Player player3 = new Player(Constants.MRS_WHITE_STR);
	    	Player player4 = new Player(Constants.MR_GREEN_STR);
	    	Player player5 = new Player(Constants.MRS_PEACOCK_STR);
	    	Player player6 = new Player(Constants.PROF_PLUM_STR);
	    	Player[] players = {player1, player2, player3,player4,player5,player6};
	    	Location[][] gameboard = Gameboard.createNewBoard(players);

        JFrame frame= new JFrame("GameboardGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
        frame.getContentPane().add(new GUIDisplay(players));
        frame.setLocationRelativeTo(null);
        frame.setSize(601, 575);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);		
    }

	private void setLocations()
	{
		if (playerlist[0] != null)
		{
			x0 = getXforLocation(playerlist[0].positionOnBoard.getRow());
			y0 = getYforLocation(playerlist[0].positionOnBoard.getCol());
		}
		if (playerlist[1] != null)
		{
			x1 = getXforLocation(playerlist[1].positionOnBoard.getRow());
			y1 = getYforLocation(playerlist[1].positionOnBoard.getCol());
		}
		if (playerlist[2] != null)
		{
			x2 = getXforLocation(playerlist[2].positionOnBoard.getRow());
			y2 = getYforLocation(playerlist[2].positionOnBoard.getCol());
		}
		if (playerlist[3] != null)
		{
			x3 = getXforLocation(playerlist[3].positionOnBoard.getRow());
			y3 = getYforLocation(playerlist[3].positionOnBoard.getCol());
		}
		if (playerlist[4] != null)
		{
			x4 = getXforLocation(playerlist[4].positionOnBoard.getRow());
			y4 = getYforLocation(playerlist[4].positionOnBoard.getCol());
		}
		if (playerlist[5] != null)
		{
			x5 = getXforLocation(playerlist[5].positionOnBoard.getRow());
			y5 = getYforLocation(playerlist[5].positionOnBoard.getCol());	
		}
	}

    private static int getXforLocation(int row)
    {
        if(row == 0) {
        		return 138;
        }
        else if(row == 1) {
        		return 210;
        }
        else if(row == 2) {
        		return 282;
        }
        else if(row == 3) {
        		return 354;
        }
        else { //row==4
        		return 426;
        }
    }

    public static int getYforLocation(int col)
    {
    		if(col == 0) {
    			return 116;
        }
        else if(col == 1) {
        		return 188;
        }
        else if(col == 2) {
        		return 260;
        }
        else if(col == 3) {
        		return 332;
        }
        else{ //col==4
        		return 404;
        }
    }
}
package clueless;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIDisplay extends JPanel
{
	
	Player[] playerlist = null;
	Location[][] gameBoard = null;
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

	Color player0 = Color.BLACK;
	Color player1 = Color.BLACK;
	Color player2 = Color.BLACK;
	Color player3 = Color.BLACK;
	Color player4 = Color.BLACK;
	Color player5 = Color.BLACK;

	private Color scarlet = new Color(220,20,60);
	private Color green   = new Color(0,100,0);
	private Color mustard = new Color(218,165,32);
	private Color white   = new Color(128,128,128);
	private Color peacock = new Color(30,144,255);
	private Color plum    = new Color(75,0,130);
	
	public GUIDisplay() {
		
	}

	public GUIDisplay(Gameboard gb)
	{
		playerlist = gb.getPlayerlist();
		gameBoard = gb.getGameboard();

		if (playerlist != null)
		{
			System.out.println("paint something");
			setLocations();

			player0 = getPlayersColor(playerlist[0]);
			player1 = getPlayersColor(playerlist[1]);
			player2 = getPlayersColor(playerlist[2]);
			player3 = getPlayersColor(playerlist[3]);
			player4 = getPlayersColor(playerlist[4]);
			player5 = getPlayersColor(playerlist[5]);
		}
		else
		{
			System.out.println("nope");
		}
	}

	public void paint(Graphics g)
	{
		Image image = new ImageIcon(this.getClass().getResource("ImageOfBoard.png")).getImage();
		g.drawImage(image, 0, 0, this);
		System.out.println("paint is called");
		if (playerlist != null)
		{

	        // Get Player 1 color
			if (playerlist[0] != null)
			{
				g.setColor(getPlayersColor(playerlist[0]));
				g.fillOval(y0, x0, 20, 20);
			}
            
	        // Get Player 2 color
			if (playerlist[1] != null)
			{
				g.setColor(getPlayersColor(playerlist[1]));
				g.fillOval(y1, x1, 20, 20);
			}
            
	        // Get Player 3 color
			if (playerlist[2] != null)
			{
				g.setColor(getPlayersColor(playerlist[2]));
				g.fillOval(y2, x2, 20, 20);
			}
            
	        // Get Player 4 color
			if (playerlist[3] != null)
			{
				g.setColor(getPlayersColor(playerlist[3]));
				g.fillOval(y3, x3, 20, 20);
			}
            
	        // Get Player 5 color
			if (playerlist[4] != null)
			{
				g.setColor(getPlayersColor(playerlist[4]));
				g.fillOval(y4, x4, 20, 20);
			}
            
	        // Get Player 6 color
			if (playerlist[5] != null)
			{
				g.setColor(getPlayersColor(playerlist[5]));
				g.fillOval(y5, x5, 20, 20);
			}
		}
	}
	
	private Color getPlayersColor(Player player)
	{
		String name = player.suspectName;
		Color result = null;
		System.out.println(name);
		if (name == Constants.MISS_SCARLET_STR)
		{
			result = scarlet;
		}
		else if (name == Constants.COL_MUSTARD_STR)
		{
			result = mustard;
		}
		else if (name == Constants.MRS_WHITE_STR)
		{
			result = white;
		}
		else if (name == Constants.MR_GREEN_STR)
		{
			result = green;
		}
		else if (name == Constants.MRS_PEACOCK_STR)
		{
			result = peacock;
		}
		else if (name == Constants.PROF_PLUM_STR)
		{
			result = plum;
		}
		return result;
	}

//	public static void main(String[] args){
//		Player player1 = new Player(Constants.MISS_SCARLET_STR);
//	    	Player player2 = new Player(Constants.COL_MUSTARD_STR);
//	    	Player player3 = new Player(Constants.MRS_WHITE_STR);
//	    	Player player4 = new Player(Constants.MR_GREEN_STR);
//	    	Player player5 = new Player(Constants.MRS_PEACOCK_STR);
//	    	Player player6 = new Player(Constants.PROF_PLUM_STR);
//	    	Player[] players = {player1, player2, player3,player4,player5,player6};
//	    	Gameboard.createNewBoard(players);
//	    /*Gameboard.moveDown(gameboard, players[5]);
//	    Gameboard.moveUp(gameboard, players[4]);
//	    	Gameboard.moveLeft(gameboard, players[3]);
//	    	Gameboard.moveUp(gameboard, players[3]);
//	    Gameboard.moveUp(gameboard, players[3]);
//	    	Gameboard.moveLeft(gameboard, players[2]);
//	    	Gameboard.moveLeft(gameboard, players[2]);
//	    	Gameboard.moveLeft(gameboard, players[2]);
//	    Gameboard.moveUp(gameboard, players[2]);
//	    Gameboard.moveUp(gameboard, players[2]);
//	    	Gameboard.moveUp(gameboard, players[1]);
//	    	Gameboard.takePassage(gameboard, players[1]);
//	    	Gameboard.moveUp(gameboard, players[1]);
//	    Gameboard.moveUp(gameboard, players[1]);
//	    	Gameboard.moveRight(gameboard, players[0]);
//	    	Gameboard.takePassage(gameboard, players[0]);
//	    Gameboard.moveUp(gameboard, players[0]);
//	    Gameboard.moveUp(gameboard, players[0]);*/
//
//        JFrame frame= new JFrame("GameboardGUI");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
//        frame.getContentPane().add(new GUIDisplay(players));
//        frame.setLocationRelativeTo(null);
//        frame.setSize(601, 575);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(true);		
//    }

	private void setLocations()
	{
		if (playerlist[0] != null)
		{
			x0 = getXforLocation(playerlist[0].positionOnBoard.getRow());
			y0 = getYforLocation(playerlist[0].positionOnBoard.getCol());
			if (playerlist[0].positionOnBoard instanceof Room)
			{
				x0 -= 25; // off-center
				y0 -= 25; // off-center
			}
		}
		if (playerlist[1] != null)
		{
			x1 = getXforLocation(playerlist[1].positionOnBoard.getRow());
			y1 = getYforLocation(playerlist[1].positionOnBoard.getCol());
			if (playerlist[1].positionOnBoard instanceof Room)
			{
				x1 -= 15; // off-center
				y1 -= 15; // off-center
			}
		}
		if (playerlist[2] != null)
		{
			x2 = getXforLocation(playerlist[2].positionOnBoard.getRow());
			y2 = getYforLocation(playerlist[2].positionOnBoard.getCol());
			if (playerlist[2].positionOnBoard instanceof Room)
			{
				x2 -= 5; // off-center
				y2 -= 5; // off-center
			}
		}
		if (playerlist[3] != null)
		{
			x3 = getXforLocation(playerlist[3].positionOnBoard.getRow());
			y3 = getYforLocation(playerlist[3].positionOnBoard.getCol());
			if (playerlist[3].positionOnBoard instanceof Room)
			{
				x3 += 5; // off-center
				y3 += 5; // off-center
			}
		}
		if (playerlist[4] != null)
		{
			x4 = getXforLocation(playerlist[4].positionOnBoard.getRow());
			y4 = getYforLocation(playerlist[4].positionOnBoard.getCol());
			if (playerlist[4].positionOnBoard instanceof Room)
			{
				x4 += 15; // off-center
				y4 += 15; // off-center
			}
		}
		if (playerlist[5] != null)
		{
			x5 = getXforLocation(playerlist[5].positionOnBoard.getRow());
			y5 = getYforLocation(playerlist[5].positionOnBoard.getCol());
			if (playerlist[5].positionOnBoard instanceof Room)
			{
				x5 += 25; // off-center
				y5 += 25; // off-center
			}
		}
	}

    private static int getYforLocation(int row) //, int index)
    {
    		int y_cord = 0;

        if(row == 0){
        		y_cord = 138;
        }
        else if(row == 1){
        		y_cord = 210;
        }
        else if(row == 2){
        		y_cord = 282;
        }
        else if(row == 3){
        		y_cord = 354;
        }
        else{ //row == 4
        		y_cord = 426;
        }
        return y_cord;
    }

    public static int getXforLocation(int col) //, int index)
    {
    		int x_cord = 0;

    		if(col == 0){
    			x_cord = 116;
        }
        else if(col == 1){
        		x_cord = 188;
        }
        else if(col == 2){
        		x_cord = 260;
        }
        else if(col == 3){
        		x_cord = 332;
        }
        else{ //col == 4
        		x_cord = 404;
        }
    		return x_cord;
    }
}
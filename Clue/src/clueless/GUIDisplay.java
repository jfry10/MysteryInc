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
			setLocations();
		}
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

		if (name.equals(Constants.MISS_SCARLET_STR))
		{
			result = scarlet;
		}
		else if (name.equals(Constants.COL_MUSTARD_STR))
		{
			result = mustard;
		}
		else if (name.equals(Constants.MRS_WHITE_STR))
		{
			result = white;
		}
		else if (name.equals(Constants.MR_GREEN_STR))
		{
			result = green;
		}
		else if (name.equals(Constants.MRS_PEACOCK_STR))
		{
			result = peacock;
		}
		else if (name.equals(Constants.PROF_PLUM_STR))
		{
			result = plum;
		}
		return result;
	}

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
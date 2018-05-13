package clueless;

import java.util.ArrayList;

public class Room extends Location
{
	
    boolean passage = false;
    String roomName;
    ArrayList<Player> playerSpace;
    
    public Room() { }

    public Room(int number, int row, int col)
    {
        determineRoom(number);
	    	setName(roomName);
	    	this.row = row;
	    	this.col = col;
    }

    public void createPassage(int row, int col)
    {
        this.passage = true;
        passageRow = row;
        passageCol = col;
    }
    
    public boolean hasSecretPassage()
    {
        return this.passage;
    }
    
    private String determineRoom(int number)
    {
    		if(number == 0) {
			roomName = Constants.ROOMS[Constants.STUDY];
			setRight();
			setDown();
    			createPassage(4,4);
    		}
    		else if(number == 1) {
			roomName = Constants.ROOMS[Constants.HALL];
			setLeft();
			setRight();
			setDown();
    		}
    		else if(number == 2) {
			roomName = Constants.ROOMS[Constants.LOUNGE];
			setLeft();
			setDown();
    			createPassage(4, 0);
    		}
    		else if(number == 3) {
			roomName = Constants.ROOMS[Constants.LIBRARY];
			setUp();
			setDown();
			setRight();
    		}
    		else if(number == 4) {
			roomName = Constants.ROOMS[Constants.BILLIARD_ROOM];
			setUp();
			setDown();
			setLeft();
			setRight();
    		}
    		else if(number == 5) {
			roomName = Constants.ROOMS[Constants.DINING_ROOM];
			setLeft();
			setUp();
			setDown();
    		}
    		else if(number == 6) {
			roomName = Constants.ROOMS[Constants.CONSERVATORY];
			setUp();
			setRight();
    			createPassage(0,4);
    		}
    		else if(number == 7) {
			roomName = Constants.ROOMS[Constants.BALL_ROOM];
			setLeft();
			setRight();
			setUp();
    		}
    		else if(number == 8) {
			roomName = Constants.ROOMS[Constants.KITCHEN];
			setUp();
			setLeft();
    			createPassage(0, 0);
    		}
    	
    		return roomName;
    }
}

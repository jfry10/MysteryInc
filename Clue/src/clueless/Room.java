package clueless;

import java.util.ArrayList;

public class Room extends Location
{
	
    boolean passage = false;
    String roomName;
    ArrayList<Player> playerSpace;

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
			roomName = "Study";
			setRight();
			setDown();
    			createPassage(4,4);
    		}
    		else if(number == 1) {
			roomName = "Hall";
			setLeft();
			setRight();
			setDown();
    		}
    		else if(number == 2) {
			roomName = "Lounge";
			setLeft();
			setDown();
    			createPassage(4, 0);
    		}
    		else if(number == 3) {
			roomName = "Library";
			setUp();
			setDown();
			setRight();
    		}
    		else if(number == 4) {
			roomName = "Billiard_Room";
			setUp();
			setDown();
			setLeft();
			setRight();
    		}
    		else if(number == 5) {
			roomName = "Dining_Room";
			setLeft();
			setUp();
			setDown();
    		}
    		else if(number == 6) {
			roomName = "Conservatory";
			setUp();
			setRight();
    			createPassage(0,4);
    		}
    		else if(number == 7) {
			roomName = "Ballroom";
			setLeft();
			setRight();
			setUp();
    		}
    		else if(number == 8) {
			roomName = "Kitchen";
			setUp();
			setLeft();
    			createPassage(0, 0);
    		}
    	
    		return roomName;
    }
}

package clueless;

import java.util.ArrayList;

public class Gameboard
{
	public Player[] playerlist;
	public Location[][] gameBoard;

	public Gameboard()
	{
		playerlist = null; // wait until we create a board
		gameBoard = null; // wait until we create board
	}

	//Creates a new gameboard
    public void createNewBoard(Player[] listOfPlayers)
    {
		int roomNum = 0;
		int hallNum = 0;
		playerlist = listOfPlayers;
		gameBoard = new Location[5][5];

		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if((i%2 == 0) && ((j % 2) == 0)) {
					gameBoard[i][j] = new Room(roomNum,i,j);
					roomNum++;
                	}
                	else if((i%2 != 0) && (j%2 == 0)) {
					gameBoard[i][j] = new Hallway(hallNum,i,j);
					hallNum++;
                	}
                	else if((i%2 != 0) && (j%2 != 0)) {
                		gameBoard[i][j] = null;
                	}
                	else {
					gameBoard[i][j] = new Hallway(hallNum,i,j);
					hallNum++;
                	}
            	}
        	}
		
		// Update Player Locations
	    	for(int i=0;i<playerlist.length;i++) {
	    		startPositions(playerlist[i]);
	    	}

    }

    //prints the board in a crude textual format - needs to be formatted is used
    public void printBoard()
    {
    		System.out.println("\n\n\n");	
    		for(int i = 0; i < 5; i++) {
    			for(int j = 0; j < 5; j++) {
    				if(gameBoard[i][j] != null) {
    					System.out.print(gameBoard[i][j].getName());
    					if (gameBoard[i][j].getName().length() > 8) {
    						System.out.print("\t\t");
    					}
    					else {
    						System.out.print("\t\t\t");
    					}
    				}
    				else {
    					System.out.print("\t\t\t");
    				}
    			}
            System.out.println("\n\n\n");	          
    		}        
    }
    
    //used to display which rooms have passages
    public void printPassages()
    {
		for(int k=0; k<5;k++)
		{
		    for(int j=0;j<5;j++)
		    {
		        if(gameBoard[k][j] instanceof Room && ((Room) gameBoard[k][j]).hasSecretPassage())
		        {
		            	System.out.println(gameBoard[k][j].getName() + " has secret passage");
		        }
		    }
		}
    }
     
    //list the possible moves a specific player has, based on their location
    public String listMoves(Player p)
    {
		String moves = "";
    		int index;
    		// get player in the list
    		for (index = 0; index < Constants.SUSPECTS.length; index++)
    		{
    			if (p.suspectName.equals(playerlist[index].suspectName))
    			{
    				break;
    			}
    		}

    		// The logic is the same for all directions. 
    		// If the position is a Hallway and not occupied, or if it's a Room, then add it to the move list
  
		if (playerlist[index].positionOnBoard.hasLeft())
		{
			if (moveValidLeft(p))
			{
				moves += "Left ";
			}
		}
		if (playerlist[index].positionOnBoard.hasUp())
		{
			if (moveValidUp(p))
			{
				moves += "Up ";
			}
		}
		if (playerlist[index].positionOnBoard.hasRight())
		{
			if (moveValidRight(p))
			{
				moves += "Right ";
			}
		}
		if (playerlist[index].positionOnBoard.hasDown())
		{
			if (moveValidDown(p))
			{
				moves += "Down ";
			}
		}
		if (playerlist[index].positionOnBoard instanceof Room)
		{
    			if(((Room) playerlist[index].positionOnBoard).hasSecretPassage())
    			{
    				moves += "Secret_Passage ";
    			}
        	}

    		return moves;
    }
    
    public void moveLeft(Player p)
    {
    		Location newSpace;
    		int index;
    		// get player in the list
    		for (index = 0; index < Constants.SUSPECTS.length; index++)
    		{
    			if (p.suspectName.equals(playerlist[index].suspectName))
    			{
    				break;
    			}
    		}
    		try {
    			if(moveValidLeft(p))
    			{
    				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col].leaveRoom(playerlist[index]);
    				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1].enterRoom(playerlist[index]);
		    		newSpace = gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1];
		    		playerlist[index].positionOnBoard = newSpace;    
	    		}
    		} catch(ArrayIndexOutOfBoundsException e) {
    			System.out.println("Unable to move left");
    		}
    }
    
    public void moveRight(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
    		try {
    			if(moveValidRight(p))
    			{
    				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col].leaveRoom(playerlist[index]);
    				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1].enterRoom(playerlist[index]);
		    		newSpace = gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1];
		    		playerlist[index].positionOnBoard = newSpace;    	
    			}
    		} catch(ArrayIndexOutOfBoundsException e) {
    			System.out.println("Unable to move left");
    		}
    }
    
    public void moveUp(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
	    	try {
	    		if(moveValidUp(p))
	    		{
	    			gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col].leaveRoom(playerlist[index]);
	    			gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col].enterRoom(playerlist[index]);
		    		newSpace = gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col];
		    		playerlist[index].positionOnBoard = newSpace;
	    		}
	    	} catch(ArrayIndexOutOfBoundsException e) {
	    		System.out.println("Unable to move Up");
	    	}
    }
    
    public void moveDown(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
    		try {
    			if(moveValidDown(p))
    			{
    				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col].leaveRoom(playerlist[index]);
    				gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col].enterRoom(playerlist[index]);
		    		newSpace = gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col];
		    		playerlist[index].positionOnBoard = newSpace;  
    			}
    		} catch(ArrayIndexOutOfBoundsException e) {
    			System.out.println("Unable to move Down");
    		}
    }
    
    public void takePassage(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
	    	try {
	    		if(((Room) playerlist[index].positionOnBoard).hasSecretPassage())
    			{
	    			gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col].leaveRoom(playerlist[index]);
	    			gameBoard[playerlist[index].positionOnBoard.passageRow][playerlist[index].positionOnBoard.passageCol].enterRoom(playerlist[index]);
		    		newSpace = gameBoard[playerlist[index].positionOnBoard.passageRow][playerlist[index].positionOnBoard.passageCol];
		    		playerlist[index].positionOnBoard = newSpace;
    			}
	    	} catch(ArrayIndexOutOfBoundsException e) {
	    		System.out.println("Unable to take passage");
	    	}
    }
 
    public boolean moveValidUp(Player p)
    {
		boolean result = false;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
		// valid movement
		if (((gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col] instanceof Hallway) &&
			!gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col].isOccupied()) ||
			gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col] instanceof Room)
		{
			result = true;
		}
		
		return result;
    }

    public boolean moveValidRight(Player p)
    {
		boolean result = false;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
		// valid movement
		if (((gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1] instanceof Hallway) &&
			!gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1].isOccupied())  ||
			gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1] instanceof Room)
		{
			result = true;
		}
		
		return result;
    }

    public boolean moveValidDown(Player p)
    {
		boolean result = false;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
		// valid movement
		if (((gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col] instanceof Hallway) && 
			!gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col].isOccupied()) || 
			gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col] instanceof Room)
		{
			result = true;
		}
		
		return result;    	
    }

    public boolean moveValidLeft(Player p)
    {
		boolean result = false;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}
		// valid movement
		if (((gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1] instanceof Hallway) &&
			!gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1].isOccupied()) ||
			gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1] instanceof Room)
		{
			result = true;
		}
    		
    		return result;
    }

    public void suspectMove(SuspectCard suspectCard, RoomCard roomCard)
    {
	    	String suspect = suspectCard.getName();
	    	String roomName = roomCard.getName();
	    	int index;
	    	int newRoomRow = 0;
	    	int newRoomCol = 0;
	    	Location newSpace;

	    	try {
		    	for(index = 0; index < playerlist.length; index++)
		    	{
		    		if(playerlist[index].suspectName.equals(suspect))
		    		{
		    			break;
		    		}    		
		    	}
		    	if(roomName.equals(Constants.ROOMS[Constants.HALL])) {
		    		newRoomRow = 0;
		    		newRoomCol = 2;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.LOUNGE])) {
		    		newRoomRow = 0;
		    		newRoomCol = 4;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.DINING_ROOM])) {
		    		newRoomRow = 2;
		    		newRoomCol = 4;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.KITCHEN])) {
		    		newRoomRow = 4;
		    		newRoomCol = 4;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.BALL_ROOM])) {
		    		newRoomRow = 4;
		    		newRoomCol = 2;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.CONSERVATORY])) {
		    		newRoomRow = 4;
		    		newRoomCol = 0;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.BILLIARD_ROOM])) {
		    		newRoomRow = 2;
		    		newRoomCol = 2;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.LIBRARY])) {
		    		newRoomRow = 2;
		    		newRoomCol = 0;
		    	}
		    	else if(roomName.equals(Constants.ROOMS[Constants.STUDY])) {
				newRoomRow = 0;
		    		newRoomCol = 0;
			}
			newSpace = gameBoard[newRoomRow][newRoomCol];
			gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col].leaveRoom(playerlist[index]);
			gameBoard[newRoomRow][newRoomCol].enterRoom(playerlist[index]);
			playerlist[index].positionOnBoard = newSpace;
			
	    } catch(Exception e) {
	    		System.out.println("Issue moving Suspect from Suggestion");
	    	}
    }
    
    //creates and sets the starting locations for each player up to 6.
    public void startPositions(Player p)
    {
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName.equals(playerlist[index].suspectName))
			{
				break;
			}
		}

	    	if(p.suspectName.equals(Constants.MISS_SCARLET_STR)) {
	    		gameBoard[0][3].enterRoom(playerlist[index]); // Hall-to-Lounge Hallway
	    		playerlist[index].positionOnBoard = gameBoard[0][3];
	    	}
	    	else if(p.suspectName.equals(Constants.COL_MUSTARD_STR)) {
	    		gameBoard[1][4].enterRoom(playerlist[index]); // Lounge-to-Dining Room Hallway
	    		playerlist[index].positionOnBoard = gameBoard[1][4];
	    	}
	    	else if(p.suspectName.equals(Constants.MRS_WHITE_STR)) {
	    		gameBoard[4][3].enterRoom(playerlist[index]); // Ballroom-to-Kitchen Hallway
	    		playerlist[index].positionOnBoard = gameBoard[4][3];
	    	}
	    	else if(p.suspectName.equals(Constants.MR_GREEN_STR)) {
	    		gameBoard[4][1].enterRoom(playerlist[index]); // Conservatory-to-Ballroom Hallway
	    		playerlist[index].positionOnBoard = gameBoard[4][1];
	    	}
	    	else if(p.suspectName.equals(Constants.MRS_PEACOCK_STR)) {
	    		gameBoard[3][0].enterRoom(playerlist[index]); // Library-to-Conservatory Hallway
	    		playerlist[index].positionOnBoard = gameBoard[3][0];
	    	}
	    	else if(p.suspectName.equals(Constants.PROF_PLUM_STR)) {
	    		gameBoard[1][0].enterRoom(playerlist[index]); // Study-to-Library Hallway
	    		playerlist[index].positionOnBoard = gameBoard[1][0];
	    	}
    }
    
    public String listToString(ArrayList list) {
		String occupants = "";

	    	Player[] playersInRoom = new Player[list.size()];
	    	list.toArray(playersInRoom);
	    	for(int i = 0; i< playersInRoom.length; i++) {
	    		occupants = occupants + playersInRoom[i].suspectName + " ";
	    	}    	
	    	return occupants;
    }
    
    public Player[] getPlayerlist()
    {
    		return playerlist;
    }
    
    public Location[][] getGameboard()
    {
    		return gameBoard;
    }

}
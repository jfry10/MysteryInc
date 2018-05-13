package clueless;

import java.util.ArrayList;

public class Gameboard
{
	public static Player[] playerlist;
	public static Location[][] gameBoard;

	//Creates a new gameboard
    public static void createNewBoard(Player[] listOfPlayers)
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
                	else if((i%2 != 0) && (j%2 ==0)) {
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
    public static void printBoard()
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
    public static void printPassages()
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
    public static String listMoves(Player p)
    {
		String moves = "";
    		int index;
    		// get player in the list
    		for (index = 0; index < Constants.SUSPECTS.length; index++)
    		{
    			if (p.suspectName == playerlist[index].suspectName)
    			{
    				break;
    			}
    		}

    		// The logic is the same for all directions. 
    		// If the position is a Hallway and not occupied, or if it's a Room, then add it to the move list
  
		if (playerlist[index].positionOnBoard.hasLeft())
		{
			if (((gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1] instanceof Hallway) && 
				!gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1].isOccupied()) || 
					gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1] instanceof Room)
			{
				moves += "Left ";
			}
		}
		if (playerlist[index].positionOnBoard.hasUp())
		{
			if (((gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col] instanceof Hallway) &&
				!gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col].isOccupied()) || 
				gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col] instanceof Room)
			{
				moves += "Up ";
			}
		}
		if (playerlist[index].positionOnBoard.hasRight())
		{
			if (((gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1] instanceof Hallway) &&
				!gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1].isOccupied()) || 
				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1] instanceof Room)
			{
				moves += "Right ";
			}
		}
		if (playerlist[index].positionOnBoard.hasDown())
		{
			if (((gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col] instanceof Hallway) && 
				!gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col].isOccupied()) ||
				gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col] instanceof Room)
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
    
    public static void moveLeft(Player p)
    {
    		Location newSpace;
    		int index;
    		// get player in the list
    		for (index = 0; index < Constants.SUSPECTS.length; index++)
    		{
    			if (p.suspectName == playerlist[index].suspectName)
    			{
    				break;
    			}
    		}
    		try {
    			if(((gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1] instanceof Hallway) &&
				!gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1].isOccupied()) ||
    				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col - 1] instanceof Room)
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
    
    public static void moveRight(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName == playerlist[index].suspectName)
			{
				break;
			}
		}
    		try {
    			if(((gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1] instanceof Hallway) &&
				!gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1].isOccupied()) ||
    				gameBoard[playerlist[index].positionOnBoard.row][playerlist[index].positionOnBoard.col + 1] instanceof Room)
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
    
    public static void moveUp(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName == playerlist[index].suspectName)
			{
				break;
			}
		}
	    	try {
	    		if(((gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col] instanceof Hallway) &&
    				!gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col].isOccupied()) ||
	    			gameBoard[playerlist[index].positionOnBoard.row - 1][playerlist[index].positionOnBoard.col] instanceof Room)
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
    
    public static void moveDown(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName == playerlist[index].suspectName)
			{
				break;
			}
		}
    		try {
    			if(((gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col] instanceof Hallway) && 
				!gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col].isOccupied()) ||
    				gameBoard[playerlist[index].positionOnBoard.row + 1][playerlist[index].positionOnBoard.col] instanceof Room)
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
    
    public static void takePassage(Player p)
    {
		Location newSpace;
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName == playerlist[index].suspectName)
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
    
    public static void suspectMove(SuspectCard suspectCard, RoomCard roomCard)
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
		    		if(playerlist[index].suspectName == suspect)
		    		{
		    			break;
		    		}    		
		    	}
		    	if(roomName == "Hall") {
		    		newRoomRow = 0;
		    		newRoomCol = 2;
		    	}
		    	else if(roomName == "Lounge") {
		    		newRoomRow = 0;
		    		newRoomCol = 4;
		    	}
		    	else if(roomName == "Dining Room") {
		    		newRoomRow = 2;
		    		newRoomCol = 4;
		    	}
		    	else if(roomName == "Kitchen") {
		    		newRoomRow = 4;
		    		newRoomCol = 4;
		    	}
		    	else if(roomName == "Ball Room") {
		    		newRoomRow = 4;
		    		newRoomCol = 2;
		    	}
		    	else if(roomName == "Conservatory") {
		    		newRoomRow = 4;
		    		newRoomCol = 0;
		    	}
		    	else if(roomName == "Billiard Room") {
		    		newRoomRow = 2;
		    		newRoomCol = 2;
		    	}
		    	else if(roomName == "Library") {
		    		newRoomRow = 2;
		    		newRoomCol = 0;
		    	}
		    	else if(roomName == "Study") {
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
    public static void startPositions(Player p)
    {
		int index;
		// get player in the list
		for (index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (p.suspectName == playerlist[index].suspectName)
			{
				break;
			}
		}

	    	if(p.suspectName == Constants.MISS_SCARLET_STR) {
	    		gameBoard[0][3].enterRoom(playerlist[index]); // Hall-to-Lounge Hallway
	    		playerlist[index].positionOnBoard = gameBoard[0][3];
	    	}
	    	else if(p.suspectName == Constants.COL_MUSTARD_STR) {
	    		gameBoard[1][4].enterRoom(playerlist[index]); // Lounge-to-Dining Room Hallway
	    		playerlist[index].positionOnBoard = gameBoard[1][4];
	    	}
	    	else if(p.suspectName == Constants.MRS_WHITE_STR) {
	    		gameBoard[4][3].enterRoom(playerlist[index]); // Ballroom-to-Kitchen Hallway
	    		playerlist[index].positionOnBoard = gameBoard[4][3];
	    	}
	    	else if(p.suspectName == Constants.MR_GREEN_STR) {
	    		gameBoard[4][1].enterRoom(playerlist[index]); // Conservatory-to-Ballroom Hallway
	    		playerlist[index].positionOnBoard = gameBoard[4][1];
	    	}
	    	else if(p.suspectName == Constants.MRS_PEACOCK_STR) {
	    		gameBoard[3][0].enterRoom(playerlist[index]); // Library-to-Conservatory Hallway
	    		playerlist[index].positionOnBoard = gameBoard[3][0];
	    	}
	    	else if(p.suspectName == Constants.PROF_PLUM_STR) {
	    		gameBoard[1][0].enterRoom(playerlist[index]); // Study-to-Library Hallway
	    		playerlist[index].positionOnBoard = gameBoard[1][0];
	    	}
    }
    
    public static String listToString(ArrayList list) {
		String occupants = "";

	    	Player[] playersInRoom = new Player[list.size()];
	    	list.toArray(playersInRoom);
	    	for(int i = 0; i< playersInRoom.length; i++) {
	    		occupants = occupants + playersInRoom[i].suspectName + " ";
	    	}    	
	    	return occupants;
    }
    
    public static Player[] getPlayerlist()
    {
    		return playerlist;
    }
    
    public static Location[][] getGameboard()
    {
    		return gameBoard;
    }
    
//    public static void main (String[] args) {
//	    	Player player1 = new Player("Scarlet");
//	    	Player player2 = new Player("Mustard");
//	    	Player player3 = new Player("White");
//	    	Player player4 = new Player("Green");
//	    	Player player5 = new Player("Peacock");
//	    	Player player6 = new Player("Plum");
//	    	Player[] players = {player1, player2, player3,player4,player5,player6};
//	    	createNewBoard(players);
//	    	
//	    	for(int j=0;j<5;j++) {
//	    		for(int k=0; k<5;k++) {
//	    			if(gameBoard[j][k] != null) {
//	    				if(gameBoard[j][k].isOccupied()) {
//	    					String room = gameBoard[j][k].getName();
//	    					String player = listToString(gameBoard[j][k].occupiedBy);
//	    					System.out.println("The "+room+" is occupied by "+player);
//	    				}
//	    			}
//	    		}
//	    	}
//	    	
//	    	moveLeft(player1);
//	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
//	    	
//	    	moveDown(player1);
//	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
//	    	moveDown(player1);
//	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
//	    	
//	    	moveRight(player1);
//	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
//	    	moveRight(player1);
//	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
//	    	
//	    	moveUp(player1);
//	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
//	    	
//	    	moveLeft(player4);
//	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
//	    	
//	    	takePassage(player4);
//	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
//	    	takePassage(player4);
//	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
//	    	
//	    	moveUp(player4);
//	    	moveUp(player4);
//	    	moveUp(player4);
//	    	moveUp(player4);
//	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
//	    	takePassage(player4);
//	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
//	    	takePassage(player4);
//	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
//    	
//    }
}
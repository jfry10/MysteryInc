package clueless;

import java.util.ArrayList;

public class Gameboard{
	
	static Player[] playerlist;

	//Creates a new gameboard
    public static Location[][] createNewBoard(Player[] listOfPlayers)
    {
		int roomNum = 0;
		int hallNum = 0;
		playerlist = listOfPlayers;
		Location[][] gameBoard = new Location[5][5];

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
	    		startPositions(gameBoard, playerlist[i],playerlist[i].suspectName);
	    	}

        return gameBoard;
    }

    //prints the board in a crude textual format - needs to be formatted is used
    public static void printBoard(Location[][] currentBoard)
    {
    		System.out.println("\n\n\n");	
    		for(int i = 0; i < 5; i++) {
    			for(int j = 0; j < 5; j++) {
    				if(currentBoard[i][j] != null) {
    					System.out.print(currentBoard[i][j].getName());
    					if (currentBoard[i][j].getName().length() > 8) {
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
    public static void printPassages(Location[][] currentBoard){
		for(int k=0; k<5;k++) {
		    for(int j=0;j<5;j++) {
		        if(currentBoard[k][j] instanceof Room) {
		            if(((Room) currentBoard[k][j]).hasSecretPassage()) {
		            		System.out.println(currentBoard[k][j].getName() +" has secret passage");
		            }
		        }
		    }
		}
    }
     
    //list the possible moves a specific player has, based on their location
    public static String listMoves(Location[][] board, Player player)
    {
    		String moves = "";

    		// The logic is the same for all directions. 
    		// If the position is a Hallway and not occupied, or if it's a Room, then add it to the move list
  
		if (player.positionOnBoard.hasLeft())
		{
			if (((board[player.positionOnBoard.row][player.positionOnBoard.col - 1] instanceof Hallway) && 
				!board[player.positionOnBoard.row][player.positionOnBoard.col - 1].isOccupied()) || 
				board[player.positionOnBoard.row][player.positionOnBoard.col - 1] instanceof Room)
			{
				moves += "Left ";
			}
		}
		if (player.positionOnBoard.hasUp())
		{
			if (((board[player.positionOnBoard.row - 1][player.positionOnBoard.col] instanceof Hallway) &&
				!board[player.positionOnBoard.row - 1][player.positionOnBoard.col].isOccupied()) || 
				board[player.positionOnBoard.row - 1][player.positionOnBoard.col] instanceof Room)
			{
				moves += "Up ";
			}
		}
		if (player.positionOnBoard.hasRight())
		{
			if (((board[player.positionOnBoard.row][player.positionOnBoard.col + 1] instanceof Hallway) &&
				!board[player.positionOnBoard.row][player.positionOnBoard.col + 1].isOccupied()) || 
				board[player.positionOnBoard.row][player.positionOnBoard.col + 1] instanceof Room)
			{
				moves += "Right ";
			}
		}
		if (player.positionOnBoard.hasDown())
		{
			if (((board[player.positionOnBoard.row + 1][player.positionOnBoard.col] instanceof Hallway) && 
				!board[player.positionOnBoard.row + 1][player.positionOnBoard.col].isOccupied()) ||
				board[player.positionOnBoard.row + 1][player.positionOnBoard.col] instanceof Room)
			{
				moves += "Down ";
			}
		}
		if (player.positionOnBoard instanceof Room)
		{
    			if(((Room) player.positionOnBoard).hasSecretPassage())
    			{
    				moves += "Secret_Passage ";
    			}
        	}

    		return moves;
    }
    
    public static void moveLeft(Location [][] board, Player player)
    {
    		Location newSpace;
    		try {
    			if(((board[player.positionOnBoard.row][player.positionOnBoard.col - 1] instanceof Hallway) &&
				!board[player.positionOnBoard.row][player.positionOnBoard.col - 1].isOccupied()) ||
				board[player.positionOnBoard.row][player.positionOnBoard.col - 1] instanceof Room)
    			{
		    		board[player.positionOnBoard.row][player.positionOnBoard.col].leaveRoom(player);
		    		board[player.positionOnBoard.row][player.positionOnBoard.col - 1].enterRoom(player);
		    		newSpace = board[player.positionOnBoard.row][player.positionOnBoard.col - 1];
		    		player.positionOnBoard = newSpace;    
	    		}
    		} catch(ArrayIndexOutOfBoundsException e) {
    			System.out.println("Unable to move left");
    		}
    }
    
    public static void moveRight(Location [][] board, Player player)
    {
    		Location newSpace;
    		try {
    			if(((board[player.positionOnBoard.row][player.positionOnBoard.col + 1] instanceof Hallway) &&
				!board[player.positionOnBoard.row][player.positionOnBoard.col + 1].isOccupied()) ||
				board[player.positionOnBoard.row][player.positionOnBoard.col + 1] instanceof Room)
    			{
		    		board[player.positionOnBoard.row][player.positionOnBoard.col].leaveRoom(player);
		    		board[player.positionOnBoard.row][player.positionOnBoard.col + 1].enterRoom(player);
		    		newSpace = board[player.positionOnBoard.row][player.positionOnBoard.col + 1];
		    		player.positionOnBoard = newSpace;    	
    			}
    		} catch(ArrayIndexOutOfBoundsException e) {
    			System.out.println("Unable to move left");
    		}
    }
    
    public static void moveUp(Location [][] board, Player player) {
	    	Location newSpace;
	    	try {
	    		if(((board[player.positionOnBoard.row - 1][player.positionOnBoard.col] instanceof Hallway) &&
    				!board[player.positionOnBoard.row - 1][player.positionOnBoard.col].isOccupied()) ||
    				board[player.positionOnBoard.row - 1][player.positionOnBoard.col] instanceof Room)
	    		{
		    		board[player.positionOnBoard.row][player.positionOnBoard.col].leaveRoom(player);
		    		board[player.positionOnBoard.row - 1][player.positionOnBoard.col].enterRoom(player);
		    		newSpace = board[player.positionOnBoard.row - 1][player.positionOnBoard.col];
		    		player.positionOnBoard = newSpace;
	    		}
	    	} catch(ArrayIndexOutOfBoundsException e) {
	    		System.out.println("Unable to move Up");
	    	}
    }
    
    public static void moveDown(Location [][] board, Player player)
    {
    		Location newSpace;
    		try {
    			if(((board[player.positionOnBoard.row + 1][player.positionOnBoard.col] instanceof Hallway) && 
				!board[player.positionOnBoard.row + 1][player.positionOnBoard.col].isOccupied()) ||
				board[player.positionOnBoard.row + 1][player.positionOnBoard.col] instanceof Room)
    			{
		    		board[player.positionOnBoard.row][player.positionOnBoard.col].leaveRoom(player);
		    		board[player.positionOnBoard.row + 1][player.positionOnBoard.col].enterRoom(player);
		    		newSpace = board[player.positionOnBoard.row + 1][player.positionOnBoard.col];
		    		player.positionOnBoard = newSpace;  
    			}
    		} catch(ArrayIndexOutOfBoundsException e) {
    			System.out.println("Unable to move Down");
    		}
    }
    
    public static void takePassage(Location [][] board, Player player) {
	    	Location newSpace;
	    	try {
	    		if(((Room) player.positionOnBoard).hasSecretPassage())
    			{
		    		board[player.positionOnBoard.row][player.positionOnBoard.col].leaveRoom(player);
		    		board[player.positionOnBoard.passageRow][player.positionOnBoard.passageCol].enterRoom(player);
		    		newSpace = board[player.positionOnBoard.passageRow][player.positionOnBoard.passageCol];
		    		player.positionOnBoard = newSpace;
    			}
	    	} catch(ArrayIndexOutOfBoundsException e) {
	    		System.out.println("Unable to take passage");
	    	}
    }
    
    public static void suspectMove(Location[][] board, SuspectCard suspectCard, RoomCard roomCard)
    {
	    	String suspect = suspectCard.getName();
	    	String roomName = roomCard.getName();
	    	Player playerToMove = null;
	    	int newRoomRow = 0;
	    	int newRoomCol = 0;
	    	Location newSpace;

	    	try {
		    	for(int i = 0; i < playerlist.length; i++) {
		    		if(playerlist[i].suspectName == suspect) {
		    			playerToMove = playerlist[i];
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
			newSpace = board[newRoomRow][newRoomCol];
			board[playerToMove.positionOnBoard.row][playerToMove.positionOnBoard.col].leaveRoom(playerToMove);
			board[newRoomRow][newRoomCol].enterRoom(playerToMove);
			playerToMove.positionOnBoard = newSpace;
			
	    } catch(Exception e) {
	    		System.out.println("Issue moving Suspect from Suggestion");
	    	}
    }
    //
    
    //creates and sets the starting locations for each player up to 6.
    public static void startPositions(Location[][] board, Player player, String suspectName) {

	    	if(suspectName == Constants.MISS_SCARLET_STR) {
	    		board[0][3].enterRoom(player); // Hall-to-Lounge Hallway
	    		player.positionOnBoard = board[0][3];
	    	}
	    	else if(suspectName == Constants.COL_MUSTARD_STR) {
	    		board[1][4].enterRoom(player); // Lounge-to-Dining Room Hallway
	    		player.positionOnBoard = board[1][4];
	    	}
	    	else if(suspectName == Constants.MRS_WHITE_STR) {
	    		board[4][3].enterRoom(player); // Ballroom-to-Kitchen Hallway
	    		player.positionOnBoard = board[4][3];
	    	}
	    	else if(suspectName == Constants.MR_GREEN_STR) {
	    		board[4][1].enterRoom(player); // Conservatory-to-Ballroom Hallway
	    		player.positionOnBoard = board[4][1];
	    	}
	    	else if(suspectName == Constants.MRS_PEACOCK_STR) {
	    		board[3][0].enterRoom(player); // Library-to-Conservatory Hallway
	    		player.positionOnBoard = board[3][0];
	    	}
	    	else if(suspectName == Constants.PROF_PLUM_STR) {
	    		board[1][0].enterRoom(player); // Study-to-Library Hallway
	    		player.positionOnBoard = board[1][0];
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
    
    public static void main (String[] args) {
	    	Player player1 = new Player("Scarlet");
	    	Player player2 = new Player("Mustard");
	    	Player player3 = new Player("White");
	    	Player player4 = new Player("Green");
	    	Player player5 = new Player("Peacock");
	    	Player player6 = new Player("Plum");
	    	Player[] players = {player1, player2, player3,player4,player5,player6};
	    	Location[][] gameboard = createNewBoard(players);
	    	
	    	for(int j=0;j<5;j++) {
	    		for(int k=0; k<5;k++) {
	    			if(gameboard[j][k] != null) {
	    				if(gameboard[j][k].isOccupied()) {
	    					String room = gameboard[j][k].getName();
	    					String player = listToString(gameboard[j][k].occupiedBy);
	    					System.out.println("The "+room+" is occupied by "+player);
	    				}
	    			}
	    		}
	    	}
	    	
	    	moveLeft(gameboard, player1);
	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
	    	
	    	moveDown(gameboard, player1);
	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
	    	moveDown(gameboard, player1);
	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
	    	
	    	moveRight(gameboard, player1);
	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
	    	moveRight(gameboard, player1);
	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
	    	
	    	moveUp(gameboard, player1);
	    	System.out.println("The " + player1.positionOnBoard.getName() + " is occupied by: " + listToString(player1.positionOnBoard.occupiedBy));
	    	
	    	moveLeft(gameboard, player4);
	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
	    	
	    	takePassage(gameboard, player4);
	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
	    	takePassage(gameboard, player4);
	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
	    	
	    	moveUp(gameboard,player4);
	    	moveUp(gameboard,player4);
	    	moveUp(gameboard,player4);
	    	moveUp(gameboard,player4);
	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
	    	takePassage(gameboard, player4);
	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
	    	takePassage(gameboard, player4);
	    	System.out.println("The " + player4.positionOnBoard.getName() + " is occupied by: " + listToString(player4.positionOnBoard.occupiedBy));
    	
    }
}
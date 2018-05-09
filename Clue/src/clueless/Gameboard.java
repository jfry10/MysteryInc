package clueless;

import java.util.ArrayList;

public class Gameboard{

	//Creates a new gameboard
    public static Location[][] createNewBoard(){
        int roomNum = 0;
        int hallNum = 0;
        Location[][] gameBoard = new Location[5][5];

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if((i%2 == 0) && ((j % 2) == 0)){
                  gameBoard[i][j] = new Room(roomNum);
                  roomNum++;
                }
                else if((i%2 != 0) && (j%2 ==0)){
                    gameBoard[i][j] = new Hallway(hallNum);
                    hallNum++;
                }
                else if((i%2 != 0) && (j%2 != 0)){
                  gameBoard[i][j] = null;
                }
                else{
                    gameBoard[i][j] = new Hallway(hallNum);
                    hallNum++;
                }
            }
        }
        return gameBoard;
    }

    //prints the board in a crude textual format - needs to be formatted is used
    public static void printBoard(Location[][] currentBoard){
      for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
              if(currentBoard[i][j] != null){
                  System.out.print(currentBoard[i][j].getName() + "   ");
              }
              else{
                  System.out.print("      ");
              }
            }
            System.out.println();	          
      }        
    }
    
    //used to display which rooms have passages
    public static void printPassages(Location[][] currentBoard){
        for(int k=0; k<5;k++) {
            for(int j=0;j<5;j++) {
                if(currentBoard[k][j] instanceof Room) {
                    if(((Room) currentBoard[k][j]).hasSecretPassage()) {
                        System.out.println(currentBoard[k][j].getName() +"  "+ ((Room)currentBoard[k][j]).hasSecretPassage());
                    }
                }
            }
        }
    }
     
    //list the possible moves a specific location has
    public static void listMoves(Location[][] board, int row, int col){
        System.out.print(board[row][col].getName() + " is able to move: ");
        if(board[row][col].hasLeft()){
            System.out.print("Left ");
        }
        if (board[row][col].hasUp()){
            System.out.print("Up ");
        }
        if (board[row][col].hasRight()){
            System.out.print("Right ");
        }
        if (board[row][col].hasDown()){
            System.out.print("Down ");
        }
        if (board[row][col] instanceof Room){
        	if(((Room) board[row][col]).hasSecretPassage()){
                System.out.print("and has Secret_Passage");
            }
        }
        	
        System.out.println();
    }
    
    //creates and sets the starting locations for each player up to 6.
    public static void startPositions(Location[][] board,Player player, int position) {
    	if(position == 0) {
    		board[0][3].enterRoom(player);
    	}
    	else if(position == 1) {
    		board[1][4].enterRoom(player);
    	}
    	else if(position == 2) {
    		board[4][3].enterRoom(player);
    	}
    	else if(position == 3) {
    		board[4][1].enterRoom(player);
    	}
    	else if(position == 4) {
    		board[3][0].enterRoom(player);
    	}
    	else if(position == 5) {
    		board[1][0].enterRoom(player);
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
    	Location[][] gameboard = createNewBoard();
    	Player player1 = new Player("Scarlet");
    	Player player2 = new Player("Mustard");
    	Player player3 = new Player("White");
    	Player player4 = new Player("Green");
    	Player player5 = new Player("Peacock");
    	Player player6 = new Player("Plum");
    	Player[] players = {player1, player2, player3,player4,player5,player6};
    	for(int i=0;i<players.length;i++) {
    		startPositions(gameboard,players[i],i);
    	}
    	
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
    	
    }
}
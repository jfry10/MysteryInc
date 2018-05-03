package clueless;

public class Gameboard{

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
        System.out.println();
    }
}
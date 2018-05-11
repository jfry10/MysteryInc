package clueless;

import java.util.ArrayList;

public class Location{
    String name = null;
    ArrayList<Player> occupiedBy = new ArrayList<Player>();
    int row;
    int col;
    int passageRow;
    int passageCol;
    boolean occupied = false;
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;

    public void setName(String newName){
        name = newName;
    }

    public String getName(){
        return name;
    }
    
    public int getRow() {
    		return row;
    }
    
    public int getCol() {
    		return col;
    }

    public boolean isOccupied(){
        return (!occupiedBy.isEmpty());
    }

    public boolean hasLeft(){
        return left;
    }

    public boolean hasRight(){
        return right;
    }

    public boolean hasUp(){
        return up;
    }

    public boolean hasDown(){
        return down;
    }

    public void setLeft(){
        left = true;
    }

    public void setRight(){
        right = true;
    }

    public void setUp(){
        up = true;
    }

    public void setDown(){
        down = true;
    }
    
    public String occupiedBy() {
    		return occupiedBy.toString();
    }
    
    public void enterRoom(Player player) {
    		occupiedBy.add(player);
    }
    
    public void leaveRoom(Player player) {
    		occupiedBy.remove(player);
    }    
}


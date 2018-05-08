package clueless;

public class Location{
    String name = null;
    Player occupiedBy = null;
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

    public boolean isOccupied(){
        return occupied;
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
    	return occupiedBy.suspectName;
    }
    
    public void changeOccupiedState(Player player) {
    	occupiedBy = player;
    	occupied = true;
    }
    
    public void changeOccupiedState() {
    	occupiedBy = null;
    	occupied = false;
    }
}


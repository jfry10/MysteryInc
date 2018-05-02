package clueless;

public class Room extends Location{
	
    boolean passage = false;
    String roomName;

    public Room(int number){
        determineRoom(number);
    	setName(roomName);
    }

    public void createPassage(){
        this.passage = true;
    }
    
    public boolean hasSecretPassage(){
        return this.passage;
    }
    
    private String determineRoom(int number) {
    	if(number == 0) {
			roomName = "Study";
			setRight();
			setDown();
    		createPassage();
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
    		createPassage();
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
    		createPassage();
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
    		createPassage();
    	}
    	
    	return roomName;
    }
}

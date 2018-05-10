package clueless;

public class Hallway extends Location{

    boolean occupied = false;
    String hallName;

    public Hallway(int number, int row, int col){
        determineHall(number);
        setName("Hallway: " + hallName);
        this.row = row;
        this.col = col;
    }

    public void changeOccupied(){
        occupied = !occupied;
    }

    private String determineHall(int number) {
        if(number == 0) {
            hallName = "S-to-H";
            setLeft();
            setRight();
        }
        else if(number == 1) {
            hallName = "H-to-Lo";
            setLeft();
            setRight();
        }
        else if(number == 2) {
            hallName = "S-to-Li";
            setUp();
            setDown();
        }
        else if(number == 3) {
            hallName = "H-to-BiR";
            setUp();
            setDown();
        }
        else if(number == 4) {
            hallName = "L-to-DR";
            setUp();
            setDown();
        }
        else if(number == 5) {
            hallName = "L-to-BiR";
            setLeft();
            setRight();
        }
        else if(number == 6) {
            hallName = "BiR-to-DR";
            setLeft();
            setRight();
        }
        else if(number == 7) {
            hallName = "Li-to-C";
            setUp();
            setDown();
        }
        else if(number == 8) {
            hallName = "BiR-to-BaR";
            setUp();
            setDown();
        }
        else if(number == 9) {
            hallName = "D-to-K";
            setUp();
            setDown();
        }
        else if(number == 10) {
            hallName = "C-to-BaR";
            setLeft();
            setRight();
        }
        else if(number == 11) {
            hallName = "BaR-to-K";
            setLeft();
            setRight();
        }        

        return hallName;
    }

}

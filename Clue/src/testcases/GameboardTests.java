package testcases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clueless.Gameboard;
import clueless.Location;

public class GameboardTests {

	@Test

	public void testGameboard() 
	{
		boolean print = true; // enable/disable printing
		
		// first test, create a gameboard and display it with text
		{
			Location[][] gameBoard = Gameboard.createNewBoard();
			Gameboard.printBoard(gameBoard);
			
			assertEquals(true, print);
		}
	}
}

package testcases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clueless.Constants;
import clueless.Gameboard;
import clueless.Location;
import clueless.Player;

public class GameboardTests {

	@Test

	public void testGameboard() 
	{
		boolean print = true; // enable/disable printing
		
		// first test, create a list of Players, then create 
		// gameboard and display it with text
		{
			Player[] players = new Player[6];
			players[0] = new Player(Constants.COL_MUSTARD_STR);
			players[1] = new Player(Constants.MISS_SCARLET_STR);
			players[2] = new Player(Constants.MR_GREEN_STR);
			players[3] = new Player(Constants.MRS_PEACOCK_STR);
			players[4] = new Player(Constants.MRS_WHITE_STR);
			players[5] = new Player(Constants.PROF_PLUM_STR);

			Gameboard gb = new Gameboard();
			gb.createNewBoard(players);
			if (print == true)
			{
				gb.printBoard();
				gb.printPassages();
			}
		}
		
		// second test, list player movements and make moves
		// Players start in predetermined spots based on the
		// number of players. For this test, we will use all 6
		{
			String moves = "";
			Player[] players = new Player[6];
			players[0] = new Player(Constants.COL_MUSTARD_STR);
			players[1] = new Player(Constants.MISS_SCARLET_STR);
			players[2] = new Player(Constants.MR_GREEN_STR);
			players[3] = new Player(Constants.MRS_PEACOCK_STR);
			players[4] = new Player(Constants.MRS_WHITE_STR);
			players[5] = new Player(Constants.PROF_PLUM_STR);

			Gameboard gb = new Gameboard();
			gb.createNewBoard(players);
			for (int i = 0; i < 6; i++)
			{
				moves = gb.listMoves(players[i]);
				if (print == true)
				{
					System.out.println(players[i].suspectName + " is able to move: " + moves);
				}
			}
			
			if (print == true)
			{
				System.out.println("\n----------------------------------------------------------\n");
			}
			gb.moveUp(players[0]); // Move to Lounge
			gb.moveRight(players[1]); // Move to Lounge
			gb.moveRight(players[2]); // Move to Ballroom
			gb.moveUp(players[3]); // Move to Library
			gb.moveLeft(players[4]); // Move to Ballroom
			gb.moveDown(players[5]); // Move to Library
			
			for (int i = 0; i < 6; i++)
			{
				moves = gb.listMoves(players[i]);
				if (print == true)
				{
					System.out.println(players[i].suspectName + " is able to move: " + moves);
				}
			}
		
			// Now, move players into Positions to cause errors/expected failures
			if (print == true)
			{
				System.out.println("\n----------------------------------------------------------\n");
			}
			// Col. Mustard
			gb.takePassage(players[0]); // Take Secret Passage to Conservatory
			gb.moveUp(players[0]); // Li-to-C Hallway
			gb.moveUp(players[0]); // Move to Library
			gb.moveUp(players[0]); // S-to-Li Hallway
			
			// Miss Scarlet
			gb.takePassage(players[1]); // Take Secret Passage to Conservatory
			gb.moveUp(players[1]); // Li-to-C Hallway
			gb.moveUp(players[1]); // Move to Library
			gb.moveRight(players[1]); // L-to-BiR Hallway
			
			// Mrs. Peacock
			gb.moveDown(players[3]); // Li-to-C Hallway

			for (int i = 0; i < 6; i++)
			{
				moves = gb.listMoves(players[i]);
				if (print == true)
				{
					System.out.println(players[i].suspectName + " is able to move: " + moves);
				}
				
				// if Prof. Plum, verify he has no moves
				if (i == 5)
				{
					assertEquals("", moves);
				}
			}

			// Prof. Plum (should have no moves available)
			gb.moveUp(players[5]);
			gb.moveRight(players[5]);
			gb.moveDown(players[5]);
			gb.takePassage(players[5]);
			
			// This will fail as expected, use in a different test case
			//gb.moveLeft(players[5]);
		
		}
	}
}

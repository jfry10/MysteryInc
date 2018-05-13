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

			Gameboard.createNewBoard(players);
			if (print == true)
			{
				Gameboard.printBoard();
				Gameboard.printPassages();
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

			Gameboard.createNewBoard(players);
			for (int i = 0; i < 6; i++)
			{
				moves = Gameboard.listMoves(players[i]);
				if (print == true)
				{
					System.out.println(players[i].suspectName + " is able to move: " + moves);
				}
			}
			
			if (print == true)
			{
				System.out.println("\n----------------------------------------------------------\n");
			}
			Gameboard.moveRight(players[0]); // Move to Lounge
			Gameboard.moveUp(players[1]); // Move to Lounge
			Gameboard.moveLeft(players[2]); // Move to Ballroom
			Gameboard.moveRight(players[3]); // Move to Ballroom
			Gameboard.moveUp(players[4]); // Move to Library
			Gameboard.moveDown(players[5]); // Move to Library
			
			for (int i = 0; i < 6; i++)
			{
				moves = Gameboard.listMoves(players[i]);
				if (print == true)
				{
					System.out.println(players[i].suspectName + " is able to move: " + moves);
				}
			}
		
			// Now, move players into Positions to cause errors/expected failures
			
			// Col. Mustard
			Gameboard.takePassage(players[0]); // Take Secret Passage to Conservatory
			Gameboard.moveUp(players[0]); // Li-to-C Hallway
			Gameboard.moveUp(players[0]); // Move to Library
			Gameboard.moveUp(players[0]); // S-to-Li Hallway
			
			// Miss Scarlet
			Gameboard.takePassage(players[1]); // Take Secret Passage to Conservatory
			Gameboard.moveUp(players[1]); // Li-to-C Hallway
			Gameboard.moveUp(players[1]); // Move to Library
			Gameboard.moveRight(players[1]); // L-to-BiR Hallway
			
			// Mrs. White
			Gameboard.moveDown(players[4]); // Li-to-C Hallway

			for (int i = 0; i < 6; i++)
			{
				moves = Gameboard.listMoves(players[i]);
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
			Gameboard.moveUp(players[5]);
			Gameboard.moveRight(players[5]);
			Gameboard.moveDown(players[5]);
			Gameboard.takePassage(players[5]);
			
			// This will fail as expected, use in a different test case
			//Gameboard.moveLeft(players[5]);
		
		}
	}
}

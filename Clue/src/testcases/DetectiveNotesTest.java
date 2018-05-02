package testcases;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import clueless.Constants;
import clueless.DetectiveNotes;

public class DetectiveNotesTest
{
	@Test
	
	public void testDetectiveNotes()
	{
		boolean print = false;
		
		// first test, print out values and true values
		{
			DetectiveNotes dn = new DetectiveNotes();
			dn.setSuspect(Constants.SUSPECTS[Constants.COL_MUSTARD]);
			dn.setSuspect(Constants.SUSPECTS[Constants.MR_GREEN]);
			dn.setSuspect(Constants.SUSPECTS[Constants.MISS_SCARLET]);

			dn.setWeapon(Constants.WEAPONS[Constants.KNIFE]);
			dn.setWeapon(Constants.WEAPONS[Constants.ROPE]);
			dn.setWeapon(Constants.WEAPONS[Constants.WRENCH]);
			dn.setWeapon(Constants.WEAPONS[Constants.CANDLESTICK]);

			dn.setRoom(Constants.ROOMS[Constants.BALL_ROOM]);
			dn.setRoom(Constants.ROOMS[Constants.HALL]);
			dn.setRoom(Constants.ROOMS[Constants.LIBRARY]);
			dn.setRoom(Constants.ROOMS[Constants.STUDY]);
			dn.setRoom(Constants.ROOMS[Constants.LOUNGE]);

			if (print == true)
			{
				System.out.println(dn.toString());
			}
			
			// Check that we correctly added Suspects
			int count = 0;
			boolean[] suspects = dn.getSuspects();
			for (int i = 0; i < suspects.length; i++)
			{
				if (suspects[i] == true) count++;
			}
			assertEquals(3, count);

			// Check that we correctly added Weapons
			count = 0;
			boolean[] weapons = dn.getWeapons();
			for (int i = 0; i < weapons.length; i++)
			{
				if (weapons[i] == true) count++;
			}
			assertEquals(4, count);

			// Check that we correctly added Rooms
			count = 0;
			boolean[] rooms = dn.getRooms();
			for (int i = 0; i < rooms.length; i++)
			{
				if (rooms[i] == true) count++;
			}
			assertEquals(5, count);
		}
		
		// second test, need to add something for setting all values in DetectiveNotes
	}

}

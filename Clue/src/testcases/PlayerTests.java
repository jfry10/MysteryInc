package testcases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clueless.Constants;
import clueless.DetectiveNotes;
import clueless.Player;

public class PlayerTests
{
	@Test
	
	public void testPlayer()
	{
		boolean print = true; // enable/disable printing
		
		// first test, general Player set/get function
		{
			Player p = new Player();
			p.updateSuspectName("Mr. Green");
			assertEquals("Mr. Green", p.suspectName);
			p.updateDetectiveNotes(Constants.WEAPON_CARD, "Knife");
            // add more testing
		}
	}
}

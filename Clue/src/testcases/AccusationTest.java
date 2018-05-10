package testcases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clueless.Accusation;
import clueless.Card;
import clueless.CardDeck;
import clueless.CaseFile;
import clueless.Constants;
import clueless.DetectiveNotes;
import clueless.Player;
import clueless.RoomCard;
import clueless.SuspectCard;
import clueless.WeaponCard;

public class AccusationTest
{
	@Test
	
	public void testAccusation()
	{
		boolean print = false; // enable/disable printing
		
		// first test, draw three random cards from CardDeck and add
		// them to the CaseFile. Create an accusation object and draw 
		// three more random cards, then send check to CaseFile to 
		// verify the Accusation is correct
		{
			CardDeck cd = new CardDeck();
			CaseFile cf = new CaseFile();
			cf.addCard(cd.drawRandomCard(Constants.SUSPECT_CARD));
			cf.addCard(cd.drawRandomCard(Constants.ROOM_CARD));
			cf.addCard(cd.drawRandomCard(Constants.WEAPON_CARD));
			
			SuspectCard s = (SuspectCard) cd.drawRandomCard(Constants.SUSPECT_CARD);
			RoomCard r = (RoomCard) cd.drawRandomCard(Constants.ROOM_CARD);
			WeaponCard w = (WeaponCard) cd.drawRandomCard(Constants.WEAPON_CARD);
			Accusation ac = new Accusation(r, w, s);

			// check the accusation against the case file
			boolean result = cf.isAccusationValid(ac.room, ac.weapon, ac.suspect);
			assertEquals(false, result);
			
			String text = ac.toString();
			if (print == true)
			{
				System.out.println(text);
			}
		}
		
		// second test, use a different CaseFile constructor
		// check that each Card type can return an invalid accusation
		// finally, check that we can return a valid accusation
		{
			RoomCard r = new RoomCard();
			r.setName(Constants.ROOMS[Constants.BALL_ROOM]);
			
			SuspectCard s = new SuspectCard();
			s.setName(Constants.MR_GREEN_STR);
			
			WeaponCard w = new WeaponCard();
			w.setName(Constants.WEAPONS[Constants.KNIFE]);
			
			RoomCard rDummy = new RoomCard();
			SuspectCard sDummy = new SuspectCard();
			WeaponCard wDummy = new WeaponCard();
			
			CaseFile cf = new CaseFile(r, w, s);

			// check if Room is wrong
			boolean result = cf.isAccusationValid(rDummy, w, s);
			assertEquals(false, result);
			
			// check if Weapon is wrong
			result = cf.isAccusationValid(r, wDummy, s);
			assertEquals(false, result);
			
			// check if Suspect is wrong
			result = cf.isAccusationValid(r, w, sDummy);
			assertEquals(false, result);
			
			// check that the accusation is correct
			result = cf.isAccusationValid(r, w, s);
			assertEquals(true, result);
		}
	}
}

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
import clueless.Suggestion;
import clueless.SuspectCard;
import clueless.WeaponCard;

public class SuggestionTest {
@Test
	
	public void testSuggestion()
	{
		boolean print = true; // enable/disable printing
		
		// first test, draw three random cards from CardDeck and add
		// them to the PlayerHand. Create a suggestion object and draw 
		// three more random cards, then send check to PlayerHand to 
		// verify the Suggestion is correct
		{
			CardDeck cd = new CardDeck();
			Player p = new Player();
			p.addCardToHand(cd.drawRandomCard(Constants.SUSPECT_CARD));
			p.addCardToHand(cd.drawRandomCard(Constants.ROOM_CARD));
			p.addCardToHand(cd.drawRandomCard(Constants.WEAPON_CARD));
			
			SuspectCard s = (SuspectCard) cd.drawRandomCard(Constants.SUSPECT_CARD);
			RoomCard r = (RoomCard) cd.drawRandomCard(Constants.ROOM_CARD);
			WeaponCard w = (WeaponCard) cd.drawRandomCard(Constants.WEAPON_CARD);
			Suggestion sug = new Suggestion(r, w, s);
	
			// check the accusation against the case file
			boolean result = p.canDisprove(sug.room, sug.weapon, sug.suspect);
			assertEquals(false, result);
			
			String text = sug.toString();
			if (print == true)
			{
				System.out.println(text);
			}
		}
	}

}

package testcases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clueless.Card;
import clueless.CardDeck;
import clueless.Constants;
import clueless.DetectiveNotes;
import clueless.Player;
import clueless.RoomCard;
import clueless.SuspectCard;
import clueless.WeaponCard;

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
           	p.updateDetectiveNotes(Constants.ROOM_CARD, "Ball Room");
           	p.updateDetectiveNotes(Constants.SUSPECT_CARD, "Prof. Plum");
           	p.updateDetectiveNotes(3, "no note");
           	DetectiveNotes dn = p.getDetectiveNotes();
           	
           	String text = dn.toString();
           	if (print == true)
           	{
           		System.out.println(text);
           	}
		}
		
		// second test, add cards to hand
		{
			Player p = new Player("Mr. Green");
			CardDeck cd = new CardDeck();
			p.addCardToHand(cd.drawCard());
			p.addCardToHand(cd.drawCard());
			p.addCardToHand(cd.drawCard());
			p.addCardToHand(cd.drawCard());
			p.addCardToHand(cd.drawCard());
			DetectiveNotes dn = p.getDetectiveNotes();
			
			String text = dn.toString();
			if (print == true)
			{
				System.out.println(text);
			}
		}
		
		// third test, can the player disprove?
		{
			Player p = new Player("Mr. Green");
			CardDeck cd = new CardDeck();
			RoomCard r = (RoomCard) cd.drawRandomCard(Constants.ROOM_CARD);
			SuspectCard s = (SuspectCard) cd.drawRandomCard(Constants.SUSPECT_CARD);
			WeaponCard w = (WeaponCard) cd.drawRandomCard(Constants.WEAPON_CARD);
			RoomCard r_bogey = (RoomCard) cd.drawRandomCard(Constants.ROOM_CARD);
			SuspectCard s_bogey = (SuspectCard) cd.drawRandomCard(Constants.SUSPECT_CARD);
			WeaponCard w_bogey = (WeaponCard) cd.drawRandomCard(Constants.WEAPON_CARD);
			p.addCardToHand(r);
			p.addCardToHand(s);
			p.addCardToHand(w);
		
			// disprove the Room
			boolean disprove = p.canDisprove(r, w_bogey, s_bogey);
			assertEquals(true, disprove);
			Card disproveCard = p.getDisproveCard(r, w_bogey, s_bogey);
			assertEquals(r.getName(), disproveCard.getName());

			// disprove the Suspect
			disprove = p.canDisprove(r_bogey, w_bogey, s);
			assertEquals(true, disprove);
			disproveCard = p.getDisproveCard(r_bogey, w_bogey, s);
			assertEquals(s.getName(), disproveCard.getName());
		
			// disprove the Weapon
			disprove = p.canDisprove(r_bogey, w, s_bogey);
			assertEquals(true, disprove);
			disproveCard = p.getDisproveCard(r_bogey, w, s_bogey);
			assertEquals(w.getName(), disproveCard.getName());
			
			// cannot disprove
			disprove = p.canDisprove(r_bogey, w_bogey, s_bogey);
			assertEquals(false, disprove);
			disproveCard = p.getDisproveCard(r_bogey, w_bogey, s_bogey);
			assertEquals(null, disproveCard);
		}
	}

}

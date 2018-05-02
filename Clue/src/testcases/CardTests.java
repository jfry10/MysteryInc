package testcases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clueless.Card;
import clueless.CardDeck;
import clueless.Constants;
import clueless.RoomCard;
import clueless.SuspectCard;
import clueless.WeaponCard;

public class CardTests {
	@Test
	
	public void testCardDeck()
	{
		boolean print = false; // enable/disable printing

		// first run, draw Random Cards by Type
		{
			//System.out.println("Card Test 1:");
			CardDeck cd = new CardDeck();
			assertEquals(0, Constants.WEAPON_CARD);
			assertEquals(1, Constants.SUSPECT_CARD);
			assertEquals(2, Constants.ROOM_CARD);
	
			// Weapons, Suspects, then Rooms
			Card w1 = cd.drawRandomCard(Constants.WEAPON_CARD);
			Card w2 = cd.drawRandomCard(Constants.WEAPON_CARD);
			Card w3 = cd.drawRandomCard(Constants.WEAPON_CARD);
			Card w4 = cd.drawRandomCard(Constants.WEAPON_CARD);
			Card w5 = cd.drawRandomCard(Constants.WEAPON_CARD);
			Card w6 = cd.drawRandomCard(Constants.WEAPON_CARD);
			Card w7 = cd.drawRandomCard(Constants.WEAPON_CARD);
			// Make sure we can only draw 6 Weapons
			assertEquals(null, w7);
			
			// print out Weapon names
			if (print == true)
			{
				System.out.println(w1.getName());
				System.out.println(w2.getName());
				System.out.println(w3.getName());
				System.out.println(w4.getName());
				System.out.println(w5.getName());
				System.out.println(w6.getName());
			}
			boolean all_weapons = 
					(w1 instanceof WeaponCard) &&
					(w2 instanceof WeaponCard) && 
					(w3 instanceof WeaponCard) && 
					(w4 instanceof WeaponCard) && 
					(w5 instanceof WeaponCard) && 
					(w6 instanceof WeaponCard);
			// Make sure all the Weapons are WeaponCard
			assertEquals(true, all_weapons);
	
			Card s1 = cd.drawRandomCard(Constants.SUSPECT_CARD);
			Card s2 = cd.drawRandomCard(Constants.SUSPECT_CARD);
			Card s3 = cd.drawRandomCard(Constants.SUSPECT_CARD);
			Card s4 = cd.drawRandomCard(Constants.SUSPECT_CARD);
			Card s5 = cd.drawRandomCard(Constants.SUSPECT_CARD);
			Card s6 = cd.drawRandomCard(Constants.SUSPECT_CARD);
			Card s7 = cd.drawRandomCard(Constants.SUSPECT_CARD);
			// Make sure we can only draw 6 Suspects
			assertEquals(null, s7);
			
			// print out Suspect names
			if (print == true)
			{
				System.out.println(s1.getName());
				System.out.println(s2.getName());
				System.out.println(s3.getName());
				System.out.println(s4.getName());
				System.out.println(s5.getName());
				System.out.println(s6.getName());
			}
			boolean all_suspects = 
					(s1 instanceof SuspectCard) &&
					(s2 instanceof SuspectCard) && 
					(s3 instanceof SuspectCard) && 
					(s4 instanceof SuspectCard) && 
					(s5 instanceof SuspectCard) && 
					(s6 instanceof SuspectCard);
			// Make sure all the Suspects are SuspectCard
			assertEquals(true, all_suspects);
			
			Card r1 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r2 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r3 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r4 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r5 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r6 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r7 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r8 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r9 = cd.drawRandomCard(Constants.ROOM_CARD);
			Card r0 = cd.drawRandomCard(Constants.ROOM_CARD);
			// Make sure we can only draw 9 Rooms
			assertEquals(null, r0);
			
			// print out Room names
			if (print == true)
			{
				System.out.println(r1.getName());
				System.out.println(r2.getName());
				System.out.println(r3.getName());
				System.out.println(r4.getName());
				System.out.println(r5.getName());
				System.out.println(r6.getName());
				System.out.println(r7.getName());
				System.out.println(r8.getName());
				System.out.println(r9.getName());
			}
			boolean all_rooms = 
					(r1 instanceof RoomCard) &&
					(r2 instanceof RoomCard) && 
					(r3 instanceof RoomCard) && 
					(r4 instanceof RoomCard) && 
					(r5 instanceof RoomCard) && 
					(r6 instanceof RoomCard) &&
					(r7 instanceof RoomCard) &&
					(r8 instanceof RoomCard) &&
					(r9 instanceof RoomCard);
			// Make sure all the Rooms are RoomCard
			assertEquals(true, all_rooms);
		}

		// second test, draw Random Cards from the deck
		{
			//System.out.println("Card Test 2:");
			CardDeck cd = new CardDeck();
			Card nextCard = cd.drawCard();
			int count = 0;
			while (nextCard != null)
			{
				if (print == true)
				{
					System.out.println(nextCard.getName());
				}
				nextCard = cd.drawCard();
				count++;
			}
			assertEquals(count, 21); // there are 21 cards
		}
	}

}

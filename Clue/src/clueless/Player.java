/**
 * 
 */
package clueless;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author joshfry
 *
 */
public class Player {

	public String suspectName;
	public Location positionOnBoard;
	public String cards;
	PlayerHand myHand;
	DetectiveNotes myNotes;

	public Player()
	{
		myHand = new PlayerHand();
		myNotes = new DetectiveNotes();
	}

	public Player(String name)
	{
		myHand = new PlayerHand();
		myNotes = new DetectiveNotes();
		suspectName = name;
	}

	public void updateSuspectName(String name)
	{
		suspectName = name;
	}

	public void addCardToHand(Card newCard)
	{
		// Received a card from Client, add it to Hand
		myHand.addCard(newCard);
		
		// automatically add a detective note
		if (newCard instanceof RoomCard)
		{
			updateDetectiveNotes(Constants.ROOM_CARD, newCard.getName());
		}
		if (newCard instanceof SuspectCard)
		{
			updateDetectiveNotes(Constants.SUSPECT_CARD, newCard.getName());
		}
		if (newCard instanceof WeaponCard)
		{
			updateDetectiveNotes(Constants.WEAPON_CARD, newCard.getName());
		}
	}

	public DetectiveNotes getDetectiveNotes()
	{
		return myNotes;
	}

	// We receive the Card Type and name, send it to the appropriate
	// function inside of our Detective Notes
	public void updateDetectiveNotes(int type, String name)
	{
		switch (type)
		{
			case Constants.WEAPON_CARD:
				myNotes.setWeapon(name);
				break;
			case Constants.SUSPECT_CARD:
				myNotes.setSuspect(name);
				break;
			case Constants.ROOM_CARD:
				myNotes.setRoom(name);
				break;
			default:
				break;
		}
	}

	public boolean canDisprove(RoomCard room, WeaponCard weapon, SuspectCard suspect)
	{
		// Look in our hand, see if we have one of these three cards
		return (myHand.contains(room) || myHand.contains(weapon) || myHand.contains(suspect));
	}

	public Card getDisproveCard(RoomCard room, WeaponCard weapon, SuspectCard suspect)
	{
		Iterator tempCards;
		Card returnCard;
		
		// For simplicity, we will auto-select the return card
		// 
		// The way this loop works is it will continue to look through
		// the cards until we find a disprove card to return
		// Note: We wouldn't call this function unless we were sure
		// we found a disprove card anyways

		// Example: num = 1
		//          num + 0 = 1, first pass looks at weapons
		//          num + 1 = 2, second pass looks at suspects
		//          num + 2 = 3, 3 % 3 = 0, third pass looks at rooms
		Random rg = new Random();
		rg.setSeed(System.currentTimeMillis());
		int num = rg.nextInt(3);
		int index = 0;
		
		while (index < 3)
		{
			if ((((num + index) % 3) == 0) && myHand.contains(room))
			{
				return room;
			}
			if ((((num + index) % 3) == 1) && myHand.contains(weapon))
			{
				return weapon;
			}
			if ((((num + index) % 3) == 2) && myHand.contains(suspect))
			{
				return suspect;
			}
			index++;
		}

		// Must return something, ideally we don't want 4 return statements but oh well
		return null;
	}
}

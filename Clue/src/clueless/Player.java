/**
 * 
 */
package clueless;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author joshfry
 *
 */
public class Player {

	public String suspectName;
	public String positionOnBoard;
	public String cards;

	PlayerHand myHand = new PlayerHand();
	DetectiveNotes myNotes = new DetectiveNotes();

	public void updateSuspectName(String name)
	{
		suspectName = name;
	}

	public void addCardToHand(Card newCard)
	{
		// Received a card from Client, add it to Hand
		myHand.addCard(newCard);
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
		Card returnCard = null;
		List<RoomCard> rooms = myHand.getRoomCards();
		List<WeaponCard> weapons = myHand.getWeaponCards();
		List<SuspectCard> suspects = myHand.getSuspectCards();

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
		//
		Random rg = new Random();
		rg.setSeed(System.currentTimeMillis());
		int num = rg.nextInt(3);
		int index = 0;

		if (num + index++ % 3 == 0)
		{
			// get the list of rooms
			tempCards = rooms.iterator();
			while (tempCards.hasNext())
			{
				returnCard = (Card) tempCards.next();
				// does this card match the room card?
				if (room.getName() == returnCard.getName())
				{
					return returnCard;
				}
			}
		}
		if (num + index++ % 3 == 1)
		{
			// get the list of weapons
			tempCards = weapons.iterator();
			while (tempCards.hasNext())
			{
				returnCard = (Card) tempCards.next();
				// does this card match the weapon card?
				if (weapon.getName() == returnCard.getName())
				{
					return returnCard;
				}
			}
		}
		if (num + index++ % 3 == 2)
		{
			// get the list of suspects
			tempCards = suspects.iterator();
			while (tempCards.hasNext())
			{
				returnCard = (Card) tempCards.next();
				// does this card match the suspect card?
				if (suspect.getName() == returnCard.getName())
				{
					return returnCard;
				}
			}
		}

		// Must return something, ideally we don't want 4 return statements but oh well
		return returnCard;
	}
}

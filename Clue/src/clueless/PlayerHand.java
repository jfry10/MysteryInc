package clueless;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerHand {

	ArrayList<Card> cards;

	private ArrayList<RoomCard> roomCards;
	private ArrayList<WeaponCard> weaponCards;
	private ArrayList<SuspectCard> suspectCards;

	public PlayerHand()
	{
		roomCards = new ArrayList<RoomCard>();
		weaponCards = new ArrayList<WeaponCard>();
		suspectCards = new ArrayList<SuspectCard>();
	}

	public void addCard(Card newCard) {

		cards.add(newCard);
	}

	// Look in the player's Hand and see if it contains the provided card
	public boolean contains(Card card)
	{
		// Iterate through Hand to find a RoomCard
		boolean returnValue = false;
		Iterator tempHand = cards.iterator();
		while (tempHand.hasNext())
		{
			Card tempCard = (Card) tempHand.next();
			if (tempCard.getName() == card.getName())
			{
				returnValue = true;
			}
		}
		return returnValue;
	}

	// Retrieves the RoomCard list from the player's Hand
	public List<RoomCard> getRoomCards()
	{
		Card tempCard;

		//Put at least one null card in the list
		roomCards.clear();
		roomCards.add(new RoomCard());

		Iterator tempHand = cards.iterator();
		while (tempHand.hasNext())
		{
			tempCard = (Card) tempHand.next();
			if (tempCard instanceof RoomCard)
			{
				roomCards.add((RoomCard) tempCard);
			}
		}
		return roomCards;
	}

	// Retrieves the WeaponCard list from the player's Hand
	public List<WeaponCard> getWeaponCards()
	{
		Card tempCard;

		//Put at least one null card in the list
		weaponCards.clear();
		weaponCards.add(new WeaponCard());

		Iterator tempHand = cards.iterator();
		while (tempHand.hasNext())
		{
			tempCard = (Card) tempHand.next();
			if (tempCard instanceof WeaponCard)
			{
				weaponCards.add((WeaponCard) tempCard);
			}
		}
		return weaponCards;
	}

	// Retrieves the SuspectCard list from the player's Hand
	public List<SuspectCard> getSuspectCards()
	{
		Card tempCard;

		//Put at least one null card in the list
		suspectCards.clear();
		suspectCards.add(new SuspectCard());

		Iterator tempHand = cards.iterator();
		while (tempHand.hasNext())
		{
			tempCard = (Card) tempHand.next();
			if (tempCard instanceof SuspectCard)
			{
				suspectCards.add((SuspectCard) tempCard);
			}
		}
		return suspectCards;
	}
}

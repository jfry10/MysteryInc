package clueless;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CardDeck
{

	ArrayList<Card> cardDeck = new ArrayList<Card>();

	public CardDeck()
	{
		// Create full deck
		//
		// Create the Weapon cards
		for (int index = 0; index < Constants.WEAPONS.length; index++)
		{
			WeaponCard w = new WeaponCard();
			w.setName(Constants.WEAPONS[index]);
			cardDeck.add(w);
		}
		//
		// Create the Suspect cards
		for (int index = 0; index < Constants.SUSPECTS.length; index++)
		{
			SuspectCard s = new SuspectCard();
			s.setName(Constants.SUSPECTS[index]);
			cardDeck.add(s);
		}
		//
		// Create the Room cards
		for (int index = 0; index < Constants.ROOMS.length; index++)
		{
			RoomCard r = new RoomCard();
			r.setName(Constants.ROOMS[index]);
			cardDeck.add(r);
		}
	}
	
	public Card drawRandomCard(int cardType)
	{
		Card randomCard;
		cardDeck = randomizeRemaining(); // randomize before drawing
		Iterator cards = cardDeck.iterator();
		while(cards.hasNext())
	    {
			randomCard = (Card) cards.next();

		  	if (cardType == Constants.WEAPON_CARD && randomCard instanceof WeaponCard)
	    		{
	    			cards.remove();
	    			return randomCard;
	    		}
	    		if (cardType == Constants.SUSPECT_CARD && randomCard instanceof SuspectCard)
	    		{
	    			cards.remove();
	    			return randomCard;
	    		}
	    		if (cardType == Constants.ROOM_CARD && randomCard instanceof RoomCard)
	    		{
	    			cards.remove();
	    			return randomCard;
	    		}
	    }
		// If we are unable to draw the specified cardType, return null
		return null;
	}
	
	public Card drawCard()
	{
		Card randomCard = null;
		cardDeck = randomizeRemaining(); // randomize before drawing
		if (!cardDeck.isEmpty())
		{
			randomCard = cardDeck.remove(0);
		}
		return randomCard;
	}
	
	public ArrayList<Card> randomizeRemaining()
	{
		// Put all the cards into an oldDeck list and save the Size
		ArrayList<Card> newDeck = new ArrayList<Card>();
		int deckSize = cardDeck.size();

		Random rg = new Random();
		rg.setSeed(System.currentTimeMillis());
		while (deckSize > 0)
		{
			int num = rg.nextInt(deckSize--); // subtract one

			// take a random card from the old deck
			newDeck.add((Card) cardDeck.get(num));
			cardDeck.remove(num); // then remove it
		}
		return newDeck;
	}
}

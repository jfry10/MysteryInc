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
		ArrayList<Card> subDeck = new ArrayList<Card>();
	
		// Create a deck of cards of one type
		for(Card card : cardDeck) {
			if(card.cardType == cardType) {
				subDeck.add(card);
			}
		}
		
		// if no cards of that type return null
		if(subDeck.size() == 0) {
			return null;
		}
	    
		// randomize the cards
		randomizeList(subDeck); // randomize before drawing
		
		// remove the card that will be returned from the main deck
		cardDeck.remove(subDeck.get(0));
		
		// return the card
		return subDeck.get(0);

	}
	
	public Card drawCard()
	{
		if(cardDeck.size() == 0) {
			return null;
		}
		randomizeRemaining(); // randomize before drawing

		return cardDeck.remove(0);
		
	}
	
	public void randomizeRemaining() {
		randomizeList(cardDeck);
	}
	
	private void randomizeList(ArrayList<Card> cards)
	{
		if(cards.size() == 0) {
			return;
		}
	
		for(int i=0; i<cards.size(); i++) {
			int j = (int) (Math.random() * cards.size());
			Card tempCard = cards.get(i);
			cards.set(i, cards.get(j));
			cards.set(j, tempCard);
		}
	}
}

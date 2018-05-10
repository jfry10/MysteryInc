package clueless;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerHand {

	ArrayList<Card> cards;

	public PlayerHand()
	{
		cards = new ArrayList<Card>();
	}

	public void addCard(Card newCard)
	{
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
		
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		cards.sort(null);
				
		for(Card card : cards) {
			sb.append(card.toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}

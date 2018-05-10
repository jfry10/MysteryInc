package clueless;

public class SuspectCard extends Card {
	
	String name;

	public SuspectCard()
	{
		// give this card a name so we can tell the type
		name = "null";
		cardType = Constants.SUSPECT_CARD;
	}
	
	public void setName(String string)
	{
		name = string;
	}

	public String getName()
	{
		return name;
	}

}

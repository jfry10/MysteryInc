package clueless;

public class RoomCard extends Card {
	String name;

	public RoomCard()
	{
		// give this card a name so we can tell the type
		name = "null";
		
		cardType = Constants.ROOM_CARD;
	}
	
	public RoomCard(String name)
	{
		this.name = name;
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

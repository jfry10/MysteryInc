package clueless;

public class WeaponCard extends Card {
	
	String name;

	public WeaponCard()
	{
		// give this card a name so we can tell the type
		name = "null";
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

package clueless;

public class CaseFile {

	public Card room;
	public Card suspect;
	public Card weapon;
	
	public CaseFile()
	{
		room = null;
		suspect = null;
		weapon = null;
	}
	
	public CaseFile(RoomCard room, SuspectCard suspect, WeaponCard weapon)
	{
		this.room = room;
		this.suspect = suspect;
		this.weapon = weapon;
	}

	public void addCard(Card card)
	{
		if (card instanceof RoomCard)
		{
			room = card;
		}

		if (card instanceof SuspectCard)
		{
			suspect = card;
		}

		if (card instanceof WeaponCard)
		{
			weapon = card;
		}
	}
	
	// Stub 
	public boolean isAccusationValid(RoomCard r, WeaponCard w, SuspectCard s)
	{
		return (r.getName() == room.getName()) && 
				(w.getName() == weapon.getName()) && 
				(s.getName() == suspect.getName());
	}
}

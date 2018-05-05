package clueless;

public class Accusation
{

	public RoomCard room;
	public WeaponCard weapon;
	public SuspectCard suspect;

	// Sending an empty accusation back to the server means
	// player does not want to make an accusation
	public Accusation() {
		
	}
	
	public Accusation(RoomCard r, WeaponCard w, SuspectCard s)
	{
		room = r;
		weapon = w;
		suspect = s;
	}
}

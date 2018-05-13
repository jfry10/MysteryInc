package clueless;

public class Accusation
{

	public RoomCard room = new RoomCard();
	public WeaponCard weapon = new WeaponCard();
	public SuspectCard suspect = new SuspectCard();

	// Sending an empty accusation back to the server means
	// player does not want to make an accusation
	public Accusation() {}

	public Accusation(RoomCard r, WeaponCard w, SuspectCard s)
	{
		room = r;
		weapon = w;
		suspect = s;
	}

	@Override
	public String toString() {
		StringBuilder accusationSB = new StringBuilder();
		accusationSB.append(suspect.getName());
		accusationSB.append(" in the ");
		accusationSB.append(room.getName());
		accusationSB.append(" with a ");
		accusationSB.append(weapon.getName());
		return accusationSB.toString();
	}
}

package clueless;

public class CaseFile {
	RoomCard room;
	WeaponCard weapon;
	SuspectCard suspect;
	
	public CaseFile() {
		
	}

	public void addCard(Card card) {
		if(card instanceof RoomCard) {
			room = (RoomCard) card;
		} else if (card instanceof WeaponCard) {
			weapon = (WeaponCard) card;
		} else if (card instanceof SuspectCard) {
			suspect = (SuspectCard) card;
		}
	}
	
	// Stub 
	public boolean isAccusationValid(RoomCard r, WeaponCard w, SuspectCard s) {
		return ((room.compareTo(r) == 0) &&
		    (weapon.compareTo(w) == 0) &&
		    (suspect.compareTo(s) == 0));
	}
}

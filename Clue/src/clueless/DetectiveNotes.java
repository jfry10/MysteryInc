package clueless;

/**
 * 
 * @author robjose
 *
 */
public class DetectiveNotes
{
	private boolean[] suspects;
	private boolean[] weapons;
	private boolean[] rooms;
	
	public DetectiveNotes()
	{
		suspects = new boolean[Constants.SUSPECTS.length];
		weapons = new boolean[Constants.WEAPONS.length];
		rooms = new boolean[Constants.ROOMS.length];
	}

	// We receive the Suspect name, find it in the Constants array
	// and mark this suspect true in the detective notes
	public void setSuspect(String name)
	{
		for (int index = 0; index < Constants.SUSPECTS.length; index++)
		{
			if (Constants.SUSPECTS[index] == name) 
			{
				this.suspects[index] = true;
				return;
			}
		}
	}

	// We receive the Weapon name, find it in the Constants array
	// and mark this weapon true in the detective notes
	public void setWeapon(String name)
	{
		for (int index = 0; index < Constants.WEAPONS.length; index++)
		{
			if (Constants.WEAPONS[index] == name) 
			{
				this.weapons[index] = true;
				return;
			}
		}
	}

	// We receive the Room name, find it in the Constants array
	// and mark this room true in the detective notes
	public void setRoom(String name)
	{
		for (int index = 0; index < Constants.ROOMS.length; index++)
		{
			if (Constants.ROOMS[index] == name) 
			{
				this.rooms[index] = true;
				return;
			}
		}
	}
	
	public boolean[] getSuspects() {
		return suspects;
	}
	
	public boolean[] getWeapons() {
		return weapons;
	}
	
	public boolean[] getRooms() {
		return rooms;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String separator = "-----------------";
		
		sb.append("DETECTIVE NOTES");
		sb.append("\n\n");
		sb.append("Suspects");
		sb.append("\n");
		sb.append(separator);
		sb.append("\n");
		for(int i=0; i<6; i++) {
			sb.append(Constants.SUSPECTS[i]);
			for(int j=14-Constants.SUSPECTS[i].length(); j>0; j--) {
				sb.append(" ");
			}
			sb.append("|");
			sb.append(suspects[i] ? "X" : " ");
			sb.append("|\n");
			sb.append(separator);
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("Weapons");
		sb.append("\n");
		sb.append(separator);
		sb.append("\n");
		for(int i=0; i<6; i++) {
			sb.append(Constants.WEAPONS[i]);
			for(int j=14-Constants.WEAPONS[i].length(); j>0; j--) {
				sb.append(" ");
			}
			sb.append("|");
			sb.append(weapons[i] ? "X" : " ");
			sb.append("|\n");
			sb.append(separator);
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("Rooms");
		sb.append("\n");
		sb.append(separator);
		sb.append("\n");
		for(int i=0; i<9; i++) {
			sb.append(Constants.ROOMS[i]);
			for(int j=14-Constants.ROOMS[i].length(); j>0; j--) {
				sb.append(" ");
			}
			sb.append("|");
			sb.append(rooms[i] ? "X" : " ");
			sb.append("|\n");
			sb.append(separator);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/*public static void main(String[] args) {
		DetectiveNotes dn = new DetectiveNotes();
		
		dn.setSuspect(Constants.SUSPECTS[Constants.COL_MUSTARD]);
		dn.setSuspect(Constants.SUSPECTS[Constants.MR_GREEN]);
		dn.setSuspect(Constants.SUSPECTS[Constants.MISS_SCARLET]);
		
		dn.setWeapon(Constants.WEAPONS[Constants.KNIFE]);
		dn.setWeapon(Constants.WEAPONS[Constants.ROPE]);
		dn.setWeapon(Constants.WEAPONS[Constants.WRENCH]);
		dn.setWeapon(Constants.WEAPONS[Constants.CANDLESTICK]);
		
		dn.setRoom(Constants.ROOMS[Constants.BALL_ROOM]);
		dn.setRoom(Constants.ROOMS[Constants.HALL]);
		dn.setRoom(Constants.ROOMS[Constants.LIBRARY]);
		dn.setRoom(Constants.ROOMS[Constants.STUDY]);
		dn.setRoom(Constants.ROOMS[Constants.LOUNGE]);
		
		System.out.println(dn);
	}*/
}

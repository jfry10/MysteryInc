package clueless;

public class Constants
{
	public static final int WEAPON_CARD = 0;
	public static final int SUSPECT_CARD = 1;
	public static final int ROOM_CARD = 2;

	public static final int MISS_SCARLET = 0;
	public static final int COL_MUSTARD = 1;
	public static final int MRS_WHITE = 2;
	public static final int MR_GREEN = 3;
	public static final int MRS_PEACOCK = 4;
	public static final int PROF_PLUM = 5;
	
	public static final String COL_MUSTARD_STR = "Col. Mustard";
	public static final String PROF_PLUM_STR = "Prof. Plum";
	public static final String MR_GREEN_STR = "Mr. Green";
	public static final String MRS_PEACOCK_STR = "Mrs. Peacock";
	public static final String MISS_SCARLET_STR = "Miss Scarlet";
	public static final String MRS_WHITE_STR = "Mrs. White";
	
	public static final String[] SUSPECTS = 
			new String[] {MISS_SCARLET_STR,
					COL_MUSTARD_STR,
					MRS_WHITE_STR,
					MR_GREEN_STR,
					MRS_PEACOCK_STR,
					PROF_PLUM_STR,
	};
	
	public static final int KNIFE = 0;
	public static final int CANDLESTICK = 1;
	public static final int PISTOL = 2;
	public static final int ROPE = 3;
	public static final int LEAD_PIPE = 4;
	public static final int WRENCH = 5;
	
	public static final String[] WEAPONS = 
			new String[] {"Knife", 
					"Candlestick",
					"Pistol",
					"Rope",
					"Lead Pipe",
					"Wrench"
	};
	
	public static final int HALL = 0;
	public static final int LOUNGE = 1;
	public static final int DINING_ROOM = 2;
	public static final int KITCHEN = 3;
	public static final int BALL_ROOM = 4;
	public static final int CONSERVATORY = 5;
	public static final int BILLIARD_ROOM = 6;
	public static final int LIBRARY = 7;
	public static final int STUDY = 8;
	
	public static final String[] ROOMS = 
			new String[] {"Hall", 
					"Lounge",
					"Dining Room",
					"Kitchen",
					"Ball Room",
					"Conservatory",
					"Billiard Room",
					"Library",
					"Study"
	};
}

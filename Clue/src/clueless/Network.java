
package clueless;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(RegisterRequest.class);
		kryo.register(RegisterResponse.class);
		kryo.register(RegisterName.class);
		kryo.register(String[].class);
		kryo.register(GetSuspects.class);
		kryo.register(SetSuspect.class);
		kryo.register(SuspectResponse.class);
        kryo.register(BeginGame.class);
		kryo.register(DealCard.class);
		kryo.register(BeginTurn.class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		kryo.register(Location.class);
		kryo.register(Suggestion.class);
		kryo.register(Accusation.class);
        kryo.register(EndTurn.class);
		kryo.register(Card.class);
		kryo.register(RoomCard.class);
		kryo.register(WeaponCard.class);
		kryo.register(SuspectCard.class);
        kryo.register(String.class);
        kryo.register(EndSuggestion.class);
        kryo.register(SuggestionDisprove.class);
        kryo.register(MoveToken.class);
        kryo.register(GUIDisplay.class);
        kryo.register(SuggestionAsk.class);  
	}
	
	static public class RegisterRequest {
		//public Integer clientId;
	}
	
	static public class RegisterResponse {
		public Integer clientId;
		public String[] suspectNames;
	}

	static public class RegisterName {
		public String name;
	}

	static public class UpdateNames {
		public String[] names;
	}

	static public class GetSuspects {
		
	}
	
	static public class SuspectResponse {
		public String[] suspectNames;
	}
	
	static public class SetSuspect {
		public String selectedSuspect;
		
		public SetSuspect() {
			
		}
		
		public SetSuspect(String selectedSuspect) {
			this.selectedSuspect = selectedSuspect;
		}
	}
	
	static public class BeginGame {
		
	}
	
	static public class DealCard {
		public Card card;
	}
	
	static public class BeginTurn {
		
	}

	static public class ChatMessage {
		public String text;
		
		public ChatMessage() {
			
		}
		
		public ChatMessage(String text) {
			this.text = text;
		}
	}

	static public class DetectiveInfo {
		public int type;
		public String name;
	}
	
	static public class ValidMove {
		public boolean valid;
	}
	
	static public class PlayerTurn {
		public boolean turn;
		public boolean left;
		public boolean right;
		public boolean up;
		public boolean down;
		public boolean passage;
	}

	static public class MoveToken {
		public int direction;
	}

	static public class EndTurn {
		public String suspect;
	}
	
	static public class EndSuggestion {
		
	}
	
	static public class SuggestionDisprove {
		// Null card means can't disprove
		public Card card;
		
		public SuggestionDisprove(Card card) {
			this.card = card;
		}
	}
	
	static public class SuggestionAsk {
		
	}
}

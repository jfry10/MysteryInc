
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
		kryo.register(SuspectRequest.class);
		kryo.register(SuspectResponse.class);
        kryo.register(BeginGame.class);
		kryo.register(DealCard.class);
		kryo.register(BeginPlay.class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		kryo.register(Location.class);
		kryo.register(Suggestion.class);
		kryo.register(Accusation.class);
        kryo.register(EndTurn.class);
		kryo.register(Card.class);
        kryo.register(String.class);
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

	static public class SuspectRequest {
		public Integer clientId;
		public String requestedSuspect;
	}
	
	static public class SuspectResponse {
		public boolean success;
		public String[] suspectNames;
		public String selectedSuspectName;
	}
	
	static public class BeginGame {
		
	}
	
	static public class DealCard {
		public Card card;
	}
	
	static public class BeginPlay {
		
	}

	static public class ChatMessage {
		public String text;
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
	}

	static public class MoveToken {
		public int positionX;
		public int positionY;
	}

	static public class EndTurn {
		public String suspect;
	}
}

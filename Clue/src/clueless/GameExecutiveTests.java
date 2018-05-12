package clueless;

import java.io.IOException;
import java.util.LinkedHashMap;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import clueless.Network.DealCard;
import clueless.Network.RegisterName;
import clueless.Network.RegisterRequest;
import clueless.Network.RegisterResponse;
import clueless.Network.SuspectResponse;
import clueless.Network.UpdateNames;

public class GameExecutiveTests {
	TestClient client0;
	TestClient client1;
	TestClient client2;
	TestClient client3;
	TestClient client4;
	TestClient client5;
	
	GameExecutive server;
	
	public GameExecutiveTests() throws IOException {
		server = new GameExecutive();
	}
	
	public void testRegisterName() {
		
		client0 = new TestClient("127.0.0.1", "Client0");
		client0.registerName();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		client1 = new TestClient("127.0.0.1", "Client1");
		client1.registerName();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		client2 = new TestClient("127.0.0.1", "Client2");
		client2.registerName();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		client3 = new TestClient("127.0.0.1", "Client3");
		client3.registerName();
		/*
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		client4 = new TestClient("127.0.0.1", "Client4");
		client4.registerName();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		client5 = new TestClient("127.0.0.1", "Client5");
		client5.registerName();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		*/
		

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		
	}
	
	public void testRegisterRequest() {
		client0.sendRegisterRequest();
	}
	
	public void testSuspectRequest() {
		client0.sendSuspectRequest(Constants.MR_GREEN_STR);
		
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client1.sendSuspectRequest(Constants.MISS_SCARLET_STR);
		
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client2.sendSuspectRequest(Constants.MR_GREEN_STR);
		
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client2.sendSuspectRequest(Constants.COL_MUSTARD_STR);
		
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client3.sendSuspectRequest(Constants.MRS_WHITE_STR);
		
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printSuspectConnectionMap() {
		LinkedHashMap<String, Integer> suspectConnMap = server.suspectConnectionMap;
		
		if(suspectConnMap != null) {
			System.out.println("suspectConnectionMap:");
			for(String suspectName : suspectConnMap.keySet()) {
				System.out.println(suspectName + " => " + suspectConnMap.get(suspectName));
			}
		} else {
			System.out.println("ERROR: suspectConnectionMap on server is null");
		}
	}
	
	public void testInitializePlayerInfoObjects() {
		server.initializePlayerInfoObjects();
		
		LinkedHashMap<Integer, PlayerInfo> playerInfoMap = server.playerInfoMap;
		if(playerInfoMap != null) {
			System.out.println("playerInfoMap:");
			for(Integer playerId : playerInfoMap.keySet()) {
				System.out.println(playerId + ": " + playerInfoMap.get(playerId).toString());
			}
		} else {
			System.out.println("ERROR: playerInfoMap on server is null");
		}
	}
	
	public void testDistributeCards() {
		
		server.distributeCards();

		System.out.println(client0.player.suspectName + ":");
		System.out.println(client0.player.myHand.toString());
		System.out.println(client1.player.suspectName + ":");
		System.out.println(client1.player.myHand.toString());
		System.out.println(client2.player.suspectName + ":");
		System.out.println(client2.player.myHand.toString());
		System.out.println(client3.player.suspectName + ":");
		System.out.println(client3.player.myHand.toString());
	}
	
	public static void main(String[] args) {
		GameExecutiveTests tester = null;
		try {
			tester = new GameExecutiveTests();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tester.testRegisterName();
		tester.testRegisterRequest();
		tester.testSuspectRequest();
		
		tester.printSuspectConnectionMap();
		
		tester.testInitializePlayerInfoObjects();
		
		tester.testDistributeCards();
	}
}

class TestClient {
	public String name;
	public Client client;
	public Player player;
	
	public TestClient(String ipAddress, String name) {
		this.name = name;
		client = new Client();
		player = new Player(this.name);
		
		
		client.start();
		
		Network.register(client);
		
		client.addListener(new Listener() {
			public void connected (Connection connection) {
				
			}
			
			public void received (Connection connection, Object object) {
				if (object instanceof UpdateNames) {
					String[] names = ((UpdateNames) object).names;
					if(names != null) {
						for(String clientName : names) {
							System.out.println(clientName);
						}
					}
				}
				
				if(object instanceof RegisterResponse) {
					String[] names = ((RegisterResponse) object).suspectNames;
					if(names != null) {
						System.out.println("Available suspect names for " + name + ":");
						for(String suspectName : names) {
							System.out.println(suspectName);
						}
					}
				}
				
				/*
				if(object instanceof SuspectResponse) {
					SuspectResponse response = (SuspectResponse) object;
					
					if(response.success) {
						System.out.println("Success! " + name + 
								"'s suspect name is " + response.selectedSuspectName);
					} else {
						System.out.println("Sorry, that suspect name is taken");
						System.out.println("Available suspect names for " + name + ":");
						for(String suspectName : response.suspectNames) {
							System.out.println(suspectName);
						}
					}
				}
				*/
				
				if(object instanceof DealCard) {
					Card theCard = ((DealCard)object).card;
					//System.out.println(name + " received card " + theCard.toString());
					player.addCardToHand(theCard);
				}
			}
			
			public void disconnected (Connection connection) {
				
			}
		});
		
		try {
			client.connect(5000, ipAddress, Network.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerName() {
		System.out.println("Registering name \"" + name + "\"");
		RegisterName registerName = new RegisterName();
		registerName.name = name;
		client.sendTCP(registerName);
	}
	
	public void sendRegisterRequest() {
		client.sendTCP(new RegisterRequest());
	}
	
	
	public void sendSuspectRequest(String suspect) {
		/*
		client.sendTCP(new SuspectRequest(suspect));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
}

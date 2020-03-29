package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.common.BasicGameStatus;
import dmescher.soapyhearts.common.GameOpCodeStatus;
import dmescher.soapyhearts.common.SHServer;
import dmescher.soapyhearts.common.Hand;

import java.util.Scanner;

public class SHTestClient3 {
	static Scanner scanner;

	public static void main(String[] args) throws Exception {
		  scanner = new Scanner(System.in);
		  URL url = new URL("http://localhost:9901/SHServer?wsdl");
		  QName qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplService");
		  Service service = Service.create(url,qname);
		  qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplPort");
		  SHServer shs = service.getPort(qname,SHServer.class);

		  boolean player0 = false;
		  for (String s : args) {
			  if (s.equals("-player0")) {
				  player0 = true;
			  }
		  }
		  
		  int gameid = 0;
		  
		  if (player0) {
		    shs.init();
		    gameid = shs.spawnGame();
		  } else {
			  while (shs.getRunningGames() == 0) {
				  Thread.sleep(2000);
			  }
			  gameid = shs.getRunningGames();
		  }
		  
		  
		  String gamestr = shs.joinGame(gameid);
		  char playerid_char = gamestr.charAt(2);
		  String token = gamestr.substring(3);
		  if ((playerid_char < '0') || (playerid_char > '3')) {
			  System.out.println("Failure in gamestr:"+gamestr);
			  System.exit(1);
		  }
		  
		  int playerid = playerid_char - '0';
		  
		  while (shs.checkStatus(gameid) == BasicGameStatus.WAITING_JOIN) {
			  
			  Thread.sleep(2000); // Sleep for 2 sec
		  }
		  
		  if ((shs.checkStatus(gameid) == BasicGameStatus.START_GAME) && (playerid == 0)) {
			  if (shs.startGame(gameid, token) != GameOpCodeStatus.SUCCESS) {
				  System.out.println("Failed to start game as player 0.");
				  System.exit(1);
			  }
		  }
		  
		  if (shs.checkStatus(gameid) == BasicGameStatus.GAME_STARTED && (playerid == 0)) {
			  if (shs.startRound(gameid, token) != GameOpCodeStatus.SUCCESS) {
				  System.out.println("Failed to start round as player 0.");
			  }
		  }
		  
		  System.out.println("Game id: "+gameid);
		  System.out.println("Player id: "+playerid);
		  System.out.println("Security token: "+token);
		  
		  String handstr = shs.getHand(gameid, token, playerid);
		  System.out.println("Hand = "+handstr);
		  
		  Hand hand = new Hand(handstr);
          // TODO:  Implement hand display to console, and awaiting selection of 3
		  // cards.
	}

}

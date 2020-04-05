package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.common.BasicGameStatus;
import dmescher.soapyhearts.common.GameOpCodeStatus;
import dmescher.soapyhearts.common.SHServer;

public class SHTestClient2b {

	public static void main(String[] args) throws Exception {
		  URL url = new URL("http://localhost:9901/SHServer?wsdl");
		  QName qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplService");
		  Service service = Service.create(url,qname);
		  qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplPort");
		  SHServer shs = service.getPort(qname,SHServer.class);

		  // Assumes a game has already been started.
		  int gameid = shs.getRunningGames();
		  String gamestr = shs.joinGame(gameid);

		  char playerid_char = gamestr.charAt(2);
		  String token = gamestr.substring(3);
		  if ((playerid_char < '0') || (playerid_char > '3')) {
			  System.out.println("Failure in gamestr:"+gamestr);
			  System.exit(1);
		  }
		  
		  int playerid = playerid_char - '0';
		  
		  while (shs.checkStatus(gameid) == BasicGameStatus.WAITING_JOIN) {
			  
			  Thread.sleep(5000); // Sleep for 5 sec
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

		  while (shs.checkStatus(gameid) != BasicGameStatus.WAITING_GROUP) {
			  Thread.sleep(5000);
		  }
		  System.out.println("Hand = "+shs.getHand(gameid, token, playerid));		  
	}

}

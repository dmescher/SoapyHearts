package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.common.DEBUG;
import dmescher.soapyhearts.common.GameOpCodeStatus;
import dmescher.soapyhearts.common.BasicGameStatus;
import dmescher.soapyhearts.common.SHServer;

public class SHTestClientRound3 {
	// Test server needs to be started with --round3 for this test to work.

	public static void main(String[] args) throws Exception {
		  URL url = new URL("http://localhost:9901/SHServer?wsdl");
		  QName qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplService");
		  Service service = Service.create(url,qname);
		  qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplPort");
		  SHServer shs = service.getPort(qname,SHServer.class);
		  
		  boolean debug = false;
		  for (String s: args) {
			  if (s.equals("--DEBUG") == true) {
				  debug = true; 
				  break;
			  }
		  }
			  
		  if (debug == true) {
			  DEBUG.setConsole(true);
		  }
	      
		  shs.init();
	      int gamecount = shs.spawnGame();
	      System.out.println(gamecount+" games started.");
	      String[] playerarr = new String[4];
	      String[] tokenarr = new String[4];
	      for (int count=0; count<4; count++) {
	      	playerarr[count] = shs.joinGame(gamecount);
	      	DEBUG.print("playerarr["+count+"] ="+playerarr[count]);
	      	tokenarr[count] = playerarr[count].substring(3);
	      }
	      GameOpCodeStatus x = shs.startGame(gamecount, tokenarr[0]);
	      if (x != GameOpCodeStatus.SUCCESS) {
	      	System.out.println("Failure at startGame");
	      }
	      x = shs.startRound(gamecount, tokenarr[0]);
	      if (x != GameOpCodeStatus.SUCCESS) {
	    	  System.out.println("Failure at startRound");
	      }
	      BasicGameStatus y = shs.checkStatus(gamecount);
	      int z = shs.checkAdvStatus(gamecount);
	      if (y != BasicGameStatus.WAITING_TURN) {
	    	  System.out.println("Round 3, game is not waiting for turn.");
	      }
	      System.out.println("Waiting on player "+z);

	}

}

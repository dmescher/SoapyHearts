package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.common.DEBUG;
import dmescher.soapyhearts.common.SHServer;
import dmescher.soapyhearts.common.GameOpCodeStatus;

/* Uses Deck.testDeck1 */

public class SHTestClient4Group {

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
          shs.spawnGame();
          String[] playerarr = new String[4];
          String[] tokenarr = new String[4];
          for (int count=0; count<4; count++) {
        	  playerarr[count] = shs.joinGame(0);
        	  tokenarr[count] = playerarr[count].substring(3);
          }
          GameOpCodeStatus x = shs.startGame(0, tokenarr[0]);
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure at startGame");
          }
          
          shs.startRound(0, tokenarr[0]);
          x = shs.passCards(0, 0, tokenarr[0], "2D", "2S", "2H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 0");
          }
          
          x = shs.passCards(0, 1, tokenarr[1], "TC", "TD", "AH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 1");
          }
          
          x = shs.passCards(0, 2, tokenarr[2], "QD", "KC", "KH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 2");
          }
          
          x = shs.passCards(0, 3, tokenarr[3], "8H", "QC", "QH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 3");
          }
          
          String handstr = shs.getHand(0, tokenarr[0], 0);
          System.out.println("Player 0 Hand: "+handstr);
          handstr = shs.getHand(0, tokenarr[1], 1);
          System.out.println("Player 1 Hand: "+handstr);
          handstr = shs.getHand(0, tokenarr[2], 2);
          System.out.println("Player 2 Hand: "+handstr);
          handstr = shs.getHand(0, tokenarr[3], 3);
          System.out.println("Player 3 Hand: "+handstr);
	}

}

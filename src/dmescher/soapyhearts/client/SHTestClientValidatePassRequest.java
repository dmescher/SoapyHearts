package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.common.DEBUG;
import dmescher.soapyhearts.common.GameOpCodeStatus;
import dmescher.soapyhearts.common.SHServer;

public class SHTestClientValidatePassRequest {

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
          boolean testpass = true;
          
          shs.startRound(gamecount, tokenarr[0]);
          // No cards, just null strings
          x = shs.passCards(gamecount, 0, tokenarr[0], new String(""), new String(""), new String(""));
          if (x != GameOpCodeStatus.BAD_PASS) {
        	  System.out.println("Failure on all null strings");
        	  testpass = false;
          }
          
          // Just one card, which is present
          x = shs.passCards(gamecount, 0, tokenarr[0], "3D", "", "");
          if (x != GameOpCodeStatus.BAD_PASS) {
        	  System.out.println("Failure on one valid");
        	  testpass = false;
          }
          
          // Two cards, both present
          x = shs.passCards(gamecount, 0, tokenarr[0], "3D", "KS", "");
          if (x != GameOpCodeStatus.BAD_PASS) {
        	  System.out.println("Failure on two valid");
        	  testpass = false;
          }
          
          // Three cards, but one not in the hand
          x = shs.passCards(gamecount, 0, tokenarr[0], "3D", "KS", "5D");
          if (x != GameOpCodeStatus.BAD_PASS) {
        	  System.out.println("Failure on two present, one in someone else's hand");
        	  testpass = false;
          }
          
          if (testpass) {
        	  System.out.println("Test passed.");
          } else {
        	  System.out.println("Test failed.");
          }
          
    }
}

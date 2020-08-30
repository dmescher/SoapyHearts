package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.common.DEBUG;
import dmescher.soapyhearts.common.SHServer;
import dmescher.soapyhearts.common.GameOpCodeStatus;
import dmescher.soapyhearts.common.BasicGameStatus;

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
          
          shs.startRound(gamecount, tokenarr[0]);
          x = shs.passCards(gamecount, 0, tokenarr[0], "2D", "2S", "2H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 0");
          }
          
          x = shs.passCards(gamecount, 1, tokenarr[1], "TC", "TD", "AH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 1");
          }
          
          x = shs.passCards(gamecount, 2, tokenarr[2], "QD", "KC", "KH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 2");
          }
          
          x = shs.passCards(gamecount, 3, tokenarr[3], "8H", "QC", "QH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on pass, player 3");
          }
          
          String handstr = shs.getHand(gamecount, tokenarr[0], 0);
          System.out.println("Player 0 Hand: "+handstr);
          handstr = shs.getHand(gamecount, tokenarr[1], 1);
          System.out.println("Player 1 Hand: "+handstr);
          handstr = shs.getHand(gamecount, tokenarr[2], 2);
          System.out.println("Player 2 Hand: "+handstr);
          handstr = shs.getHand(gamecount, tokenarr[3], 3);
          System.out.println("Player 3 Hand: "+handstr);
          
          int y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 0 (1) = "+y);
          y = shs.getCurrentTrickNum(gamecount);
          System.out.println("Trick (0) "+y);
          
          // Trick 0
          x = shs.playCard(gamecount, 1, tokenarr[1], "2C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 0, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "AC");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 0, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "KC");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 0, player 3");
          }
          x = shs.playCard(gamecount, 0, tokenarr[0], "QC");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 0, player 0");
          }
          
          String trickstr = shs.getTrick(gamecount, 0);
          System.out.println("Trick 0 (1+4+2C+AC+KC+QC): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 0 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          
          // Trick 1
          System.out.println("Lead player, trick 1 (2) "+y);
          x = shs.playCard(gamecount, 2, tokenarr[2], "4S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 1, player 2");        	  
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "9S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 1, player 3");
          }
          x = shs.playCard(gamecount, 0, tokenarr[0], "AS");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 1, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "3S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 1, player 1");
          }
          trickstr = shs.getTrick(gamecount, 1);
          System.out.println("Trick 1 (2+4+4S+9S+AS+3S): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 1 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 2 (0) "+y);
          
          // Trick 2
          x = shs.playCard(gamecount, 0, tokenarr[0], "KS");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 2, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "2S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 2, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "7S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 2, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "TS");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 2, player 3");
          }
          trickstr = shs.getTrick(gamecount, 2);
          System.out.println("Trick 2 (0+4+KS+2S+7S+TS): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 2 acknowledge, player "+count);
        	  }
          }
          
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 3 (0) "+y);
 
          // Trick 3
          x = shs.playCard(gamecount, 0, tokenarr[0], "AD");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 3, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "9D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 3, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "TD");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 3, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "QD");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 3, player 3");
          }
          trickstr = shs.getTrick(gamecount, 3);
          System.out.println("Trick 3 (0+4+AD+9D+TD+QD): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 3 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 4 (0) "+y);

          // Hand check!
          handstr = shs.getHand(gamecount, tokenarr[0], 0);
          System.out.println("Player 0 Hand: "+handstr);
          handstr = shs.getHand(gamecount, tokenarr[1], 1);
          System.out.println("Player 1 Hand: "+handstr);
          handstr = shs.getHand(gamecount, tokenarr[2], 2);
          System.out.println("Player 2 Hand: "+handstr);
          handstr = shs.getHand(gamecount, tokenarr[3], 3);
          System.out.println("Player 3 Hand: "+handstr);


          // Trick 4
          x = shs.playCard(gamecount, 0, tokenarr[0], "KD");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 4, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "8D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 4, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "AH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 4, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "7D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 4, player 3");
          }
          trickstr = shs.getTrick(gamecount, 4);
          System.out.println("Trick 4 (0+4+KD+8D+AH+7D): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 4 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 5 (0) "+y);

          // Trick 5
          x = shs.playCard(gamecount, 0, tokenarr[0], "QS");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 5, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "2H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 5, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "6S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 5, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "5S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 5, player 3");
          }
          trickstr = shs.getTrick(gamecount, 5);
          System.out.println("Trick 5 (0+4+QS+2H+6S+5S): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 5 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 6 (0) "+y);

          // Trick 6
          x = shs.playCard(gamecount, 0, tokenarr[0], "JS");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 6, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "6H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 6, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "TH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 6, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "KH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 6, player 3");
          }
          trickstr = shs.getTrick(gamecount, 6);
          System.out.println("Trick 6 (0+4+JS+6H+TH+KH): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 6 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 7 (0) "+y);

          // Trick 7
          x = shs.playCard(gamecount, 0, tokenarr[0], "8S");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 7, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "2D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 7, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "9C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 7, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "5H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 7, player 3");
          }
          trickstr = shs.getTrick(gamecount, 7);
          System.out.println("Trick 7 (0+4+8S+2D+9C+5H): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 7 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 8 (0) "+y);
          
          // Trick 8
          x = shs.playCard(gamecount, 0, tokenarr[0], "JD");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 8, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "6D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 8, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "7H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 8, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "4D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 8, player 3");
          }
          trickstr = shs.getTrick(gamecount, 8);
          System.out.println("Trick 8 (0+4+JD+6D+7H+4D): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 8 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 9 (0) "+y);

          // Trick 9
          x = shs.playCard(gamecount, 0, tokenarr[0], "QH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 9, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "8C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 9, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "9H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 9, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "4H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 9, player 3");
          }
          trickstr = shs.getTrick(gamecount, 9);
          System.out.println("Trick 9 (0+4+QH+8C+9H+4H): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 9 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 10 (0) "+y);

          // Trick 10
          x = shs.playCard(gamecount, 0, tokenarr[0], "3D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 10, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "5D");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 10, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "TC");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 10, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "JH");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 10, player 3");
          }
          trickstr = shs.getTrick(gamecount, 10);
          System.out.println("Trick 10 (0+4+3D+5D+TC+JH): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 10 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 11 (1) "+y);
          
          // Trick 11
          x = shs.playCard(gamecount, 1, tokenarr[1], "4C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 11, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "6C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 11, player 2");
          }
          x = shs.playCard(gamecount, 3, tokenarr[3], "JC");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 11, player 3");
          }
          x = shs.playCard(gamecount, 0, tokenarr[0], "8H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 11, player 0");
          }
          trickstr = shs.getTrick(gamecount, 11);
          System.out.println("Trick 11 (0+4+4C+6C+JC+8H): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 11 acknowledge, player "+count);
        	  }
          }
          y = shs.checkAdvStatus(gamecount);
          System.out.println("Lead player, trick 12 (3) "+y);
          
          // Trick 12
          x = shs.playCard(gamecount, 3, tokenarr[3], "7C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 12, player 3");
          }
          x = shs.playCard(gamecount, 0, tokenarr[0], "3H");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 12, player 0");
          }
          x = shs.playCard(gamecount, 1, tokenarr[1], "3C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 12, player 1");
          }
          x = shs.playCard(gamecount, 2, tokenarr[2], "5C");
          if (x != GameOpCodeStatus.SUCCESS) {
        	  System.out.println("Failure on trick 12, player 3");
          }
          trickstr = shs.getTrick(gamecount, 12);
          System.out.println("Trick 12 (0+4+7C+3H+3C+5C): "+trickstr);
          for (int count=0; count<4; count++) {
        	  GameOpCodeStatus xx = shs.acknowledgeTrick(gamecount, count, tokenarr[count]);
        	  if (xx != GameOpCodeStatus.SUCCESS) {
        		  System.out.println("Failure on trick 12 acknowledge, player "+count);
        	  }
          }
          
          BasicGameStatus gs = shs.checkStatus(gamecount);
          if (gs != BasicGameStatus.SCORING) {
        	  System.out.println("Weird status at end of round.");
          }

	}

}

package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.client.TextMenu;

import dmescher.soapyhearts.common.BasicGameStatus;
import dmescher.soapyhearts.common.GameOpCodeStatus;
import dmescher.soapyhearts.common.SHServer;
import dmescher.soapyhearts.common.Hand;
import dmescher.soapyhearts.common.Card;

import dmescher.soapyhearts.common.DEBUG;

public class SHTestClient3b {
	private static Card[] pick3(Hand h) {
		String[] hand = h.handToArray();
		if (DEBUG.checkConsole()) {
			for (int count=0; count<hand.length; count++) {
				DEBUG.print("DEBUG: Card #"+count+" is "+hand[count]);
			}
		}
		TextMenu tm = new TextMenu(hand);
		tm.setPrompt("Card letter>> ");
		
        int charcount = 0;
        char[] charlist = new char[3];
        while (charcount < 3) {
        	tm.display(new String(charlist));
        	char ch = tm.getChoice();
        	if (!(Character.isLetter(ch))) {
        		System.out.println("Please input a letter."); continue;
        	}
        	ch = Character.toUpperCase(ch);
        	if (ch > ((char)('A'+h.getSize()+1))) {
        		System.out.println("Letter out of range."); continue;
        	}
        	boolean matched = false;
        	for (int count=0; count<charcount; count++) {
        		if (charlist[count] == ch) {
        			matched = true;
        		}
        	}
        	
        	if (matched) {
        		System.out.println("No duplication, please.");
        	} else {
        		charlist[charcount++] = ch;
        	}
        }
        
        System.out.println("Selected cards:");
        Card[] cards = new Card[3];
        for (int count=0; count<3; count++) {
        	System.out.println(h.cardAt(charlist[count]-'A').getFullname());
        	cards[count] = h.cardAt(charlist[count]-'A');
        }
        
		
		return cards;
	}

	public static void main(String[] args) throws Exception {
		  URL url = new URL("http://localhost:9901/SHServer?wsdl");
		  QName qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplService");
		  Service service = Service.create(url,qname);
		  qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplPort");
		  SHServer shs = service.getPort(qname,SHServer.class);

		  boolean player0 = false;
		  boolean debug = false;
		  for (String s : args) {
			  if (s.equals("-player0")) {
				  player0 = true;
			  }
  			  if (s.equals("--DEBUG") == true) {
				  debug = true; 
				  break;
			  }
		  }
		  
		  if (debug == true) {
			  DEBUG.setConsole(true);
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
		  
		  while (shs.checkStatus(gameid) != BasicGameStatus.WAITING_GROUP) {
			  Thread.sleep(2000);
		  }  // Need to wait while the cards are dealt.
		  
		  String handstr = shs.getHand(gameid, token, playerid);
		  System.out.println("Hand = "+handstr);
		  
		  Hand hand = new Hand(handstr);
		  Card[] passcards = pick3(hand);
		  GameOpCodeStatus passstat = shs.passCards(gameid, playerid, token, 
				        passcards[0].toString(), 
				        passcards[1].toString(), 
				        passcards[2].toString());
		  
		  switch (passstat) {
		  case SUCCESS: System.out.println("Successful pass status reported"); break;
		  case BAD_PASS: System.out.println("Bad pass status reported"); break;
		  case INVALID_STATE:  System.out.println("Invalid state reported"); break;
		  default:  System.out.println("Other bad status reported"); break;
		  }
		  
		  while (shs.checkStatus(gameid) == BasicGameStatus.WAITING_GROUP) {
			  Thread.sleep(2000);
		  }
		  handstr = shs.getHand(gameid,  token, playerid);
		  System.out.println("New hand = "+handstr);

	}

}

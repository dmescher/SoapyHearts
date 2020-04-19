package dmescher.soapyhearts.server;

import dmescher.soapyhearts.common.*;

import javax.jws.WebService;
import java.util.Vector;
import java.util.Iterator;
import java.util.NoSuchElementException;

@WebService(endpointInterface = "dmescher.soapyhearts.common.SHServer")

public class SHServerImpl implements SHServer {
	static Vector<Game> gameList;
	static int runningGameCount;
	static int startedGameCount;  // Use startedGameCount, as opposed to the gameList.size, because gameList can shrink if we maintain it.
	

	@Override
	public synchronized void init() {
		if (gameList == null) {
		  gameList = new Vector<Game>(0,1);
		  // We're going to start with an empty list, and set it for slowly expanding.
		  // This project is intended to be a demonstration, so we don't need fast
		  // expansion.
		  
		  runningGameCount = 0;
		  startedGameCount = 0;
		  // We want this in here, so some joker doesn't spam SHServer::init to reset
		  // the server.
		} else {
			System.out.println("Very funny.  Someone ran SHServerImpl.init twice.");
		}
		
	}
	
	@Override
	public int getRunningGames() {
      if (gameList == null)
    	  return 0;  // gameList isn't initialized, 0 is valid and correct.
      
      return runningGameCount;
	}

	@Override
	public int getTotalGames() {
		if (gameList == null)
			return 0;  // gameList isn't initialized, so 0 is valid and correct.
		
		return startedGameCount;
	}
	
	@Override
	public synchronized int spawnGame() {
		Game newGame = new Game();
		
		// If someone got sloppy, init the list.
		if (gameList == null)
			init();
		
		gameList.add(newGame);
		runningGameCount++;
		startedGameCount++;
		newGame.setId(startedGameCount); // Set the game ID
		
		return startedGameCount;
	}

	private Game findGame(int id) {
		Iterator<Game> itr = gameList.iterator();
		Game ptr = null;  // Iterator pointer
		Game g = null;    // Found game.  If we end with null, then the game ID doesn't exist.
		while (itr.hasNext()) {
			ptr = itr.next();
			if (ptr.getId() == id) {
				g = ptr;
				break;
			}
		}
        return g;		
	}
	
	
	@Override
	public BasicGameStatus checkStatus(int id) {
		if (gameList == null)
			return BasicGameStatus.INVALID_GAME;

		Game g = findGame(id);
		if (g != null)
			return g.getStatus();
		
	    return BasicGameStatus.INVALID_GAME;
	}
	
	@Override
	public int checkAdvStatus(int id) {
		if (gameList == null) // Recoverable fault, client is being stupid
			return -1;

		Game g = findGame(id);
		if (g != null)
			return g.getAdvStatus();

		return -1;  // No game, so no advanced status
	}

	@Override
	public synchronized String joinGame(int id) {
		// Returned string is in format of:
		// <int> <space> <playerid> <token>
		// int :  game id
		// space : char 0x20
		// playerid:  0, 1, 2, or 3
		// token : A randomly generated 8 character string
		// Example, joining game 2 as player 1:
		// 2 1Xw9pASv2
		// If there is a problem joining the requested game, the playerid will be replaced
		// with X, and then an explanation of the error.
		// # XFULLFULL  - Given game is full
		// # XDONEDONE  - Given game is done
		// # XVOIDVOID  - Given game ID doesn't exist.
		// # XINPROGRS  - Given game ID is already running
		// # XNULLNULL  - No games started
		
		if (gameList == null) {
			return new String(new Integer(id).toString()+" XNULLNULL");
		}

		Game g = findGame(id);
		
		if (g == null) {
			return new String(new Integer(id).toString()+" XVOIDVOID");
		}
		
		// We've established we have the correct game object.
		// See if we can join the game.
		if (g.getStatus() == BasicGameStatus.GAME_OVER) {
			return new String(new Integer(id).toString()+" XDONEDONE");
		}
		if (g.getStatus() != BasicGameStatus.WAITING_JOIN) {
			return new String(new Integer(id).toString()+" XINPROGRS");
		}
		
		
		int playerid = g.joinGame();
		if (playerid == -1) {
			return new String(new Integer(id).toString()+" XFULLFULL");
		}
			
		String token = RandomString.create62(8);
		Player p = new Player(playerid, token);
		g.addPlayer(p,playerid);
		return new String(new Integer(id).toString()+" "+new Integer(playerid).toString()+token);
	}
	
	private Game basicCheck(int id, String token) 
	  throws NoSuchElementException, SecurityException {
		Game g = findGame(id);
			
		if (g == null) {
			throw new NoSuchElementException();
		}
		
		if (g.checkToken(token) < 0) {
			throw new SecurityException();
		}
		
		return g;
	}
	
	@Override
	public synchronized GameOpCodeStatus startGame(int id, String token) {
		Game g = findGame(id);
		
		if (g == null) {
			return GameOpCodeStatus.BAD_GAMEID;
		}
		
		if (g.checkToken(token) < 0) {
			return GameOpCodeStatus.BAD_TOKEN;
		}
		
		if (g.getStatus() != BasicGameStatus.START_GAME) {
			return GameOpCodeStatus.INVALID_STATE;
		}
		
		try {
			g.startGame();
		} catch (IllegalStateException e) {
			return GameOpCodeStatus.INVALID_STATE;
		}
		
		return GameOpCodeStatus.SUCCESS;
	}
	
	@Override
	public synchronized GameOpCodeStatus startRound(int id, String token) {
		Game g = null;
		try {
			g=basicCheck(id,token);
		} catch (NoSuchElementException e) {
			return GameOpCodeStatus.BAD_GAMEID;
		} catch (SecurityException e) {
			return GameOpCodeStatus.BAD_TOKEN;
		}
		
		g.startRound();
		// TODO:  Make sure to catch any IllegalStateExceptions
		return GameOpCodeStatus.SUCCESS;
	}

	@Override
	public synchronized String getHand(int gameid, String token, int playerid) {
		Game g = null;
		try {
			g=basicCheck(gameid,token);
		} catch (NoSuchElementException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}
		
		String rtnval = null;
		
		try {
			rtnval = g.getHand(playerid).toString();
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		}
		
		return rtnval;

	}
	
	@Override
	public int getRoundNum(int gameid) {
		Game g = findGame(gameid);
		
		if (g == null) {
			return -1;
		}
		
		return g.getRound();
	}
	
	@Override
	public GameOpCodeStatus passCards(int gameid, int playerid, String token, 
			                          String card1, String card2, String card3) {
		Game g = null;
		try {
			g=basicCheck(gameid,token);
		} catch (NoSuchElementException e) {
			return GameOpCodeStatus.BAD_GAMEID;
		} catch (SecurityException e) {
			return GameOpCodeStatus.BAD_TOKEN;
		}
		
		if (g.getStatus() != BasicGameStatus.WAITING_GROUP) {
			return GameOpCodeStatus.INVALID_STATE;
		}
		
		Card[] cards = new Card[3];
		try {
			cards[0] = new Card(card1);
			cards[1] = new Card(card2);
			cards[2] = new Card(card3);
		} catch (IllegalArgumentException e) {
			return GameOpCodeStatus.BAD_PASS;
		}
		
		try {
			g.passRequest(playerid, cards);
		} catch (IllegalStateException e) {  // If somebody tries to do two passes
			return GameOpCodeStatus.INVALID_STATE;
		}
		
		return GameOpCodeStatus.SUCCESS;
	}
	
	@Override
	public GameOpCodeStatus playCard(int gameid, int playerid, String token, String card) {
		// TODO:  Implement this method to be useful
		Game g = null;
		try {
			g=basicCheck(gameid,token);
		} catch (NoSuchElementException e) {
			return GameOpCodeStatus.BAD_GAMEID;
		} catch (SecurityException e) {
			return GameOpCodeStatus.BAD_TOKEN;
		}
		if (g.getStatus() != BasicGameStatus.WAITING_TURN) {
			return GameOpCodeStatus.INVALID_STATE;
		}
		
		// Get the active Trick from the Game class.
		int curtrick = g.getCurrentTrick();
		Trick t = g.getTrick(curtrick);
		
		// Are we the lead for this trick? (Check the Trick class)
		int leadplayer = t.getLeaderid();
		boolean AreWeTheLead = (leadplayer == playerid) ? true : false;
		
		// Is this trick 0? (first trick)
		boolean TrickZero = (curtrick == 0) ? true : false;
		
		// Is the card worth points?  (*H or QS)
		// Is this card a heart? (*H)
		// Does this player have non-points cards?
		// Does this player have non-hearts cards?
		// Has a points card been played?
		// Iff this player is not the lead, do they have cards of the same suit as the lead?
		// Does the played card match the lead
		// Is the card 2C?
		
		// If Lead && Trick0 && 2C return SUCCESS
		// If Lead && Trick0 && !2C return BAD_LEAD_2C
		// If Trick0 && Points && NonPointsCards return BAD_CARD_POINTS
		// If Lead && Heart && !PointsPlayed && NonHeartsCards return BAD_LEAD_H
		// If !Lead && PlayerMatchLead && !CardMatchLead return BAD_CARD_SUIT
		
		// If Trick is complete, determine taker, assign taken cards to that player,
		//   and set advstatus
		// return SUCCESS
		
		
		return GameOpCodeStatus.SUCCESS;
		
	}
}

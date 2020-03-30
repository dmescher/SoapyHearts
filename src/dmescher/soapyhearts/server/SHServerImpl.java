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
		// TODO:  Change return type to Game, and throw exceptions for
		// Bad Game ID and Bad Token.  Catch those exceptions in the caller
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
	public String getHand(int gameid, String token, int playerid) {
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
	public GameOpCodeStatus passCards(int id, int playerid, String token, 
			                          String card1, String card2, String card3) {
		// TODO-In Progress:  Implement card passing
		Game g = null;
		try {
			g=basicCheck(id,token);
		} catch (NoSuchElementException e) {
			return GameOpCodeStatus.BAD_GAMEID;
		} catch (SecurityException e) {
			return GameOpCodeStatus.BAD_TOKEN;
		}
		
		if (g.getStatus() != BasicGameStatus.WAITING_GROUP) {
			return GameOpCodeStatus.INVALID_STATE;
		}
		
		// TODO:  Implement Game.passRequest(int playerid, Card[] cards)
		Card[] cards = new Card[3];
		try {
			cards[0] = new Card(card1);
			cards[1] = new Card(card2);
			cards[2] = new Card(card3);
		} catch (IllegalArgumentException e) {
			return GameOpCodeStatus.BAD_PASS;
		}
		
		try {
			g.passRequest(id, cards);
		} catch (IllegalStateException e) {  // If somebody tries to do two passes
			return GameOpCodeStatus.INVALID_STATE;
		}
		
		return GameOpCodeStatus.SUCCESS;
	}
}

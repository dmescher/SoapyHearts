package dmescher.soapyhearts.common;

public class Game {
	BasicGameStatus status;
	int listid=-1;    // Set the game ID to -1 until it is set by the server.
	int playercount=0;
	int roundcount=0;
	int advstatus;
	Deck deck;
	Player[] players;

	// We're going to assume a basic 4-player game of hearts.  If this expands, we'll
	// want to modify the constructors to allow variable numbers of players, as well as
	// which game.  We'd probably make the Game class abstract, with a static method
	// to construct the correct subclass instance.
	public Game() {
		status = BasicGameStatus.WAITING_JOIN;
		players = new Player[4];
	}
	
	public BasicGameStatus getStatus() {
		return status;
	}
	
	public int getAdvStatus() {
		if (status == BasicGameStatus.WAITING_JOIN) {
			return (4-playercount);
		}
		if (status == BasicGameStatus.START_GAME || status == BasicGameStatus.GAME_STARTED) {
			return 0;
		}
		if (status == BasicGameStatus.WAITING_GROUP) {
			return advstatus;
		}
		return -1;
	}
	
	public void setId(int id) {
		listid = id;
	}

	public int getId() {
		return listid;
	}
	
	public synchronized int joinGame() {
		int rtnval = 0;
		if (playercount < 4) {
			rtnval = playercount++;
		} else {
			rtnval = -1;
		}
		
		if (playercount == 4) {
			status = BasicGameStatus.START_GAME;
		}
		return rtnval;
	}
	
	public void addPlayer(Player p, int id) {
		players[id] = p;
	}
	
	public int checkToken(String token) {
		int playerid = -1;
		for (int count=0; count<4; count++) {
			if (players[count] == null) continue;
			if (players[count].checkToken(token) == 0)
				playerid = count;
		}
		return playerid;
	}
	
	public int checkToken(int id, String token) {
		if (players[id] == null) return -1;
		if (players[id].checkToken(token) == 0)
			return id;
		return -1;
	}
	
	public synchronized void startGame() throws IllegalStateException {
		if (status == BasicGameStatus.START_GAME) {
			status = BasicGameStatus.GAME_STARTED;
			// TODO:  Start the game, but only once.  Throw an exception if it is already
			// started, but catch the exception at the server level, and handle appropriately.			
		} else {
			// TODO:  Throw an appropriate IllegalStateException.?
			// Should I subclass it, or not?  Things to noodle on.
			// Fairly minor, though, since I'm writing the client.
		}
	}
	
	public synchronized void startRound() {
		deck = Deck.standard52();
		deck.shuffle();
		for (int count=0; count<4; count++) {
			players[count].setHand(deck.createHand(13));
		}
		status = BasicGameStatus.WAITING_GROUP;
		advstatus = ((roundcount % 4) << 4) + 15;
	}
	
	public Hand getHand(int playerid) {
		if ((playerid < 0) || (playerid > 3)) {
			throw new ArrayIndexOutOfBoundsException("Invalid playerid for Game.getHand");
		}
		
		return players[playerid].getHand();
	}
}

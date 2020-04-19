package dmescher.soapyhearts.common;

import dmescher.soapyhearts.common.Trick;

public class Game {
	BasicGameStatus status;
	int listid=-1;    // Set the game ID to -1 until it is set by the server.
	int playercount=0;
	int roundcount=0;
	int advstatus;
	int tricknum=0;
	boolean heartsbroken=false;
	Deck deck;
	Player[] players;
	Card[][] passcards;
	Trick[] tricks;

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
		if (status == BasicGameStatus.WAITING_TURN) {
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
		advstatus = ((roundcount % 4) << 4) + 0xF;
		passcards = new Card[4][3];
		
		// If this is round 4 / 8 / 12 / etc. we're not waiting on the group
		if ((roundcount % 4) == 3) {
			startTricks();
		}
	}
	
	public int getRound() {
		return roundcount;
	}
	
	public Hand getHand(int playerid) throws IllegalStateException {
		if ((playerid < 0) || (playerid > 3)) {
			throw new ArrayIndexOutOfBoundsException("Invalid playerid for Game.getHand");
		}
		
		if (players[playerid].getHand() == null) {
			throw new IllegalStateException("Hand not initialized.");
		}
		
		return players[playerid].getHand();
	}
	
	public synchronized int passRequest(int playerid, Card[] cards) {
		// Will accept the pass request.
		// In addition, if all pass requests have been submitted, process them and prepare for start of play
		if (((0x1 << playerid) & advstatus) == 0) {
			throw new IllegalStateException("Pass request for playerid "+playerid+" already received.");
		}
		
		for (int count=0; count<cards.length; count++) {
			passcards[playerid][count] = cards[count];
		}
		
		
		advstatus -= (0x1 << playerid); // Clear the appropriate bit in advstatus
		
		if ((advstatus & 0xF) == 0) {
			passTheCards();
			startTricks();
		}
		return 0;
	}
	
	private void passTheCards() {
		
		for (int count1=0; count1<4; count1++) {
			for (int count2=0; count2<3; count2++) {
				
				if (players[count1].getHand().removeCard(passcards[count1][count2]) == false) {
					System.out.println("removal failed.");
				}
			}
		}
		
		switch (roundcount % 4) {
		case 0:  // Pass left
			for (int count1=0; count1<4; count1++) {
				for (int count2=0; count2<3; count2++) {
					players[(count1+1) % 4].getHand().addCard(passcards[count1][count2]);
				}
			} break;
		case 1:  // Pass right
			for (int count1=0; count1<4; count1++) {
				for (int count2=0; count2<3; count2++) {
					players[(count1+3) % 4].getHand().addCard(passcards[count1][count2]);
				}
			} break;
		case 2:  // Pass across
			for (int count1=0; count1<4; count1++) {
				for (int count2=0; count2<3; count2++) {
					players[(count1+2) % 4].getHand().addCard(passcards[count1][count2]);
				}
			} break;
		case 3:  // Stand pat, do nothing, shouldn't get here, as this method shouldn't even be called
			break;
		
		default:  // This is impossible, given the switch.
			break;			
		} 		
	}
	
	private int findACard(Card card) {
		// Returns the player who has the card
		for (int count1=0; count1<4; count1++) {
			for (int count2=0; count2<getHand(count1).getSize(); count2++) {
				if (getHand(count1).cardAt(count2).toString().compareTo(card.toString()) == 0) {
					return count1;
				}
			}
		}
		return -1;
	}
	
	private void startTricks() {
		// Start of play here.  Find 2C, set the status flags, and set play to start.
		int starting_player = findACard(new Card("2C"));
		advstatus = starting_player;
		status = BasicGameStatus.WAITING_TURN;
		tricknum = 0;
		heartsbroken = false;
		tricks = new Trick[13];
		tricks[0] = new Trick(starting_player);
		DEBUG.print("starting player is "+starting_player);
	}
	
	public Trick getTrick(int tid) {
		if (tid > tricknum) {
			throw new IllegalStateException("Invalid trick id");
		}
		
		return tricks[tid];
	}
	
	public int getCurrentTrick() {
		return tricknum;
	}
	
}

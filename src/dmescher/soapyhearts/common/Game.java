package dmescher.soapyhearts.common;

import dmescher.soapyhearts.common.Trick;

import java.util.Arrays;
import java.util.Vector;

public class Game {
	private BasicGameStatus status;
	private int listid=-1;    // Set the game ID to -1 until it is set by the server.
	private int playercount=0;
	private int roundcount=0;
	private int advstatus;
	private int tricknum=0;
	private int nextplayer=-1;
	private boolean heartsbroken=false;
	private Deck deck;
	private Player[] players;
	private Card[][] passcards;
	private Trick[] tricks;
	private Vector<RoundScoreArr> roundscores;
	
	// Variables for testing purposes
	static private boolean isTest;
	static private int deckno=1;
	static private int testround=0;

	// We're going to assume a basic 4-player game of hearts.  If this expands, we'll
	// want to modify the constructors to allow variable numbers of players, as well as
	// which game.  We'd probably make the Game class abstract, with a static method
	// to construct the correct subclass instance.
	public Game() {
		status = BasicGameStatus.WAITING_JOIN;
		players = new Player[4];
		roundscores = new Vector<RoundScoreArr>();
	}
	
	public static void setTest(boolean choice) {
		isTest = choice;
	}
	
	public static void setRound(int newround) {
		testround = newround;
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
		if (id > 3) return -1;  // OutOfBounds, but we don't want to throw an exception
		if (players[id] == null) return -1;
		if (players[id].checkToken(token) == 0)
			return id;
		return -1;
	}
	
	public void setPlayerName(int playerid, String name) {
		if (playerid > 3) return; // OutOfBounds, but we don't want to throw an exception
		players[playerid].setPlayerName(name);
	}
	
	public String getPlayerName(int playerid) {
		if (playerid > 3) return null;  // OutOfBounds, but we don't want to throw an exception
		return players[playerid].getPlayerName();
	}
	
	public String[] getAllPlayerNames() {
		String[] rtnval = new String[4];
		for (int count=0; count<4; count++) {
			rtnval[count] = players[count].getPlayerName();
		}
		return rtnval;
	}
	
	public synchronized void startGame() throws IllegalStateException {
		if (status == BasicGameStatus.START_GAME) {
			status = BasicGameStatus.GAME_STARTED;
			// TODO:  Start the game, but only once.  Throw an exception if it is already
			// started, but catch the exception at the server level, and handle appropriately.
			
			DEBUG.print("Game::startGame called while valid");
			// Set the test round
			if (isTest) {
				roundcount = testround;
				DEBUG.print("Setting roundcount to "+roundcount);
			}
		} else {
			// TODO:  Throw an appropriate IllegalStateException.?
			// Should I subclass it, or not?  Things to noodle on.
			// Fairly minor, though, since I'm writing the client.
		}
		
	}
	
	public synchronized void startRound() {
		if (!isTest) {
			deck = Deck.standard52();
			deck.shuffle();
		} else {
			switch (deckno) {
			  case 1:  deck = Deck.testDeck1(); break;
			  default:  deck = Deck.standard52(); deck.shuffle(); break;
			}
		}

		
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
	
	public boolean HeartsBroken() {
		return heartsbroken;
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
	
	public void incrementPlayer() {
		advstatus = (advstatus + 1) % 4;
	}

	public synchronized void processCurrentTrick() {
		// The trick has a winner, take the relevant cards from the player hands,
		// assign them to the winner, increment tricknum, and set advstatus
		if (tricks[tricknum].getWinner() == -1) {
			throw new IllegalStateException("Trick "+tricknum+" not won yet.");
		}
		
		status = BasicGameStatus.PROCESSING;
		Trick t = tricks[tricknum];
		nextplayer = t.getWinner();
		for (int count=0; count<4; count++) {
			Card c = t.getCard(count);
			int playerid = findACard(c);
			players[playerid].getHand().removeCard(c);
			players[playerid].takeCard(c);
			
			// We need to check if the card is a heart and set it if it is. 
			// We do it here because we're already looping through all four cards of the trick
			if (c.getSuit() == 2) { // Is Hearts
				heartsbroken = true;
			}
		}
		if (tricknum < 12) {
		    tricks[++tricknum] = new Trick(nextplayer);
		} else {
			status = BasicGameStatus.SCORING;
			tricknum++;
		}
		advstatus = 0xF;
	}
	
	private void startNextTrick() {
		if (advstatus != 0 || status != BasicGameStatus.PROCESSING) {
			throw new IllegalStateException("Trick not fully acknowledged.");
		}
		advstatus = nextplayer;
		status = BasicGameStatus.WAITING_TURN;
		return;
	}
	
	public synchronized int acknowledgeCompleteTrick(int playerid) {
		if (((0x1 << playerid) & advstatus) == 0) {
			return 0;  // No need to throw an exception, ack'ing a trick twice isn't
			           // a problem.
		}
		
		if (status != BasicGameStatus.SCORING && status != BasicGameStatus.PROCESSING) {
			return -1;  // We're in a strange state, so we should throw an error code.
			            // Not sure what's wrong, though, the client is in a weird state.
		}
		
		advstatus -= (0x1 << playerid); // Clear the appropriate bit in advstatus
		
		if (advstatus == 0) {
			if (status == BasicGameStatus.SCORING) {
				scoreRound();
			} else {
				startNextTrick();
			}
		}
		
		return 0;
	}
	
	public int scorePlayer(int playerid) {
		if (playerid > 3) return 0;  // OutOfBounds, but we're not throwing an exception
		Hand h = players[playerid].getTaken();
		return h.scoreCards();
	}
	
	public void addPoints(int playerid, int points) {
		if (playerid > 3) return;  // OutOfBounds, but we're not throwing an exception
		players[playerid].addPoints(points);
	}
	
	private void recordRound(RoundScoreArr rsa) {
		roundscores.add(rsa);
	}
	
	private void scoreRound() {
		int initialscores[] = new int[4];
		int moonshot = -1;
		
		// Determine point counts, and whether the moon has been shot.
		for (int count=0; count<4; count++) {
			initialscores[count] = scorePlayer(count);
			if (initialscores[count] == 26) moonshot=count; // If the player scored 26, they shot the moon
		}
		
		// Second pass, actually add the points to the player objects
		// If the moon has been shot, add 26 pts to all players that is not the moon shooter
		for (int count=0; count<4; count++) {
			if (moonshot >= 0 && count != moonshot) addPoints(count, 26);
			if (moonshot == -1) addPoints(count, initialscores[count]);
			if (moonshot >= 0 && count == moonshot) addPoints(count, 0);
		}
		
		if (moonshot != -1) {  // If the moon is shot, set the round score array correctly
			for (int count=0; count<4; count++) {
				if (count != moonshot)
					initialscores[count] = 26;
				else
					initialscores[count] = 0;
			}
		}
		
		RoundScoreArr rsa = new RoundScoreArr(roundcount, initialscores);
		recordRound(rsa);
	}
	
	public int[] getRoundScore(int round) {
		int[] rtnval = new int[4];

		if (round > roundscores.size()) {
			Arrays.fill(rtnval, -1);
			return rtnval;
		}
		RoundScoreArr rsa = roundscores.get(round);
		rtnval = rsa.getScores();
		return rtnval;
	}
	
}

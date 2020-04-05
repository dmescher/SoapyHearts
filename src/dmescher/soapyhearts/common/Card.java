package dmescher.soapyhearts.common;

public class Card {
	int rank;
	int suit;
	String fullname;
	
	public Card(int rank, int suit, String fullname) {
		this.rank = rank;
		this.suit = suit;
		this.fullname = fullname;
	}
	
	public Card(String c) {
	// Incoming string is two characters.  First character is rank, second character is
	// suit.
		String rankname = new String(""); // Init to shut up the compiler
		String suitname = new String(""); // Init to shut up the compiler
		switch (c.charAt(0)) {
		// I could probably do something clever for 2-9, but I'd still have to handle
		// 10-Ace separately.  This is easier.
		case '2': rank=0; rankname = new String("TWO"); break;
		case '3': rank=1; rankname = new String("THREE"); break;
		case '4': rank=2; rankname = new String("FOUR"); break;
		case '5': rank=3; rankname = new String("FIVE"); break;
		case '6': rank=4; rankname = new String("SIX"); break;
		case '7': rank=5; rankname = new String("SEVEN"); break;
		case '8': rank=6; rankname = new String("EIGHT"); break;
		case '9': rank=7; rankname = new String("NINE"); break;
		case 't':
		case 'T': rank=8; rankname = new String("TEN"); break;
		case 'j':
		case 'J': rank=9; rankname = new String("JACK"); break;
		case 'q':
		case 'Q': rank=10; rankname = new String("QUEEN"); break;
		case 'k':
		case 'K': rank=11; rankname = new String("KING"); break;
		case 'a':
		case 'A': rank=12; rankname = new String("ACE"); break;
		default: throw new IllegalArgumentException("Invalid card rank");
		}
		
		switch (c.charAt(1)) {
		case 'c':
		case 'C': suit=0; suitname = new String("CLUBS"); break;
		case 'd':
		case 'D': suit=1; suitname = new String("DIAMONDS"); break;
		case 'h':
		case 'H': suit=2; suitname = new String("HEARTS"); break;
		case 's':
		case 'S': suit=3; suitname = new String("SPADES"); break;
		default: throw new IllegalArgumentException("Invalid suit");
		}
		
		fullname = rankname + " OF " + suitname;
	}
	
	public int getSuit() {
		return suit;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String getFullname() {
		return fullname;
	}
	
	public String toString() {
		String rtnval = new String("");  // Have to init it here to shut up the compiler
		if (rank <= 7)
			rtnval=new String(Integer.toString(rank+2));
		else
			switch (rank) {
			case 8: rtnval=new String("T"); break;
			case 9: rtnval=new String("J"); break;
			case 10: rtnval=new String("Q"); break;
			case 11: rtnval=new String("K"); break;
			case 12: rtnval=new String("A"); break;
			}
		
		switch (suit) {
		case 0: rtnval=rtnval.concat("C"); break;
		case 1: rtnval=rtnval.concat("D"); break;
		case 2: rtnval=rtnval.concat("H"); break;
		case 3: rtnval=rtnval.concat("S"); break;
		}
		
		return rtnval;
	}
	
	public int compare(Card comparee, Card lead) {
		if (comparee.getSuit() == this.getSuit())
			// If this card plus the compared card are the same suit, then whichever
			// one is higher in rank could take the other
			return (comparee.getRank() - this.getRank());
		if (comparee.getSuit() == lead.getSuit())
			// If they don't match suit, and the compared card matches the lead, this
			// card loses
			return -1;
		if (this.getSuit() == lead.getSuit())
			// Seeing whether this card matches the lead.  Since we know the two cards
			// aren't the same suit, this card wins.
			return 1;
		// Neither one matches the lead, so both are equally losers.
		return 0;
	}
}

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
	
	public int getSuit() {
		return suit;
	}
	
	public int getRank() {
		return rank;
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

package dmescher.soapyhearts.common;

import dmescher.soapyhearts.common.Card;

public class Trick {
	private Card cards[];
	private int lead_player;
	private int winner = -1;
	private int cardsplayed = 0;
	
	public Trick() {
		cards = new Card[4];
		lead_player = -1;
	}
	
	public Trick(int lead) {
		lead_player = lead;
		cards = new Card[4];
	}
	
	public Trick(String s) {
		// String in format of:
		// lead+cards played+card0+card1+card2+card3
		String[] _cards = s.split("[+]");
		cards = new Card[4];
		lead_player = Integer.parseInt(_cards[0]);
		cardsplayed = Integer.parseInt(_cards[1]);
		for (int count=0; count<cardsplayed; count++) {
			if (_cards[count+2].length() > 0) {
				cards[count] = new Card(_cards[count+2]);
			}
		}
		
		if (cardsplayed == 4) {
			int _winner = 0;
			for (int count=1; count<4; count++) {
				if (cards[_winner].compare(cards[count],cards[0]) > 0) {
					_winner = count;
				}
			}
			// _winner is for capturing which card 1st/2nd/3rd/4th won the trick.
			// winner is adjusted to find which actual player won the trick.
			
			winner = (_winner+lead_player) % 4;			
		} else {
			winner = -1;
		}
	}
	
	public String toString() {
		String rtnval = new String(lead_player + "+" + cardsplayed);
		
		for (int count=0; count<4; count++) {
			if (count < cardsplayed) {
				rtnval = rtnval.concat("+" + cards[count]);
			} else {
				rtnval = rtnval.concat("+");
			}
		}
		
		DEBUG.print("Trick.toString() = "+rtnval);
		return rtnval;
	}
	
	public boolean matchLead(Card c) {
		if (cards[0] == null || cardsplayed == 0) {
			throw new IllegalStateException("No cards played, cannot match lead");
		}
		
		if (c.getSuit() == cards[0].getSuit()) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getLeadSuit() {
		if (cards[0] == null || cardsplayed == 0) {
			throw new IllegalStateException("No cards played, no lead suit to match");
		}
		
		return cards[0].getSuit();
	}
	
	public void playCard(Card c) {
		if (cardsplayed == 4) {
			throw new IllegalStateException("Trick overflow.");
		}
		
		cards[cardsplayed++] = c;
		if (cardsplayed < 4 ) {
			return;
		}
		
		int _winner = 0;
		for (int count=1; count<4; count++) {
			if (cards[_winner].compare(cards[count],cards[0]) > 0) {
				_winner = count;
			}
		}
		// _winner is for capturing which card 1st/2nd/3rd/4th won the trick.
		// winner is adjusted to find which actual player won the trick.
		
		winner = (_winner+lead_player) % 4;
	}
	
	public int getCardCount() {
		return cardsplayed;
	}
	
	public int getLeaderid() {
		return lead_player;
	}
	
	public void setLeader(int leaderid) {
		lead_player=leaderid;
	}
	
	public int getWinner() {
		return winner;
	}
	
	public Card getCard(int c) {
		if ((c < 0) || (c > 3)) {
			throw new IndexOutOfBoundsException("getCard out of range.");
		}
		return cards[c];
	}
}

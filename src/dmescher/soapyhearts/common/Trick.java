package dmescher.soapyhearts.common;

import dmescher.soapyhearts.common.Card;

public class Trick {
	Card cards[];
	int lead_player;
	int winner = -1;
	int cardsplayed = 0;
	
	public Trick() {
		cards = new Card[4];
		lead_player = -1;
	}
	
	public Trick(int lead) {
		lead_player = lead;
		cards = new Card[4];
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
}

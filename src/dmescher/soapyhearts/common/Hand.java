package dmescher.soapyhearts.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class Hand {
	private Vector<Card> cards;
	
	public Hand() {
		cards = new Vector<Card>();
	}
	
	public Hand(Vector<Card> _cards) {
		cards = _cards;
	}

	public String toString() {
		List<String> cardabbrevs = new LinkedList<>();
		for (int count=0; count<cards.size(); count++) {
			cardabbrevs.add(cards.elementAt(count).toString());
		}
		String rtnval = String.join("+", cardabbrevs);
		return rtnval;
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
}

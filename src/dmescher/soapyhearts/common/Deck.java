package dmescher.soapyhearts.common;

import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import java.security.SecureRandom;
import java.util.stream.IntStream;
import java.util.OptionalInt;

public class Deck {
	Vector<Card> cards;
		
	private Deck() {
		
	}
	
	private Deck(int size) {
		cards = new Vector<Card>(size);
	}
	
	private void setCard(int pos, Card card) {
		cards.add(pos,card);
	}
	
	public static Deck standard52() {
		Deck rtnval = new Deck(52);
		for (int count=0; count<52; count++) {
			rtnval.setCard(count, new Card(count % 13, count / 13,
					new String(RanksStrArr.GetName(count%13)+" OF "+SuitsStrArr.GetName(count/13))));
		}
		return rtnval;
	}
	
	public String toString() {
		List<String> cardabbrevs = new LinkedList<>();
		for (int count=0; count<cards.size(); count++) {
			cardabbrevs.add(cards.elementAt(count).toString());
		}
		String rtnval = String.join("+", cardabbrevs);
		return rtnval;
	}
	
	public synchronized void shuffle() {
		Vector<Card> newconfig = new Vector<Card>();
		
		SecureRandom rng = new SecureRandom();
		
		// Iterate through the deck, pulling the Rth (R=random int) element, and putting
		// it into the new deck.  Remove it from the old deck.
		for (int count=cards.size()-1; count>=0; count--) {
			IntStream ints = rng.ints(1,0,count+1);
			OptionalInt _random = ints.findFirst();
			if (!(_random.isPresent())) {
				// We created an intStream of 1 element, so findFirst should come back
				// with something.  If it doesn't, throw an exception so we can grouse
				// at the JDK vendor.
				throw new IllegalStateException("Empty intStream in Deck.shuffle");
			}
			int random = _random.getAsInt();
			newconfig.add(cards.elementAt(random));  // Add into the new
			cards.remove(random); // Remove from the old
		}
		
		// Replace the card array with newconfig
		cards = newconfig;
	}
	
	public synchronized void shuffle(int qty) {
		for (int count=0; count<qty; count++)
			shuffle();
	}

	public synchronized Hand createHand(int size) {
		Hand rtnval = new Hand();
		
		for (int count=0; count<size; count++) {
			rtnval.addCard(cards.remove(0));			
		}
		
		return rtnval;
	}
}

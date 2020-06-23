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
	
	public static Deck testDeck1() {
		Deck rtnval = new Deck(52);
		
		// Player 0
		rtnval.setCard(0,  new Card(6, 3, "EIGHT OF SPADES"));
		rtnval.setCard(1,  new Card(0, 1, "DEUCE OF DIAMONDS")); // Passed
		rtnval.setCard(2,  new Card(12, 1, "ACE OF DIAMONDS")); // Trick 3
		rtnval.setCard(3,  new Card(0, 3, "DEUCE OF SPADES")); // Passed
		rtnval.setCard(4,  new Card(12, 3, "ACE OF SPADES")); // Trick 1
		rtnval.setCard(5,  new Card(1, 1, "THREE OF DIAMONDS"));
		rtnval.setCard(6,  new Card(1, 2, "THREE OF HEARTS"));
		rtnval.setCard(7,  new Card(9, 3, "JACK OF SPADES"));
		rtnval.setCard(8,  new Card(9, 1, "JACK OF DIAMONDS"));
		rtnval.setCard(9,  new Card(10, 3, "QUEEN OF SPADES"));
		rtnval.setCard(10, new Card(0, 2, "DEUCE OF HEARTS")); // Passed
		rtnval.setCard(11, new Card(11, 3, "KING OF SPADES")); // Trick 2
		rtnval.setCard(12, new Card(11, 1, "KING OF DIAMONDS")); // Trick 4
		// Rcvd from Player 3:  QC (Trick 0), 8H, QH
		
		// Player 1
		rtnval.setCard(13, new Card(1, 0, "THREE OF CLUBS"));
		rtnval.setCard(14, new Card(12, 2, "ACE OF HEARTS"));  // Passed
		rtnval.setCard(15, new Card(7, 1, "NINE OF DIAMONDS")); // Trick 3
		rtnval.setCard(16, new Card(2, 0, "FOUR OF CLUBS"));
		rtnval.setCard(17, new Card(3, 1, "FIVE OF DIAMONDS"));
		rtnval.setCard(18, new Card(4, 1, "SIX OF DIAMONDS"));
		rtnval.setCard(19, new Card(1, 3, "THREE OF SPADES")); // Trick 1
		rtnval.setCard(20, new Card(8, 1, "TEN OF DIAMONDS")); // Passed
		rtnval.setCard(21, new Card(6, 0, "EIGHT OF CLUBS"));
		rtnval.setCard(22, new Card(8, 0, "TEN OF CLUBS"));  // Passed
		rtnval.setCard(23, new Card(6, 1, "EIGHT OF DIAMONDS")); // Trick 4
		rtnval.setCard(24, new Card(4, 2, "SIX OF HEARTS"));
		rtnval.setCard(25, new Card(0, 0, "DEUCE OF CLUBS"));  // Lead - Trick 0
		// Rcvd from Player 0:  2D, 2H, 2S (T2)
		
		// Player 2
		rtnval.setCard(26, new Card(10, 1, "QUEEN OF DIAMONDS")); // Passed
		rtnval.setCard(27, new Card(11, 0, "KING OF CLUBS"));  // Passed
		rtnval.setCard(28, new Card(11, 2, "KING OF HEARTS"));  // Passed
		rtnval.setCard(29, new Card(5, 3, "SEVEN OF SPADES")); // Trick 2
		rtnval.setCard(30, new Card(7, 0, "NINE OF CLUBS"));
		rtnval.setCard(31, new Card(7, 2, "NINE OF HEARTS"));
		rtnval.setCard(32, new Card(3, 0, "FIVE OF CLUBS"));
		rtnval.setCard(33, new Card(4, 3, "SIX OF SPADES"));
		rtnval.setCard(34, new Card(4, 0, "SIX OF CLUBS"));
		rtnval.setCard(35, new Card(5, 2, "SEVEN OF HEARTS"));
		rtnval.setCard(36, new Card(12, 0, "ACE OF CLUBS"));  // Trick 0
		rtnval.setCard(37, new Card(8, 2, "TEN OF HEARTS"));
		rtnval.setCard(38, new Card(2, 3, "FOUR OF SPADES")); // Trick 1
		// Rcvd from Player 1:  TC, TD (Trick 3), AH (Trick 4 - Hearts Broken)
		
		// Player 3
		rtnval.setCard(39, new Card(6, 2, "EIGHT OF HEARTS")); // Passed
		rtnval.setCard(40, new Card(7, 3, "NINE OF SPADES")); // Trick 1
		rtnval.setCard(41, new Card(3, 2, "FIVE OF HEARTS"));
		rtnval.setCard(42, new Card(3, 3, "FIVE OF SPADES"));
		rtnval.setCard(43, new Card(2, 2, "FOUR OF HEARTS"));
		rtnval.setCard(44, new Card(5, 0, "SEVEN OF CLUBS"));
		rtnval.setCard(45, new Card(5, 1, "SEVEN OF DIAMONDS")); // Trick 4
		rtnval.setCard(46, new Card(10, 0, "QUEEN OF CLUBS")); // Passed
		rtnval.setCard(47, new Card(8, 3, "TEN OF SPADES")); // Trick 2
		rtnval.setCard(48, new Card(9, 2, "JACK OF HEARTS"));
		rtnval.setCard(49, new Card(9, 0, "JACK OF CLUBS"));
		rtnval.setCard(50, new Card(2, 1, "FOUR OF DIAMONDS"));
		rtnval.setCard(51, new Card(10, 2, "QUEEN OF HEARTS")); // Passed
		// Rcvd from Player 2:  KC (Trick 0), QD (Trick 3), KH
		
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

package dmescher.soapyhearts.client;

import dmescher.soapyhearts.common.Deck;
import dmescher.soapyhearts.common.Hand;

public class DeckTest {

	public static void main(String[] args) {
		Deck deck = Deck.standard52();
		
		System.out.println(deck.toString());
		deck.shuffle();
		System.out.println(deck.toString());

		Hand hand1 = deck.createHand(13);
		System.out.println(hand1.toString());
		Hand hand2 = deck.createHand(13);
		System.out.println(hand2.toString());
	}

}

package dmescher.soapyhearts.client;

import dmescher.soapyhearts.common.Deck;
import dmescher.soapyhearts.common.Hand;
import java.util.Scanner;

public class ConsoleTest {
	static Scanner scanner;
		
	private static char getCharFromConsole() {
		char ch = scanner.next().charAt(0);
 		return ch;
		
	}

	public static void main(String[] args) throws Exception {
        Deck deck = Deck.standard52();
        deck.shuffle();
        Hand hand = deck.createHand(13);
        scanner = new Scanner(System.in);
        
        hand.displayHand();
        int charcount = 0;
        char[] charlist = new char[3];
        while (charcount < 3) {
        	System.out.print("Card letter>> ");
        	char ch = getCharFromConsole();
        	if (!(Character.isLetter(ch))) {
        		System.out.println("Please input a letter."); continue;
        	}
        	ch = Character.toUpperCase(ch);
        	if (ch > ((char)('A'+hand.getSize()+1))) {
        		System.out.println("Letter out of range."); continue;
        	}
        	boolean matched = false;
        	for (int count=0; count<charcount; count++) {
        		if (charlist[count] == ch) {
        			matched = true;
        		}
        	}
        	
        	if (matched) {
        		System.out.println("No duplication, please.");
        	} else {
        		charlist[charcount++] = ch;
        	}
        	
        	hand.displayHand(charlist);
        }
        
        System.out.println("Selected cards:");
        for (int count=0; count<3; count++) {
        	System.out.println(hand.cardAt(charlist[count]-'A').getFullname());
        }
        scanner.close();
	}

}

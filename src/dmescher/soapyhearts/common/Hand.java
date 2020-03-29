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
	
	public Hand(String handstr) {
      String[] _cardstrarr = handstr.split("[+]");
      cards = new Vector<Card>();
      
      for (String s : _cardstrarr) {
    	  if (s.length() > 0) { 
    		  try {
    		  cards.add(new Card(s));
    		  } catch (IllegalArgumentException e) {
    			  System.out.println("Funny card "+s+", ignoring.");
    		  }
    	  }
      }
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
	
	public Card cardAt(int pos) {
		return cards.elementAt(pos);
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public void displayHand() {
		for (byte count=0; count<getSize(); count++) {
			char counter = (char) ('A' + count);
			System.out.println("["+counter+"] -"+cardAt(count).getFullname());
		}		
	}
	
	public void displayHand(char[] starred) {
		for (byte cardcount=0; cardcount<getSize(); cardcount++) {
			char counter = (char) ('A' + cardcount);
			boolean matched = false;
			for (int count=0; count<starred.length; count++) {
				if (starred[count] == counter) {
					matched = true;
				}
			}
			if (!matched) {
				System.out.println("["+counter+"] -"+cardAt(cardcount).getFullname());				
			} else {
				System.out.println("[*] -"+cardAt(cardcount).getFullname());
			}
		}		
		
	}
}

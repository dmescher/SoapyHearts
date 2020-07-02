package dmescher.soapyhearts.common;

import java.util.Vector;

public class Player {
	String token = null;
	String playername = null;
	int id = -1;
	int pts = 0;
	Hand hand;
	Vector<Card> taken;
	
	
	public Player(int _id, String _token) {
		token = _token;
		id = _id;
		taken = new Vector<Card>();
	}

	public Player(int _id, String _token, String _name) {
		token = _token;
		id = _id;
		taken = new Vector<Card>();
		playername = _name;
	}

	public void setToken(String _token) {
		token = _token;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getPlayerName() {
		return playername;
	}
	
	public void setPlayerName(String s) {
		playername = s;
	}
	
	public int checkToken(String incoming) {
		return token.compareTo(incoming);
	}
	
	public int getPoints() {
		return pts;
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public void setHand(Hand h) {
		hand = h;
	}
	
	public void takeCard(Card c) {
		taken.add(c);
	}
	
	public Hand getTaken() {
		return new Hand(taken);
	}
	
	public void clearTaken() {
		taken.clear();
	}
}

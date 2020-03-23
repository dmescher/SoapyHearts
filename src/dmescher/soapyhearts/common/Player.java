package dmescher.soapyhearts.common;

public class Player {
	String token = null;
	int id = -1;
	int pts = 0;
	Hand hand;
	
	public Player(int _id, String _token) {
		token = _token;
		id = _id;
	}

	public void setToken(String _token) {
		token = _token;
	}
	
	public void setId(int _id) {
		id = _id;
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
}

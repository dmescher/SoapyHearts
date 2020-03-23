package dmescher.soapyhearts.common;

public class RanksStrArr {
  private static final String[] RankStr = {
		  "DEUCE", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
		  "JACK", "QUEEN", "KING", "ACE"
  };
  
  public static String GetName(int rank) {
	  return RankStr[rank];
  }
}

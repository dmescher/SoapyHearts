package dmescher.soapyhearts.common;

public class SuitsStrArr {
  private static final String[] SuitStr = {
		  "CLUBS", "DIAMONDS", "HEARTS", "SPADES"
  };
  
  public static String GetName(int suit) {
	  return SuitStr[suit];
  }
}

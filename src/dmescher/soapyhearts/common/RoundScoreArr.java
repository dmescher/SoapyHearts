package dmescher.soapyhearts.common;

public class RoundScoreArr {
  private int[] scores;
  
  public RoundScoreArr() {
	  scores = new int[4];
	  for (int count=0; count<4; count++) scores[count] = 0;
  }
  
  public RoundScoreArr(int[] scoreslist) {
	  scores = scoreslist;
  }
  
  public int[] getScores() {
	  return scores;
  }
  
  public void setScore(int player, int points) {
	  scores[player] += points;
  }
}

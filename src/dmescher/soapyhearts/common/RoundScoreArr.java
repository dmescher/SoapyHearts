package dmescher.soapyhearts.common;

public class RoundScoreArr {
  private int[] scores;
  private int roundno;
  
  public RoundScoreArr(int _roundno) {
	  scores = new int[4];
	  roundno = _roundno;
	  for (int count=0; count<4; count++) scores[count] = 0;
  }
  
  public RoundScoreArr(int _roundno, int[] scoreslist) {
	  roundno = _roundno;
	  scores = scoreslist;
  }
  
  public int[] getScores() {
	  return scores;
  }
  
  public int getRound() {
	  return roundno;
  }
  
  public void setScore(int player, int points) {
	  scores[player] += points;
  }
}

package dmescher.soapyhearts.common;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface SHServer {
  @WebMethod void init();
  @WebMethod int getRunningGames();
  @WebMethod int getTotalGames();
  @WebMethod int spawnGame();
  @WebMethod BasicGameStatus checkStatus(int id);
  @WebMethod int checkAdvStatus(int id);
  @WebMethod String joinGame(int id);
  @WebMethod GameOpCodeStatus startGame(int id, String token);
  @WebMethod GameOpCodeStatus startRound(int id, String token);
  @WebMethod String getHand(int id, String token, int playerid);
  @WebMethod int getRoundNum(int id);
  @WebMethod GameOpCodeStatus passCards(int id, int playerid, String token,  
		                                String card1, String card2, String card3);
  @WebMethod GameOpCodeStatus playCard(int gameid, int playerid, String token,
		                               String card);
}

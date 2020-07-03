package dmescher.soapyhearts.common;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface SHServer {
  @WebMethod void init();
  @WebMethod int getRunningGames();
  @WebMethod int getTotalGames();
  @WebMethod int spawnGame();
  @WebMethod BasicGameStatus checkStatus(int gameid);
  @WebMethod int checkAdvStatus(int gameid);
  @WebMethod String joinGame(int gameid);
  @WebMethod GameOpCodeStatus setName(int gameid, int playerid, String token, String name);
  @WebMethod String getName(int gameid, int playerid);
  @WebMethod String[] getAllNames(int gameid);
  @WebMethod GameOpCodeStatus startGame(int id, String token);
  @WebMethod GameOpCodeStatus startRound(int id, String token);
  @WebMethod String getHand(int gameid, String token, int playerid);
  @WebMethod int getRoundNum(int gameid);
  @WebMethod int getCurrentTrickNum(int gameid);
  @WebMethod GameOpCodeStatus passCards(int gameid, int playerid, String token,  
		                                String card1, String card2, String card3);
  @WebMethod GameOpCodeStatus playCard(int gameid, int playerid, String token,
		                               String card);
  @WebMethod GameOpCodeStatus acknowledgeTrick(int gameid, int playerid, String token);
  @WebMethod String getTrick(int gameid, int trickid);
  @WebMethod GameOpCodeStatus scoreRound(int gameid);

}

package dmescher.soapyhearts.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import dmescher.soapyhearts.common.SHServer;

public class SHTestClient1 {
  public static void main(String[] args) throws Exception
  {
	  URL url = new URL("http://localhost:9901/SHServer?wsdl");
	  QName qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplService");
	  Service service = Service.create(url,qname);
	  qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplPort");
	  SHServer shs = service.getPort(qname,SHServer.class);
	  shs.init();
	  int id1 = shs.spawnGame();
	  System.out.println("Games started: "+shs.getTotalGames());
	  System.out.println("Games running: "+shs.getRunningGames());
	  String gamestr = shs.joinGame(id1);
	  System.out.println("Game token: "+gamestr);
  }
}

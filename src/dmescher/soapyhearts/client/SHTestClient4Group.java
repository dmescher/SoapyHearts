package dmescher.soapyhearts.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dmescher.soapyhearts.common.DEBUG;
import dmescher.soapyhearts.common.SHServer;

public class SHTestClient4Group {

	public static void main(String[] args) throws Exception {
		  URL url = new URL("http://localhost:9901/SHServer?wsdl");
		  QName qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplService");
		  Service service = Service.create(url,qname);
		  qname = new QName("http://server.soapyhearts.dmescher/","SHServerImplPort");
		  SHServer shs = service.getPort(qname,SHServer.class);
		  
		  boolean debug = false;
		  for (String s: args) {
  			  if (s.equals("--DEBUG") == true) {
				  debug = true; 
				  break;
			  }
		  }
  			  
  		  if (debug == true) {
  			  DEBUG.setConsole(true);
  		  }

          shs.init();
          shs.spawnGame();
          String[] playerarr = new String[4];
          String[] tokenarr = new String[4];
          for (int count=0; count<4; count++) {
        	  playerarr[count] = shs.joinGame(0);
        	  tokenarr[count] = playerarr[count].substring(3);
          }
	}

}

package dmescher.soapyhearts.server;

import javax.xml.ws.Endpoint;
import dmescher.soapyhearts.common.DEBUG;
import dmescher.soapyhearts.common.Game;
import dmescher.soapyhearts.server.SHServerImpl;

public class SHPublishTest {

	public static void main(String[] args) {
		// Check for --DEBUG flag
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
		
		SHServerImpl.setTest(true);
		Game.setTest(true);
		
		Endpoint.publish("http://localhost:9901/SHServer", new SHServerImpl());
	}

}

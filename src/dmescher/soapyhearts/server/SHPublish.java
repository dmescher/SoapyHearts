package dmescher.soapyhearts.server;

import javax.xml.ws.Endpoint;
import dmescher.soapyhearts.server.SHServerImpl;

public class SHPublish {

	public static void main(String[] args)
	{
		Endpoint.publish("http://localhost:9901/SHServer", new SHServerImpl());
	}
}

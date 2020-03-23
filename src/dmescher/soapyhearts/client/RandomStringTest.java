package dmescher.soapyhearts.client;

import dmescher.soapyhearts.common.RandomString;

public class RandomStringTest {

	public static void main(String[] args) {
		System.out.println(RandomString.create26(20));
		System.out.println(RandomString.create52(20));
		System.out.println(RandomString.create62(30));

	}

}

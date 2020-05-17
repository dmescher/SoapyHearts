package dmescher.soapyhearts.client;

import java.util.Scanner;

public class StdinScanner {
	private static Scanner scanner;
	
	static void init() {
		scanner = new Scanner(System.in);
	}
	
	static Scanner getScanner() {
		if (scanner == null) {
			scanner = new Scanner(System.in);
		}
		return scanner;
	}

}

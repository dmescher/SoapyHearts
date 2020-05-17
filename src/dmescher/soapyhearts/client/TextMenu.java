package dmescher.soapyhearts.client;

import dmescher.soapyhearts.client.StdinScanner;
import java.util.Vector;
import java.util.Scanner;

public class TextMenu {
	private Vector<String> options;
	private boolean useLetters;  // TODO:  Implement this to be useful.
	private String prompt;
	
	// While creating the ability to call methods directly as part of a text menu
	// would be really cool (and I could do it), for this particular application 
	// it's overkill.
	
	public TextMenu() {
		options = null;
		useLetters = true;
		prompt = new String("");
	}
	
	public TextMenu(String[] _choices) {
		options = new Vector<String>(_choices.length);
		
		options.copyInto(_choices);
		useLetters = true;
		prompt = new String("");
	}
	
	public void setPrompt(String s) {
		if (s == null) {
			prompt = new String("");
		} else {
			prompt = new String(s);
		}
	}
	
	public String getPrompt() {
		return prompt;
	}
	
	public int getSize() {
		return options.size();
	}

	public void setLettersDisplay() {
		useLetters = true;
	}
	
	public void setNumbersDisplay() {
		useLetters = false;
	}
	
	public boolean getDisplay() {
		return useLetters;
	}
		
	public void display(String exclusions) {
		// String contains a list of choices that are excluded from being shown.
		// Exclusions are separated with "+" for the numerics.  Letters it's optional,
		//  unless there are more than 26 options.
		// TODO:  Implement the optional exclusion.  Not necessary for Hearts.
		// TODO:  Implement numerics
		
		for (int count=0; count<getSize(); count++) {
			char choice = (char)('A'+count);
			String strchoice = new String("") + choice;
			
			if (exclusions.contains(strchoice)) {
				System.out.println("[*] - "+options.elementAt(count));
			} else {
				System.out.println("["+choice+"] - "+options.elementAt(count));				
			}
		}
		
		System.out.print(prompt);
	}
	
	public char getChoice() {
		Scanner scanner = StdinScanner.getScanner();
		char ch = scanner.next().charAt(0);
 		return ch;
	}
}

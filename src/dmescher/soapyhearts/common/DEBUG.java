package dmescher.soapyhearts.common;

import java.io.*;

public class DEBUG {
	static boolean toconsole = false;
	static FileOutputStream debuglogFile = null;
	
    static public void setConsole(boolean val) {
    	toconsole = val;
    }
    
    static public boolean checkConsole() {
    	return toconsole;
    }
    
    static public void setFile(String filename) throws FileNotFoundException {
    	if (debuglogFile != null) {
    		try {
    		  debuglogFile.close();
    		} catch (IOException e) {
    			// We're closing the file, we don't need to do anything
    		}
    	}
    	
    	debuglogFile = new FileOutputStream(filename, true);   	
    }
    
    static public void print(String s) {
    	if (toconsole) {
    		System.out.println(s);
    	}
    	
    	if (debuglogFile != null) {
    		try {
    		  debuglogFile.write(s.getBytes());
    		} catch (IOException e) {
    			if (toconsole) {
    				System.out.println("Cannot print to debug log file");
    				e.printStackTrace();
    			}
    		}
    	}
    }
}

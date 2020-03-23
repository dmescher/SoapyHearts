package dmescher.soapyhearts.common;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public class RandomString {
  public static String create26(int len) {
	  // Creates a string of length len, using only A-Z
	  if (len < 0)
		  throw new IllegalArgumentException("Negative length specified for create26");

	  SecureRandom rng = new SecureRandom();
	  IntStream stream = rng.ints(len,0,25);
	  int[] intarr = stream.toArray();
	  byte[] bytearr = new byte[intarr.length];
	  for (int count=0; count<len; count++) {
		  bytearr[count] = (byte)((intarr[count])+'A'); 
	  }
	  
	  return new String(bytearr);
  }
  
  public static String create52(int len) {
	  // Creates a string of length len, using A-Z + a-z
	  if (len < 0)
		  throw new IllegalArgumentException("Negative length specified for create52");
	  
	  SecureRandom rng = new SecureRandom();
	  IntStream stream = rng.ints(len,0,51);
	  int[] intarr = stream.toArray();
	  byte[] bytearr = new byte[intarr.length];
	  for (int count=0; count<len; count++) {
		  bytearr[count] = (intarr[count] < 26) ? (byte)(intarr[count]+'A') : (byte)(intarr[count]-26+'a');
	  }
	  
	  return new String(bytearr);
  }
  
  public static String create62(int len) {
	  // Creates a string of length len, using A-Z, a-z, and 0-9
	  if (len < 0)
		  throw new IllegalArgumentException("Negative length specified for create62");
	  
	  SecureRandom rng = new SecureRandom();
	  IntStream stream = rng.ints(len,0,61);
	  int[] intarr = stream.toArray();
	  byte[] bytearr = new byte[intarr.length];
	  for (int count=0; count<len; count++) {
		  if (intarr[count] < 26)
			  bytearr[count] = (byte)(intarr[count]+'A');
		  else if (intarr[count]< 52)
			  bytearr[count] = (byte)(intarr[count]-26+'a');
		  else if (intarr[count] < 62)
			  bytearr[count] = (byte)(intarr[count]-52+'0');
		  else  // Shouldn't get here....
			  bytearr[count] = '*';
	  }
	  
	  return new String(bytearr);
  }
}

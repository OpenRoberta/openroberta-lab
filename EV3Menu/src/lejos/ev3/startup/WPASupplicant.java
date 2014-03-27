package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class WPASupplicant {
	public static void writeConfiguration(String in, String out, String ssid, String pwd) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(in)));
			FileWriter fw = new FileWriter(new File(out));
			String line;
			while((line = br.readLine()) != null) {
				int i = line.indexOf("ssid=");
				if (i >= 0) {
					line += "\"" + ssid + "\"";
				}
				i=line.indexOf("psk=");
				if (i >= 0) {
					line += computePSK(ssid, pwd);
				}
				fw.write(line + "\n");
			}
			br.close();
			fw.close();
		} catch (Exception e) {
			System.err.println("Failed to write wpa supplication configuration: " + e);
		}
	}
	
	protected static final char[] hexChars = "0123456789abcdef".toCharArray();
	 
	public static String bytesToHex(byte[] buf) {
		int len = buf.length;
		char[] r = new char[len * 2];
		for (int i = 0; i < len; i++) {
			int v = buf[i];
			r[i * 2] = hexChars[(v >>> 4) & 0xF];
			r[i * 2 + 1] = hexChars[v & 0xF];
		}
		return new String(r);
	}
	
	 private static String computePSK(String ssid, String password)
	   throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
		 byte[] ss = ssid.getBytes("utf8");
		 char[] pass = password.toCharArray();
	 
		 SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		 KeySpec ks = new PBEKeySpec(pass, ss, 4096, 256);
		 SecretKey s = f.generateSecret(ks);
		 byte[] k = s.getEncoded();
	 
		 return bytesToHex(k);
	 }
}

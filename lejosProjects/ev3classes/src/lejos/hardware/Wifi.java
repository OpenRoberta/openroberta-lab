package lejos.hardware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Wifi {
	public static LocalWifiDevice getLocalDevice(String ifName) {
		return new LocalWifiDevice(ifName);
	}
	
	public static String[] getIfNames() throws IOException {
		ArrayList<String> results = new ArrayList<String>(); 
		BufferedReader br = new BufferedReader(new FileReader("/proc/net/wireless"));
		String s;
		br.readLine();
		br.readLine();
		
		int n=0;
		while ((s = br.readLine()) != null) {
			String [] ss = s.split(":");
			String ifn = ss[0].trim();
			System.out.println("Interface " + n++ + " is " + ifn);
			results.add(ifn);
		}
		
		br.close();
		return results.toArray(new String[results.size()]);
	}

}

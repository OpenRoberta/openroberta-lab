package lejos.hardware;

import java.util.HashSet;

import com.sun.jna.Memory;

import lejos.internal.io.NativeWifi;
import lejos.utility.Delay;

public class LocalWifiDevice {
	private String ifName;
	private NativeWifi wifi = new NativeWifi();
	NativeWifi.WReqPoint reqP = new NativeWifi.WReqPoint();
	NativeWifi.WReqSocket reqS = new NativeWifi.WReqSocket();
	HashSet<String> results;

	LocalWifiDevice(String ifName) {
		this.ifName = ifName;
	}
	
	public String[] getAccessPointNames() {
		results = new HashSet<String>();
		
		System.out.println("Starting a scan");
		
		// Copy the name to the request structure
		System.arraycopy(ifName.getBytes(), 0, reqP.ifname, 0, ifName.length());
		
		int ret = wifi.ioctl(NativeWifi.SIOCSIWSCAN , reqP);
		System.out.println("ioctl ret is " + ret);
		if (ret >= 0) {
			// Wait for the results
			
			Delay.msDelay(1000);
			
			// Create buffer for the results
			
			reqP.point.flags = 0;
			reqP.point.length = 8192;
			reqP.point.p = new Memory(reqP.point.length);
			
			// Get the results
		
			System.out.println("Getting the results");
			// wait for the results to be available
			// TODO: This code is very ugly. We should probably either use
			// exceptions or use return codes not both!
			ret = -1;
			int retryCnt = 30;
			while (ret < 0)
			{
			    try {
			        ret = wifi.ioctl(NativeWifi.SIOCGIWSCAN , reqP);
			    } catch (RuntimeException e)
			    {
			        if (retryCnt <= 0)
			            throw e;
			        ret = -1;
			        System.out.println("Got error retry cnt " + retryCnt);
			    }
			    Delay.msDelay(1000);
			    retryCnt--;
			}
			System.out.println("get results returns " + ret);
			if (ret >= 0) {
				int offset= 0;
				while(offset < reqP.point.length) {
					int len = reqP.point.p.getShort(offset);
					int ev = reqP.point.p.getShort(offset+2) & 0xFFFF;
					
					if (ev == NativeWifi.SIOCGIWESSID) {
								
						StringBuilder sb = new StringBuilder();
						for(int j=0;j<len-8;j++) {
							sb.append((char) reqP.point.p.getByte(offset+j+8));
						}
						
						//System.out.println(sb.toString());
						results.add(sb.toString());
					}
					
					offset += len;
				}
			}
		}
		
		return results.toArray(new String[results.size()]);
	}
	
	public String getAccessPoint() {
		reqS.sockaddr.bd_addr[0] = 1;
		int ret = wifi.ioctl(NativeWifi.SIOCGIWAP, reqS);
		
		if (ret > 0 ) {
	
			StringBuilder sb = new StringBuilder();
			
			sb = new StringBuilder();
			for(int j=0;j<6;j++) {
				String hex = Integer.toHexString(reqS.sockaddr.bd_addr[j] & 0xFF).toUpperCase();
				if (hex.length() == 1) sb.append('0');
				sb.append(hex);
				if (j<5) sb.append(':');
			}
			
			System.out.println("Access Point:" + sb.toString());
			
			return sb.toString();
		}
		return null;
	}
}

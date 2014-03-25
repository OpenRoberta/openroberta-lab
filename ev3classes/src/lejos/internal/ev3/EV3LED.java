package lejos.internal.ev3;

import lejos.hardware.LED;
import lejos.internal.io.NativeDevice;

public class EV3LED implements LED {	
	private static NativeDevice dev = new NativeDevice("/dev/lms_ui");

	@Override
	public void setPattern(int pattern) {
		byte [] cmd = new byte[2];
	      
		cmd[0] = (byte)('0' + pattern);
		dev.write(cmd, cmd.length);	
	}
}

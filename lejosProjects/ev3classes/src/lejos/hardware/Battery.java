package lejos.hardware;

public class Battery {
	private static Power power = BrickFinder.getDefault().getPower();

	public static int getVoltageMilliVolt() {
		return power.getVoltageMilliVolt();
	}

	public static float getVoltage() {
		return power.getVoltage();
	}

	public static float getBatteryCurrent() {
		return power.getBatteryCurrent();
	}

	public static float getMotorCurrent() {
		return power.getMotorCurrent();
	}
}

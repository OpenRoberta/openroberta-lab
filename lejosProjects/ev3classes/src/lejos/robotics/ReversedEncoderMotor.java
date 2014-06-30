package lejos.robotics;

class ReversedEncoderMotor implements EncoderMotor {

	EncoderMotor encoderMotor;
	
	ReversedEncoderMotor(EncoderMotor motor) {
		this.encoderMotor = motor;
	}
	
	public int getPower() {
		return encoderMotor.getPower();
	}

	public void setPower(int power) {
		encoderMotor.setPower(power);
	}

	public void backward() {
		encoderMotor.forward();
	}

	public void flt() {
		encoderMotor.flt();
	}

	public void forward() {
		encoderMotor.backward();
	}

	public boolean isMoving() {
		return encoderMotor.isMoving();
	}

	public void stop() {
		encoderMotor.stop();
	}

	public int getTachoCount() {
		return -encoderMotor.getTachoCount();
	}

	public void resetTachoCount() {
		encoderMotor.resetTachoCount();
	}
}

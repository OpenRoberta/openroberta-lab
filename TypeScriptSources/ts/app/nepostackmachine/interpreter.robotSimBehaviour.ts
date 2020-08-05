import { ARobotBehaviour } from "./interpreter.aRobotBehaviour";
import { State } from "./interpreter.state";
import * as C from "./interpreter.constants";
import * as U from "./interpreter.util";

export class RobotMbedBehaviour extends ARobotBehaviour {

	constructor() {
		super();
		this.hardwareState.motors = {};
		U.loggingEnabled(false, false);
	}


	public getSample(s: State, name: string, sensor: string, port: any, mode: string) {
		var robotText = 'robot: ' + name + ', port: ' + port + ', mode: ' + mode;
		U.debug(robotText + ' getsample from ' + sensor);
		var sensorName = sensor;


		if (sensorName == C.TIMER) {
			s.push(this.timerGet(port));
		} else if (sensorName == C.ENCODER_SENSOR_SAMPLE) {
			s.push(this.getEncoderValue(mode, port));
		} else {
			//workaround due to mbots sensor names
			if (name == "mbot"){
				port = "ORT_" + port;
			}
			s.push(this.getSensorValue(sensorName, port, mode));
		}

	}

	private getEncoderValue(mode: string, port: string) {
		const sensor = this.hardwareState.sensors.encoder;
		port = port == C.MOTOR_LEFT ? C.LEFT : C.RIGHT;
		if (port != undefined) {
			const v = sensor[port];
			if (v === undefined) {
				return "undefined";
			} else {
				return this.rotation2Unit(v, mode);
			}
		}
		return sensor;
	}

	private rotation2Unit(value: number, unit: string): number {
		switch (unit) {
			case C.DEGREE:
				return value;
			case C.ROTATIONS:
				return value / 360.0;
			case C.DISTANCE:
				return value * C.WHEEL_DIAMETER * Math.PI / 360.0;
			default:
				return 0;
		}
	}

	private getSensorValue(sensorName: string, port: string, mode: string): any {
		const sensor = this.hardwareState.sensors[sensorName];
		if (sensor === undefined) {
			return "undefined";
		}
		var v: string;
		if (mode != undefined) {
			if (port != undefined) {
				v = sensor[port][mode];
				if (sensorName === 'gyro' && mode === 'angle') {
					var reset = this.hardwareState['angleReset'];
					if (reset != undefined) {
						var resetValue : number = reset[port];
						if (resetValue != undefined) {
							var value : number = +v;
							value = value - resetValue;
							if (value < 0) {
								value = value + 360;
							}
							v = '' + value;
						}
					}
				}
			} else {
				v = sensor[mode];
			}
		} else if (port != undefined) {
			if (mode === undefined) {
				v = sensor[port];
			}
		} else {
			return sensor;
		}
		if (v === undefined) {
			return false;
		} else {
			return v;
		}
	}

	public encoderReset(port: string) {
		U.debug('encoderReset for ' + port);
		this.hardwareState.actions.encoder = {};
		if (port == C.MOTOR_LEFT) {
			this.hardwareState.actions.encoder.leftReset = true;
		}
		else {
			this.hardwareState.actions.encoder.rightReset = true;
		}
	}

	public timerReset(port: number) {
		this.hardwareState.timers[port] = Date.now();
		U.debug('timerReset for ' + port);
	}

	public timerGet(port: number) {
		const now = Date.now();
		var startTime = this.hardwareState.timers[port];
		if (startTime === undefined) {
			startTime = this.hardwareState.timers['start'];
		}
		const delta = now - startTime;
		U.debug('timerGet for ' + port + ' returned ' + delta);
		return delta;
	}

	public ledOnAction(name: string, port: number, color: number) {
		const robotText = 'robot: ' + name + ', port: ' + port;
		U.debug(robotText + ' led on color ' + color);
		this.hardwareState.actions.led = {};
		this.hardwareState.actions.led.color = color;
	}

	public statusLightOffAction(name: string, port: number) {
		const robotText = 'robot: ' + name + ', port: ' + port;
		U.debug(robotText + ' led off');

		if (name === "mbot"){
			if (!this.hardwareState.actions.leds){
				this.hardwareState.actions.leds = {};
			}
			this.hardwareState.actions.leds[port] = {};
			this.hardwareState.actions.leds[port].mode = C.OFF;
		}
		else{
			this.hardwareState.actions.led = {};
			this.hardwareState.actions.led.mode = C.OFF;
		}
	}

	public toneAction(name: string, frequency: number, duration: number): number {
		U.debug(name + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration);
		this.hardwareState.actions.tone = {};
		this.hardwareState.actions.tone.frequency = frequency;
		this.hardwareState.actions.tone.duration = duration;
		this.setBlocking(true);
		return 0;
	}

	public playFileAction(file: string): number {
		U.debug('play file: ' + file);
		this.hardwareState.actions.tone = {};
		this.hardwareState.actions.tone.file = file;
		switch (file) {
			case '0':
				return 1000;
			case '1':
				return 350;
			case '2':
				return 700;
			case '3':
				return 700;
			case '4':
				return 500;

		}
	}

	public setVolumeAction(volume: number): void {
		U.debug('set volume: ' + volume);
		this.hardwareState.actions.volume = Math.max(Math.min(100, volume), 0);
		this.hardwareState.volume = Math.max(Math.min(100, volume), 0);
	}

	public getVolumeAction(s: State): void {
		U.debug('get volume');
		s.push(this.hardwareState.volume);
	}

	public setLanguage(language: string): void {
		U.debug('set language ' + language);
		this.hardwareState.actions.language = language;
	}

	public sayTextAction(text: string, speed: number, pitch: number): number {
		if (this.hardwareState.actions.sayText == undefined) {
			this.hardwareState.actions.sayText = {};
		}
		this.hardwareState.actions.sayText.text = text;
		this.hardwareState.actions.sayText.speed = speed;
		this.hardwareState.actions.sayText.pitch = pitch;
		this.setBlocking(true);
		return 0;
	}

	public motorOnAction(name: string, port: any, duration: number, speed: number): number {
		const robotText = 'robot: ' + name + ', port: ' + port;
		const durText = duration === undefined ? ' w.o. duration' : (' for ' + duration + ' msec');
		U.debug(robotText + ' motor speed ' + speed + durText);
		if (this.hardwareState.actions.motors == undefined) {
			this.hardwareState.actions.motors = {};
		}
		this.hardwareState.actions.motors[port] = speed;
		this.hardwareState.motors[port] = speed;
		return 0;
	}

	public motorStopAction(name: string, port: any) {
		const robotText = 'robot: ' + name + ', port: ' + port;
		U.debug(robotText + ' motor stop');
		this.motorOnAction(name, port, 0, 0);

	}

	public driveAction(name: string, direction: string, speed: number, distance: number, time: number ): number {
		const robotText = 'robot: ' + name + ', direction: ' + direction;
		const durText = distance === undefined ? ' w.o. duration' : (' for ' + distance + ' msec');
		U.debug(robotText + ' motor speed ' + speed + durText);
		if (this.hardwareState.actions.motors == undefined) {
			this.hardwareState.actions.motors = {};
		}
		// This is to handle negative values entered in the distance parameter in the drive block
		if ((direction != C.FOREWARD && distance > 0) || (direction == C.FOREWARD && distance < 0) || (direction != C.FOREWARD && !distance)) {
			speed *= -1;
		}
		// This is to handle 0 distance being passed in
		if (distance === 0) {
			speed = 0;
		}
		this.hardwareState.actions.motors[C.MOTOR_LEFT] = speed;
		this.hardwareState.actions.motors[C.MOTOR_RIGHT] = speed;
		this.hardwareState.motors[C.MOTOR_LEFT] = speed;
		this.hardwareState.motors[C.MOTOR_RIGHT] = speed;

		if (time !== undefined){
			return time;
		}
		const rotationPerSecond = C.MAX_ROTATION * Math.abs(speed) / 100.0;
		if (rotationPerSecond == 0.0 || distance === undefined) {
			return 0;
		} else {
			const rotations = Math.abs(distance) / (C.WHEEL_DIAMETER * Math.PI);
			return rotations / rotationPerSecond * 1000;
		}
	}

	public curveAction(name: string, direction: string, speedL: number, speedR: number, distance: number, time: number ): number {
		const robotText = 'robot: ' + name + ', direction: ' + direction;
		const durText = distance === undefined ? ' w.o. duration' : (' for ' + distance + ' msec');
		U.debug(robotText + ' left motor speed ' + speedL + ' right motor speed ' + speedR + durText);
		if (this.hardwareState.actions.motors == undefined) {
			this.hardwareState.actions.motors = {};
		}
		// This is to handle negative values entered in the distance parameter in the steer block
		if ((direction != C.FOREWARD && distance > 0) || (direction == C.FOREWARD && distance < 0) || (direction != C.FOREWARD && !distance)) {
			speedL *= -1;
			speedR *= -1;
		}
		// This is to handle 0 distance being passed in
		if (distance === 0) {
			speedR = 0;
			speedL = 0;
		}
		this.hardwareState.actions.motors[C.MOTOR_LEFT] = speedL;
		this.hardwareState.actions.motors[C.MOTOR_RIGHT] = speedR;
		this.hardwareState.motors[C.MOTOR_LEFT] = speedL;
		this.hardwareState.motors[C.MOTOR_RIGHT] = speedR;
		const avgSpeed = 0.5 * (Math.abs(speedL) + Math.abs(speedR))

		if (time !== undefined){
			return time;
		}
		const rotationPerSecond = C.MAX_ROTATION * avgSpeed / 100.0;
		if (rotationPerSecond == 0.0 || distance === undefined) {
			return 0;
		} else {
			const rotations = Math.abs(distance) / (C.WHEEL_DIAMETER * Math.PI);
			return rotations / rotationPerSecond * 1000;
		}
	}

	public turnAction(name: string, direction: string, speed: number, angle: number, time: number ): number {
		const robotText = 'robot: ' + name + ', direction: ' + direction;
		const durText = angle === undefined ? ' w.o. duration' : (' for ' + angle + ' msec');
		U.debug(robotText + ' motor speed ' + speed + durText);
		if (this.hardwareState.actions.motors == undefined) {
			this.hardwareState.actions.motors = {};
		}
		// This is to handle negative values entered in the degree parameter in the turn block
		if ((direction == C.LEFT && angle < 0) || (direction == C.RIGHT && angle < 0)) {
			speed *= -1;
		}
		// This is to handle an angle of 0 being passed in
		if (angle === 0) {
			speed = 0;
		}
		this.setTurnSpeed(speed, direction);

		if (time !== undefined){
			return time;
		}
		const rotationPerSecond = C.MAX_ROTATION * Math.abs(speed) / 100.0;
		if (rotationPerSecond == 0.0 || angle === undefined) {
			return 0;
		} else {
			const rotations = C.TURN_RATIO * (Math.abs(angle) / 720.);
			return rotations / rotationPerSecond * 1000;
		}
	}

	private setTurnSpeed(speed: number, direction: string): void {
		if (direction == C.LEFT) {
			this.hardwareState.actions.motors[C.MOTOR_LEFT] = -speed;
			this.hardwareState.actions.motors[C.MOTOR_RIGHT] = speed;
		} else {
			this.hardwareState.actions.motors[C.MOTOR_LEFT] = speed;
			this.hardwareState.actions.motors[C.MOTOR_RIGHT] = -speed;
		}
	}

	public driveStop(name: string): void {
		U.debug('robot: ' + name + ' stop motors');
		if (this.hardwareState.actions.motors == undefined) {
			this.hardwareState.actions.motors = {};
		}
		this.hardwareState.actions.motors[C.MOTOR_LEFT] = 0;
		this.hardwareState.actions.motors[C.MOTOR_RIGHT] = 0;
	}

	public getMotorSpeed(s: State, name: string, port: any): void {
		const robotText = 'robot: ' + name + ', port: ' + port;
		U.debug(robotText + ' motor get speed');
		const speed = this.hardwareState.motors[port];
		s.push(speed);
	}

	public setMotorSpeed(name: string, port: any, speed: number): void {
		const robotText = 'robot: ' + name + ', port: ' + port;
		U.debug(robotText + ' motor speed ' + speed);
		if (this.hardwareState.actions.motors == undefined) {
			this.hardwareState.actions.motors = {};
		}
		this.hardwareState.actions.motors[port] = speed;
		this.hardwareState.motors[port] = speed;
	}

	public showTextAction(text: any, mode: string): number {
		const showText = "" + text;
		U.debug('***** show "' + showText + '" *****');
		this.hardwareState.actions.display = {};
		this.hardwareState.actions.display[mode.toLowerCase()] = showText;
		this.setBlocking(true);
		return 0;
	}

	public showTextActionPosition(text: any, x: number, y: number): void {
		const showText = "" + text;
		U.debug('***** show "' + showText + '" *****');
		this.hardwareState.actions.display = {};
		this.hardwareState.actions.display.text = showText;
		this.hardwareState.actions.display.x = x;
		this.hardwareState.actions.display.y = y;
	}

	public showImageAction(image: any, mode: string): number {
		const showImage = "" + image;
		U.debug('***** show "' + showImage + '" *****');
		const imageLen = image.length;
		let duration = 0;
		if (mode == C.ANIMATION) {
			duration = imageLen * 200;
		}
		this.hardwareState.actions.display = {};
		this.hardwareState.actions.display.picture = image;
		if (mode) {
			this.hardwareState.actions.display.mode = mode.toLowerCase();
		}
		return duration;
	}

	public displaySetBrightnessAction(value: number): number {
		U.debug('***** set brightness "' + value + '" *****');
		this.hardwareState.actions.display = {};
		this.hardwareState.actions.display[C.BRIGHTNESS] = value;
		return 0;
	}

	public lightAction(mode: string, color: string, port: string): void {
		U.debug('***** light action mode= "' + mode + ' color=' + color + '" *****');

		if (port !== undefined){
			if (!this.hardwareState.actions.leds){
				this.hardwareState.actions.leds = {};
			}
			this.hardwareState.actions.leds[port] = {};
			this.hardwareState.actions.leds[port][C.MODE] = mode;
			this.hardwareState.actions.leds[port][C.COLOR] = color;
		}
		else{
			this.hardwareState.actions.led = {};
			this.hardwareState.actions.led[C.MODE] = mode;
			this.hardwareState.actions.led[C.COLOR] = color;
		}

	}

	public displaySetPixelBrightnessAction(x: number, y: number, brightness: number): number {
		U.debug('***** set pixel x="' + x + ", y=" + y + ", brightness=" + brightness + '" *****');
		this.hardwareState.actions.display = {};
		this.hardwareState.actions.display[C.PIXEL] = {};
		this.hardwareState.actions.display[C.PIXEL][C.X] = x;
		this.hardwareState.actions.display[C.PIXEL][C.Y] = y;
		this.hardwareState.actions.display[C.PIXEL][C.BRIGHTNESS] = brightness;
		return 0;
	}

	public displayGetPixelBrightnessAction(s: State, x: number, y: number): void {
		U.debug('***** get pixel x="' + x + ", y=" + y + '" *****');
		const sensor = this.hardwareState.sensors[C.DISPLAY][C.PIXEL];
		s.push(sensor[y][x]);
	}

	public clearDisplay(): void {
		U.debug('clear display');
		this.hardwareState.actions.display = {};
		this.hardwareState.actions.display.clear = true;
	}

	public writePinAction(pin: any, mode: string, value: number): void {
		this.hardwareState.actions["pin" + pin] = {};
		this.hardwareState.actions["pin" + pin][mode] = {};
		this.hardwareState.actions["pin" + pin][mode] = value;
	}

	public gyroReset(_port: number): void {
		var gyro = this.hardwareState.sensors['gyro'];
		if (gyro !== undefined) {
				var port = gyro[_port];
				if (port !== undefined) {
						var angle = port['angle'];
						if (angle !== undefined) {
						    if (this.hardwareState['angleReset'] == undefined) {
						     	 this.hardwareState['angleReset'] = {};
						    }
								this.hardwareState['angleReset'][_port] = angle;
						}
				}
		}
	}


	public getState(): any {
		return this.hardwareState;
	}

	public debugAction(value: any): void {
		U.debug('***** debug action "' + value + '" *****');
		console.log(value);
	}

	public assertAction(_msg: string, _left: any, _op: string, _right: any, value: boolean): void {
		U.debug('***** assert action "' + value + ' ' + _msg + ' ' + _left + ' ' + _op + ' ' + _right + '" *****');
		console.assert(value, _msg + " " + _left + " " + _op + " " + _right);
	}

	public close() {
	}
}

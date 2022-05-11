import { State } from 'interpreter.state';

export abstract class ARobotBehaviour {
    hardwareState;
    private blocking;

    constructor() {
        this.hardwareState = {};
        this.hardwareState.actions = {};
        this.hardwareState.sensors = {};
        this.blocking = false;
    }

    public getActionState(actionType: string, resetState = false): any {
        let v = this.hardwareState.actions[actionType];
        if (resetState) {
            delete this.hardwareState.actions[actionType];
        }
        return v;
    }

    public setBlocking(value: boolean) {
        this.blocking = value;
    }

    public getBlocking(): boolean {
        return this.blocking;
    }

    abstract clearDisplay(): void;

    abstract getSample(s: State, name: string, sensor: string, port: number, mode: string, slot: string): void;

    abstract timerReset(port: number): void;

    abstract encoderReset(port: string): void;

    abstract gyroReset(port: number): void;

    abstract odometryReset(slot: string): void;

    abstract timerGet(port: number): number;

    abstract ledOnAction(name: string, port: number, color: number): void;

    abstract lightAction(mode: string, color: string, port: string): void;

    abstract statusLightOffAction(name: string, port: number): void;

    abstract toneAction(name: string, frequency: number, duration: number): number;

    abstract playFileAction(file: string): number;

    abstract setVolumeAction(volume: number): void;

    abstract getVolumeAction(s: State): void;

    abstract setLanguage(language: string): void;

    abstract sayTextAction(text: string, speed: number, pitch: number): number;

    abstract motorOnAction(name: string, port: any, durationType: string, duration: number, speed: number, time: number): number;

    abstract getMotorSpeed(s: State, name: string, port: any): void;

    abstract setMotorSpeed(name: string, port: any, speed: number): void;

    abstract motorStopAction(name: string, port: any): void;

    abstract driveStop(name: string): void;

    abstract driveAction(name: string, direction: string, speed: number, distance: number, time: number): number;

    abstract curveAction(name: string, direction: string, speedL: number, speedR: number, distance: number, time: number): number;

    abstract turnAction(name: string, direction: string, speed: number, angle: number, time: number): number;

    abstract omniDriveAction(xVel: number, yVel: number, thetaVel: number): void;

    abstract omniDriveDistAction(xVel: number, yVel: number, distance: number): number;

    abstract omniStopDriveAction(): void;

    abstract omniDriveTurnAction(direction: string, thetaVel: number, angle: number): number;

    abstract omniDrivePositionAction(power, x, y): number;

    abstract writePinAction(pin: any, mode: string, value: number): void;

    abstract showTextAction(text: any, mode: string): number;

    abstract showTextActionPosition(text: any, x: number, y: number): void;

    abstract showImageAction(image: any, mode: string): number;

    abstract displaySetBrightnessAction(value: number): number;

    abstract displaySetPixelBrightnessAction(x: number, y: number, brightness: number): number;

    abstract displayGetPixelBrightnessAction(s: State, x: number, y: number): void;

    abstract remember(num: number): void;

    abstract recall(s: State): void;

    abstract debugAction(value: any): void;

    abstract assertAction(msg: string, left: any, op: string, right: any, value: boolean): void;

    abstract close(): void;

    abstract circleLedAction(ledValues: number[]): void;

    abstract buttonLedAction(ledValues: number[]): void;

    abstract proxHLedAction(ledValues: number[]): void;

    abstract temperatureLedAction(blue: number, red: number): void;

    abstract soundLedAction(val: number): void;
}

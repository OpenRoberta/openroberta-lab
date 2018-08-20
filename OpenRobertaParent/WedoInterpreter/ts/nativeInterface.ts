import { State } from "interpreter.state";

export interface Native {
    clearDisplay(): void;
    getSample( s: State, name: string, port: number, sensor: string, slot: string ): void;
    timerReset( port: number ): void;
    timerGet( port: number ): number;
    ledOnAction( name: string, port: number, color: number ): void;
    statusLightOffAction( name: string, port: number ): void;
    toneAction( name: string, frequency: number, duration: number ): void;
    motorOnAction( name: string, port: number, duration: number, speed: number ): void;
    motorStopAction( name: string, port: number ): void;
    showTextAction( text: any ): void;
    close(): void;
}
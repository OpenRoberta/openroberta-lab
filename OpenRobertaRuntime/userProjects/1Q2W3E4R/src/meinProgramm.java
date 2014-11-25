package generated.main;

import de.fhg.iais.roberta.ast.syntax.*;
import de.fhg.iais.roberta.codegen.lejos.Hal;

import de.fhg.iais.roberta.ast.syntax.action.*;
import de.fhg.iais.roberta.ast.syntax.sensor.*;
import de.fhg.iais.roberta.hardwarecomponents.ev3.*;
import de.fhg.iais.roberta.brickconfiguration.ev3.*;
public class meinProgramm {
private static final boolean TRUE = true;
    private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()
    .setWheelDiameter(5.6)
    .setTrackWidth(13.0)
    .addActor(ActorPort.B, new EV3Actor(HardwareComponentEV3Actor.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addActor(ActorPort.C, new EV3Actor(HardwareComponentEV3Actor.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addSensor(SensorPort.S1, new EV3Sensor(HardwareComponentEV3Sensor.EV3_TOUCH_SENSOR))
    .addSensor(SensorPort.S4, new EV3Sensor(HardwareComponentEV3Sensor.EV3_ULTRASONIC_SENSOR))
    .build();

    public static void main(String[] args) {
        try {
            new meinProgramm().run();
        } catch ( Exception e ) {
            lejos.hardware.lcd.TextLCD lcd = lejos.hardware.ev3.LocalEV3.get().getTextLCD();
            lcd.clear();
            lcd.drawString("Fehler im EV3-Roboter", 0, 2);
            lcd.drawString("Fehlermeldung", 0, 4);
            lcd.drawString(e.getMessage(), 0, 5);
            lcd.drawString("Press any key", 0, 7);
            lejos.hardware.Button.waitForAnyPress();
        }
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        while ( true ) {
            if ( true ) {
                break;
            }
        }
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}

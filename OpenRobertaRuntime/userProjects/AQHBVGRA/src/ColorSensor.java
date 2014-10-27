package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.codegen.lejos.Hal;

import de.fhg.iais.roberta.ast.syntax.action.*;
import de.fhg.iais.roberta.ast.syntax.sensor.*;
public class ColorSensor {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .setWheelDiameter(5.6)
    .setTrackWidth(13.0)
    .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addActor(ActorPort.C, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3GyroSensor))
    .addSensor(SensorPort.S2, new HardwareComponent(HardwareComponentType.EV3UltrasonicSensor))
    .addSensor(SensorPort.S3, new HardwareComponent(HardwareComponentType.EV3ColorSensor))
    .addSensor(SensorPort.S4, new HardwareComponent(HardwareComponentType.EV3TouchSensor))
    .build();

    public static void main(String[] args) {
        new ColorSensor().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.drawText("Hallo", 0, 0);
        while ( true ) {
            if ( hal.isPressed(SensorPort.S4) == true ) {
                break;
            }
        }
        hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);
        while ( true ) {
            if ( hal.isPressed(SensorPort.S4) == true ) {
                break;
            }
        }
        hal.drawText(String.valueOf(hal.getGyroSensorValue(SensorPort.S1)), 0, 0);
        while ( true ) {
            if ( hal.isPressed(SensorPort.S4) == true ) {
                break;
            }
        }
        hal.drawText(String.valueOf(hal.getColorSensorModeName(SensorPort.S3)), 0, 0);
        hal.drawText(String.valueOf(hal.getColorSensorValue(SensorPort.S2)), 0, 0);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}

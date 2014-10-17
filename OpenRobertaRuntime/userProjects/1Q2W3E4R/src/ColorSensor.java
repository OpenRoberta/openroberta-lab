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
    .addActor(ActorPort.A, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3ColorSensor))
    .build();

    public static void main(String[] args) {
        new ColorSensor().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.drawText(String.valueOf(hal.getColorSensorModeName(SensorPort.S1)), 0, 0);
        hal.drawText(String.valueOf(hal.getColorSensorValue(SensorPort.S1)), 0, 0);
        while ( true ) {
            if ( hal.isPressed(BrickKey.ENTER) == true ) {
                break;
            }
        }
        hal.clearDisplay();
        hal.setColorSensorMode(SensorPort.S1, ColorSensorMode.AMBIENTLIGHT);
        hal.drawText(String.valueOf(hal.getColorSensorModeName(SensorPort.S1)), 0, 0);
        hal.drawText(String.valueOf(hal.getColorSensorValue(SensorPort.S1)), 0, 0);
        while ( true ) {
            if ( hal.isPressed(BrickKey.UP) == true ) {
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

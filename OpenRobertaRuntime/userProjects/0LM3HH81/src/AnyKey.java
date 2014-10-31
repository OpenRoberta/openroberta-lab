package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.codegen.lejos.Hal;

import de.fhg.iais.roberta.ast.syntax.action.*;
import de.fhg.iais.roberta.ast.syntax.sensor.*;
public class AnyKey {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .setWheelDiameter(5.6)
    .setTrackWidth(13.0)
    .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addActor(ActorPort.C, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3TouchSensor))
    .addSensor(SensorPort.S4, new HardwareComponent(HardwareComponentType.EV3UltrasonicSensor))
    .build();

    public static void main(String[] args) {
        new AnyKey().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.drawText("Hallo", 0, 0);
        while ( true ) {
            if ( hal.isPressed(SensorPort.S1) == true ) {
                break;
            }
        }
        hal.driveDistance(ActorPort.B, ActorPort.C, false, DriveDirection.FOREWARD, 100, 20);
        hal.rotateDirectionAngle(ActorPort.B, ActorPort.C, false, TurnDirection.RIGHT, 50, 20);
        hal.driveDistance(ActorPort.B, ActorPort.C, false, DriveDirection.FOREWARD, 50, 100);
        while ( true ) {
            if ( hal.isPressed(SensorPort.S1) == true ) {
                break;
            }
        }
        hal.drawText(String.valueOf(hal.getUltraSonicSensorValue(SensorPort.S4)), 0, 0);
        hal.drawText(String.valueOf(hal.getUltraSonicSensorModeName(SensorPort.S4)), 0, 0);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}

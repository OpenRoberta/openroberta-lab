package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorMoveMode;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.action.MotorType;
import de.fhg.iais.roberta.ast.syntax.action.TurnDirection;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickKey;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class example1 {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .setWheelDiameter(5.6)
    .setTrackWidth(13.0)
    .addActor(ActorPort.A, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addSensor(SensorPort.S4, new HardwareComponent(HardwareComponentType.EV3TouchSensor))
    .build();

    public static void main(String[] args) {
        new example1().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.drawText("Press enter to start", 0, 0);
        while ( false == hal.isPressed(BrickKey.ENTER) ) {
        }
        for ( int i = 0; i < 4; i++ ) {
            hal.driveDistance(ActorPort.A, ActorPort.B, false, DriveDirection.FOREWARD, 50, 40);
            hal.rotateDirectionAngle(ActorPort.A, ActorPort.B, false, TurnDirection.RIGHT, 20, 90);
        }
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}

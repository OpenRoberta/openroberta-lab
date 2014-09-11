package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorMoveMode;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;
import de.fhg.iais.roberta.ast.syntax.action.MotorType;
import de.fhg.iais.roberta.ast.syntax.action.TurnDirection;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class test111 {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .build();

    public static void main(String[] args) {
        new test111().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.rotateDirectionDistanceRegulated(ActorPort.A, ActorPort.B, TurnDirection.RIGHT, 50, [[EmptyExpr [defVal=class java.lang.Integer]]]);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}

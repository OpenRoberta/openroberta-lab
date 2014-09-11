package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorMoveMode;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class rotation {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .build();

    public static void main(String[] args) {
        new rotation().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.rotateUnregulatedMotor(ActorPort.B, 30, MotorMoveMode.ROTATIONS, 10);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}

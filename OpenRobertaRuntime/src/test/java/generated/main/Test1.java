package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class Test1 {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
        .addActor(ActorPort.A, HardwareComponent.EV3MediumRegulatedMotor)
        .addActor(ActorPort.D, HardwareComponent.EV3LargeRegulatedMotor)
        .addSensor(SensorPort.S1, HardwareComponent.EV3TouchSensor)
        .addSensor(SensorPort.S2, HardwareComponent.EV3UltrasonicSensor)
        .build();

    public static void main(String[] args) {
        new Test1().run();
    }

    public void run() {
        Hal hal = new Hal(this.brickConfiguration);
        hal.drawText("Hallo", 0, 3);
    }
}
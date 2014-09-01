package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.ast.syntax.action.TurnDirection;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class Test2 {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
        .addActor(ActorPort.A, HardwareComponent.EV3MediumRegulatedMotor)
        .addActor(ActorPort.D, HardwareComponent.EV3LargeRegulatedMotor)
        .addSensor(SensorPort.S1, HardwareComponent.EV3TouchSensor)
        .addSensor(SensorPort.S2, HardwareComponent.EV3UltrasonicSensor)
        .build();

    public static void main(String[] args) {
        new Test2().run();
    }

    public void run() {
        Hal hal = new Hal(this.brickConfiguration);
        if ( 5 < hal.getRegulatedMotorSpeed(ActorPort.B) ) {

            hal.rotateDirectionRegulated(ActorPort.A, ActorPort.B, TurnDirection.RIGHT, 50);
        }
        if ( hal.getRegulatedMotorTachoValue(ActorPort.A) + hal.getInfraredSensorValue(SensorPort.S4) == hal.getUltraSonicSensorValue(SensorPort.S4) ) {
            hal.setInfraredSensorMode(SensorPort.S4, InfraredSensorMode.SEEK);
            hal.ledOff();
        } else {
            hal.resetGyroSensor(SensorPort.S2);
            while ( hal.isPressed(SensorPort.S1) ) {
                hal.drawPicture("SMILEY1", 0, 0);
                hal.clearDisplay();
            }
            hal.ledOn(BrickLedColor.GREEN, true);
        }
    }
}
//package generated.main;
//
//import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
//import de.fhg.iais.roberta.ast.syntax.HardwareComponentOld;
//import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
//import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
//import de.fhg.iais.roberta.codegen.lejos.Hal;
//
//public class Test1 {
//    private final BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
//        .addActor(ActorPort.A, HardwareComponentOld.EV3MediumRegulatedMotor)
//        .addActor(ActorPort.D, HardwareComponentOld.EV3LargeRegulatedMotor)
//        .addSensor(SensorPort.S1, HardwareComponentOld.EV3TouchSensor)
//        .addSensor(SensorPort.S2, HardwareComponentOld.EV3UltrasonicSensor)
//        .build();
//
//    public static void main(String[] args) {
//        new Test1().run();
//    }
//
//    public void run() {
//        Hal hal = new Hal(this.brickConfiguration);
//        hal.drawText("Hallo", 0, 3);
//    }
//}
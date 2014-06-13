import java.util.Properties;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Delay;

public class EV3HelloWorld {

    private static Properties devices = new Properties();

    public static void main(String[] args) {
        //Some Helloworld program
        /*LCD.clear();
        LCD.drawString("Hallo Welt", 0, 3);
        Button.waitForAnyPress();
        LCD.clear();
        LCD.refresh();*/

        // init from configuration
        //        devices.setProperty("rightMotor", "A");
        //        devices.setProperty("leftMotor", "D");
        //        devices.setProperty("ultraSonicSensor", "S1");
        //        devices.setProperty("rightTouchSensor", "S2");
        //        devices.setProperty("leftTouchSensor", "S3");
        //devices.setProperty("gyroSensor", "S4");
        //devices.setProperty("colorSensor", "S4");

        EV3LargeRegulatedMotor largeRM = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
        //        largeRM.setSpeed(300);
        //
        //        largeRM.forward();
        //        Delay.msDelay(5000);

        largeRM.setSpeed(1000);

        Delay.msDelay(5000);
        //largeRM.flt();
        largeRM.flt();
        Delay.msDelay(5000);

        //        RobertaFunctions roberta = new RobertaFunctions(devices);
        //        roberta.validateDevices();
        //        roberta.startup();

        // <"main" program from blocks>

        //        roberta.shutdown();

    }

}

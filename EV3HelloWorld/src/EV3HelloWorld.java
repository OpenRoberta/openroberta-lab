import java.util.Properties;

public class EV3HelloWorld {

    private static Properties devices = new Properties();

    public static void main(String[] args) {
        /*LCD.clear();
        LCD.drawString("Hallo Welt", 0, 3);
        Button.waitForAnyPress();
        LCD.clear();
        LCD.refresh();*/

        // init from block configuration, example: "roberta car"
        devices.setProperty("rightMotor", "A");
        devices.setProperty("leftMotor", "D");
        devices.setProperty("ultraSonicSensor", "S1");
        devices.setProperty("rightTouchSensor", "S2");
        devices.setProperty("leftTouchSensor", "S3");
        //devices.setProperty("gyroSensor", "S4");
        //devices.setProperty("colorSensor", "S4");

        RobertaFunctions roberta = new RobertaFunctions(devices);
        roberta.validateDevices();
        roberta.startup();

        // <"main" program from blocks>

        roberta.shutdown();

    }

}

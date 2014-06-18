import java.util.Properties;

public class EV3HelloWorld {

    private static Properties sensorConfig = new Properties();
    private static Properties motorConfig = new Properties();

    public static void main(String[] args) {
        // Some Helloworld program
        /*
         * LCD.clear(); LCD.drawString("Hallo Welt", 0, 3);
         * Button.waitForAnyPress(); LCD.clear(); LCD.refresh();
         */

        // init from configuration
        sensorConfig.setProperty("S1", "EV3UltrasonicSensor");
        sensorConfig.setProperty("S2", "EV3TouchSensor");
        sensorConfig.setProperty("S3", "EV3GyroSensor");
        sensorConfig.setProperty("S4", "EV3ColorSensor");

        motorConfig.setProperty("A", "EV3LargeRegulatedMotor");
        motorConfig.setProperty("B", "EV3LargeRegulatedMotor");
        motorConfig.setProperty("C", "EV3MediumRegulatedMotor");
        motorConfig.setProperty("D", "");

        RobertaFunctions roberta = new RobertaFunctions();
        roberta.checkSensorConfig(sensorConfig);
        roberta.startup();

        // <"main" program from blocks>

        roberta.shutdown();

    }

}

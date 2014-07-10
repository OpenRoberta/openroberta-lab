import java.util.Properties;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3TouchSensor;

public class Main {

    private static Properties sensorConfig = new Properties();
    private static Properties motorConfig = new Properties();

    public static void main(String[] args) {
        // A Helloworld program
        /*
         * LCD.clear();
         * LCD.drawString("Hallo Welt", 0, 3);
         * Button.waitForAnyPress();
         * LCD.clear();
         * LCD.refresh();
         */

        // init from configuration
        sensorConfig.setProperty("S1", "EV3UltraSonicSensor");
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

        float[] sample = new float[distance.sampleSize()];
        new EV3TouchSensor(LocalEV3.get().getPort("S1")).fetchSample(sample, 0);

        distance.fetchSample(sample, 0);

        // <"main" program from blocks>

        roberta.shutdown();

    }

}

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Main {

    //    private static Properties sensorConfig = new Properties();
    //    private static Properties motorConfig = new Properties();

    public static void main(String[] args)
        throws InstantiationException,
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException,
        NoSuchMethodException,
        SecurityException {
        // A Helloworld program
        /*
         * LCD.clear();
         * LCD.drawString("Hallo Welt", 0, 3);
         * Button.waitForAnyPress();
         * LCD.clear();
         * LCD.refresh();
         */

        // init from configuration
        //        sensorConfig.setProperty("S1", "EV3UltraSonicSensor");
        //        sensorConfig.setProperty("S2", "EV3TouchSensor");
        //        sensorConfig.setProperty("S3", "EV3GyroSensor");
        //        sensorConfig.setProperty("S4", "EV3ColorSensor");
        //
        //        motorConfig.setProperty("A", "EV3LargeRegulatedMotor");
        //        motorConfig.setProperty("B", "EV3LargeRegulatedMotor");
        //        motorConfig.setProperty("C", "EV3MediumRegulatedMotor");
        //        motorConfig.setProperty("D", "");
        //
        //        RobertaFunctions roberta = new RobertaFunctions();
        //        roberta.checkSensorConfig(sensorConfig);
        //        roberta.startup();
        //
        //        // <"main" program from blocks>
        //
        //        roberta.shutdown();

        // get all sensor modes of a sensor + poly test
        BaseSensor sonic = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
        List<String> list = sonic.getAvailableModes();
        for ( String tmp : list ) {
            System.out.println(tmp);
            SampleProvider sp = sonic.getMode(tmp);
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            for ( int i = 0; i < sample.length; i++ ) {
                System.out.println(sample[i]);
            }
        }
        sonic.close();

        BaseSensor color = new EV3ColorSensor(LocalEV3.get().getPort("S2"));
        List<String> list2 = color.getAvailableModes();
        for ( String tmp : list2 ) {
            System.out.println(tmp);
            SampleProvider sp = color.getMode(tmp);
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            for ( int i = 0; i < sample.length; i++ ) {
                System.out.println(sample[i]);
            }
        }
        color.close();

        BaseSensor gyro = new EV3GyroSensor(LocalEV3.get().getPort("S3"));
        List<String> list3 = gyro.getAvailableModes();
        for ( String tmp : list3 ) {
            System.out.println(tmp);
            SampleProvider sp = gyro.getMode(tmp);
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            for ( int i = 0; i < sample.length; i++ ) {
                System.out.println(sample[i]);
            }
        }
        gyro.close();

        BaseSensor touch = new EV3TouchSensor(LocalEV3.get().getPort("S4"));
        List<String> list4 = touch.getAvailableModes();
        for ( String tmp : list4 ) {
            System.out.println(tmp);
            SampleProvider sp = touch.getMode(tmp);
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            for ( int i = 0; i < sample.length; i++ ) {
                System.out.println(sample[i]);
            }
        }
        touch.close();

        //differential drive forward
        //        EV3LargeRegulatedMotor motor1 = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
        //        EV3LargeRegulatedMotor motor2 = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
        //        DifferentialPilot pilot = new DifferentialPilot(2.1f, 4.4f, motor1, motor2, false);
        //        pilot.setRotateSpeed(rotateSpeed);
        //        pilot.forward();
        //        pilot.rotateRight();
        //        pilot.rotateLeft();
        //        pilot.steer(turnRate, angle, false);
        //        pilot.stop();
        //        pilot.rotate(angle, immediateReturn);
        //        LocalEV3.get().getLED().setPattern(0);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(1);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(2);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(3);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(4);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(5);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(6);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(7);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(8);
        //        Delay.msDelay(3000);
        //        LocalEV3.get().getLED().setPattern(0);
        //        Delay.msDelay(3000);
        //        Port port = LocalEV3.get().getPort("S2");
        //        BaseSensor sensor = null;
        //        sensor = new EV3ColorSensor(port);
    }
}

package sensortest;

import java.util.List;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Main {

    public static void main(String[] args) {

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

    }

}

//package de.fhg.iais.roberta.codegen.lejos;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.TreeMap;
//
//import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
//import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
//import de.fhg.iais.roberta.ast.syntax.action.LightAction;
//import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
//import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
//import de.fhg.iais.roberta.ast.syntax.sensor.UltraSSensor;
//import de.fhg.iais.roberta.conf.transformer.BrickConfiguration;
//
///**
// * Connection class between generated code and leJOS methods
// * TODO replace object by reference to lejos classes for motors/sensors
// * TODO reference to textlcd, graphicslcd, audio, buttons
// * TODO BrickConfiguration empty class
// * TODO actor/sensor bindings initialisation
// * 
// * @author dpyka
// */
//public class Hal {
//
//    private final BrickConfiguration brickConfiguration;
//    // TODO HashMap or TreeMap?
//    private final Map<ActorPort, Object> lejosActorBindings = new TreeMap<>();
//    private final Map<SensorPort, BaseSensor> lejosSensorBindings = new TreeMap<>();
//    private final Map<SensorPort, String> lejosSensorModeBindings = new TreeMap<>();
//
//    public Hal(BrickConfiguration brickConfiguration) {
//        this.brickConfiguration = brickConfiguration;
//    }
//
//    /**
//     * @param percent
//     * @return degrees per second
//     */
//    private int toDegPerSec(int percent) {
//        return 720 / 100 * percent;
//    }
//
//    /**
//     * @param degrees per second
//     * @return percent
//     */
//    private int toPercent(int degPerSec) {
//        return degPerSec * 100 / 720;
//    }
//
//    /**
//     * @param port
//     * @return motor object from BrickConfiguration
//     */
//    private Object getMotorObject(ActorPort port) {
//        String motorType = this.brickConfiguration.getActorOnPort(port);
//        if ( !this.lejosActorBindings.containsValue(port) ) {
//            try {
//                Object motor = Class.forName(motorType).newInstance();
//                this.lejosActorBindings.put(port, motor);
//            } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return this.lejosActorBindings.get(port);
//    }
//
//    /**
//     * @param port
//     * @return sensor object from BrickConfiguration
//     */
//    private BaseSensor getSensorObject(SensorPort port) {
//        String sensorType = this.brickConfiguration.getSensorOnPort(port);
//        String sensorModeName = this.brickConfiguration.getSensorModeName(port);
//        if ( !this.lejosSensorBindings.containsValue(port) ) {
//            try {
//                BaseSensor sensor = Class.forName(sensorType).newInstance();
//                this.lejosSensorBindings.put(port, sensor);
//                saveSensorModeName(sensor, sensorModeName);
//            } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return this.lejosSensorBindings.get(port);
//    }
//
//    private void saveSensorModeName(SensorPort port, String sensorModeName) {
//        this.lejosSensorModeBindings.put(port, sensorModeName);
//    }
//
//    private String getSensorModeName(SensorPort port) {
//        return this.lejosSensorModeBindings.get(port);
//    }
//
//    private SampleProvider configureSensorMode(SensorPort port, String sensorMode) {
//        return getSensorObject(port).getMode(sensorMode);
//    }
//
//    // --- Aktion Bewegung ---
//
//    /**
//     * @param port
//     * @param speedPercent
//     */
//    public void setMotorSpeed(ActorPort port, int speedPercent) {
//        try {
//            Method m = getMotorObject(port).getClass().getDeclaredMethod("setSpeed");
//            m.invoke(getMotorObject(port), toDegPerSec(speedPercent));
//        } catch ( NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * @param port
//     * @param speedPercent
//     * @param rotations
//     */
//    public void rotateMotor(ActorPort port, int speedPercent, int rotations) {
//        Object motor = getMotorObject(port);
//        motor.setSpeed(toDegPerSec(speedPercent));
//        motor.rotate(rotations);
//    }
//
//    /**
//     * @param port
//     * @return
//     */
//    public int getSpeed(ActorPort port) {
//        return toPercent(getMotorObject(port).getSpeed());
//    }
//
//    /**
//     * @param port
//     * @param floating
//     */
//    public void stopMotor(ActorPort port, boolean floating) {
//        getMotorObject(port).flt(floating); // stop & (floating | (!floating))
//    }
//
//    // --- END Aktion Bewegung ---
//    // --- Aktion Fahren ---
//
//    /**
//     * @param port1
//     * @param port2
//     * @param direction
//     * @param speedPercent
//     */
//    public void drive(ActorPort port1, ActorPort port2, DriveAction.Di direction, int speedPercent) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        if ( direction == DriveAction.Di.foreward ) {
//            dPilot.forward();
//        } else if ( direction == DriveAction.Di.backward ) {
//            dPilot.backward();
//        }
//    }
//
//    /**
//     * TODO conversion from rotation to distance (cm)
//     * 
//     * @param port
//     * @param speedPercent
//     * @param rotations
//     */
//    public void driveDistance(ActorPort port1, ActorPort port2, DriveAction.Di direction, int speedPercent, int distance) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        if ( direction == DriveAction.Di.foreward ) {
//            dPilot.travel(distance);
//        } else if ( direction == DriveAction.Di.backward ) {
//            dPilot.travel(-distance);
//        }
//    }
//
//    /**
//     * all motors??? two driving motors???
//     * 
//     * @param port1
//     * @param port2
//     */
//    public void stop(ActorPort port1, ActorPort port2) {
//        Object motor = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.stop();
//    }
//
//    /**
//     * @param port1
//     * @param port2
//     * @param direction
//     * @param speedPercent
//     */
//    public void rotateDirection(ActorPort port1, ActorPort port2, TurnAction.Di direction, int speedPercent) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        if ( direction == TurnAction.Di.right ) { // rechts drehen
//            pilot.rotateRight();
//        } else if ( direction == TurnAction.Di.left ) { // links drehen
//            pilot.rotateLeft();
//        }
//    }
//
//    /**
//     * TODO conversion
//     * TODO return immediately or not?
//     * 
//     * @param port1
//     * @param port2
//     * @param direction
//     * @param speedPercent
//     * @param distance
//     */
//    public void rotateDirectionDistance(ActorPort port1, ActorPort port2, TurnAction.Di direction, int speedPercent, int distance) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        // check if conversion is correct!!!
//        int angle = (int) Math.round(Math.tan(distance / (this.brickConfiguration.getTrackWidth() / 2)));
//        if ( direction == TurnAction.Di.right ) {
//            // negative turnRate(speedPercent) ^= turn right
//            speedPercent = speedPercent * -1;
//            dPilot.steer(speedPercent, angle, false);
//        } else if ( direction == TurnAction.Di.left ) {
//            dPilot.steer(speedPercent, angle, false);
//        }
//    }
//
//    // --- END Aktion Fahren ---
//    // --- Aktion Anzeige ---
//
//    /**
//     * TODO create textlcd in brickConfiguration??
//     * 
//     * @param text
//     * @param x
//     * @param y
//     */
//    public void drawText(String text, int x, int y) {
//        TextLCD lcd = LocalEV3.get().getTextLCD();
//        lcd.drawString(text, x, y);
//    }
//
//    /**
//     * TODO create graphicslcd in brickConfiguration??
//     * 
//     * @param smiley
//     * @param x
//     * @param y
//     */
//    public void drawPicture(int smiley, int x, int y) {
//        GraphicsLCD glcd = LocalEV3.get().getGraphicsLCD();
//        switch ( smiley ) {
//            case 1:
//                // number to image
//                break;
//            case 2:
//                // number to image
//                break;
//            case 3:
//                // number to image
//                break;
//            case 4:
//                // number to image
//                break;
//        }
//        //glcd.drawImage(image, x, y, 0);
//    }
//
//    /**
//     * TODO create textlcd in brickConfiguration??
//     */
//    public void clearDisplay() {
//        TextLCD lcd = LocalEV3.get().getTextLCD();
//        lcd.clear();
//    }
//
//    // --- END Aktion Anzeige ---
//    // -- Aktion Klang ---
//
//    /**
//     * @param frequency
//     * @param duration
//     */
//    public void playTone(int frequency, int duration) {
//        Audio audio = LocalEV3.get().getAudio();
//        audio.playTone(frequency, duration);
//    }
//
//    /**
//     * play one out of 4 soundfiles
//     * TODO create sound files
//     * 
//     * @param fileNumber
//     */
//    public void playFile(int fileNumber) {
//        Audio audio = LocalEV3.get().getAudio();
//        // number to file
//        audio.playSample(file);
//    }
//
//    /**
//     * set the volume of the speaker
//     * 
//     * @param volume
//     */
//    public void setVolume(int volume) {
//        Audio audio = LocalEV3.get().getAudio();
//        audio.setVolume(volume);
//    }
//
//    /**
//     * @return volume of the speaker
//     */
//    public int getVolume() {
//        Audio audio = LocalEV3.get().getAudio();
//        return audio.getVolume();
//    }
//
//    // -- END Aktion Klang ---
//    // --- Aktion Statusleuchte ---
//
//    /**
//     * TODO enum for blink 3colors x3 modes
//     * 
//     * @param color
//     * @param blink
//     */
//    public void ledOn(LightAction.Co color, boolean blink) {
//        LED led = LocalEV3.get().getLED();
//        // since java 7
//        switch ( color ) {
//            case green:
//                if ( blink ) {
//                    led.setPattern(4);
//                } else {
//                    led.setPattern(1);
//                }
//                break;
//            case orange:
//                if ( blink ) {
//                    led.setPattern(6);
//                } else {
//                    led.setPattern(3);
//                }
//                break;
//            case red:
//                if ( blink ) {
//                    led.setPattern(5);
//                } else {
//                    led.setPattern(2);
//                }
//                break;
//        }
//    }
//
//    /**
//     * turn off led
//     */
//    public void ledOff() {
//        LED led = LocalEV3.get().getLED();
//        led.setPattern(0);
//    }
//
//    /**
//     * TODO what should this block do???
//     */
//    public resetLED() {
//        LED led = LocalEV3.get().getLED();
//        led.setPattern(0);
//    }
//
//    // --- END Aktion Statusleuchte ---
//    // --- Sensoren Berührungssensor ---
//
//    /**
//     * check if touchsensor is pressed
//     * TODO implicite sampleSize = 0
//     * 
//     * @param port
//     * @return
//     */
//    public boolean isPressed(SensorPort port) {
//        SampleProvider sp = configureSensorMode(port, getSensorModeName(port));
//        // always 1 cell for touch sensor
//        float[] sample = new float[sp.sampleSize()];
//        sp.fetchSample(sample, 0);
//        if ( sample[0] == 1.0 ) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    // --- END Sensoren Berührungssensor ---
//    // --- Sensoren Ultraschallsensor ---
//
//    /**
//     * set ultrasonic mode distance/presence
//     * 
//     * @param port
//     * @param mode
//     */
//    public void setUltrasonicSensorMode(SensorPort port, UltraSSensor.Mode mode) {
//        if ( mode == UltraSSensor.Mode.DISTANCE ) {
//            configureSensorMode(port, "Distance");
//        } else if ( mode == UltraSSensor.Mode.PRESENCE ) {
//            configureSensorMode(port, "Listen");
//        }
//    }
//
//    /**
//     * @param port
//     * @return
//     */
//    public String getUltraSonicModeName(SensorPort port) {
//        return getSensorModeName(port);
//    }
//
//    /**
//     * TODO check sample size (=1 or more?)
//     * 
//     * @param port
//     * @return
//     */
//    public int getUltraSonicValue(SensorPort port) {
//        SampleProvider sp = configureSensorMode(port, getSensorModeName(port));
//        // always 1 cell (distance and listen mode)
//        float[] sample = new float[sp.sampleSize()];
//        sp.fetchSample(sample, 0);
//        if ( getSensorModeName(port).equals("Distance") ) {
//            return Math.round(sample[0]) * 100; // distance in cm
//        } else if ( getSensorModeName(port).equals("Listen") ) {
//            return Math.round(sample[0]);
//        }
//    }
//    // END Sensoren Ultraschallsensor ---
//}

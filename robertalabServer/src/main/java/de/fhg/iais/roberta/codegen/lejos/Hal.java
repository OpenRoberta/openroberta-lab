//package de.fhg.iais.roberta.codegen.lejos;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.TreeMap;
//
//import lejos.hardware.Audio;
//import lejos.hardware.LED;
//import lejos.hardware.ev3.LocalEV3;
//import lejos.hardware.lcd.GraphicsLCD;
//import lejos.hardware.lcd.TextLCD;
//import lejos.hardware.motor.EV3LargeRegulatedMotor;
//import lejos.hardware.motor.EV3MediumRegulatedMotor;
//import lejos.hardware.motor.NXTMotor;
//import lejos.hardware.motor.NXTRegulatedMotor;
//import lejos.hardware.port.Port;
//import lejos.hardware.sensor.BaseSensor;
//import lejos.hardware.sensor.EV3ColorSensor;
//import lejos.hardware.sensor.EV3GyroSensor;
//import lejos.hardware.sensor.EV3IRSensor;
//import lejos.hardware.sensor.EV3TouchSensor;
//import lejos.hardware.sensor.EV3UltrasonicSensor;
//import lejos.robotics.SampleProvider;
//import lejos.robotics.navigation.DifferentialPilot;
//import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
//import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
//import de.fhg.iais.roberta.ast.syntax.action.LightAction;
//import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
//import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
//import de.fhg.iais.roberta.ast.syntax.sensor.UltraSSensor;
//import de.fhg.iais.roberta.conf.transformer.BrickConfiguration;
//import de.fhg.iais.roberta.conf.transformer.HardwareComponent;
//import de.fhg.iais.roberta.dbc.DbcException;
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
//    private final Map<ActorPort, EV3LargeRegulatedMotor> lejosEV3LargeMotors = new TreeMap<>();
//    private final Map<ActorPort, EV3MediumRegulatedMotor> lejosEV3MediumMotors = new TreeMap<>();
//    private final Map<ActorPort, NXTMotor> lejosNXTMotors = new TreeMap<>();
//    private final Map<ActorPort, NXTRegulatedMotor> lejosNXTRegulatedMotors = new TreeMap<>();
//
//    // TODO test nested map
//    private final Map<ActorPort, Map<ActorPort, TreeMap<ActorPort, ?>>> referenceMap = new TreeMap<>();
//
//    private final Map<SensorPort, BaseSensor> lejosSensorBindings = new TreeMap<>();
//    private final Map<SensorPort, String> lejosSensorModeNames = new TreeMap<>();
//    private final Map<SensorPort, SampleProvider> lejosSampleProvider = new TreeMap<>();
//
//    public Hal(BrickConfiguration brickConfiguration) {
//        this.brickConfiguration = brickConfiguration;
//    }
//
//    /**
//     * instantiate all lejos objects for devices that are needed
//     * 
//     * @param actorPort1
//     * @param actorPort2
//     * @param actorPort3
//     * @param actorPort4
//     * @param sensorPort1
//     * @param sensorPort2
//     * @param sensorPort3
//     * @param sensorPort4
//     */
//    public void createDeviceObjectsFromConfiguration(
//        ActorPort actorPort1,
//        ActorPort actorPort2,
//        ActorPort actorPort3,
//        ActorPort actorPort4,
//        SensorPort sensorPort1,
//        SensorPort sensorPort2,
//        SensorPort sensorPort3,
//        SensorPort sensorPort4) {
//        createMotorObject(actorPort1);
//        createMotorObject(actorPort2);
//        createMotorObject(actorPort3);
//        createMotorObject(actorPort4);
//        createSampleProvider(sensorPort1);
//        createSampleProvider(sensorPort2);
//        createSampleProvider(sensorPort3);
//        createSampleProvider(sensorPort4);
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
//    private void createMotorObject(ActorPort actorPort) {
//        HardwareComponent actorType = null;
//        Port hardwarePort = LocalEV3.get().getPort(actorPort.toString());
//        switch ( actorPort ) {
//            case A:
//                actorType = this.brickConfiguration.getActorA();
//                break;
//            case B:
//                actorType = this.brickConfiguration.getActorB();
//                break;
//            case C:
//                actorType = this.brickConfiguration.getActorC();
//                break;
//            case D:
//                actorType = this.brickConfiguration.getActorD();
//                break;
//            default:
//                throw new DbcException("Invalid Actor Port!");
//        }
//
//        if ( actorType != null ) {
//            switch ( actorType ) {
//                case EV3LargeRegulatedMotor:
//                    EV3LargeRegulatedMotor ev3LargeRegulatedMotor = new EV3LargeRegulatedMotor(hardwarePort);
//                    this.lejosEV3LargeMotors.put(actorPort, ev3LargeRegulatedMotor);
//                    //this.referenceMap.put(actorPort, this.lejosEV3LargeMotors);
//                    break;
//                case EV3MediumRegulatedMotor:
//                    EV3MediumRegulatedMotor ev3MediumRegulatedMotor = new EV3MediumRegulatedMotor(hardwarePort);
//                    this.lejosEV3MediumMotors.put(actorPort, ev3MediumRegulatedMotor);
//                    break;
//                case NXTMotor:
//                    NXTMotor nxtMotor = new NXTMotor(hardwarePort);
//                    this.lejosNXTMotors.put(actorPort, nxtMotor);
//                    break;
//                case NXTRegulatedMotor:
//                    NXTRegulatedMotor nxtRegulatedMotor = new NXTRegulatedMotor(hardwarePort);
//                    this.lejosNXTRegulatedMotors.put(actorPort, nxtRegulatedMotor);
//                    break;
//                default:
//                    throw new DbcException("Invalid/unsupported Actor name!");
//            }
//        }
//    }
//
//    private void createSampleProvider(SensorPort sensorPort) {
//        HardwareComponent sensorType = null;
//        Port hardwarePort = LocalEV3.get().getPort(sensorPort.toString());
//        switch ( sensorPort ) {
//            case S1:
//                sensorType = this.brickConfiguration.getSensor1();
//                break;
//            case S2:
//                sensorType = this.brickConfiguration.getSensor2();
//                break;
//            case S3:
//                sensorType = this.brickConfiguration.getSensor3();
//                break;
//            case S4:
//                sensorType = this.brickConfiguration.getSensor4();
//                break;
//            default:
//                throw new DbcException("Invalid Sensor Port!");
//        }
//
//        if ( !this.lejosSensorBindings.containsKey(sensorPort) ) {
//            // instantiate specific lejos sensor object
//            BaseSensor sensor = null;
//            if ( sensorType != null ) {
//                switch ( sensorType ) {
//                    case EV3ColorSensor:
//                        sensor = new EV3ColorSensor(hardwarePort);
//                        break;
//                    case EV3IRSensor:
//                        sensor = new EV3IRSensor(hardwarePort);
//                        break;
//                    case EV3GyroSensor:
//                        sensor = new EV3GyroSensor(hardwarePort);
//                        break;
//                    case EV3TouchSensor:
//                        sensor = new EV3TouchSensor(hardwarePort);
//                        break;
//                    case EV3UltrasonicSensor:
//                        sensor = new EV3UltrasonicSensor(hardwarePort);
//                        break;
//                    default:
//                        throw new DbcException("Invalid/unsupported Sensor name!");
//                }
//                this.lejosSensorBindings.put(sensorPort, sensor);
//
//                String initialSensorModeName = this.brickConfiguration.getSensorModeName(sensorPort);
//                setNewSensorMode(sensorPort, sensor, initialSensorModeName);
//            }
//        }
//        //return this.lejosSampleProvider.get(sensorPort);
//    }
//
//    private void setNewSensorMode(SensorPort sensorPort, BaseSensor sensor, String sensorMode) {
//        // save the sensorModeName (only used for return sensorModeName block!, keep synchronized with sampleprovider hashmap)
//        this.lejosSensorModeNames.put(sensorPort, sensorMode);
//        // set & save the sampleprovider of a sensor
//        SampleProvider sp = sensor.getMode(sensorMode);
//        this.lejosSampleProvider.put(sensorPort, sp);
//    }
//
//    private String getSensorModeName(SensorPort sensorPort) {
//        return this.lejosSensorModeNames.get(sensorPort);
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
//    public void drive(ActorPort port1, ActorPort port2, DriveAction.Direction direction, int speedPercent) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        switch ( direction ) {
//            case FOREWARD:
//                dPilot.forward();
//                break;
//            case BACKWARD:
//                dPilot.backward();
//                break;
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
//    public void driveDistance(ActorPort port1, ActorPort port2, DriveAction.Direction direction, int speedPercent, int distance) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        switch ( direction ) {
//            case FOREWARD:
//                dPilot.travel(distance);
//                break;
//            case BACKWARD:
//                dPilot.travel(-distance);
//                break;
//
//            default:
//                break;
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
//        Object motor1 = getMotorObject(port1);
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
//    public void rotateDirection(ActorPort port1, ActorPort port2, TurnAction.Direction direction, int speedPercent) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        switch ( direction ) {
//            case RIGHT:
//                dPilot.rotateRight();
//                break;
//            case LEFT:
//                dPilot.rotateLeft();
//                break;
//        }
//    }
//
//    /**
//     * TODO speedPercent
//     * TODO return immediately or not?
//     * 
//     * @param port1
//     * @param port2
//     * @param direction
//     * @param speedPercent
//     * @param distance
//     */
//    public void rotateDirectionDistance(ActorPort port1, ActorPort port2, TurnAction.Direction direction, int speedPercent, int distance) {
//        Object motor1 = getMotorObject(port1);
//        Object motor2 = getMotorObject(port2);
//        DifferentialPilot dPilot =
//            new DifferentialPilot(this.brickConfiguration.getWheelDiameter(), this.brickConfiguration.getTrackWidth(), motor1, motor2, false);
//        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
//        // check if conversion is correct!!!
//        int angle = (int) Math.round(Math.tan(distance / (this.brickConfiguration.getTrackWidth() / 2)));
//        switch ( direction ) {
//            case RIGHT:
//                dPilot.rotate(angle, false);
//                break;
//            case LEFT:
//                angle = angle * -1;
//                dPilot.rotate(angle, false);
//                break;
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
//    public void ledOn(LightAction.Color color, boolean blink) {
//        LED led = LocalEV3.get().getLED();
//        // since java 7
//        switch ( color ) {
//            case GREEN:
//                if ( blink ) {
//                    led.setPattern(4);
//                } else {
//                    led.setPattern(1);
//                }
//                break;
//            case ORANGE:
//                if ( blink ) {
//                    led.setPattern(6);
//                } else {
//                    led.setPattern(3);
//                }
//                break;
//            case RED:
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
//        SampleProvider sp = getSampleProvider(sensorPort);
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

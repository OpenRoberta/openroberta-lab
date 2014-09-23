package de.fhg.iais.roberta.codegen.lejos;

import java.io.File;

import lejos.hardware.Audio;
import lejos.hardware.Keys;
import lejos.hardware.LED;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.lcd.TextLCD;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Stopwatch;
import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorMoveMode;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;
import de.fhg.iais.roberta.ast.syntax.action.TurnDirection;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickKey;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Connection class between generated code and leJOS methods
 * 
 * @author dpyka
 */
public class Hal {

    private final DeviceHandler deviceHandler;

    private final EV3 brick;
    private final TextLCD lcd;
    private final GraphicsLCD glcd;
    private final LED led;
    private final Keys keys;
    private final Audio audio;

    private final Stopwatch[] timers = new Stopwatch[5];

    private final double wheelDiameter;
    private final double trackWidth;

    /**
     * @param brickConfiguration
     */
    public Hal(BrickConfiguration brickConfiguration) {
        this.deviceHandler = new DeviceHandler(brickConfiguration);

        this.wheelDiameter = brickConfiguration.getWheelDiameterCM();
        this.trackWidth = brickConfiguration.getTrackWidthCM();

        this.brick = LocalEV3.get();
        // TODO test if needed for Motor/ Sensorport enums from lejos??
        this.brick.setDefault();
        this.lcd = this.brick.getTextLCD();
        this.glcd = this.brick.getGraphicsLCD();
        this.led = this.brick.getLED();
        this.keys = this.brick.getKeys();
        this.audio = this.brick.getAudio();
    }

    /**
     * @param speedPercent
     * @return degrees per second
     */
    private int toDegPerSec(int speedPercent) {
        return 720 / 100 * speedPercent;
    }

    /**
     * @param degreesPerSecond
     * @return speedPercent
     */
    private int toPercent(int degPerSec) {
        return degPerSec * 100 / 720;
    }

    /**
     * convert rotations count to motor rotation angle<br>
     * maybe used for unregulated motors?
     * 
     * @param rotations
     * @return
     */
    private int rotationsToAngle(int rotations) {
        return rotations * 360;
    }

    // --- Aktion Bewegung ---

    /**
     * @param actorPort
     * @param speedPercent
     */
    public void turnOnRegulatedMotor(ActorPort actorPort, int speedPercent) {
        setRegulatedMotorSpeed(actorPort, speedPercent);
        this.deviceHandler.getRegulatedMotor(actorPort).forward();
    }

    /**
     * @param actorPort
     * @param speedPercent
     */
    public void turnOnUnregulatedMotor(ActorPort actorPort, int speedPercent) {
        setUnregulatedMotorSpeed(actorPort, speedPercent);
        this.deviceHandler.getUnregulatedMotor(actorPort).forward();
    }

    /**
     * @param actorPort
     * @param speedPercent
     */
    public void setRegulatedMotorSpeed(ActorPort actorPort, int speedPercent) {
        this.deviceHandler.getRegulatedMotor(actorPort).setSpeed(toDegPerSec(speedPercent));
    }

    /**
     * @param actorPort
     * @param speedPercent
     */
    public void setUnregulatedMotorSpeed(ActorPort actorPort, int speedPercent) {
        this.deviceHandler.getUnregulatedMotor(actorPort).setPower(speedPercent);
    }

    /**
     * @param actorPort
     * @param speedPercent
     * @param rotations
     */
    public void rotateRegulatedMotor(ActorPort actorPort, int speedPercent, MotorMoveMode mode, int rotations) {
        this.deviceHandler.getRegulatedMotor(actorPort).setSpeed(toDegPerSec(speedPercent));
        switch ( mode ) {
            case DEGREE:
                this.deviceHandler.getRegulatedMotor(actorPort).rotate(rotations);
                break;
            case ROTATIONS:
                this.deviceHandler.getRegulatedMotor(actorPort).rotate(rotationsToAngle(rotations));
                break;
            default:
                throw new DbcException("incorrect MotorMoveMode");
        }
    }

    /**
     * @param actorPort
     * @param speedPercent
     * @param mode
     * @param value
     */
    public void rotateUnregulatedMotor(ActorPort actorPort, int speedPercent, MotorMoveMode mode, int value) {
        this.deviceHandler.getUnregulatedMotor(actorPort).setPower(speedPercent);
        if ( value >= 0 ) {
            this.deviceHandler.getUnregulatedMotor(actorPort).forward();
            switch ( mode ) {
                case DEGREE:
                    while ( this.deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() < value ) {
                        // do nothing
                    }
                    break;
                case ROTATIONS:
                    while ( this.deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() < rotationsToAngle(value) ) {
                        // do nothing
                    }
                    break;
                default:
                    throw new DbcException("incorrect MotorMoveMode");
            }

        } else {
            // rotations < 0 -> backward
            this.deviceHandler.getUnregulatedMotor(actorPort).backward();
            switch ( mode ) {
                case DEGREE:
                    while ( this.deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() > value ) {
                        // do nothing
                    }
                    break;
                case ROTATIONS:
                    while ( this.deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() > rotationsToAngle(value) ) {
                        // do nothing
                    }
                    break;
                default:
                    throw new DbcException("incorrect MotorMoveMode");
            }
        }
        this.deviceHandler.getUnregulatedMotor(actorPort).stop();
    }

    public int getRegulatedMotorSpeed(ActorPort actorPort) {
        return toPercent(this.deviceHandler.getRegulatedMotor(actorPort).getSpeed());
    }

    public int getUnregulatedMotorSpeed(ActorPort actorPort) {
        return this.deviceHandler.getUnregulatedMotor(actorPort).getPower();
    }

    public void stopRegulatedMotor(ActorPort actorPort, MotorStopMode floating) {
        switch ( floating ) {
            case FLOAT:
                this.deviceHandler.getRegulatedMotor(actorPort).flt();
                this.deviceHandler.getRegulatedMotor(actorPort).stop();
                break;
            case NONFLOAT:
                this.deviceHandler.getRegulatedMotor(actorPort).stop();
                break;
            default:
                throw new DbcException("Wrong MotorStopMode");
        }
    }

    public void stopUnregulatedMotor(ActorPort actorPort, MotorStopMode floating) {
        switch ( floating ) {
            case FLOAT:
                this.deviceHandler.getUnregulatedMotor(actorPort).flt();
                this.deviceHandler.getUnregulatedMotor(actorPort).stop();
            case NONFLOAT:
                this.deviceHandler.getUnregulatedMotor(actorPort).stop();
            default:
                throw new DbcException("Wrong MotorStopMode");
        }
    }

    // --- END Aktion Bewegung ---
    // --- Aktion Fahren ---

    public void regulatedDrive(ActorPort left, ActorPort right, boolean isReverse, DriveDirection direction, int speedPercent) {
        DifferentialPilot dPilot =
            new DifferentialPilot(
                this.wheelDiameter,
                this.trackWidth,
                this.deviceHandler.getRegulatedMotor(left),
                this.deviceHandler.getRegulatedMotor(right),
                isReverse);
        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
        switch ( direction ) {
            case FOREWARD:
                dPilot.forward();
                break;
            case BACKWARD:
                dPilot.backward();
                break;
            default:
                throw new DbcException("wrong DriveAction.Direction");
        }
    }

    // not needed at the moment
    //    public void unregulatedDrive(ActorPort left, ActorPort right, DriveDirection direction, int speedPercent) {
    //        switch ( direction ) {
    //            case FOREWARD:
    //                this.deviceHandler.getUnregulatedMotor(left).forward();
    //                this.deviceHandler.getUnregulatedMotor(right).forward();
    //                break;
    //            case BACKWARD:
    //                this.deviceHandler.getUnregulatedMotor(left).backward();
    //                this.deviceHandler.getUnregulatedMotor(right).backward();
    //                break;
    //            default:
    //                throw new DbcException("wrong DriveAction.Direction");
    //        }
    //    }

    public void driveDistance(ActorPort left, ActorPort right, boolean isReverse, DriveDirection direction, int speedPercent, int distance) {
        DifferentialPilot dPilot =
            new DifferentialPilot(
                this.wheelDiameter,
                this.trackWidth,
                this.deviceHandler.getRegulatedMotor(left),
                this.deviceHandler.getRegulatedMotor(right),
                isReverse);
        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
        switch ( direction ) {
            case FOREWARD:
                dPilot.travel(distance);
                break;
            case BACKWARD:
                dPilot.travel(-distance);
                break;
            default:
                throw new DbcException("incorrect DriveAction");
        }
    }

    public void stopRegulatedDrive(ActorPort left, ActorPort right) {
        this.deviceHandler.getRegulatedMotor(left).stop();
        this.deviceHandler.getRegulatedMotor(right).stop();
    }

    // not needed at the moment
    //    public void stopUnregulatedDrive(ActorPort left, ActorPort right) {
    //        this.deviceHandler.getUnregulatedMotor(left).stop();
    //        this.deviceHandler.getUnregulatedMotor(right).stop();
    //    }

    /**
     * @param left
     * @param right
     * @param direction
     * @param speedPercent
     */
    public void rotateDirectionRegulated(ActorPort left, ActorPort right, boolean isReverse, TurnDirection direction, int speedPercent) {
        DifferentialPilot dPilot =
            new DifferentialPilot(
                this.wheelDiameter,
                this.trackWidth,
                this.deviceHandler.getRegulatedMotor(left),
                this.deviceHandler.getRegulatedMotor(right),
                isReverse);
        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
        switch ( direction ) {
            case RIGHT:
                dPilot.rotateRight();
                break;
            case LEFT:
                dPilot.rotateLeft();
                break;
            default:
                throw new DbcException("incorrect TurnAction");
        }
    }

    /**
     * @param left
     * @param right
     * @param direction
     * @param speedPercent
     */
    public void rotateDirectionUnregulated(ActorPort left, ActorPort right, boolean isReverse, TurnDirection direction, int speedPercent) {
        DifferentialPilot dPilot =
            new DifferentialPilot(
                this.wheelDiameter,
                this.trackWidth,
                this.deviceHandler.getRegulatedMotor(left),
                this.deviceHandler.getRegulatedMotor(right),
                isReverse);
        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
        switch ( direction ) {
            case RIGHT:
                dPilot.rotateRight();
                break;
            case LEFT:
                dPilot.rotateLeft();
                break;
            default:
                throw new DbcException("incorrect TurnAction");
        }
    }

    // not needed at the moment
    //    public void rotateDirectionDistanceRegulated(ActorPort left, ActorPort right, TurnDirection direction, int speedPercent, int distance) {
    //        int angle = distance * 0;
    //        switch ( direction ) {
    //            case RIGHT:
    //                this.deviceHandler.getRegulatedMotor(left).rotate(angle);
    //                this.deviceHandler.getRegulatedMotor(right).rotate(-angle);
    //                break;
    //            case LEFT:
    //                this.deviceHandler.getRegulatedMotor(left).rotate(-angle);
    //                this.deviceHandler.getRegulatedMotor(right).rotate(angle);
    //                break;
    //            default:
    //                throw new DbcException("wrong TurnAction.Direction");
    //        }
    //    }

    /**
     * @param left
     * @param right
     * @param direction
     * @param speedPercent
     * @param angle
     */
    public void rotateDirectionAngle(ActorPort left, ActorPort right, boolean isReverse, TurnDirection direction, int speedPercent, int angle) {
        DifferentialPilot dPilot =
            new DifferentialPilot(
                this.wheelDiameter,
                this.trackWidth,
                this.deviceHandler.getRegulatedMotor(left),
                this.deviceHandler.getRegulatedMotor(right),
                isReverse);
        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
        switch ( direction ) {
            case RIGHT:
                dPilot.rotate(angle, false);
                break;
            case LEFT:
                angle = angle * -1;
                dPilot.rotate(angle, false);
                break;
            default:
                throw new DbcException("incorrect TurnAction");
        }
    }

    // --- END Aktion Fahren ---
    // --- Aktion Anzeige ---

    /**
     * @param text
     * @param x
     * @param y
     */
    public void drawText(String text, int x, int y) {
        this.lcd.drawString(text, x, y);
    }

    /**
     * TODO map smiley selection to image<br>
     * TODO create images
     * 
     * @param smiley
     * @param x
     * @param y
     */
    public void drawPicture(String smiley, int x, int y) {
        Image image = null;
        switch ( smiley ) {
            case "smiley1":
                // name to image
                break;
            case "smiley2":
                // name to image
                break;
        }
        image = image == null ? null /* default image */: image;
        this.glcd.drawImage(image, x, y, 0);
    }

    public void clearDisplay() {
        this.lcd.clear();
    }

    // --- END Aktion Anzeige ---
    // -- Aktion Klang ---

    /**
     * @param frequency
     * @param duration
     */
    public void playTone(int frequency, int duration) {
        this.audio.playTone(frequency, duration);
    }

    /**
     * play one out of 4 soundfiles<br>
     * TODO create sound files + directory reference
     * 
     * @param fileNumber
     */
    public void playFile(String file) {
        this.audio.playSample(new File("/home/roberta/soundfiles/" + file + ".wav"));
    }

    /**
     * set the volume of the speaker
     * 
     * @param volume
     */
    public void setVolume(int volume) {
        this.audio.setVolume(volume);
    }

    /**
     * @return volume of the speaker
     */
    public int getVolume() {
        return this.audio.getVolume();
    }

    // -- END Aktion Klang ---
    // --- Aktion Statusleuchte ---

    /**
     * TODO enum for blink 3colors x3 modes
     * 
     * @param color
     * @param blink
     */
    public void ledOn(BrickLedColor color, boolean blink) {
        switch ( color ) {
            case GREEN:
                if ( blink ) {
                    this.led.setPattern(4);
                } else {
                    this.led.setPattern(1);
                }
                break;
            case RED:
                if ( blink ) {
                    this.led.setPattern(5);
                } else {
                    this.led.setPattern(2);
                }
                break;
            case ORANGE:
                if ( blink ) {
                    this.led.setPattern(6);
                } else {
                    this.led.setPattern(3);
                }
                break;
        }
    }

    /**
     * turn off led
     */
    public void ledOff() {
        this.led.setPattern(0);
    }

    /**
     * needed as soon as we decide to have a led pattern while running a roberta program<br>
     * change to this pattern then
     */
    public void resetLED() {
        this.led.setPattern(0);
    }

    // --- END Aktion Statusleuchte ---
    // --- Sensoren Berührungssensor ---

    /**
     * @param sensorPort
     * @return
     */
    public boolean isPressed(SensorPort sensorPort) {
        float[] sample = new float[this.deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        this.deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);
        if ( sample[0] == 1.0 ) {
            return true;
        } else {
            return false;
        }
    }

    // --- END Sensoren Berührungssensor ---
    // --- Sensoren Ultraschallsensor ---

    /**
     * @param sensorPort
     * @param sensorMode
     */
    public void setUltrasonicSensorMode(SensorPort sensorPort, UltrasonicSensorMode sensorMode) {
        this.deviceHandler.setUltrasonicSensorMode(sensorPort, sensorMode);
    }

    /**
     * @param sensorPort
     * @return
     */
    public UltrasonicSensorMode getUltraSonicSensorModeName(SensorPort sensorPort) {
        return this.deviceHandler.getUltrasonicSensorModeName(sensorPort);
    }

    /**
     * Returns value of Ultrasonic sensor<br>
     * if mode is set to "distance: returns value in cm<br>
     * if mode is set to "listen": returns ?? :-D<br>
     * 
     * @param sensorPort
     * @return
     */
    public int getUltraSonicSensorValue(SensorPort sensorPort) {
        float[] sample = new float[this.deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        this.deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);
        switch ( this.deviceHandler.getUltrasonicSensorModeName(sensorPort) ) {
            case PRESENCE:
                return Math.round(sample[0]); // whatever :-D
            case DISTANCE:
                return Math.round(sample[0]) * 100; // ^= distance in cm
            default:
                throw new DbcException("sensor type or sensor mode missmatch");
        }
    }

    // END Sensoren Ultraschallsensor ---
    // --- Sensoren Farbsensor ---

    /**
     * @param sensorPort
     * @param sensorMode
     */
    public void setColorSensorMode(SensorPort sensorPort, ColorSensorMode sensorMode) {
        this.deviceHandler.setColorSensorMode(sensorPort, sensorMode);
    }

    /**
     * @param sensorPort
     * @return
     */
    public ColorSensorMode getColorSensorModeName(SensorPort sensorPort) {
        return this.deviceHandler.getColorSensorModeName(sensorPort);
    }

    /**
     * TODO interpretation/conversion before return (rgb!)
     * 
     * @param sensorPort
     * @return
     */
    public int getColorSensorValue(SensorPort sensorPort) {
        float[] sample = new float[this.deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        this.deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);
        switch ( this.deviceHandler.getColorSensorModeName(sensorPort) ) {
            case AMBIENTLIGHT:
                return Math.round(sample[0]);
            case COLOUR:
                return Math.round(sample[0]/* 3 values */);
            case LIGHT:
                return Math.round(sample[0]);
            default:
                throw new DbcException("sensor type or sensor mode missmatch");
        }
    }

    // END Sensoren Farbsensor ---
    // --- Sensoren IRSensor ---

    /**
     * @param sensorPort
     * @param sensorMode
     */
    public void setInfraredSensorMode(SensorPort sensorPort, InfraredSensorMode sensorMode) {
        this.deviceHandler.setInfraredMode(sensorPort, sensorMode);
    }

    /**
     * @param sensorPort
     * @return
     */
    public InfraredSensorMode getInfraredSensorModeName(SensorPort sensorPort) {
        return this.deviceHandler.getInfraredSensorModeName(sensorPort);
    }

    /**
     * TODO interpretation/ conversion before return
     * 
     * @param sensorPort
     * @return
     */
    public int getInfraredSensorValue(SensorPort sensorPort) {
        float[] sample = new float[this.deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        this.deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);
        switch ( this.deviceHandler.getInfraredSensorModeName(sensorPort) ) {
            case DISTANCE:
                return Math.round(sample[0]);
            case SEEK:
                return Math.round(sample[0]);
            default:
                throw new DbcException("sensor type or sensor mode missmatch");
        }
    }

    // END Sensoren IRSensor ---
    // --- Aktorsensor Drehsensor ---

    //    /**
    //     * TODO motor tacho does not work the same way as sensors and sample provider<br>
    //     * block needs to be changed, no mode can be stored in motors
    //     * 
    //     * @param actorPort
    //     */
    //    public void setMotorTachoMode(ActorPort actorPort, Enum mode) {
    //        //
    //    }

    //    public PH getMotorTachoMode(ActorPort actorPort) {
    //        return PH;
    //    }

    public void resetRegulatedMotorTacho(ActorPort actorPort) {
        this.deviceHandler.getRegulatedMotor(actorPort).resetTachoCount();
    }

    public void resetUnregulatedMotorTacho(ActorPort actorPort) {
        this.deviceHandler.getUnregulatedMotor(actorPort).resetTachoCount();
    }

    public int getRegulatedMotorTachoValue(ActorPort actorPort) {
        return this.deviceHandler.getRegulatedMotor(actorPort).getTachoCount();
    }

    public int getUnregulatedMotorTachoValue(ActorPort actorPort) {
        return this.deviceHandler.getUnregulatedMotor(actorPort).getTachoCount();
    }

    // END Aktorsensor Drehsensor ---
    // --- Sensoren Steintasten ---

    /**
     * test if works
     * 
     * @param key
     * @return
     */
    public boolean isPressed(BrickKey key) {
        switch ( key ) {
            case ANY:
                if ( this.keys.readButtons() != 0 ) {
                    return true;
                } else {
                    return false;
                }
            case DOWN:
                return this.brick.getKey("Down").isDown();
            case ENTER:
                return this.brick.getKey("Enter").isDown();
            case ESCAPE:
                return this.brick.getKey("Escape").isDown();
            case LEFT:
                return this.brick.getKey("Left").isDown();
            case RIGHT:
                return this.brick.getKey("Right").isDown();
            case UP:
                return this.brick.getKey("Up").isDown();
            default:
                throw new DbcException("wrong button name??");
        }
    }

    //    public boolean isPressedAndReleased(BrickKey key) {
    //        switch ( key ) {
    //            case ANY:
    //                //
    //            case DOWN:
    //                //
    //            case ENTER:
    //                //
    //            case ESCAPE:
    //                //
    //            case LEFT:
    //                //
    //            case RIGHT:
    //                //
    //            case UP:
    //                //
    //            default:
    //                throw new DbcException("wrong button name??");
    //        }
    //    }

    // END Sensoren Steintasten ---
    // --- Sensor Gyrosensor ---
    /**
     * @param sensorPort
     * @param sensorMode
     */
    public void setGyroSensorMode(SensorPort sensorPort, GyroSensorMode sensorMode) {
        this.deviceHandler.setGyroSensorMode(sensorPort, sensorMode);
    }

    public GyroSensorMode getGyroSensorModeName(SensorPort sensorPort) {
        return this.deviceHandler.getGyroSensorModeName(sensorPort);
    }

    /**
     * TODO interpretation and conversion before return
     * 
     * @param sensorPort
     * @return
     */
    public int getGyroSensorValue(SensorPort sensorPort) {
        float[] sample = new float[this.deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        this.deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);
        switch ( this.deviceHandler.getGyroSensorModeName(sensorPort) ) {
            case ANGLE:
                return Math.round(sample[0]);
            case RATE:
                return Math.round(sample[0]);
            default:
                throw new DbcException("sensor type or sensor mode missmatch");
        }
    }

    /**
     * @param sensorPort
     */
    public void resetGyroSensor(SensorPort sensorPort) {
        this.deviceHandler.getGyroSensor(sensorPort).reset();
    }

    // END Sensoren Gyrosensor ---
    // --- Sensoren Zeitgeber ---
    /**
     * TODO
     * 
     * @param timer
     * @return
     */
    public int getTimerValue(int timerNumber) {
        return this.timers[timerNumber].elapsed();
    }

    /**
     * TODO
     * 
     * @param timer
     */
    public void resetTimer(int timerNumber) {
        this.timers[timerNumber].reset();
    }

    // END Sensoren Zeitgeber ---
}

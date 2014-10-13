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
import de.fhg.iais.roberta.ast.syntax.sensor.MotorTachoMode;
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
        deviceHandler = new DeviceHandler(brickConfiguration);

        wheelDiameter = brickConfiguration.getWheelDiameterCM();
        trackWidth = brickConfiguration.getTrackWidthCM();

        brick = LocalEV3.get();
        // TODO test if needed for Motor/ Sensorport enums from lejos??
        brick.setDefault();
        lcd = brick.getTextLCD();
        glcd = brick.getGraphicsLCD();
        led = brick.getLED();
        keys = brick.getKeys();
        audio = brick.getAudio();
        for ( int i = 0; i < timers.length; i++ ) {
            timers[i] = new Stopwatch();
        }
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
        deviceHandler.getRegulatedMotor(actorPort).forward();
    }

    /**
     * @param actorPort
     * @param speedPercent
     */
    public void turnOnUnregulatedMotor(ActorPort actorPort, int speedPercent) {
        setUnregulatedMotorSpeed(actorPort, speedPercent);
        deviceHandler.getUnregulatedMotor(actorPort).forward();
    }

    /**
     * @param actorPort
     * @param speedPercent
     */
    public void setRegulatedMotorSpeed(ActorPort actorPort, int speedPercent) {
        deviceHandler.getRegulatedMotor(actorPort).setSpeed(toDegPerSec(speedPercent));
    }

    /**
     * @param actorPort
     * @param speedPercent
     */
    public void setUnregulatedMotorSpeed(ActorPort actorPort, int speedPercent) {
        deviceHandler.getUnregulatedMotor(actorPort).setPower(speedPercent);
    }

    /**
     * @param actorPort
     * @param speedPercent
     * @param mode
     * @param rotations
     */
    public void rotateRegulatedMotor(ActorPort actorPort, int speedPercent, MotorMoveMode mode, int rotations) {
        deviceHandler.getRegulatedMotor(actorPort).setSpeed(toDegPerSec(speedPercent));
        switch ( mode ) {
            case DEGREE:
                deviceHandler.getRegulatedMotor(actorPort).rotate(rotations);
                break;
            case ROTATIONS:
                deviceHandler.getRegulatedMotor(actorPort).rotate(rotationsToAngle(rotations));
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
        deviceHandler.getUnregulatedMotor(actorPort).setPower(speedPercent);
        if ( value >= 0 ) {
            deviceHandler.getUnregulatedMotor(actorPort).forward();
            switch ( mode ) {
                case DEGREE:
                    while ( deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() < value ) {
                        // do nothing
                    }
                    break;
                case ROTATIONS:
                    while ( deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() < rotationsToAngle(value) ) {
                        // do nothing
                    }
                    break;
                default:
                    throw new DbcException("incorrect MotorMoveMode");
            }

        } else {
            // rotations < 0 -> backward
            deviceHandler.getUnregulatedMotor(actorPort).backward();
            switch ( mode ) {
                case DEGREE:
                    while ( deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() > value ) {
                        // do nothing
                    }
                    break;
                case ROTATIONS:
                    while ( deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() > rotationsToAngle(value) ) {
                        // do nothing
                    }
                    break;
                default:
                    throw new DbcException("incorrect MotorMoveMode");
            }
        }
        deviceHandler.getUnregulatedMotor(actorPort).stop();
    }

    /**
     * @param actorPort
     * @return
     */
    public int getRegulatedMotorSpeed(ActorPort actorPort) {
        return toPercent(deviceHandler.getRegulatedMotor(actorPort).getSpeed());
    }

    /**
     * @param actorPort
     * @return
     */
    public int getUnregulatedMotorSpeed(ActorPort actorPort) {
        return deviceHandler.getUnregulatedMotor(actorPort).getPower();
    }

    /**
     * @param actorPort
     * @param floating
     */
    public void stopRegulatedMotor(ActorPort actorPort, MotorStopMode floating) {
        switch ( floating ) {
            case FLOAT:
                deviceHandler.getRegulatedMotor(actorPort).flt();
                deviceHandler.getRegulatedMotor(actorPort).stop();
                break;
            case NONFLOAT:
                deviceHandler.getRegulatedMotor(actorPort).stop();
                break;
            default:
                throw new DbcException("Wrong MotorStopMode");
        }
    }

    /**
     * @param actorPort
     * @param floating
     */
    public void stopUnregulatedMotor(ActorPort actorPort, MotorStopMode floating) {
        switch ( floating ) {
            case FLOAT:
                deviceHandler.getUnregulatedMotor(actorPort).flt();
                deviceHandler.getUnregulatedMotor(actorPort).stop();
            case NONFLOAT:
                deviceHandler.getUnregulatedMotor(actorPort).stop();
            default:
                throw new DbcException("Wrong MotorStopMode");
        }
    }

    // --- END Aktion Bewegung ---
    // --- Aktion Fahren ---

    /**
     * @param left
     * @param right
     * @param isReverse
     * @param direction
     * @param speedPercent
     */
    public void regulatedDrive(ActorPort left, ActorPort right, boolean isReverse, DriveDirection direction, int speedPercent) {
        DifferentialPilot dPilot =
            new DifferentialPilot(wheelDiameter, trackWidth, deviceHandler.getRegulatedMotor(left), deviceHandler.getRegulatedMotor(right), isReverse);
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

    /**
     * @param left
     * @param right
     * @param isReverse
     * @param direction
     * @param speedPercent
     * @param distance
     */
    public void driveDistance(ActorPort left, ActorPort right, boolean isReverse, DriveDirection direction, int speedPercent, int distance) {
        DifferentialPilot dPilot =
            new DifferentialPilot(wheelDiameter, trackWidth, deviceHandler.getRegulatedMotor(left), deviceHandler.getRegulatedMotor(right), isReverse);
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

    /**
     * @param left
     * @param right
     */
    public void stopRegulatedDrive(ActorPort left, ActorPort right) {
        deviceHandler.getRegulatedMotor(left).stop();
        deviceHandler.getRegulatedMotor(right).stop();
    }

    // not needed at the moment
    //    public void stopUnregulatedDrive(ActorPort left, ActorPort right) {
    //        this.deviceHandler.getUnregulatedMotor(left).stop();
    //        this.deviceHandler.getUnregulatedMotor(right).stop();
    //    }

    /**
     * @param left
     * @param right
     * @param isReverse
     * @param direction
     * @param speedPercent
     */
    public void rotateDirectionRegulated(ActorPort left, ActorPort right, boolean isReverse, TurnDirection direction, int speedPercent) {
        DifferentialPilot dPilot =
            new DifferentialPilot(wheelDiameter, trackWidth, deviceHandler.getRegulatedMotor(left), deviceHandler.getRegulatedMotor(right), isReverse);
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

    // TODO needed? -> ask beate
    //    /**
    //     * @param left
    //     * @param right
    //     * @param direction
    //     * @param speedPercent
    //     */
    //    public void rotateDirectionUnregulated(ActorPort left, ActorPort right, boolean isReverse, TurnDirection direction, int speedPercent) {
    //        DifferentialPilot dPilot =
    //            new DifferentialPilot(
    //                this.wheelDiameter,
    //                this.trackWidth,
    //                this.deviceHandler.getRegulatedMotor(left),
    //                this.deviceHandler.getRegulatedMotor(right),
    //                isReverse);
    //        dPilot.setRotateSpeed(toDegPerSec(speedPercent));
    //        switch ( direction ) {
    //            case RIGHT:
    //                dPilot.rotateRight();
    //                break;
    //            case LEFT:
    //                dPilot.rotateLeft();
    //                break;
    //            default:
    //                throw new DbcException("incorrect TurnAction");
    //        }
    //    }

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
     * @param isReverse
     * @param direction
     * @param speedPercent
     * @param angle
     */
    public void rotateDirectionAngle(ActorPort left, ActorPort right, boolean isReverse, TurnDirection direction, int speedPercent, int angle) {
        DifferentialPilot dPilot =
            new DifferentialPilot(wheelDiameter, trackWidth, deviceHandler.getRegulatedMotor(left), deviceHandler.getRegulatedMotor(right), isReverse);
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
        lcd.drawString(text, x, y);
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
        glcd.drawImage(image, x, y, 0);
    }

    public void clearDisplay() {
        lcd.clear();
    }

    // --- END Aktion Anzeige ---
    // -- Aktion Klang ---

    /**
     * @param frequency
     * @param duration
     */
    public void playTone(int frequency, int duration) {
        audio.playTone(frequency, duration);
    }

    /**
     * play one out of 4 soundfiles<br>
     * TODO create sound files + directory reference
     *
     * @param fileNumber
     */
    public void playFile(String file) {
        audio.playSample(new File("/home/roberta/soundfiles/" + file + ".wav"));
    }

    /**
     * set the volume of the speaker
     *
     * @param volume
     */
    public void setVolume(int volume) {
        audio.setVolume(volume);
    }

    /**
     * @return volume of the speaker
     */
    public int getVolume() {
        return audio.getVolume();
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
                    led.setPattern(4);
                } else {
                    led.setPattern(1);
                }
                break;
            case RED:
                if ( blink ) {
                    led.setPattern(5);
                } else {
                    led.setPattern(2);
                }
                break;
            case ORANGE:
                if ( blink ) {
                    led.setPattern(6);
                } else {
                    led.setPattern(3);
                }
                break;
        }
    }

    /**
     * turn off led
     */
    public void ledOff() {
        led.setPattern(0);
    }

    /**
     * needed as soon as we decide to have a led pattern while running a roberta program<br>
     * change to this pattern then
     */
    public void resetLED() {
        led.setPattern(0);
    }

    // --- END Aktion Statusleuchte ---
    // --- Sensoren Berührungssensor ---

    /**
     * @param sensorPort
     * @return
     */
    public boolean isPressed(SensorPort sensorPort) {
        float[] sample = new float[deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);
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
        deviceHandler.setUltrasonicSensorMode(sensorPort, sensorMode);
    }

    /**
     * @param sensorPort
     * @return
     */
    public UltrasonicSensorMode getUltraSonicSensorModeName(SensorPort sensorPort) {
        return deviceHandler.getUltrasonicSensorModeName(sensorPort);
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
        float[] sample = new float[deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);

        switch ( deviceHandler.getUltrasonicSensorModeName(sensorPort) ) {
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
        deviceHandler.setColorSensorMode(sensorPort, sensorMode);
    }

    /**
     * @param sensorPort
     * @return
     */
    public ColorSensorMode getColorSensorModeName(SensorPort sensorPort) {
        return deviceHandler.getColorSensorModeName(sensorPort);
    }

    /**
     * TODO interpretation/conversion before return (rgb!)
     *
     * @param sensorPort
     * @return
     */
    public int getColorSensorValue(SensorPort sensorPort) {
        float[] sample = new float[deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);

        switch ( deviceHandler.getColorSensorModeName(sensorPort) ) {
            case AMBIENTLIGHT:
                return Math.round(sample[0] * 100);
            case COLOUR:
                return Math.round(sample[0]/* 3 values */);
            case LIGHT:
                return Math.round(sample[0] * 100);
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
        deviceHandler.setInfraredMode(sensorPort, sensorMode);
    }

    /**
     * @param sensorPort
     * @return
     */
    public InfraredSensorMode getInfraredSensorModeName(SensorPort sensorPort) {
        return deviceHandler.getInfraredSensorModeName(sensorPort);
    }

    /**
     * TODO interpretation/ conversion before return
     *
     * @param sensorPort
     * @return
     */
    public int getInfraredSensorValue(SensorPort sensorPort) {
        float[] sample = new float[deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);

        switch ( deviceHandler.getInfraredSensorModeName(sensorPort) ) {
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

    /**
     * @param actorPort
     * @param tachoMode
     */
    public void setMotorTachoMode(ActorPort actorPort, MotorTachoMode tachoMode) {
        deviceHandler.setTachoSensorMode(actorPort, tachoMode);
    }

    /**
     * @param actorPort
     * @return
     */
    public MotorTachoMode getMotorTachoMode(ActorPort actorPort) {
        return deviceHandler.getTachoSensorModeName(actorPort);
    }

    /**
     * @param actorPort
     */
    public void resetRegulatedMotorTacho(ActorPort actorPort) {
        deviceHandler.getRegulatedMotor(actorPort).resetTachoCount();
    }

    /**
     * @param actorPort
     */
    public void resetUnregulatedMotorTacho(ActorPort actorPort) {
        deviceHandler.getUnregulatedMotor(actorPort).resetTachoCount();
    }

    /**
     * @param actorPort
     * @param tachoMode
     * @return tacho count (degrees) or rotations as double
     */
    public double getRegulatedMotorTachoValue(ActorPort actorPort) {
        MotorTachoMode tachoMode = deviceHandler.getTachoSensorModeName(actorPort);
        switch ( tachoMode ) {
            case DEGREE:
                return deviceHandler.getRegulatedMotor(actorPort).getTachoCount();
            case ROTATION:
                return Math.round(deviceHandler.getRegulatedMotor(actorPort).getTachoCount() / 360.0 * 100.0) / 100.0;
            default:
                throw new DbcException("incorrect MotorTachoMode");
        }
    }

    /**
     * @param actorPort
     * @param tachoMode
     * @return tacho count (degrees) or rotations as double
     */
    public double getUnregulatedMotorTachoValue(ActorPort actorPort) {
        MotorTachoMode tachoMode = deviceHandler.getTachoSensorModeName(actorPort);
        switch ( tachoMode ) {
            case DEGREE:
                return deviceHandler.getUnregulatedMotor(actorPort).getTachoCount();
            case ROTATION:
                return Math.round(deviceHandler.getUnregulatedMotor(actorPort).getTachoCount() / 360 * 100) / 100;
            default:
                throw new DbcException("incorrect MotorTachoMode");
        }
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
                if ( keys.readButtons() != 0 ) {
                    return true;
                } else {
                    return false;
                }
            case DOWN:
                return brick.getKey("Down").isDown();
            case ENTER:
                return brick.getKey("Enter").isDown();
            case ESCAPE:
                return brick.getKey("Escape").isDown();
            case LEFT:
                return brick.getKey("Left").isDown();
            case RIGHT:
                return brick.getKey("Right").isDown();
            case UP:
                return brick.getKey("Up").isDown();
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
        deviceHandler.setGyroSensorMode(sensorPort, sensorMode);
    }

    public GyroSensorMode getGyroSensorModeName(SensorPort sensorPort) {
        return deviceHandler.getGyroSensorModeName(sensorPort);
    }

    /**
     * TODO interpretation and conversion before return
     *
     * @param sensorPort
     * @return
     */
    public int getGyroSensorValue(SensorPort sensorPort) {
        float[] sample = new float[deviceHandler.getSampleProvider(sensorPort).sampleSize()];
        deviceHandler.getSampleProvider(sensorPort).fetchSample(sample, 0);
        switch ( deviceHandler.getGyroSensorModeName(sensorPort) ) {
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
        deviceHandler.getGyroSensor(sensorPort).reset();
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
        return timers[timerNumber - 1].elapsed();
        //return this.timer.elapsed();
    }

    /**
     * TODO
     *
     * @param timer
     */
    public void resetTimer(int timerNumber) {
        timers[timerNumber - 1].reset();
        //this.timer.reset();
    }

    // END Sensoren Zeitgeber ---
}

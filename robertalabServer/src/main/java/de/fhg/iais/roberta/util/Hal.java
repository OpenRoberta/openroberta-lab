package de.fhg.iais.roberta.util;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensoren.SensorPort;

/**
 * Connection class between generated code and leJOS methods
 * TODO replace object by reference to lejos classes for motors/sensors
 * TODO reference to textlcd, graphicslcd, audio, buttons
 * TODO BrickConfiguration empty class
 * TODO actor/sensor bindings initialisation
 * 
 * @author dpyka
 */
public class Hal {

    private final BrickConfiguration brickConfiguration;

    private final Map<ActorPort, Object> lejosActorBindings = new HashMap<>();
    private final Map<SensorPort, Object> lejosSensorBindings = new HashMap<>();

    public Hal(BrickConfiguration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    /**
     * @param percent
     * @return degrees per second
     */
    private int toDegPerSec(int percent) {
        return 720 / 100 * percent;
    }

    /**
     * @param degrees per second
     * @return percent
     */
    private int toPercent(int degPerSec) {
        return degPerSec * 100 / 720;
    }

    /**
     * @param port
     * @return motor object from BrickConfiguration
     */
    private Object getMotorObject(ActorPort port) {
        Object motor = this.lejosActorBindings.get(port);
        if ( motor == null ) {
            // brickConfiguration lesen und objekt erzeugen
            motor = ""; // TODO create motor object
            this.lejosActorBindings.put(port, motor);
        }
        return motor;
    }

    /**
     * @param port
     * @return sensor object from BrickConfiguration
     */
    private Object getSensorObject(SensorPort port) {
        Object sensor = this.lejosSensorBindings.get(port);
        if ( sensor == null ) {
            // brickConfiguration lesen und objekt erzeugen
            sensor = ""; // TODO create sensor object
            this.lejosSensorBindings.put(port, sensor);
        }
        return sensor;
    }

    // --- Aktion Bewegung ---

    /**
     * @param port
     * @param speedPercent
     */
    public void setMotorSpeed(ActorPort port, int speedPercent) {
        getMotorObject(port).setSpeed(toDegPerSec(speedPercent));
    }

    /**
     * @param port
     * @param speedPercent
     * @param rotations
     */
    public void rotateMotor(ActorPort port, int speedPercent, int rotations) {
        Object motor = getMotorObject(port);
        motor.setSpeed(toDegPerSec(speedPercent));
        motor.rotate(rotations);
    }

    /**
     * @param port
     * @return
     */
    public int getSpeed(ActorPort port) {
        return toPercent(getMotorObject(port).getSpeed());
    }

    /**
     * @param port
     * @param floating
     */
    public void stopMotor(ActorPort port, boolean floating) {
        getMotorObject(port).flt(floating); // stop & (floating | (!floating))
    }

    // --- END Aktion Bewegung ---
    // --- Aktion Fahren ---

    /**
     * @param port1
     * @param port2
     * @param direction
     * @param speedPercent
     */
    public void drive(ActorPort port1, ActorPort port2, boolean direction, int speedPercent) {
        Object motor = getMotorObject(port);
        Object motor2 = getMotorObject(port2);
        if ( direction ) {
            motor.forward();
            motor2.forward();
        } else {
            motor.backward();
            motor2.backward();
        }
    }

    /**
     * TODO conversion from rotation to distance (cm)
     * 
     * @param port
     * @param speedPercent
     * @param rotations
     */
    public void driveDistance(ActorPort port1, ActorPort port2, boolean direction, int speedPercent, int distance) {
        Object motor = getMotorObject(port);
        Object motor2 = getMotorObject(port2);
        // convert rotations to distance (cm)
        if ( direction ) {
            // drive distance forward
        } else {
            // drive distance backward
        }
    }

    /**
     * all motors??? two driving motors???
     * 
     * @param port1
     * @param port2
     */
    public void stop(ActorPort port1, ActorPort port2) {
        Object motor = getMotorObject(port);
        Object motor2 = getMotorObject(port2);
        motor.stop();
        motor2.stop();
    }

    /**
     * @param port1
     * @param port2
     * @param direction
     * @param speedPercent
     */
    public void rotateDirection(ActorPort port1, ActorPort port2, boolean direction, int speedPercent) {
        Object motor = getMotorObject(port);
        Object motor2 = getMotorObject(port2);
        motor.setSpeed(speedPercent);
        motor2.setSpeed(speedPercent);
        if ( direction ) { // rechts drehen
            motor.backward(); // "rechts"
            motor2.forward(); // "links"
        } else { // links drehen
            motor.forward(); // "rechts"
            motor2.backward(); // "links"
        }
    }

    /**
     * TODO rotate distance (cm) instead of degrees???
     * 
     * @param port1
     * @param port2
     * @param direction
     * @param speedPercent
     * @param distance
     */
    public void rotateDirectionDistance(ActorPort port1, ActorPort port2, boolean direction, int speedPercent, int distance) {
        Object motor = getMotorObject(port);
        Object motor2 = getMotorObject(port2);
        if ( direction ) { // rechts drehen
            motor.rotate(/* -formula */); // "rechts"
            motor2.rotate(/* +formula */); // "links"
        } else { // links drehen
            motor.rotate(/* +formula */); // "rechts"
            motor2.rotate(/* -formula */); // "links"
        }
    }

    // --- END Aktion Fahren ---
    // --- Aktion Anzeige ---

    /**
     * TODO create textlcd in brickConfiguration??
     * 
     * @param text
     * @param x
     * @param y
     */
    public void drawText(String text, int x, int y) {
        TextLCD lcd = LocalEV3.get().getTextLCD();
        lcd.drawString(text, x, y);
    }

    /**
     * TODO create graphicslcd in brickConfiguration??
     * 
     * @param smiley
     * @param x
     * @param y
     */
    public void drawPicture(int smiley, int x, int y) {
        GraphicsLCD glcd = LocalEV3.get().getGraphicsLCD();
        switch ( smiley ) {
            case 1:
                // number to image
                break;
            case 2:
                // number to image
                break;
            case 3:
                // number to image
                break;
            case 4:
                // number to image
                break;
        }
        //glcd.drawImage(image, x, y, 0);
    }

    /**
     * TODO create textlcd in brickConfiguration??
     */
    public void clearDisplay() {
        TextLCD lcd = LocalEV3.get().getTextLCD();
        lcd.clear();
    }

    // --- END Aktion Anzeige ---
    // -- Aktion Klang ---

    /**
     * @param frequency
     * @param duration
     */
    public void playTone(int frequency, int duration) {
        Audio audio = LocalEV3.get().getAudio();
        audio.playTone(frequency, duration);
    }

    /**
     * play one out of 4 soundfiles
     * TODO create sound files
     * 
     * @param fileNumber
     */
    public void playFile(int fileNumber) {
        Audio audio = LocalEV3.get().getAudio();
        // number to file
        audio.playSample(file);
    }

    /**
     * set the volume of the speaker
     * 
     * @param volume
     */
    public void setVolume(int volume) {
        Audio audio = LocalEV3.get().getAudio();
        audio.setVolume(volume);
    }

    /**
     * @return volume of the speaker
     */
    public int getVolume() {
        Audio audio = LocalEV3.get().getAudio();
        return audio.getVolume();
    }

    // -- END Aktion Klang ---
    // --- Aktion Statusleuchte ---

    /**
     * TODO correct pattern numbers
     * 
     * @param color
     * @param blink
     */
    public void ledOn(String color, boolean blink) {
        LED led = LocalEV3.get().getLED();
        // since java 7
        switch ( color ) {
            case "gruen":
                if ( blink ) {
                    led.setPattern(0);
                } else {
                    led.setPattern(0);
                }
                break;
            case "orange":
                if ( blink ) {
                    led.setPattern(0);
                } else {
                    led.setPattern(0);
                }
                break;
            case "rot":
                if ( blink ) {
                    led.setPattern(0);
                } else {
                    led.setPattern(0);
                }
                break;
        }
    }

    /**
     * turn off led
     */
    public void ledOff() {
        LED led = LocalEV3.get().getLED();
        led.setPattern(0);
    }

    /**
     * TODO what should this block do???
     */
    public resetLED() {
        LED led = LocalEV3.get().getLED();
        led.setPattern(0);
    }

    // --- END Aktion Statusleuchte ---
    // --- Sensoren Berührungssensor ---

    /**
     * check if touchsensor is pressed
     * TODO implicite sampleSize = 0
     * 
     * @param port
     * @return
     */
    public boolean isPressed(SensorPort port) {
        float[] sample = {
            0
        };
        Object sensor = getSensorObject(port).fetchSample(sample, 0);
        if ( sample[0] == 1.0 ) {
            return true;
        } else {
            return false;
        }
    }

    // --- END Sensoren Berührungssensor ---
    // --- Sensoren Ultraschallsensor ---

    /**
     * set ultrasonic mode distance/presence
     * 
     * @param port
     * @param mode
     */
    public void setUltraSonicMode(SensorPort port, String mode) {
        if ( mode.equals("Abstand") ) {
            getSensorObject(port).getMode("Distance");
        } else if ( mode.equals("Anwesenheit") ) {
            getSensorObject(port).getMode("Presence"); // mode name?
        }
    }

    /**
     * @param port
     * @return
     */
    public String getUltraSonicModeName(SensorPort port) {
        return getSensorObject(port).getName();
    }

    /**
     * TODO check sample size (=1 or more?)
     * 
     * @param port
     * @return
     */
    public int getUltraSonicValue(SensorPort port) {
        float[] sample = {
            0
        };
        getSensorObject(port).fetchSample(sample, 0);
        return Math.round(sample[0]);
    }

    // END Sensoren Ultraschallsensor ---
}

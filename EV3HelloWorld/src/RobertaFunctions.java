import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import lejos.hardware.Button;
import lejos.hardware.DeviceException;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;

/**
 * Class that contains methods that will be executed at the beginning and at the end of each "Roberta program".<br>
 * Contains properties of which device is connected to which port. This need to be set somewhere on server side.
 * 
 * @author dpyka
 */
public class RobertaFunctions {

    private final TextLCD lcd = LocalEV3.get().getTextLCD();
    private final GraphicsLCD glcd = LocalEV3.get().getGraphicsLCD();

    private RegulatedMotor rightMotor;
    private RegulatedMotor leftMotor;
    private RegulatedMotor mediumMotor;

    private SensorModes ultraSonic;
    private SensorModes colorSensor;
    private SensorModes gyroSensor;
    private SensorModes rightTouchSensor;
    private SensorModes leftTouchSensor;

    private final Properties devices;

    public RobertaFunctions(Properties devices) {
        this.devices = devices;
    }

    /**
     * Clears the display.<br>
     * TBC.
     */
    public void startup() {
        LCD.clear();
    }

    /**
     * Closes motor devices if available.<br>
     * Turn off button LEDs.<br>
     * Clear and refresh display.<br>
     */
    public void shutdown() {
        if ( this.devices.containsKey("rightMotor") ) {
            this.rightMotor.stop();
            this.rightMotor.close();
        }
        if ( this.devices.containsKey("leftMotor") ) {
            this.leftMotor.stop();
            this.leftMotor.close();
        }
        if ( this.devices.containsKey("mediumMotor") ) {
            this.mediumMotor.stop();
            this.mediumMotor.close();
        }
        Button.LEDPattern(0);
        LCD.clear();
        LCD.refresh();
    }

    /**
     * Test if devices are present and connected correctly.<br>
     * Send error report to server (empty string if no error).
     * Incorrect motor connection can not be detected...
     * TODO exception handling malformed url (should never be thrown later)
     */
    public void validateDevices() {
        URL errorMessageServletURL = null;

        String errorMessage = initialiseDevices();
        try {
            errorMessageServletURL = new URL("http://10.0.1.18:1999/error");
            sendErrorMessageToServer(openConnection(errorMessageServletURL), errorMessage);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisation of motors and sensors as available and described in Properties "devices".<br>
     * Display errors on brick screen.<br>
     * 
     * @return error report
     */
    private String initialiseDevices() {
        String errorMessage = "";
        int i = 0;
        if ( this.devices.containsKey("rightMotor") ) {
            this.rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort((String) this.devices.get("rightMotor")));
        }

        if ( this.devices.containsKey("leftMotor") ) {
            this.leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort((String) this.devices.get("leftMotor")));
        }

        if ( this.devices.containsKey("mediumMotor") ) {
            this.mediumMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort((String) this.devices.get("mediumMotor")));
        }

        if ( this.devices.containsKey("ultraSonicSensor") ) {
            try {
                this.ultraSonic = new EV3UltrasonicSensor(LocalEV3.get().getPort((String) this.devices.get("ultraSonicSensor")));
            } catch ( DeviceException e ) {
                errorMessage = errorMessage + "No ultrasonic sensor connected on port " + this.devices.getProperty("ultraSonicSensor") + "!\n";
                this.lcd.drawString("No ultra on " + this.devices.getProperty("ultraSonicSensor"), 0, i);
                i++;
            }
        }

        if ( this.devices.containsKey("rightTouchSensor") ) {
            try {
                int sensorType = LocalEV3.get().getPort((String) this.devices.get("rightTouchSensor")).getSensorType();
                // x = 126 if no sensor connected, 125 if any sensor connected
                if ( sensorType == 126 ) {
                    throw new DeviceException();
                }
                this.rightTouchSensor = new EV3TouchSensor(LocalEV3.get().getPort((String) this.devices.get("rightTouchSensor")));
            } catch ( DeviceException e ) {
                errorMessage = errorMessage + "No touch sensor connected on port " + this.devices.getProperty("rightTouchSensor") + "!\n";
                this.lcd.drawString("No touch on " + this.devices.getProperty("rightTouchSensor"), 0, i);
                i++;
            }
        }

        if ( this.devices.containsKey("leftTouchSensor") ) {
            try {
                int sensorType = LocalEV3.get().getPort((String) this.devices.get("leftTouchSensor")).getSensorType();
                if ( sensorType == 126 ) {
                    throw new DeviceException();
                }
                this.leftTouchSensor = new EV3TouchSensor(LocalEV3.get().getPort((String) this.devices.get("leftTouchSensor")));
            } catch ( DeviceException e ) {
                errorMessage = errorMessage + "No touch sensor connected on port " + this.devices.getProperty("leftTouchSensor") + "!\n";
                this.lcd.drawString("No touch on " + this.devices.getProperty("leftTouchSensor"), 0, i);
                i++;
            }
        }

        if ( this.devices.containsKey("gyroSensor") ) {
            try {
                this.gyroSensor = new EV3GyroSensor(LocalEV3.get().getPort((String) this.devices.get("gyroSensor")));
            } catch ( DeviceException e ) {
                errorMessage = errorMessage + "No gyro sensor connected on port " + this.devices.getProperty("gyroSensor") + "!\n";
                this.lcd.drawString("No gyro on " + this.devices.getProperty("gyroSensor"), 0, i);
                i++;
            }
        }

        if ( this.devices.containsKey("colorSensor") ) {
            try {
                this.colorSensor = new EV3ColorSensor(LocalEV3.get().getPort((String) this.devices.get("colorSensor")));
            } catch ( DeviceException e ) {
                errorMessage = errorMessage + "No color sensor connected on port " + this.devices.getProperty("colorSensor") + "!\n";
                this.lcd.drawString("No color on " + this.devices.getProperty("colorSensor"), 0, i);
                i++;
            }
        }
        // display lines of error Message for 2 sec on brick screen
        //Delay.msDelay(2000);
        return errorMessage;
    }

    /**
     * Opens http connection to server. "POST" as request method. Input, output set to "true".
     * 
     * @param url the robertalab server url or ip+port
     * @return httpURLConnection http connection object to the server
     * @throws IOException opening a connection failed
     */
    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        return httpURLConnection;
    }

    /**
     * Send the error report (String) to the server
     * 
     * @param httpURLConnection http connection to the server
     * @param errorMessage error report
     * @throws IOException output or input fails
     */
    private void sendErrorMessageToServer(HttpURLConnection httpURLConnection, String errorMessage) throws IOException {
        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(errorMessage);
        dos.flush();
        dos.close();
        // needed at "server side"?!
        InputStream is = httpURLConnection.getInputStream();
        is.close();
    }

}

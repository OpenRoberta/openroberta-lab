package sensortest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;

/**
 * Class that contains methods that will be executed at the beginning and at the
 * end of each "Roberta program".<br>
 * Contains properties of which device is connected to which port. This need to
 * be set somewhere on server side.
 * 
 * @author dpyka
 */
public class RobertaFunctions {

    //    private final TextLCD lcd = LocalEV3.get().getTextLCD();
    //    private final GraphicsLCD glcd = LocalEV3.get().getGraphicsLCD();

    //TODO need better solution: multi key -> one value data structure
    final Properties sensorModes = new Properties();

    public RobertaFunctions() {
        this.sensorModes.put("TOUCH", "EV3TouchSensor");
        this.sensorModes.put("US-DIST-CM", "EV3UltraSonicSensor");
        this.sensorModes.put("COL-REFLECT", "EV3ColorSensor");
        this.sensorModes.put("GYRO-ANG", "EV3GyroSensor");
        this.sensorModes.put("NO", "");
        this.sensorModes.put("I2C", "Some I2C Sensor");
    }

    /**
     * Clears the display.<br>
     * TBC.
     */
    public void startup() {
        LCD.clear();
    }

    /*public void test(Properties motorConfig) {
        for ( String key : motorConfig.stringPropertyNames() ) {
            String motorType = motorConfig.getProperty(key);
            if ( !motorType.equals("") ) {
                try {
                    RegulatedMotor motor = (RegulatedMotor) Class.forName(motorType).newInstance();
                } catch ( InstantiationException e ) {
                    e.printStackTrace();
                } catch ( IllegalAccessException e ) {
                    e.printStackTrace();
                } catch ( ClassNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    /**
     * Closes motor devices if available.<br>
     * Turn off button LEDs.<br>
     * Clear and refresh display.<br>
     */
    public void shutdown() {
        Button.LEDPattern(0);
    }

    /**
     * Test if devices are present and connected correctly.<br>
     * Send error report to server (empty string if no error). Incorrect motor
     * connection can not be detected... TODO exception handling malformed url
     * (should never be thrown later)
     */
    public void checkSensorConfig(Properties expectedSensorConfig) {
        URL errorMessageServletURL = null;
        String errorMessage = compareSensorConfig(getActualSensorConfig(), expectedSensorConfig);
        System.out.println(errorMessage);
        try {
            errorMessageServletURL = new URL("http://10.0.1.10:1999/error");
            sendErrorMessage(openConnection(errorMessageServletURL), errorMessage);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise sensor ports and retrieve their modename.<br>
     * 
     * @return sensorname per port in an array
     */
    private String[] getActualSensorConfig() {
        int sensorCount = 4;
        String[] actualSensorConfig = new String[sensorCount];
        Port[] p = new Port[sensorCount];
        p[0] = LocalEV3.get().getPort("S1");
        p[1] = LocalEV3.get().getPort("S2");
        p[2] = LocalEV3.get().getPort("S3");
        p[3] = LocalEV3.get().getPort("S4");
        for ( int i = 0; i < actualSensorConfig.length; i++ ) {
            actualSensorConfig[i] = getSensorName(p[i]).trim(); // remove spaces
            // map the sensor mode names to lejos sensor class names!
            actualSensorConfig[i] = this.sensorModes.getProperty(actualSensorConfig[i]);
        }
        return actualSensorConfig;
    }

    /**
     * Read the port to receive the sensor modename
     * 
     * @param port Sensorport of the EV3 brick
     * @return String name of the sensor that is connected on this port
     */
    private String getSensorName(Port port) {
        try {
            AnalogPort aP = port.open(AnalogPort.class);
            float v1 = aP.getPin1();
            aP.close();
            if ( v1 > 4.5 ) {
                return "NO";
            } else if ( v1 > 0.3 && v1 < 0.5 ) {
                return "TOUCH";
            } else {
                // TODO: test Pin6 for NXT sensors
            }
        } catch ( Exception e ) {
            // TODO: nothing
        }
        try {
            UARTPort uP = port.open(UARTPort.class);
            uP.resetSensor();
            uP.initialiseSensor(0);
            String name = uP.getModeName(0);
            uP.close();
            return name;
        } catch ( Exception e ) {
            return "I2C";
            // TODO: test I2c port for NXT Sensors
        }
    }

    /**
     * Check if the current sensor configuration matches with the expected configuration of the program from the website
     * 
     * @param sensorName the names of the sensors of the current configuration
     * @param sensorConfig the sensor configuration for which the web application program is made for
     * @return String error message about missmatched sensors
     */
    private String compareSensorConfig(String[] sensorName, Properties expectedSensorConfig) {
        String errorMessage = "";
        // take care of index i
        // for loop 0-3 (array), sensorports S1-S4 in properties
        for ( int i = 0; i < sensorName.length; i++ ) {
            if ( !sensorName[i].equals(expectedSensorConfig.getProperty("S" + (i + 1))) ) {
                errorMessage = errorMessage + "Falscher Sensor an Port S" + (i + 1) + " angeschlossen!\n";
                errorMessage = errorMessage + "Gefunden: " + sensorName[i] + "\n";
                errorMessage = errorMessage + "Erwartet: " + expectedSensorConfig.getProperty("S" + (i + 1)) + "\n\n";
            }
        }
        return errorMessage;
    }

    /**
     * Opens http connection to server. "POST" as request method. Input, output
     * set to "true".
     * 
     * @param url
     *        the robertalab server url or ip+port
     * @return httpURLConnection http connection object to the server
     * @throws IOException
     *         opening a connection failed
     */
    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        return httpURLConnection;
    }

    /**
     * Send the error report to the server
     * 
     * @param httpURLConnection
     *        http connection to the server
     * @param errorMessage
     *        error report
     * @throws IOException
     *         output or input fails
     */
    private void sendErrorMessage(HttpURLConnection httpURLConnection, String errorMessage) throws IOException {
        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(errorMessage);
        dos.flush();
        dos.close();
        // needed at "server side"?!
        InputStream is = httpURLConnection.getInputStream();
        is.close();
    }

}

package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Delay;

import org.json.JSONObject;

/**
 * Class for brick <-> server communication based on json and key words "cmds".
 *
 * @author dpyka
 */
public class ORApushCmd implements Runnable {

    private URL pushServiceURL;
    private HttpURLConnection httpURLConnection;

    private final ORAdownloader oraDownloader;
    private final ORAupdater oraUpdater;
    private final ORAlauncher oraLauncher;

    private final boolean TRUE = true;

    private int reconnectAttempts = 0;
    private final int maxAttempts = 3;

    private final JSONObject brickData = new JSONObject();

    // brick data keywords
    public static final String KEY_BRICKNAME = "brickname";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_MACADDR = "macaddr";
    public static final String KEY_BATTERY = "battery";
    public static final String KEY_FIRMWARENAME = "firmwarename";
    public static final String KEY_FIRMWAREVERSION = "firmwareversion";
    public static final String KEY_MENUVERSION = "menuversion";
    public static final String KEY_CMD = "cmd";

    // brickdata + cmds send to server
    public static final String CMD_REGISTER = "register";
    public static final String CMD_PUSH = "push";

    // cmds receive from server
    public static final String CMD_REPEAT = "repeat";
    public static final String CMD_ABORT = "abort";
    public static final String CMD_UPDATE = "update";
    public static final String CMD_DOWNLOAD = "download";
    public static final String CMD_CONFIGURATION = "configuration";

    /**
     * Creates a new Open Roberta Lab "push" communication object. Additional
     * objects for downloading user programs, updating the brick and launching a
     * program are being created.
     *
     * @param serverBaseIP
     *        The base IP like 192.168.56.1:1999
     * @param ind
     *        title bar of the brick's screen
     */
    public ORApushCmd(String serverBaseIP, String token) {
        // add brick data pairs which will not change during runtime
        this.brickData.put(KEY_TOKEN, token);
        this.brickData.put(KEY_MACADDR, GraphicStartup.getORAmacAddress());
        this.brickData.put(KEY_MENUVERSION, GraphicStartup.getORAmenuVersion());
        this.brickData.put(KEY_FIRMWARENAME, "lejos");
        this.brickData.put(KEY_FIRMWAREVERSION, GraphicStartup.getLejosVersion());

        try {
            this.pushServiceURL = new URL("http://" + serverBaseIP + "/pushcmd");
        } catch ( MalformedURLException e ) {
            // ok
        }
        this.oraDownloader = new ORAdownloader(serverBaseIP);
        this.oraUpdater = new ORAupdater(serverBaseIP);
        this.oraLauncher = new ORAlauncher();
    }

    /**
     * Expose http connection to allow the user to cancel the registration
     * process. Otherwise user has to wait until timeout occurs (5minutes). Http
     * connection will "hang" in another thread trying to read from inputstream.
     *
     * @return The http connection the brick uses to communicate with the server.
     */
    public HttpURLConnection getHttpConnection() {
        return this.httpURLConnection;
    }

    /**
     * Method which processes the brick server communication in a separate thread.
     * The brick reacts on specific commands from the server.
     */
    @Override
    public void run() {
        OutputStream os = null;
        BufferedReader br = null;

        while ( this.TRUE ) {
            try {
                // add or update brick data pairs which can be changed by the user at runtime
                this.brickData.put(KEY_BRICKNAME, GraphicStartup.getBrickName());
                this.brickData.put(KEY_BATTERY, GraphicStartup.getBatteryStatus());

                if ( ORAhandler.isRegistered() ) {
                    this.brickData.put(KEY_CMD, CMD_PUSH);
                    this.httpURLConnection = openConnection(15000);
                } else {
                    this.brickData.put(KEY_CMD, CMD_REGISTER);
                    System.out.println("ORA register: " + this.brickData);
                    this.httpURLConnection = openConnection(330000);
                }

                os = this.httpURLConnection.getOutputStream();
                os.write(this.brickData.toString().getBytes("UTF-8"));

                br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));
                StringBuilder responseStrBuilder = new StringBuilder();
                String responseString;
                while ( (responseString = br.readLine()) != null ) {
                    responseStrBuilder.append(responseString);
                }
                JSONObject responseEntity = new JSONObject(responseStrBuilder.toString());

                String command = responseEntity.getString("cmd");
                System.out.println("ORA cmd from server: " + command);
                switch ( command ) {
                    case CMD_REPEAT:
                        ORAhandler.setRegistered(true);
                        ORAhandler.setConnectionError(false);
                        ORAhandler.setTimeout(false);
                        break;
                    case CMD_ABORT:
                        // if brick is waiting for registration, server sends abort as timeout message
                        if ( ORAhandler.isRegistered() == false ) {
                            ORAhandler.setTimeout(true);
                        } else { // this should never happen
                            ORAhandler.setRegistered(false);
                            LocalEV3.get().getAudio().systemSound(Sounds.DESCENDING);
                        }
                        return;
                    case CMD_UPDATE:
                        this.oraUpdater.update();
                        GraphicStartup.restartMenu();
                        return;
                    case CMD_DOWNLOAD:
                        if ( GraphicStartup.getUserprogram() == null ) {
                            String programName = this.oraDownloader.downloadProgram(this.brickData);
                            this.oraLauncher.runProgram(programName);
                        }
                        break;
                    case CMD_CONFIGURATION:
                        break;
                    default:
                        System.out.println("ORA unknown command from server, do nothing!");
                        break;
                }
                this.reconnectAttempts = 0;
            } catch ( SocketTimeoutException ste ) {
                if ( ORAhandler.isRegistered() == false ) {
                    ORAhandler.setTimeout(true);
                    break;
                } else {
                    this.reconnectAttempts++;
                    System.out.println(this.reconnectAttempts + "(timeout)");
                }
            } catch ( IOException ioe ) {
                if ( ORAhandler.isRegistered() == false ) {
                    ORAhandler.setConnectionError(true);
                    return;
                } else {
                    if ( this.reconnectAttempts >= this.maxAttempts ) {
                        connectionLost();
                        return;
                    } else {
                        this.reconnectAttempts++;
                        Delay.msDelay(100);
                    }
                    System.out.println(this.reconnectAttempts + "(ioex)");
                }
            } finally {
                try {
                    if ( os != null ) {
                        os.close();
                    }
                    if ( br != null ) {
                        br.close();
                    }
                } catch ( IOException e ) {
                    // ok
                }
            }
        }
    }

    private void connectionLost() {
        ORAhandler.setRegistered(false);
        LocalEV3.get().getAudio().systemSound(Sounds.DESCENDING);
        GraphicStartup.menu.suspend();
        TextLCD lcd = LocalEV3.get().getTextLCD();
        lcd.drawString(" Open Roberta Lab", 0, 2);
        lcd.drawString(" connection lost!", 0, 3);
        lcd.drawString(" (press any key)", 0, 5);
        LocalEV3.get().getKeys().waitForAnyPress();
        Delay.msDelay(1000);
        GraphicStartup.menu.resume();
        GraphicStartup.redrawIPs();
    }

    /**
     * Opens an http connection to server. "POST" as request method. Input, output
     * set to "true". 5 minutes readtimeout set. This connection is used for the
     * "push" service. The server will answer the request every few seconds.
     *
     * @return HttpURLConnection http connection object
     * @throws IOException
     *         Connection to server failed
     */
    private HttpURLConnection openConnection(int readTimeOut) throws SocketTimeoutException, IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) this.pushServiceURL.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setReadTimeout(readTimeOut);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        return httpURLConnection;
    }
}

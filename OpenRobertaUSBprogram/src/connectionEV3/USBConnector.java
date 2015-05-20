package connectionEV3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import main.ORArmiControl;

import org.json.JSONObject;

import brick.ORAdownloader;
import brick.ORAtokenGenerator;
import brick.ORAupdater;

public class USBConnector extends Observable implements Runnable, Connector {

    private String brickIp = "10.0.1.1";
    private String serverIp = "localhost";
    private String serverPort = "1999";
    private final String serverBaseIP;

    private URL pushServiceURL;
    private HttpURLConnection httpURLConnection;

    private final ORAdownloader oraDownloader;
    private final ORAupdater oraUpdater;
    private final ORAtokenGenerator tokenGenerator;

    private final ORArmiControl remoteControl;

    private final boolean running = true;

    // brick data keywords
    public static final String KEY_BRICKNAME = "brickname";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_MACADDR = "macaddr";
    public static final String KEY_BATTERY = "battery";
    public static final String KEY_LEJOSVERSION = "lejosversion";
    public static final String KEY_MENUVERSION = "menuversion";
    public static final String KEY_CMD = "cmd";

    //brickdata + cmds send to server
    private static final String CMD_REGISTER = "register";
    private static final String CMD_PUSH = "push";

    // cmds receive from server
    private static final String CMD_REPEAT = "repeat";
    private static final String CMD_ABORT = "abort";
    private static final String CMD_UPDATE = "update";
    private static final String CMD_DOWNLOAD = "download";
    private static final String CMD_CONFIGURATION = "configuration";

    // Temp directory, tested on windows 7, ubuntu 14.04, mac os ?.
    public static File TEMPDIRECTORY = null;
    private static Logger log = Logger.getLogger("Connector");

    public enum State {
        DISCOVER,
        WAIT_FOR_CONNECT,
        CONNECT,
        WAIT_FOR_CMD,
        DISCOVER_CONNECTED,
        DISCOVER_UPDATE,
        DISCONNECT,
        WAIT_FOR_SERVER,
        UPDATE,
        UPDATE_SUCCESS,
        UPDATE_FAIL,
        ERROR_HTTP,
        ERROR_UPDATE,
        ERROR_BRICK,
        DISCOVER_UPDATED,
        TOKEN_TIMEOUT
    }

    private State state = State.DISCOVER; // First state when program starts
    private String brickName = "";
    private String brickBatteryVoltage = "";
    private String token = "";
    private boolean userDisconnect = false;

    public USBConnector(ResourceBundle serverProps) {
        if ( serverProps != null ) {
            this.brickIp = serverProps.getString("brickIp");
            this.serverIp = serverProps.getString("serverIp");
            this.serverPort = serverProps.getString("serverPort");
        }
        this.serverBaseIP = this.serverIp + ":" + this.serverPort;
        try {
            this.pushServiceURL = new URL("http://" + this.serverBaseIP + "/pushcmd");
        } catch ( MalformedURLException e ) {
            // ok
        }
        log.config("Robot ip " + this.brickIp);
        log.config("Server ip " + this.serverIp);
        log.config("Server port " + this.serverPort);
        this.remoteControl = new ORArmiControl(this.brickIp);
        this.oraDownloader = new ORAdownloader(this.serverBaseIP);
        this.oraUpdater = new ORAupdater(this.serverBaseIP);
        this.tokenGenerator = new ORAtokenGenerator();
    }

    /**
     * Set a path where all firmware files and user programs are temporary stored on the PC.
     */
    private void setTempPath() {
        TEMPDIRECTORY = new File(System.getProperty("java.io.tmpdir"), "OpenRoberta");
        if ( !USBConnector.TEMPDIRECTORY.exists() ) {
            USBConnector.TEMPDIRECTORY.mkdirs();
        }
        log.config("System " + System.getProperty("os.name"));
        log.config("TempDir " + TEMPDIRECTORY.getPath());
    }

    @Override
    public void run() {

        OutputStream os = null;
        BufferedReader br = null;
        boolean registered = false;

        setTempPath();

        JSONObject brickData = new JSONObject();
        brickData.put(KEY_MACADDR, "usb");

        while ( this.running ) {
            //log.fine(state.toString());
            switch ( this.state ) {
                case DISCOVER:
                case DISCOVER_CONNECTED:
                case DISCOVER_UPDATED:
                    try {
                        if ( InetAddress.getByName(this.brickIp).isReachable(3000) ) {
                            this.remoteControl.connectToMenu();
                            log.info("Robot connected");
                            brickData.put(KEY_LEJOSVERSION, this.remoteControl.getlejosVersion());
                            brickData.put(KEY_MENUVERSION, this.remoteControl.getORAversion());
                            String brickName = this.remoteControl.getBrickname();
                            brickData.put(KEY_BRICKNAME, brickName);
                            this.brickName = brickName;
                        } else {
                            Thread.sleep(1000);
                            break;
                        }
                        // necessary update to 1.2.0 can only be discovered by exeption
                        // lejos rmi interface is different to ours
                    } catch ( ClassCastException cce ) {
                        log.info("Version 1.1.0");
                        if ( this.state == State.DISCOVER_UPDATED ) {
                            notifyConnectionStateChanged(State.UPDATE_SUCCESS);
                        } else {
                            notifyConnectionStateChanged(State.DISCOVER_UPDATE);
                        }
                        break;
                    } catch ( Exception e ) {
                        log.warning("Robot not ready");
                        notifyConnectionStateChanged(State.ERROR_BRICK);
                        break;
                    }
                    if ( this.state == State.DISCOVER || this.state == State.DISCOVER_UPDATED ) {
                        this.state = State.WAIT_FOR_CONNECT;
                    } else {
                        this.state = State.WAIT_FOR_CMD;
                    }
                    notifyConnectionStateChanged(this.state);
                    break;
                case UPDATE:
                    try {
                        Process p = Runtime.getRuntime().exec("cmd.exe /c update.bat", null, new File("firmware-1.2"));
                        p.waitFor();
                        if ( p.exitValue() == 0 ) {
                            log.info("Upload firmware update successful");
                            notifyConnectionStateChanged(State.UPDATE_SUCCESS);
                            this.state = State.DISCOVER_UPDATED;
                            break;
                        }
                    } catch ( Exception e ) {
                        log.severe("Error upload firmware");
                        notifyConnectionStateChanged(State.UPDATE_FAIL);
                    }
                    break;
                case WAIT_FOR_CONNECT:
                    // brick is performing restart, go back to discover to not overjump this state for next connection
                    // do not make other state changes here
                    try {
                        if ( !InetAddress.getByName(this.brickIp).isReachable(3000) ) {
                            this.state = State.DISCOVER;
                            notifyConnectionStateChanged(State.DISCOVER);
                        }
                    } catch ( IOException ioe ) {
                        log.warning("EV3 is restarting most likely");
                        this.state = State.DISCOVER;
                        notifyConnectionStateChanged(State.DISCOVER);
                    }
                    break;
                case CONNECT:
                    String token = this.tokenGenerator.generateToken();
                    try {
                        if ( this.remoteControl.getORAupdateState() == false ) {
                            this.remoteControl.disconnectFromMenu();
                            this.state = State.DISCOVER;
                            notifyConnectionStateChanged(State.UPDATE_SUCCESS);
                            notifyConnectionStateChanged(State.DISCOVER);
                            break;
                        }
                        brickData.put(KEY_TOKEN, token);
                        String batteryVoltage = this.remoteControl.getBatteryVoltage();
                        brickData.put(KEY_BATTERY, batteryVoltage);
                        brickData.put(KEY_CMD, CMD_REGISTER);
                        this.token = token;
                        this.brickBatteryVoltage = batteryVoltage;
                        this.state = State.WAIT_FOR_SERVER;
                        notifyConnectionStateChanged(State.WAIT_FOR_SERVER);
                        this.httpURLConnection = openConnection(330000);
                        os = this.httpURLConnection.getOutputStream();

                        os.write(brickData.toString().getBytes("UTF-8"));
                        log.info("Register " + brickData);
                        br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));
                        StringBuilder responseStrBuilder = new StringBuilder();
                        String responseString;
                        while ( (responseString = br.readLine()) != null ) {
                            responseStrBuilder.append(responseString);
                        }
                        JSONObject responseEntity = new JSONObject(responseStrBuilder.toString());

                        String command = responseEntity.getString("cmd");
                        log.info("Command " + command);
                        switch ( command ) {
                            case CMD_REPEAT:
                                registered = true;
                                this.remoteControl.setORAregistration(registered);
                                this.state = State.WAIT_FOR_CMD;
                                notifyConnectionStateChanged(State.WAIT_FOR_CMD);
                                break;
                            case CMD_ABORT:
                                this.state = State.DISCOVER;
                                notifyConnectionStateChanged(State.TOKEN_TIMEOUT);
                                notifyConnectionStateChanged(State.DISCOVER);
                                break;
                            default:
                                log.info("Command " + command + " unknown");
                                this.state = State.DISCOVER;
                                notifyConnectionStateChanged(State.DISCOVER);
                                break;
                        }
                        break;
                    } catch ( IOException e1 ) {
                        if ( !this.userDisconnect ) {
                            this.brickName = "";
                            this.brickBatteryVoltage = "";
                            log.severe("Error HTTP");
                            notifyConnectionStateChanged(State.ERROR_HTTP);
                            notifyConnectionStateChanged(State.DISCOVER);
                        }
                        this.state = State.DISCOVER;
                        this.userDisconnect = false;
                        break;
                    }
                case WAIT_FOR_CMD:
                    try {
                        brickData.put(KEY_BATTERY, this.remoteControl.getBatteryVoltage());
                        brickData.put(KEY_BRICKNAME, this.remoteControl.getBrickname());
                        brickData.put(KEY_CMD, CMD_PUSH);

                        this.httpURLConnection = openConnection(15000);
                        os = this.httpURLConnection.getOutputStream();
                        os.write(brickData.toString().getBytes("UTF-8"));

                        br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));
                        StringBuilder responseStrBuilder = new StringBuilder();
                        String responseString;
                        while ( (responseString = br.readLine()) != null ) {
                            responseStrBuilder.append(responseString);
                        }
                        JSONObject responseEntity = new JSONObject(responseStrBuilder.toString());

                        String command = responseEntity.getString("cmd");
                        log.info("Command " + command);
                        switch ( command ) {
                            case CMD_REPEAT:
                                break;
                            case CMD_ABORT:
                                registered = false;
                                this.remoteControl.setORAregistration(false);
                                this.remoteControl.disconnectFromMenu();
                                this.state = State.DISCOVER;
                                this.brickName = "";
                                this.brickBatteryVoltage = "";
                                notifyConnectionStateChanged(State.DISCOVER);
                                return;
                            case CMD_UPDATE:
                                log.info("Download firmware update");
                                // this should never fail (return false), unless we screw up the directory
                                // in which the files are stored, see setTempPath()
                                if ( this.oraUpdater.downloadToPC() == true ) {
                                    log.info("Upload firmware update");
                                    if ( this.remoteControl.uploadFirmwareFiles() == true ) {
                                        this.remoteControl.setORAupdateState();
                                        Thread.sleep(1000);
                                        disconnect();
                                        notifyConnectionStateChanged(State.DISCOVER);
                                        this.state = State.DISCOVER;
                                        break;
                                    } else {
                                        notifyConnectionStateChanged(State.ERROR_UPDATE);
                                        notifyConnectionStateChanged(State.WAIT_FOR_CMD);
                                        break;
                                    }
                                }
                                break;
                            case CMD_DOWNLOAD:
                                String programName = this.oraDownloader.downloadProgram(brickData);
                                log.info("Download " + programName);
                                log.info("File " + new File(USBConnector.TEMPDIRECTORY.getAbsolutePath(), programName).getPath());
                                this.remoteControl.uploadFile(new File(USBConnector.TEMPDIRECTORY, programName));
                                log.info("Upload " + programName);
                                log.info("File " + new File(USBConnector.TEMPDIRECTORY.getAbsolutePath(), programName).getPath());
                                this.remoteControl.runORAprogram(programName);
                                break;
                            case CMD_CONFIGURATION:
                                break;
                            default:
                                log.warning("Command " + command + " unknown");
                                break;
                        }
                    } catch ( RemoteException re ) {
                        log.severe("Connection to brick lost");
                        this.state = State.DISCOVER_CONNECTED;
                        notifyConnectionStateChanged(State.DISCOVER_CONNECTED);
                    } catch ( Exception e ) {
                        if ( !this.userDisconnect ) {
                            this.brickName = "";
                            this.brickBatteryVoltage = "";
                            this.state = State.DISCOVER;
                            log.severe("Error HTTP");
                            notifyConnectionStateChanged(State.ERROR_HTTP);
                            notifyConnectionStateChanged(State.DISCOVER);
                        }
                        this.userDisconnect = false;
                        break;
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
                default:
                    break;
            }
            try {
                Thread.sleep(50);
            } catch ( InterruptedException ie ) {
                // ok
            }
        }
    }

    /**
     * Open a connection to the Open Roberta Lab server.
     *
     * @param readTimeOut 330s for register, 15s for push
     * @return connection object
     * @throws SocketTimeoutException
     * @throws IOException
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

    @Override
    public void update() {
        this.state = State.UPDATE;
    }

    @Override
    public void connect() {
        this.state = State.CONNECT;
    }

    @Override
    public void disconnect() {
        this.userDisconnect = true;
        try {
            if ( this.state != State.WAIT_FOR_SERVER ) {
                this.remoteControl.setORAregistration(false);
            }
            this.remoteControl.disconnectFromMenu();
            if ( this.httpURLConnection != null ) {
                this.httpURLConnection.disconnect();
            }
        } catch ( RemoteException e ) {
            // ok
        }
        this.state = State.DISCOVER;
        notifyConnectionStateChanged(State.DISCOVER);
    }

    @Override
    public void notifyConnectionStateChanged(State state) {
        setChanged();
        notifyObservers(state);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getBrickName() {
        return this.brickName;
    }

    @Override
    public String getBrickBatteryVoltage() {
        return this.brickBatteryVoltage;
    }

    public State getConnectionState() {
        return this.state;
    }

    @Override
    public void close() {
        disconnect();
        if ( this.httpURLConnection != null ) {
            this.httpURLConnection.disconnect();
        }
    }
}

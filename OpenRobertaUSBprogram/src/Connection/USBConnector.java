package Connection;

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

import main.ORArmiControl;

import org.json.JSONObject;

import brick.ORAdownloader;
import brick.ORAtokenGenerator;
import brick.ORAupdater;

public class USBConnector extends Observable implements Runnable, Connector {

    private String brickIp = "10.0.1.1";
    private String serverIp = "localhost";
    private String serverPort = "1999";
    private String serverBaseIP;

    private URL pushServiceURL;
    private HttpURLConnection httpURLConnection;

    private final ORAdownloader oraDownloader;
    private final ORAupdater oraUpdater;
    private final ORAtokenGenerator tokenGenerator;

    private final ORArmiControl remoteControl = new ORArmiControl();

    private boolean running = true;

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

    public static final String USERPROFILE = System.getenv("USERPROFILE");
    public static final File TEMPDIRECTORY = new File(new File(new File(new File(USERPROFILE, "Appdata"), "Local"), "Temp"), "OpenRoberta");

    public enum State {
        DISCOVER, WAIT_FOR_CONNECT, CONNECT, WAIT_FOR_CMD, DISCOVER_CONNECTED, DISCONNECT, WAIT_FOR_SERVER, ERROR_HTTP
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
            this.pushServiceURL = new URL("http://" + serverBaseIP + "/pushcmd");
        } catch ( MalformedURLException e ) {
            // ok
        }
        this.oraDownloader = new ORAdownloader(serverBaseIP);
        this.oraUpdater = new ORAupdater(serverBaseIP);
        this.tokenGenerator = new ORAtokenGenerator();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        USBConnector con = new USBConnector(null);
        Thread thread = new Thread(con, "USBConnector");
        thread.start();
        Thread.sleep(2000);
        con.state = State.CONNECT;
    }

    @Override
    public void run() {

        OutputStream os = null;
        BufferedReader br = null;
        boolean registered = false;

        JSONObject brickData = new JSONObject();
        brickData.put(KEY_MACADDR, "usb");

        while ( this.running ) {
            // System.out.println(state.toString());
            switch ( state ) {
                case DISCOVER:
                case DISCOVER_CONNECTED:
                    try {
                        if ( InetAddress.getByName(brickIp).isReachable(3000) ) {
                            this.remoteControl.connectToMenu();
                            brickData.put(KEY_LEJOSVERSION, this.remoteControl.getlejosVersion());
                            brickData.put(KEY_MENUVERSION, this.remoteControl.getORAversion());
                            String brickName = this.remoteControl.getBrickname();
                            brickData.put(KEY_BRICKNAME, brickName);
                            this.brickName = brickName;
                        } else {
                            Thread.sleep(1000);
                            break;
                        }
                    } catch ( Exception e ) {
                        System.out.println("Cannot connect to brick.");
                        break;
                    }
                    if ( state == State.DISCOVER ) {
                        this.state = State.WAIT_FOR_CONNECT;
                    } else {
                        this.state = State.WAIT_FOR_CMD;
                    }
                    notifyConnectionStateChanged(this.state);
                    break;
                case WAIT_FOR_CONNECT:
                    break; // just waiting for user input
                case CONNECT:
                    String token = tokenGenerator.generateToken();
                    try {
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
                        System.out.println("ORA register: " + brickData);
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
                                registered = true;
                                this.remoteControl.setORAregistration(registered);
                                // TODO always?
                                //                                System.out.println("---Download firmware files to PC---");
                                //                                if ( this.oraUpdater.update() == true ) {
                                //                                    System.out.println("---Upload firmware files to EV3---");
                                //                                    if ( this.remoteControl.uploadFirmwareFiles() == true ) {
                                //                                        // TODO sound + flag setzen
                                //                                    }
                                //                                }
                                break;
                            default:
                                System.out.println("ORA unknown command from server, do nothing!");
                                break;
                        }
                    } catch ( IOException e1 ) {
                        if ( !this.userDisconnect ) {
                            this.state = State.DISCOVER;
                            this.brickName = "";
                            this.brickBatteryVoltage = "";
                            notifyConnectionStateChanged(State.ERROR_HTTP);
                            notifyConnectionStateChanged(State.DISCOVER);
                            e1.printStackTrace();
                        }
                        this.userDisconnect = false;
                        break;
                    }
                    this.state = State.WAIT_FOR_CMD;
                    notifyConnectionStateChanged(State.WAIT_FOR_CMD);
                    break;
                case WAIT_FOR_CMD:
                    try {
                        brickData.put(KEY_BATTERY, this.remoteControl.getBatteryVoltage());
                        brickData.put(KEY_BRICKNAME, this.remoteControl.getBrickname());
                        brickData.put(KEY_CMD, CMD_PUSH);
                        if ( !USBConnector.TEMPDIRECTORY.exists() ) {
                            USBConnector.TEMPDIRECTORY.mkdirs();
                        }
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
                        System.out.println("ORA cmd from server: " + command);
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
                                break;
                            case CMD_DOWNLOAD:
                                String programName = this.oraDownloader.downloadProgram(brickData);
                                System.out.println("Downloaded: " + programName);
                                System.out.println(USBConnector.TEMPDIRECTORY + "\\" + programName);
                                this.remoteControl.uploadFile(USBConnector.TEMPDIRECTORY + "\\" + programName);
                                this.remoteControl.runORAprogram(programName);
                                break;
                            case CMD_CONFIGURATION:
                                break;
                            default:
                                System.out.println("ORA unknown command from server, do nothing!");
                                break;
                        }
                    } catch ( RemoteException re ) {
                        System.out.println("Connection to brick lost.");
                        this.state = State.DISCOVER_CONNECTED;
                        notifyConnectionStateChanged(State.DISCOVER_CONNECTED);
                    } catch ( Exception e ) {
                        if ( !this.userDisconnect ) {
                            this.brickName = "";
                            this.brickBatteryVoltage = "";
                            this.state = State.DISCOVER;
                            notifyConnectionStateChanged(State.ERROR_HTTP);
                            notifyConnectionStateChanged(State.DISCOVER);
                            System.out.println("Error @ http connection!");
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
        }
        this.remoteControl.disconnectFromMenu();
    }

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
            // TODO Auto-generated catch block
            //e.printStackTrace();
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
        return token;
    }

    @Override
    public String getBrickName() {
        return brickName;
    }

    @Override
    public String getBrickBatteryVoltage() {
        return brickBatteryVoltage;
    }

    public State getConnectionState() {
        return state;
    }

    @Override
    public void close() {
        disconnect();
        if ( this.httpURLConnection != null ) {
            this.httpURLConnection.disconnect();
        }
    }
}

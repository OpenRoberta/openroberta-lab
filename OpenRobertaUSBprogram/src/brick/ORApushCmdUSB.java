package brick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.RemoteException;

import main.Main;
import main.ORArmiControl;

import org.json.JSONObject;

/**
 * EV3 to Open Roberta Lab connection class via usb.
 *
 * @author dpyka
 */
public class ORApushCmdUSB {

    private URL pushServiceURL;
    private HttpURLConnection httpURLConnection;

    private final ORAdownloader oraDownloader;
    private final ORAupdater oraUpdater;

    private final ORArmiControl remoteControl = new ORArmiControl();

    private boolean loop = true;

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

    public ORApushCmdUSB(String serverBaseIP) {
        try {
            this.pushServiceURL = new URL("http://" + serverBaseIP + "/pushcmd");
        } catch ( MalformedURLException e ) {
            // ok
        }
        this.oraDownloader = new ORAdownloader(serverBaseIP);
        this.oraUpdater = new ORAupdater(serverBaseIP);
    }

    public void interruptMainLoop() {
        this.loop = false;
        if ( this.httpURLConnection != null ) {
            this.httpURLConnection.disconnect();
        }
    }

    public void mainloop() {
        OutputStream os = null;
        BufferedReader br = null;
        boolean registered = false;
        this.loop = true;

        String token = new ORAtokenGenerator().generateToken();
        JSONObject brickData = new JSONObject();
        brickData.put(KEY_TOKEN, token);
        brickData.put(KEY_MACADDR, "usb");

        try {
            this.remoteControl.connectToMenu();
            brickData.put(KEY_LEJOSVERSION, this.remoteControl.getlejosVersion());
            brickData.put(KEY_MENUVERSION, this.remoteControl.getORAversion());
        } catch ( Exception e ) {
            System.out.println("Cannot connect to brick.");
            return;
        }

        while ( this.loop ) {
            try {
                brickData.put(KEY_BATTERY, this.remoteControl.getBatteryVoltage());
                brickData.put(KEY_BRICKNAME, this.remoteControl.getBrickname());

                if ( registered ) {
                    brickData.put(KEY_CMD, CMD_PUSH);
                    this.httpURLConnection = openConnection(15000);
                } else {
                    brickData.put(KEY_CMD, CMD_REGISTER);
                    System.out.println("ORA register: " + brickData);
                    this.httpURLConnection = openConnection(330000);
                }

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
                        if ( registered == false ) {
                            registered = true;
                            this.remoteControl.setORAregistration(registered);
                            System.out.println("---Download firmware files to PC---");
                            if ( this.oraUpdater.update() == true ) {
                                System.out.println("---Upload firmware files to EV3---");
                                if ( this.remoteControl.uploadFirmwareFiles() == true ) {
                                    // TODO sound + flag setzen
                                }
                            }
                        }
                        break;
                    case CMD_ABORT:
                        registered = false;
                        this.remoteControl.setORAregistration(false);
                        this.remoteControl.disconnectFromMenu();
                        return;
                    case CMD_UPDATE:

                        break;
                    case CMD_DOWNLOAD:
                        String programName = this.oraDownloader.downloadProgram(brickData);
                        System.out.println("Downloaded: " + programName);
                        this.remoteControl.uploadFile(Main.TEMPDIRECTORY + "\\" + programName);
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
                try {
                    while ( !InetAddress.getByName(Main.brickIp).isReachable(2000) ) {
                        if ( this.loop == false ) {
                            return;
                        }
                        System.out.println("Cannot find brick...retry in 1 sec.");
                        Thread.sleep(1000);
                    }
                    this.remoteControl.connectToMenu();
                } catch ( Exception e ) {
                    // ok
                }
            } catch ( Exception e ) {
                System.out.println("Error @ http connection!");
                return;
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

}

package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import lejos.ev3.startup.GraphicStartup.IndicatorThread;

import org.json.JSONObject;

/**
 * Class for brick <-> server communication based on json and key words "cmds".
 * 
 * @author dpyka
 */
public class ORApushCmd implements Runnable {

    private URL pushService = null;
    private HttpURLConnection httpURLConnection;

    private final ORAdownloader oraDownloader;
    private final ORAupdater oraUpdater;
    private final ORAlauncher oraLauncher;

    // brickdata + cmds send to server
    private static final String CMD_REGISTER = "register";
    private static final String CMD_PUSH = "push";

    // cmds receive from server
    private static final String CMD_REPEAT = "repeat";
    private static final String CMD_ABORT = "abort";
    private static final String CMD_UPDATE = "update";
    private static final String CMD_DOWNLOAD = "download";
    private static final String CMD_CONFIGURATION = "configuration";

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
    public ORApushCmd(String serverBaseIP, IndicatorThread ind) {
        try {
            this.pushService = new URL("http://" + serverBaseIP + "/pushcmd");
        } catch ( MalformedURLException e ) {
            // ok
        }
        this.oraDownloader = new ORAdownloader(serverBaseIP);
        this.oraUpdater = new ORAupdater(serverBaseIP);
        this.oraLauncher = new ORAlauncher(ind);
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
     * Method which processes the brick server communication in another thread.
     * The brick reacts on specific commands from the server.
     */
    @Override
    public void run() {
        OutputStream os = null;
        BufferedReader br = null;
        while ( true ) {
            try {
                this.httpURLConnection = openConnection();

                // batterystatus or brickname can change, token and macaddr stay the same
                ORAhandler.setBrickData(ORAhandler.KEY_BRICKNAME, GraphicStartup.getBrickName());
                ORAhandler.setBrickData(ORAhandler.KEY_BATTERY, GraphicStartup.getBatteryStatus());

                JSONObject requestEntity = ORAhandler.getBrickData();

                if ( ORAhandler.isRegistered() ) {
                    requestEntity.put("cmd", CMD_PUSH);
                } else {
                    requestEntity.put("cmd", CMD_REGISTER);
                }

                os = this.httpURLConnection.getOutputStream();
                os.write(requestEntity.toString().getBytes("UTF-8"));

                br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));
                StringBuilder responseStrBuilder = new StringBuilder();
                String responseString;
                while ( (responseString = br.readLine()) != null ) {
                    responseStrBuilder.append(responseString);
                }
                JSONObject responseEntity = new JSONObject(responseStrBuilder.toString());

                String command = responseEntity.getString("cmd");
                System.out.println("cmd from server: " + command);
                switch ( command ) {
                    case CMD_REPEAT:
                        ORAhandler.setRegistered(true);
                        break;
                    case CMD_ABORT:
                        throw new IOException();
                    case CMD_UPDATE:
                        this.oraUpdater.update();
                        break;
                    case CMD_DOWNLOAD:
                        String programName = this.oraDownloader.downloadProgram();
                        if ( GraphicStartup.getUserprogram() == null ) {
                            this.oraLauncher.runProgram(programName);
                        }
                        break;
                    case CMD_CONFIGURATION:
                        break;
                    default:
                        System.out.println("Unknown command from server, skip!");
                        break;
                }
            } catch ( IOException ioe ) {
                ORAhandler.setRegistered(false);
                ORAhandler.setConnectionError(true);
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
        }
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
    private HttpURLConnection openConnection() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) this.pushService.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setReadTimeout(300000);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        return httpURLConnection;
    }
}

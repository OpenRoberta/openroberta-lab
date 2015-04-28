package brick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Connection.USBConnector;

/**
 * Download all required library files and menu for Open Roberta lab to the brick.<br>
 *
 * @author dpyka
 */
public class ORAupdater {

    private final String serverBaseIP;

    private boolean DOWNLOADOK = true;

    /**
     * Creates an object for updating the brick.
     *
     * @param serverBaseIP The server's base IP like 192.168.56.1:1999.
     */
    public ORAupdater(String serverBaseIP) {
        this.serverBaseIP = serverBaseIP;
    }

    /**
     * Download all files required for Open Roberta Lab. Each method sets DOWNLOADOK to false if error occurs.
     *
     * @Return True if all files are downloaded successfully. False if at least one file not successful.
     */
    public boolean update() {
        this.DOWNLOADOK = true;
        getRuntime();
        getShared();
        getJsonLib();
        getEV3Menu();
        return this.DOWNLOADOK;
    }

    /**
     * Download the generated jar from OpenRobertaRuntime.
     */
    private void getRuntime() {
        URL runtimeURL = null;
        try {
            runtimeURL = new URL("http://" + this.serverBaseIP + "/update/runtime");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(runtimeURL);
    }

    /**
     * Download the generated jar from OpenRobertaShared.
     */
    private void getShared() {
        URL sharedURL = null;
        try {
            sharedURL = new URL("http://" + this.serverBaseIP + "/update/shared");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(sharedURL);
    }

    /**
     * Download the JSON library for brick server communication.
     */
    private void getJsonLib() {
        URL jsonURL = null;
        try {
            jsonURL = new URL("http://" + this.serverBaseIP + "/update/jsonlib");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(jsonURL);
    }

    /**
     * Download the EV3Menu. Restart is needed to launch the "new" one.
     */
    private void getEV3Menu() {
        URL menuURL = null;
        try {
            menuURL = new URL("http://" + this.serverBaseIP + "/update/ev3menu");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(menuURL);
    }

    /**
     * Opens http connection to server. "POST" as request method. Input, output
     * set to "true".
     * no readTimeOut, connection will be hold forever or until data was send or until force disconnect
     *
     * @param url
     *        the robertalab server url or ip+port
     * @return httpURLConnection http connection object to the server
     * @throws IOException
     *         opening a connection failed
     */
    private HttpURLConnection openConnection(URL serverURL) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) serverURL.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(false);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(30000);
        return httpURLConnection;
    }

    /**
     * Download a file from a specific REST service.
     *
     * @param url
     * @param directory
     */
    private void downloadFile(URL url) {
        InputStream is = null;
        FileOutputStream fos = null;
        String fileName = "";

        try {
            HttpURLConnection httpURLConnection = openConnection(url);
            is = httpURLConnection.getInputStream();
            byte[] buffer = new byte[4096];
            int n;

            String raw = httpURLConnection.getHeaderField("Content-Disposition");
            if ( raw != null && raw.indexOf("=") != -1 ) {
                fileName = raw.substring(raw.indexOf("=") + 1);
                fos = new FileOutputStream(new File(USBConnector.TEMPDIRECTORY, fileName));
                while ( (n = is.read(buffer)) != -1 ) {
                    fos.write(buffer, 0, n);
                }
            }
            System.out.println("Download of " + fileName + " successful!");
        } catch ( IOException e ) {
            System.out.println("Error @ downloading firmware file" + fileName);
            this.DOWNLOADOK = this.DOWNLOADOK && false;
        } finally {
            try {
                if ( is != null ) {
                    is.close();
                }
                if ( fos != null ) {
                    fos.close();
                }
            } catch ( IOException e ) {
                // ok
            }
        }
    }
}

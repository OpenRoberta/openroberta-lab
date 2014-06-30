package lejos.ev3.startup;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * class that handles file downloading <br>
 * holds addition information about the downloaded file <br>
 * no singleton pattern but use only one instance of the class
 * 
 * @author dpyka
 */
public class RobertaDownloadThread implements Runnable {

    // fileName of program that was downloaded from RobertaLab
    private String fileName;
    // taken from GraphicStartup.java
    // location where user programs are saved to
    private final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    private final URL serverURL;
    private final String token;
    private boolean downloaded = false;

    private HttpURLConnection httpURLConnection;

    public RobertaDownloadThread(URL serverURL, String token) {
        this.serverURL = serverURL;
        this.token = token;
    }

    /**
     * executes a download attempt
     */
    @Override
    public void run() {
        /*setHasDownloaded(false);
        // http request already hanging
        if ( isConnected() ) {
            try {
                downloadProgramFromServer(this.httpURLConnection, this.token);
                setHasDownloaded(true);
            } catch ( IOException e ) {
                e.printStackTrace();
                System.out.println("error @existing http request");
            }
            return;
        } // new http request
        else {*/
        setHasDownloaded(false);
        try {
            downloadProgramFromServer(openConnection(this.serverURL), this.token);
            setHasDownloaded(true);
        } catch ( IOException e ) {
            e.printStackTrace();
            System.out.println("error @new http request");
        }
        //}
    }

    private boolean isConnected() {
        try {
            if ( this.httpURLConnection.getResponseCode() == 200 ) {
                System.out.println(this.httpURLConnection.getResponseCode() == 200);
                return true;
            }
        } catch ( IOException e ) {
            return false;
        }
        return false;
    }

    /**
     * status: downloaded/ not downloaded
     * 
     * @return boolean
     */
    public boolean getHasDownloaded() {
        return this.downloaded;
    }

    /**
     * change download status
     * 
     * @param status
     */
    public void setHasDownloaded(boolean status) {
        this.downloaded = status;
    }

    /**
     * Returns the name of the last file which was downloaded from the server.
     * Returns null if no file downloaded before
     * 
     * @return fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Saves the name of the downloaded file from http header to instance variable
     * 
     * @param fileName
     *        the name of the file + file extension
     */
    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Opens http connection to server. "POST" as request method. Input, output
     * set to "true".
     * no readTimeOut, connection will be held forever or until data was send
     * 
     * @param url
     *        the robertalab server url or ip+port
     * @return httpURLConnection http connection object to the server
     * @throws IOException
     *         opening a connection failed
     */
    private HttpURLConnection openConnection(URL url) throws IOException {
        this.httpURLConnection = (HttpURLConnection) url.openConnection();
        this.httpURLConnection.setDoInput(true);
        this.httpURLConnection.setDoOutput(true);
        this.httpURLConnection.setRequestMethod("POST");
        this.httpURLConnection.setReadTimeout(0); // hold connection
        return this.httpURLConnection;
    }

    /**
     * Method that sends the token via http connection (server servlet) and
     * download the corresponding program as bytearray.<br>
     * Filename is retrieved from http header field.
     * 
     * @param httpURLConnection
     *        http connection to the server
     * @param token
     *        brick<->client identification token
     * @throws IOException
     *         sending token or downloading file failed
     */
    private void downloadProgramFromServer(HttpURLConnection httpURLConnection, String token) throws IOException {
        // send code to RobertaLab (example ZXCV)
        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(token);
        dos.flush();
        dos.close();

        // read fileName from http header
        setFileName(httpURLConnection.getHeaderField("fileName"));
        System.out.println("http header fileName: " + getFileName());

        // download bytearray and save to file
        InputStream is = httpURLConnection.getInputStream();
        byte[] buffer = new byte[4096];
        int n;
        OutputStream output = new FileOutputStream(new File(this.PROGRAMS_DIRECTORY, getFileName()));
        while ( (n = is.read(buffer)) != -1 ) {
            output.write(buffer, 0, n);
        }
        output.close();
        is.close();
    }
}

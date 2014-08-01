package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class RobertaHttpCalls {

    // fileName of program that was downloaded from RobertaLab
    private String fileName;
    private boolean registered = false;

    // taken from GraphicStartup.java
    // location where user programs are saved to
    private final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    public RobertaHttpCalls() {
        //
    }

    private void setRegistered(boolean bool) {
        this.registered = bool;
    }

    public boolean getRegistered() {
        return this.registered;
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
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setReadTimeout(0); // hold connection
        return httpURLConnection;
    }

    /**
     * send token to server, get response code if registered
     * TODO refactor with json library instead of string
     * 
     * @param serverURL
     * @param token
     * @throws IOException
     */
    public void sendTokenToServer(URL serverURL, String token) throws IOException {
        HttpURLConnection httpURLConnection = openConnection(serverURL);

        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(token);
        dos.flush();
        dos.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String serverResponse;
        while ( (serverResponse = in.readLine()) != null ) {
            System.out.println(serverResponse);
        }
        in.close();
        if ( serverResponse.equals("OK") ) {
            setRegistered(true);
        } else {
            setRegistered(false);
        }
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
    public void downloadProgramFromServer(HttpURLConnection httpURLConnection, String token) throws IOException {
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
    // downloadProgramFromServer(openConnection(this.serverURL), this.token);
}

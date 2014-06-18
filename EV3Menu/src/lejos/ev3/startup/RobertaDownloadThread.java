package lejos.ev3.startup;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RobertaDownloadThread implements Runnable {

    // fileName of program that was downloaded from RobertaLab
    private String fileName;
    // taken from GraphicStartup.java
    // location where user programs are saved to
    private final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    private final URL serverURL;
    private String token;

    public RobertaDownloadThread(URL serverURL, String token) {
        this.serverURL = serverURL;
        this.token = token;
    }

    /**
     * executes a download attempt
     */
    @Override
    public void run() {
        try {
            downloadProgramFromServer(openConnection(this.serverURL), this.token);
        } catch ( IOException e ) {
            e.printStackTrace();
            System.out.println("something went wrong");
        }
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

    public void updateToken(String token) {
        this.token = token;
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
        // httpURLConnection.setConnectTimeout(5000); //establish tcp handshake
        httpURLConnection.setReadTimeout(60000);
        // brick try to receive the program from server for 60sec
        return httpURLConnection;
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

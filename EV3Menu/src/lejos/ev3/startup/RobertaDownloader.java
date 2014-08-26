package lejos.ev3.startup;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * class that handles file downloading <br>
 * holds addition information about the downloaded file <br>
 * no singleton pattern but use only one instance of this class
 * 
 * @author dpyka
 */
public class RobertaDownloader implements Runnable {

    // status variables which are checked in main menu
    private boolean downloadRequestHanging = false;
    private boolean hasDownloaded = false;

    private boolean dlInterruptRequest = false;

    private final URL serverURL;
    private final String token;

    private String fileName;
    private final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    public RobertaDownloader(URL serverURL, String token) {
        this.serverURL = serverURL;
        this.token = token;
    }

    public boolean getHangingRequestInfo() {
        return this.downloadRequestHanging;
    }

    public void setHangingRequestInfo(boolean bool) {
        this.downloadRequestHanging = bool;
    }

    public boolean getDownloadCompleteInfo() {
        return this.hasDownloaded;
    }

    public void setDownloadCompleteInfo(boolean bool) {
        this.hasDownloaded = bool;
    }

    public String getFileName() {
        return this.fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDLInterruptInfo(boolean bool) {
        this.dlInterruptRequest = bool;
    }

    /**
     * Method that sends the token via http connection (server servlet) and
     * download the corresponding program as bytearray.<br>
     * Filename is retrieved from http header field.
     */
    @Override
    public void run() {
        try {
            HttpURLConnection httpURLConnection = openConnection(this.serverURL);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(this.token);
            dos.flush();
            dos.close();

            System.out.println("before download");

            setFileName(httpURLConnection.getHeaderField("fileName"));
            System.out.println("http header fileName: " + getFileName());

            InputStream is = httpURLConnection.getInputStream();
            byte[] buffer = new byte[4096];
            int n;
            FileOutputStream output = new FileOutputStream(new File(this.PROGRAMS_DIRECTORY, getFileName()));

            while ( (n = is.read(buffer)) != -1 ) {
                output.write(buffer, 0, n);
            }
            output.close();
            is.close();
            setDownloadCompleteInfo(true);

            System.out.println("after download");

        } catch ( IOException e ) {
            setHangingRequestInfo(false);
            setDownloadCompleteInfo(true);
            e.printStackTrace();
        }
        setHangingRequestInfo(false);
    }

    /**
     * Opens http connection to server. "POST" as request method. Input, output
     * set to "true".
     * no readTimeOut, connection will be hold forever or until data was send
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
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setReadTimeout(0); // hold connection
        return httpURLConnection;
    }

}

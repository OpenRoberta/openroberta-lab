package lejos.ev3.startup;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author dpyka
 */
public class RobertaDownloader implements Runnable {

    private boolean downloadRequestHanging = false;
    private boolean hasDownloaded = false;

    private HttpURLConnection httpURLConnection;

    private final URL serverURL;
    private final String token;

    private String fileName;
    private final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    public RobertaDownloader(URL serverURL, String token) {
        this.serverURL = serverURL;
        this.token = token;
    }

    public HttpURLConnection getHttpConnection() {
        return this.httpURLConnection;
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

    @Override
    public void run() {
        setHangingRequestInfo(true);
        setDownloadCompleteInfo(false);

        DataOutputStream dos = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            this.httpURLConnection = openConnection(this.serverURL);

            dos = new DataOutputStream(this.httpURLConnection.getOutputStream());
            dos.writeBytes(this.token);
            dos.flush();

            is = this.httpURLConnection.getInputStream();
            byte[] buffer = new byte[4096];
            int n;

            this.fileName = this.httpURLConnection.getHeaderField("fileName");
            System.out.println("http header fileName: " + this.fileName);
            fos = new FileOutputStream(new File(this.PROGRAMS_DIRECTORY, this.fileName));

            while ( (n = is.read(buffer)) != -1 ) {
                fos.write(buffer, 0, n);
            }
            setDownloadCompleteInfo(true);
        } catch ( IOException e ) {
            System.out.println("force disconnect");
        } finally {
            try {
                if ( dos != null ) {
                    dos.close();
                }
                if ( is != null ) {
                    is.close();
                }
                if ( fos != null ) {
                    fos.close();
                }
            } catch ( IOException e ) {
            }
            setHangingRequestInfo(false);
        }
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
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setReadTimeout(0);
        return httpURLConnection;
    }

}

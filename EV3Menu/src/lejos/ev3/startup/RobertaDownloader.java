package lejos.ev3.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lejos.utility.Delay;

import org.json.JSONObject;

/**
 * Class that handles the file download (.jar file) on /download service of the server
 * 
 * @author dpyka
 */
public class RobertaDownloader implements Runnable {

    private HttpURLConnection httpURLConnection;

    private final URL serverURL;
    private final String token;

    private final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    public RobertaDownloader(URL serverURL, String token) {
        this.serverURL = serverURL;
        this.token = token;
    }

    public HttpURLConnection getHttpConnection() {
        return this.httpURLConnection;
    }

    @Override
    public void run() {
        while ( RobertaObserver.isAutorun() ) {
            if ( RobertaObserver.isExecuted() == true && RobertaObserver.isDownloaded() == false ) {

                OutputStream os = null;
                InputStream is = null;
                FileOutputStream fos = null;

                try {
                    this.httpURLConnection = openConnection(this.serverURL);

                    JSONObject requestEntity = new JSONObject();
                    requestEntity.put("BrickName", RobertaObserver.getBrickName());
                    requestEntity.put("Token", this.token);

                    os = this.httpURLConnection.getOutputStream();
                    os.write(requestEntity.toString().getBytes("UTF-8"));

                    is = this.httpURLConnection.getInputStream();
                    byte[] buffer = new byte[4096];
                    int n;

                    String raw = this.httpURLConnection.getHeaderField("Content-Disposition");
                    String fileName = "";
                    if ( raw != null && raw.indexOf("=") != -1 ) {
                        fileName = raw.substring(raw.indexOf("=") + 1);
                    } else {
                        fileName = "unknown.jar";
                    }
                    RobertaObserver.setUserFileName(fileName);

                    fos = new FileOutputStream(new File(this.PROGRAMS_DIRECTORY, RobertaObserver.getUserFileName()));
                    while ( (n = is.read(buffer)) != -1 ) {
                        fos.write(buffer, 0, n);
                    }
                    RobertaObserver.setDownloaded(true);
                } catch ( IOException e ) {
                    System.out.println("force disconnect (download)");
                } finally {
                    try {
                        if ( os != null ) {
                            os.close();
                        }
                        if ( is != null ) {
                            is.close();
                        }
                        if ( fos != null ) {
                            fos.close();
                        }
                    } catch ( IOException e ) {
                        //
                    }
                }
            } else {
                Delay.msDelay(500);
            }
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
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        return httpURLConnection;
    }

}

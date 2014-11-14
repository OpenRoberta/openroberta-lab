package lejos.ev3.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Download all required files for Open Roberta lab to the brick.<br>
 * 
 * @author dpyka
 */
public class RobertaUpdater {

    private final String serverURLString;
    private final File libDir = new File("/home/roberta/lib");
    private final File menuDir = new File("/home/root/lejos/bin/utils");

    public RobertaUpdater(String serverURLString) {
        this.serverURLString = serverURLString;
    }

    public void update() {
        getRuntime();
        getShared();
        getJsonLib();
        getEV3Menu();
    }

    private void getRuntime() {
        URL runtimeURL = null;
        try {
            runtimeURL = new URL("http://" + this.serverURLString + "/update/runtime");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(runtimeURL, this.libDir);
    }

    private void getShared() {
        URL sharedURL = null;
        try {
            sharedURL = new URL("http://" + this.serverURLString + "/update/shared");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(sharedURL, this.libDir);
    }

    private void getJsonLib() {
        URL jsonURL = null;
        try {
            jsonURL = new URL("http://" + this.serverURLString + "/update/jsonlib");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(jsonURL, this.libDir);
    }

    private void getEV3Menu() {
        URL menuURL = null;
        try {
            menuURL = new URL("http://" + this.serverURLString + "/update/ev3menu");
        } catch ( MalformedURLException e ) {
            // ok
        }
        downloadFile(menuURL, this.menuDir);
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
        httpURLConnection.setReadTimeout(30);
        return httpURLConnection;
    }

    /**
     * @param url
     */
    private void downloadFile(URL url, File directory) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            HttpURLConnection httpURLConnection = openConnection(url);
            is = httpURLConnection.getInputStream();
            byte[] buffer = new byte[4096];
            int n;

            String raw = httpURLConnection.getHeaderField("Content-Disposition");
            String fileName = "";
            if ( raw != null && raw.indexOf("=") != -1 ) {
                fileName = raw.substring(raw.indexOf("=") + 1);
                fos = new FileOutputStream(new File(directory, fileName));
                while ( (n = is.read(buffer)) != -1 ) {
                    fos.write(buffer, 0, n);
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            System.out.println("Error while updating!");
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

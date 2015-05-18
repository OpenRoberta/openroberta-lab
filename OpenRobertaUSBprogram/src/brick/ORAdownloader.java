package brick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import connectionEV3.USBConnector;

/**
 * Class that downloads user program from /download service.
 *
 * @author dpyka
 */
public class ORAdownloader {
    private URL downloadService = null;

    /**
     * Create a new object for downloading the user program from the server to the
     * brick.
     *
     * @param serverBaseIP
     *        The base IP like 192.168.56.1:1999
     */
    public ORAdownloader(String serverBaseIP) {
        try {
            this.downloadService = new URL("http://" + serverBaseIP + "/download");
        } catch ( MalformedURLException e ) {
            // ok
        }
    }

    /**
     * * Download the user program from the server. Send brickdata as request as
     * JSON, receives application/octet-stream.
     *
     * @param brickData The content of the communication between brick and ORA server.
     * @return The name of the downloaded file
     */
    public String downloadProgram(JSONObject brickData) {
        OutputStream os = null;
        InputStream is = null;
        FileOutputStream fos = null;

        String fileName = "";

        try {
            HttpURLConnection httpURLConnection = openConnection();

            os = httpURLConnection.getOutputStream();
            os.write(brickData.toString().getBytes("UTF-8"));

            is = httpURLConnection.getInputStream();
            byte[] buffer = new byte[4096];
            int n;

            String raw = httpURLConnection.getHeaderField("Content-Disposition");
            if ( raw != null && raw.indexOf("=") != -1 ) {
                fileName = raw.substring(raw.indexOf("=") + 1);
            } else {
                fileName = "unknown.jar";
            }

            fos = new FileOutputStream(new File(USBConnector.TEMPDIRECTORY, fileName));
            while ( (n = is.read(buffer)) != -1 ) {
                fos.write(buffer, 0, n);
            }
        } catch ( IOException e ) {
            System.out.println("Downloading the program failed!");
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
        return fileName;
    }

    /**
     * Opens http connection to server to download the user program.
     *
     * @return httpURLConnection http connection object to the server
     * @throws IOException
     *         opening a connection failed
     */
    private HttpURLConnection openConnection() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) this.downloadService.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setReadTimeout(0);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        return httpURLConnection;
    }

}

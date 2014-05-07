package lejos.ev3.startup;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RobertaUtils {

    // taken from GraphicStartup.java
    private static final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    public RobertaUtils() {
        //
    }

    // exception handling?!
    public String getProgram(URL serverURL, String token) {
        try {
            return downloadProgramFromServer(openConnection(serverURL), token);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        return httpURLConnection;
    }

    private String downloadProgramFromServer(HttpURLConnection httpURLConnection, String code) throws IOException {
        // send code (example zxcv)
        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(code);
        dos.flush();
        dos.close();

        // read fileName from http header
        String fileName = httpURLConnection.getHeaderField("fileName");
        System.out.println("http header fileName: " + fileName);

        // download bytearray and save to file
        InputStream is = httpURLConnection.getInputStream();
        byte[] buffer = new byte[4096];
        int n;
        OutputStream output = new FileOutputStream(new File(PROGRAMS_DIRECTORY, fileName));
        while ( (n = is.read(buffer)) != -1 ) {
            output.write(buffer, 0, n);
        }
        output.close();
        return fileName;
    }
}

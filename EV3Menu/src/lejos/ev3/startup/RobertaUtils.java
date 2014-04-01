package lejos.ev3.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RobertaUtils {

    public RobertaUtils() {
        //
    }

    public void getProgram(URL serverURL, String programDir, String programName) {
        try {
            saveProgram(requestToServer(openConnection(serverURL)), programDir, programName);
        } catch ( ClassNotFoundException | IOException e ) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = null;
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        return httpURLConnection;
    }

    private byte[] requestToServer(HttpURLConnection httpURLConnection) throws IOException, ClassNotFoundException {
        InputStream is = httpURLConnection.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        byte[] ServletAnswer = (byte[]) ois.readObject();
        ois.close();
        is.close();
        return ServletAnswer;
    }

    private void saveProgram(byte[] program, String programDir, String programName) throws IOException {
        File userProgram = new File(programDir, programName);
        FileOutputStream fis = new FileOutputStream(userProgram);
        try {
            fis.write(program);
        } finally {
            fis.close();
        }
    }
}

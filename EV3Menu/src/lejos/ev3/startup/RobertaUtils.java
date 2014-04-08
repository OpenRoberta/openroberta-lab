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

    public RobertaUtils() {
        //
    }

    public void getProgram(URL serverURL, String programDir, String programName, String code) {
        try {
            //saveProgram(requestToServer(openConnection(serverURL)), programDir, programName);
            requestToServer(openConnection(serverURL), programDir, programName, code);
        } catch ( ClassNotFoundException | IOException e ) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = null;
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        return httpURLConnection;
    }

    private void requestToServer(HttpURLConnection httpURLConnection, String programDir, String programName, String code)
        throws IOException,
        ClassNotFoundException {

        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(code);
        dos.flush();
        dos.close();

        String fileName = httpURLConnection.getHeaderField("fileName");
        System.out.println(fileName);

        InputStream is = httpURLConnection.getInputStream();
        byte[] buffer = new byte[4096];
        int n;
        OutputStream output = new FileOutputStream(new File(programDir, programName));
        while ( (n = is.read(buffer)) != -1 ) {
            output.write(buffer, 0, n);
        }
        output.close();

        /*InputStream is = httpURLConnection.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        byte[] ServletAnswer = (byte[]) ois.readObject();
        ois.close();
        is.close();
        return ServletAnswer;*/
    }

    /*private void saveProgram(byte[] program, String programDir, String programName) throws IOException {
        File userProgram = new File(programDir, programName);
        FileOutputStream fis = new FileOutputStream(userProgram);
        try {
            fis.write(program);
        } finally {
            fis.close();
        }
    }*/
}

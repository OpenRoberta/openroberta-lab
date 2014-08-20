package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * handles registration of the token<br>
 * no singleton pattern but use only one instance of this class
 * 
 * @author dpyka
 */
public class RobertaTokenRegister implements Runnable {

    private final URL serverURL;
    private final String token;
    private boolean isTimeOut = false;
    private boolean isRegistered = false;

    public RobertaTokenRegister(URL serverURL, String token) {
        this.serverURL = serverURL;
        this.token = token;
    }

    public void setTimeOutInfo(boolean bool) {
        this.isTimeOut = bool;
    }

    public void setRegisteredInfo(boolean bool) {
        this.isRegistered = bool;
    }

    public boolean getTimeOutInfo() {
        return this.isTimeOut;
    }

    public boolean getRegisteredInfo() {
        return this.isRegistered;
    }

    /**
     * send token to server, get response code if registered
     * TODO refactor with json library instead of string
     */
    @Override
    public void run() {
        try {
            HttpURLConnection httpURLConnection = openConnection(this.serverURL);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(this.token);
            dos.flush();
            dos.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String serverResponse = "";
            while ( (serverResponse = in.readLine()) != null ) {
                System.out.println(serverResponse);
            }
            in.close();
        } catch ( SocketTimeoutException ste ) {
            ste.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Opens http connection to server. "POST" as request method. Input, output
     * set to "true".
     * 5 minutes readTimeOut, in this time, the token has to be registered with the server
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
        httpURLConnection.setReadTimeout(300000);
        return httpURLConnection;
    }

}

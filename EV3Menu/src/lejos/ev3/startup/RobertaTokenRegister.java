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
public class RobertaTokenRegister {

    public enum Status {
        TIMEOUT(), ERROR(), OK();
    }

    private final URL serverURL;

    public RobertaTokenRegister(URL serverURL) {
        this.serverURL = serverURL;
    }

    /**
     * send token to server, get response code if registered
     * TODO refactor with json library instead of string
     */
    public Status connectToServer(String token) {
        try {
            HttpURLConnection httpURLConnection = openConnection(this.serverURL);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(token);
            dos.flush();
            dos.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String serverResponse = "";
            while ( (serverResponse = in.readLine()) != null ) {
                System.out.println(serverResponse);
            }
            in.close();
            return Status.OK;
        } catch ( SocketTimeoutException ste ) {
            ste.printStackTrace();
            return Status.TIMEOUT;
        } catch ( IOException e ) {
            e.printStackTrace();
            return Status.ERROR;
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

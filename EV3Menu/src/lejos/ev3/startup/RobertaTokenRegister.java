package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.json.JSONObject;

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
    private boolean hasError = false;

    private HttpURLConnection httpURLConnection;

    public RobertaTokenRegister(URL serverURL, String token) {
        this.serverURL = serverURL;
        this.token = token;
    }

    public HttpURLConnection getHttpConnection() {
        return this.httpURLConnection;
    }

    public void setTimeOutInfo(boolean bool) {
        this.isTimeOut = bool;
    }

    public boolean getTimeOutInfo() {
        return this.isTimeOut;
    }

    public void setRegisteredInfo(boolean bool) {
        this.isRegistered = bool;
    }

    public boolean getRegisteredInfo() {
        return this.isRegistered;
    }

    public void setErrorInfo(boolean bool) {
        this.hasError = bool;
    }

    public boolean getErrorInfo() {
        return this.hasError;
    }

    /**
     * send token to server, get OK response if registered successfully
     * TODO refactor with json library instead of string
     */
    @Override
    public void run() {
        //DataOutputStream dos = null;
        OutputStream os = null;
        BufferedReader br = null;

        try {
            this.httpURLConnection = openConnection(this.serverURL);

            JSONObject jsonTest = new JSONObject();
            jsonTest.put("Name", "Roberta01");
            jsonTest.put("Token", this.token);

            os = this.httpURLConnection.getOutputStream();
            os.write(jsonTest.toString().getBytes("UTF-8"));

            //            dos = new DataOutputStream(this.httpURLConnection.getOutputStream());
            //            dos.flush();
            //            dos.close();

            br = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));
            String serverResponse = "";
            while ( (serverResponse = br.readLine()) != null ) {
                System.out.println(serverResponse);
            }
            br.close();
            setRegisteredInfo(true);
        } catch ( SocketTimeoutException ste ) {
            setTimeOutInfo(true);
            ste.printStackTrace();
        } catch ( IOException e ) {
            setErrorInfo(true);
            System.out.println("force disconnect (registerToken)");
        } finally {
            try {
                if ( os != null ) {
                    os.close();
                }
                if ( br != null ) {
                    br.close();
                }
            } catch ( IOException e ) {
            }
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
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        return httpURLConnection;
    }

}

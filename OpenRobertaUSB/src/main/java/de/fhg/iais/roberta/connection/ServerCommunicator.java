package de.fhg.iais.roberta.connection;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * The server communicator emulates an EV3 brick. This class provides access to push requests, downloads the user program and download system libraries for
 * the upload funtion.
 *
 * @author dpyka
 */
public class ServerCommunicator {

    private String serverpushAddress;
    private String serverdownloadAddress;
    private String serverupdateAddress;

    private final CloseableHttpClient httpclient;
    private HttpPost post = null;

    private String filename = "";

    /**
     * @param serverAddress either the default address taken from the properties file or the custom address entered in the gui.
     */
    public ServerCommunicator(String serverAddress) {
        updateCustomServerAddress(serverAddress);
        this.httpclient = HttpClients.createDefault();
    }

    /**
     * Update the server address if the user wants to use an own installation of open roberta with a different IP address.
     *
     * @param customServerAddress for example localhost:1999 or 192.168.178.10:1337
     */
    public void updateCustomServerAddress(String customServerAddress) {
        this.serverpushAddress = customServerAddress + "/rest/pushcmd";
        this.serverdownloadAddress = customServerAddress + "/rest/download";
        this.serverupdateAddress = customServerAddress + "/rest/update";
    }

    /**
     * @return the file name of the last binary file downloaded of the server communicator object.
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Sends a push request to the open roberta server for registration or keeping the connection alive. This will be hold by the server for approximately 10
     * seconds and then answered.
     *
     * @param requestContent data from the EV3 plus the token and the command send to the server (CMD_REGISTER or CMD_PUSH)
     * @return response from the server
     * @throws IOException if the server is unreachable for whatever reason.
     */
    public JSONObject pushRequest(JSONObject requestContent) throws IOException {
        this.post = new HttpPost("http://" + this.serverpushAddress);
        StringEntity requestEntity = new StringEntity(requestContent.toString(), ContentType.create("application/json", "UTF-8"));
        this.post.setEntity(requestEntity);

        CloseableHttpResponse response = this.httpclient.execute(this.post);
        HttpEntity responseEntity = response.getEntity();
        String responseText = "";
        if ( responseEntity != null ) {
            responseText = EntityUtils.toString(responseEntity);
        }
        response.close();
        return new JSONObject(responseText);
    }

    /**
     * Downloads a user program from the server as binary. The http POST is used here.
     *
     * @param requestContent all the content of a standard push request.
     * @return
     * @throws IOException if the server is unreachable or something is wrong with the binary content.
     */
    public byte[] downloadProgram(JSONObject requestContent) throws IOException {
        HttpPost post = new HttpPost("http://" + this.serverdownloadAddress);
        StringEntity requestEntity = new StringEntity(requestContent.toString(), ContentType.create("application/json", "UTF-8"));
        post.setEntity(requestEntity);
        CloseableHttpResponse response = this.httpclient.execute(post);
        HttpEntity responseEntity = response.getEntity();
        byte[] binaryfile = null;
        if ( responseEntity != null ) {
            this.filename = response.getFirstHeader("Filename").getValue();
            binaryfile = EntityUtils.toByteArray(responseEntity);
        }
        response.close();
        return binaryfile;
    }

    /**
     * Basically the same as downloading a user program but without any information about the EV3. It uses http GET(!).
     *
     * @param fwFile name of the file in the url as suffix ( .../rest/update/ev3menu)
     * @return
     * @throws IOException if the server is unreachable or something is wrong with the binary content.
     */
    public byte[] downloadFirmwareFile(String fwFile) throws IOException {
        HttpGet get = new HttpGet("http://" + this.serverupdateAddress + "/" + fwFile);
        CloseableHttpResponse response = this.httpclient.execute(get);
        HttpEntity responseEntity = response.getEntity();
        byte[] binaryfile = null;
        if ( responseEntity != null ) {
            this.filename = response.getFirstHeader("Filename").getValue();
            binaryfile = EntityUtils.toByteArray(responseEntity);
        }
        response.close();
        return binaryfile;
    }

    /**
     * Cancel a pending push request (which is blocking in another thread), if the user wants to disconnect.
     */
    public void abort() {
        if ( this.post != null ) {
            this.post.abort();
        }
    }

    /**
     * Shut down the http client.
     */
    public void shutdown() {
        try {
            this.httpclient.close();
        } catch ( IOException e ) {
            // ok
        }
    }
}

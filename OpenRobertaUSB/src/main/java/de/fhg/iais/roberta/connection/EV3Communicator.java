package de.fhg.iais.roberta.connection;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * The EV3 is running an http server. We initialise the connection to the robot by the USB program, because of possible firewall issues.
 *
 * @author dpyka
 */
public class EV3Communicator {

    private final String brickIp;
    private final String brickinfo;
    private final String brickprogram;
    private final String brickfirmware;

    private final CloseableHttpClient httpclient;

    /**
     * @param brickIp is 10.0.1.1 for leJOS
     */
    public EV3Communicator(String brickIp) {
        this.brickIp = brickIp;
        this.brickinfo = this.brickIp + "/brickinfo";
        this.brickprogram = this.brickIp + "/program";
        this.brickfirmware = this.brickIp + "/firmware";

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000).build();
        this.httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * Send a push request to the http server on the EV3. On EV3 side this will also control the connection icon and sound when connected successfully to Open
     * Roberta. This using http POST.
     *
     * @param command String CMD_REGISTER or CMD_REPEAT
     * @return JSONObject Information about the EV3 like brickname, versions, battery, ...
     * @throws IOException
     */
    public JSONObject pushToBrick(String command) throws IOException {
        JSONObject request = new JSONObject();
        request.put(Connector.KEY_CMD, command);
        HttpPost post = new HttpPost("http://" + this.brickinfo);
        StringEntity jsoncontent = new StringEntity(request.toString(), ContentType.create("application/json", "UTF-8"));
        post.setEntity(jsoncontent);

        CloseableHttpResponse response = this.httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        JSONObject responseJSON = new JSONObject(new String(EntityUtils.toString(entity)));

        response.close();
        return responseJSON;
    }

    /**
     * Upload a binary user program to the EV3. It uses http POST.
     *
     * @param binaryfile
     * @param filename
     * @return
     * @throws IOException should only occur if you disconnect the cable
     */
    public JSONObject uploadProgram(byte[] binaryfile, String filename) throws IOException {
        HttpPost post = new HttpPost("http://" + this.brickprogram);
        return uploadBinary(post, binaryfile, filename);
    }

    /**
     * Upload a binary system file to the EV3. It uses http POST.
     *
     * @param binaryfile
     * @param filename
     * @return
     * @throws IOException should only occur if you disconnect the cable
     */
    public JSONObject uploadFirmwareFile(byte[] binaryfile, String filename) throws IOException {
        HttpPost post = new HttpPost("http://" + this.brickfirmware);
        return uploadBinary(post, binaryfile, filename);
    }

    private JSONObject uploadBinary(HttpPost post, byte[] binaryfile, String filename) throws IOException {
        ByteArrayEntity content = new ByteArrayEntity(binaryfile);
        post.setEntity(content);
        post.setHeader("Filename", filename);
        CloseableHttpResponse response = this.httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        JSONObject jsonresponse = new JSONObject(new String(EntityUtils.toString(entity)));
        response.close();
        return jsonresponse;
    }

    /**
     * Send an abort command to the EV3 to disconnect it, reset the connection state.
     *
     * @throws IOException should only occur if you disconnect the cable
     */
    public void disconnectBrick() throws IOException {
        pushToBrick("abort");
    }

    /**
     * Send a command to the EV3 to restart the menu after updating the system libraries.
     *
     * @throws IOException should only occur if you disconnect the cable
     */
    public void restartBrick() throws IOException {
        pushToBrick("update");
    }

    /**
     * Shut down the connection to the EV3.
     */
    public void shutdown() {
        try {
            this.httpclient.close();
        } catch ( IOException e ) {
            // ok
        }
    }

    /**
     * Check if a program is currently running on the EV3.
     *
     * @return true (as string) if a program is running, false (as string) if no program is running.
     * @throws IOException should only occur if you disconnect the cable
     */
    public String checkBrickState() throws IOException {
        return pushToBrick(Connector.CMD_ISRUNNING).getString("isrunning");
    }
}

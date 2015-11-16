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

public class EV3Communicator {

    private final String brickIp;
    private final String brickinfo;
    private final String brickprogram;
    private final String brickfirmware;

    private final CloseableHttpClient httpclient;

    public EV3Communicator(String brickIp) {
        this.brickIp = brickIp;
        this.brickinfo = this.brickIp + "/brickinfo";
        this.brickprogram = this.brickIp + "/program";
        this.brickfirmware = this.brickIp + "/firmware";

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000).build();
        this.httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    public JSONObject pushToBrick(String command) throws IOException {
        JSONObject request = new JSONObject();
        request.put(USBConnector.KEY_CMD, command);
        HttpPost post = new HttpPost("http://" + this.brickinfo);
        StringEntity jsoncontent = new StringEntity(request.toString(), ContentType.create("application/json", "UTF-8"));
        post.setEntity(jsoncontent);

        CloseableHttpResponse response = this.httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        JSONObject responseJSON = new JSONObject(new String(EntityUtils.toString(entity)));

        response.close();
        return responseJSON;
    }

    public JSONObject uploadProgram(byte[] binaryfile, String filename) throws IOException {
        HttpPost post = new HttpPost("http://" + this.brickprogram);
        return uploadBinary(post, binaryfile, filename);
    }

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

    public void disconnectBrick() throws IOException {
        pushToBrick("abort");
    }

    public void restartBrick() throws IOException {
        pushToBrick("update");
    }

    public void shutdown() {
        try {
            this.httpclient.close();
        } catch ( IOException e ) {
            // ok
        }
    }

    public String checkBrickState(String command) throws IOException {
        return pushToBrick(command).getString("isrunning");
    }
}

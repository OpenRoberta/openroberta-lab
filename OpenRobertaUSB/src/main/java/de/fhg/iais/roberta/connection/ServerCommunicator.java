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

public class ServerCommunicator {

    private final String serverpushAddress;
    private final String serverdownloadAddress;
    private final String serverupdateAddress;

    private final CloseableHttpClient httpclient;
    private HttpPost post = null;

    private String filename = "";

    public ServerCommunicator(String serverAddress) {
        this.serverpushAddress = serverAddress + "/pushcmd";
        this.serverdownloadAddress = serverAddress + "/download";
        this.serverupdateAddress = serverAddress + "/update";
        this.httpclient = HttpClients.createDefault();
    }

    public String getFilename() {
        return this.filename;
    }

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

    public byte[] downloadFirmwareFile(JSONObject requestContent, String fwFile) throws IOException {
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

    public void disconnect() {
        if ( this.post != null ) {
            this.post.abort();
        }
    }

    public void shutdown() {
        try {
            this.httpclient.close();
        } catch ( IOException e ) {
            // TODO ok?
        }
    }
}

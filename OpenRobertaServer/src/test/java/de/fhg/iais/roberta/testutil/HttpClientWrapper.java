package de.fhg.iais.roberta.testutil;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientWrapper.class);
    private static final int RETRY_COUNTER = 1;

    private final HttpClient client;
    private final IdleConnectionMonitorThread idleConnectionMonitorThread;
    private RequestConfig requestConfig;

    public HttpClientWrapper() throws Exception {

        this.requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).build();
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Charset.forName("UTF-8")).build();
        PlainConnectionSocketFactory plainSF = PlainConnectionSocketFactory.getSocketFactory();

        RegistryBuilder<ConnectionSocketFactory> regBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
        Registry<ConnectionSocketFactory> registry = regBuilder.register("http", plainSF).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(100);
        connectionManager.setValidateAfterInactivity(1000);
        connectionManager.setDefaultConnectionConfig(connectionConfig);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connectionManager);

        this.client = httpClientBuilder.build();

        this.idleConnectionMonitorThread = new IdleConnectionMonitorThread(connectionManager);
        new Thread(null, this.idleConnectionMonitorThread, "IdleConn").start();
    }

    public void shutdown() {
        this.idleConnectionMonitorThread.shutdown();
    }

    public String put(String path, Object requestEntity, String mimeType) throws Exception {
        HttpPut put = new HttpPut(path);
        put.setConfig(this.requestConfig);
        put.addHeader(HttpHeaders.ACCEPT, "*");
        HttpEntity httpEntity = null;
        if ( requestEntity instanceof File ) {
            ContentType contentType = ContentType.create(mimeType, "UTF_8");
            httpEntity = new FileEntity((File) requestEntity, contentType);
        } else if ( requestEntity instanceof String ) {
            ContentType contentType = ContentType.create(mimeType, "UTF_8");
            httpEntity = new StringEntity((String) requestEntity, contentType);
        } else if ( requestEntity != null ) {
            LOG.error("Entity should be either a file or a String, but is a " + requestEntity.getClass().getName());
        }
        if ( httpEntity != null ) {
            put.setEntity(httpEntity);
        }
        return mkRequest(put);
    }

    public String post(String path, String accept, Object requestEntity, String mimeType) throws Exception {
        if ( accept == null ) {
            throw new RuntimeException("param for ACCEPT-header is missing");
        }
        if ( requestEntity != null && mimeType == null ) {
            throw new RuntimeException("mime type for entity is missing");
        }
        HttpPost post = new HttpPost(path);
        post.setConfig(this.requestConfig);
        post.addHeader(HttpHeaders.ACCEPT, accept);
        HttpEntity httpEntity = null;
        if ( requestEntity instanceof File ) {
            ContentType contentType = ContentType.create(mimeType, "UTF-8");
            httpEntity = new FileEntity((File) requestEntity, contentType);
        } else if ( requestEntity instanceof String ) {
            ContentType contentType = ContentType.create(mimeType, "UTF-8");
            httpEntity = new StringEntity((String) requestEntity, contentType);
        } else if ( requestEntity != null ) {
            LOG.error("Entity should be either a file or a String, but is a " + requestEntity.getClass().getName());
        }
        if ( httpEntity != null ) {
            post.setEntity(httpEntity);
        }
        return mkRequest(post);
    }

    public String get(String path, String accept) throws Exception {
        if ( accept == null ) {
            throw new RuntimeException("param for ACCEPT-header is missing");
        }
        HttpGet get = new HttpGet(path);
        get.setConfig(this.requestConfig);
        get.addHeader(HttpHeaders.ACCEPT, accept);
        return mkRequest(get);
    }

    private String mkRequest(HttpUriRequest uriRequest) {
        int retryCounter;
        String message = null;
        String responseEntity = null;
        for ( retryCounter = 0; retryCounter < RETRY_COUNTER; retryCounter++ ) {
            try {
                HttpResponse response = this.client.execute(uriRequest);
                HttpEntity responseEntityObject = null;
                try {
                    responseEntityObject = response.getEntity();
                    if ( responseEntityObject != null ) {
                        InputStream responseEntityStream = null;
                        try {
                            responseEntityStream = responseEntityObject.getContent();
                            responseEntity = convertStreamToString(responseEntityStream);
                        } finally {
                            if ( responseEntityStream != null ) {
                                responseEntityStream.close();
                            }
                        }
                    }

                } finally {
                    EntityUtils.consume(responseEntityObject);
                }
                StatusLine statusLine = response.getStatusLine();
                message = mkMessage(statusLine);
                if ( message == null ) {
                    break; // successful, no need to retry :-)
                }
            } catch ( Exception e ) {
                message = mkMessage(null);
                LOG.warn(message, e);
                if ( uriRequest != null ) {
                    uriRequest.abort();
                }
            }
        }
        if ( retryCounter > 0 && message == null ) {
            LOG.warn(uriRequest.getURI().toString() + " succeeded after " + retryCounter + " retries");
        } else if ( message != null ) {
            LOG.error(uriRequest.getURI().toString() + " FAILED after " + RETRY_COUNTER + " retries with message " + message);
        }
        return responseEntity;
    }

    /**
     * prepare status message to inform about errors from the server side
     *
     * @param statusLine from {@link HttpResponse}, maybe null, only read
     * @return null, if no error occured, an error message otherwise
     */
    private String mkMessage(StatusLine statusLine) {
        String msg = null;
        if ( statusLine == null ) {
            msg = "no response from server";
        } else if ( statusLine.getStatusCode() >= 300 ) {
            msg = "status code from server: " + statusLine.getStatusCode();
        }
        return msg;
    }

    private static String convertStreamToString(java.io.InputStream is) {
        try (java.util.Scanner scanner = new java.util.Scanner(is)) {
            java.util.Scanner s = scanner.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    /**
     * responsible to remove expired or closed connections from the connection pool.<br>
     * You may have to adjust the timing of the IdleConnectionMonitorThread
     */
    public static class IdleConnectionMonitorThread implements Runnable {
        private static final long EXPIRE_CHECK_INTERVAL = 15000;
        private final HttpClientConnectionManager cm;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connectionManager) {
            this.cm = connectionManager;
        }

        @Override
        public synchronized void run() {
            while ( !this.shutdown ) {
                try {
                    wait(EXPIRE_CHECK_INTERVAL);
                    this.cm.closeExpiredConnections(); // Close expired connections
                    this.cm.closeIdleConnections(30, TimeUnit.SECONDS); // Optionally, close connections that have been idle longer than 30 sec
                } catch ( InterruptedException ex ) {
                    // wait again :-)
                }
            }
        }

        public synchronized void shutdown() {
            this.shutdown = true;
            notifyAll();
        }
    }
}
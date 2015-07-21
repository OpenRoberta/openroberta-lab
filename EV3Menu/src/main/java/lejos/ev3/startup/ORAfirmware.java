package lejos.ev3.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ORAfirmware implements HttpHandler {

    private static final String libDir = "/home/roberta/lib";
    private static final String menuDir = "/home/root/lejos/bin/utils";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String filename = exchange.getRequestHeaders().getFirst("Filename");
        String dir = null;
        if ( filename.equals("EV3Menu.jar") ) {
            dir = menuDir;
        } else {
            dir = libDir;
        }

        InputStream is = exchange.getRequestBody();
        int n;
        byte[] buffer = new byte[4096];
        FileOutputStream fos = new FileOutputStream(new File(dir, filename));
        while ( (n = is.read(buffer)) != -1 ) {
            fos.write(buffer, 0, n);
        }
        fos.flush();
        fos.close();

        JSONObject response = new JSONObject();
        response.put("Notify", "OK");
        exchange.sendResponseHeaders(200, response.toString().getBytes().length);
        exchange.getResponseBody().write(response.toString().getBytes());
        exchange.close();

    }

}

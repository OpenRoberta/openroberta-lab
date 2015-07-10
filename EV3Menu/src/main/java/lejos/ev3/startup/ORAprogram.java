package lejos.ev3.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ORAprogram implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        for ( String key : exchange.getRequestHeaders().keySet() ) {
            System.out.println(key + ": " + exchange.getRequestHeaders().getFirst(key));
        }
        //String filename = exchange.getRequestHeaders().getFirst("Filename");

        InputStream is = exchange.getRequestBody();
        String filename = "chromeapptest.jar";
        int n;
        byte[] buffer = new byte[4096];
        FileOutputStream fos = new FileOutputStream(new File(ORAdownloader.PROGRAMS_DIRECTORY, filename));
        while ( (n = is.read(buffer)) != -1 ) {
            fos.write(buffer, 0, n);
        }
        fos.flush();
        fos.close();
    }

}

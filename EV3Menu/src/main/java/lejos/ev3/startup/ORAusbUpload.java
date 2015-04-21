package lejos.ev3.startup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.utility.Delay;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ORAusbUpload implements HttpHandler {

    private final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    @Override
    public void handle(HttpExchange exchange) {
        InputStream is = null;
        FileOutputStream fos = null;

        String fileName = "";

        String requestMethod = exchange.getRequestMethod();
        if ( requestMethod.equalsIgnoreCase("POST") ) {
            String raw = exchange.getRequestHeaders().getFirst("Content-Disposition");
            System.out.println(raw);
            try {
                is = exchange.getRequestBody();
                byte[] buffer = new byte[4096];
                int n;

                if ( raw != null && raw.indexOf("=") != -1 ) {
                    fileName = raw.substring(raw.indexOf("=") + 1);
                } else {
                    fileName = "unknown.jar";
                }

                fos = new FileOutputStream(new File(this.PROGRAMS_DIRECTORY, fileName));
                while ( (n = is.read(buffer)) != -1 ) {
                    fos.write(buffer, 0, n);
                }

                Delay.msDelay(1000);
                // TODO check if response is needed

                ORAlauncher launcher = new ORAlauncher();
                launcher.runProgram(fileName);
            } catch ( IOException e ) {
                System.out.println("OR Lab File not downloaded (USB)!");
            } finally {
                try {
                    if ( is != null ) {
                        is.close();
                    }
                    if ( fos != null ) {
                        fos.close();
                    }
                } catch ( IOException e ) {
                    // ok
                }
            }
        }

    }
}

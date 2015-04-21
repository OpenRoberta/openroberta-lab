package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ORAusbCmd implements HttpHandler {

    public static final String KEY_CMD = "cmd";
    public static final String KEY_REG = "reg";

    private final static String CMD_GETDATA = "getdata";

    private final static String CMD_REG = "register";
    private final static String CMD_DEREG = "deregister";

    @Override
    public void handle(HttpExchange exchange) {
        BufferedReader br = null;
        OutputStream os = null;
        try {
            String requestMethod = exchange.getRequestMethod();
            if ( requestMethod.equalsIgnoreCase("POST") ) {
                System.out.println(exchange.getRequestHeaders().getFirst("Content-Type"));

                br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                StringBuilder requestBuilder = new StringBuilder();
                String requestString;
                while ( (requestString = br.readLine()) != null ) {
                    requestBuilder.append(requestString);
                }
                JSONObject requestBody = new JSONObject(requestBuilder.toString());

                String command = requestBody.getString(KEY_CMD);
                System.out.println("ORA cmd from USB program: " + command);
                switch ( command ) {
                    case CMD_GETDATA:
                        JSONObject data = getData();
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, data.toString().length());
                        os = exchange.getResponseBody();
                        os.write(data.toString().getBytes("UTF-8"));
                        os.flush();
                        break;
                    default:
                        break;
                }
                command = requestBody.getString(KEY_REG);
                System.out.println("ORA register cmd from USB program: " + command);
                switch ( command ) {
                    case CMD_REG:
                        ORAhandler.setRegistered(true);
                        break;
                    case CMD_DEREG:
                        ORAhandler.setRegistered(false);
                        break;
                }
            }
        } catch ( IOException e ) {
            System.out.println("OR Lab HttpExchange broken (USB)!");
        } finally {
            try {
                if ( br != null ) {
                    br.close();
                }
                if ( os != null ) {
                    os.close();
                }
            } catch ( IOException e ) {
                // ok
            }
        }
    }

    private JSONObject getData() {
        JSONObject brickData = new JSONObject();
        brickData.put("macaddr", "usb");
        brickData.put("menuversion", GraphicStartup.getORAmenuVersion());
        brickData.put("lejosversion", GraphicStartup.getLejosVersion());
        brickData.put("battery", GraphicStartup.getBatteryStatus());
        brickData.put("brickname", GraphicStartup.getBrickName());
        // cmd, token are added in usb program, for a correct request at rest service
        return brickData;
    }

}

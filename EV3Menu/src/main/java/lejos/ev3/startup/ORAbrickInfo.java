package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ORAbrickInfo implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder builder = new StringBuilder();
        String str;
        while ( (str = br.readLine()) != null ) {
            builder.append(str);
        }
        JSONObject content = new JSONObject(builder.toString());
        System.out.println(content);

        String cmd = content.getString(ORApushCmd.KEY_CMD);
        System.out.println(cmd);
        if ( cmd.equals(ORApushCmd.CMD_REPEAT) && ORAhandler.isRegistered() == false ) {
            ORAhandler.setRegistered(true);
            LocalEV3.get().getAudio().systemSound(Sounds.ASCENDING);
        }

        JSONObject response = new JSONObject();
        response.put(ORApushCmd.KEY_LEJOSVERSION, GraphicStartup.getLejosVersion());
        response.put(ORApushCmd.KEY_MENUVERSION, GraphicStartup.getORAmenuVersion());
        response.put(ORApushCmd.KEY_BRICKNAME, GraphicStartup.getBrickName());
        response.put(ORApushCmd.KEY_BATTERY, GraphicStartup.getBatteryStatus());
        response.put(ORApushCmd.KEY_MACADDR, "usb");

        System.out.println(response);
        System.out.println(response.toString().getBytes().length);

        exchange.sendResponseHeaders(200, response.toString().getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
        exchange.close();
    }
}

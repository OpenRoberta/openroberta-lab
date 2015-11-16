package de.fhg.iais.roberta.runtime.ev3;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Actors;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.sensor.ev3.ColorSensorMode;
import de.fhg.iais.roberta.shared.sensor.ev3.GyroSensorMode;
import de.fhg.iais.roberta.shared.sensor.ev3.InfraredSensorMode;
import de.fhg.iais.roberta.shared.sensor.ev3.MotorTachoMode;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.shared.sensor.ev3.UltrasonicSensorMode;

class ClientWebSocket extends WebSocketClient {

    private final String token;
    private final Ev3Configuration brickConfiguration;
    private final Hal hal;

    private final Map<SensorPort, EV3Sensor> sensors;
    private final Map<ActorPort, EV3Actor> actors;

    public ClientWebSocket(URI serverUri, String token, Ev3Configuration brickConfiguration, Hal hal) {
        super(serverUri, new Draft_17());
        this.token = token;
        this.brickConfiguration = brickConfiguration;
        this.hal = hal;

        this.sensors = this.brickConfiguration.getSensors();
        this.actors = this.brickConfiguration.getActors();
    }

    /**
     * Call in a separate thread for sending sensor values to Open Roberta Lab via websocket.
     * The thread should be interrupted from the NEPO program to stop logging.
     */
    public void logSensorValues() {
        while ( !Thread.currentThread().isInterrupted() ) {
            try {
                switch ( this.getReadyState() ) {
                    case NOT_YET_CONNECTED:
                        this.connectBlocking();
                        break;
                    case CONNECTING:
                        // do nothing
                        break;
                    case OPEN:
                        this.sendSensorValue();
                        Thread.sleep(900);
                        break;
                    case CLOSING:
                        // do nothing
                        break;
                    case CLOSED:
                        this.connectBlocking();
                        break;
                    default:
                        break;
                }
                Thread.sleep(100);
            } catch ( InterruptedException ie ) {
                // ok
            } catch ( Exception e ) {
                // connection is not necessarily stable.
                // Attempt to reconnect in the next iteration if connection is lost.
            }
        }
    };

    /**
     * Write sensor values to the websocket.
     */
    private void sendSensorValue() {
        JSONObject sensorvalues = new JSONObject();
        sensorvalues.put("token", this.token);

        getSensorDebugInfo(sensorvalues);
        getActorsDebugInfo(sensorvalues);

        this.send(sensorvalues.toString());
    }

    private void getActorsDebugInfo(JSONObject sensorvalues) {
        for ( Entry<ActorPort, EV3Actor> mapEntry : this.actors.entrySet() ) {
            int hardwareId = mapEntry.getValue().getComponentType().hashCode();
            ActorPort port = mapEntry.getKey();
            String partKey = port.name() + "-";

            partKey +=
                EV3Actors.EV3_LARGE_MOTOR.hashCode() == hardwareId ? EV3Actors.EV3_LARGE_MOTOR.getShortName() : EV3Actors.EV3_MEDIUM_MOTOR.getShortName();

            if ( this.brickConfiguration.isMotorRegulated(port) ) {
                sensorvalues.put(partKey + MotorTachoMode.DEGREE, this.hal.getRegulatedMotorTachoValue(port, MotorTachoMode.DEGREE));
            } else {
                sensorvalues.put(partKey + MotorTachoMode.DEGREE, this.hal.getUnregulatedMotorTachoValue(port, MotorTachoMode.DEGREE));
            }

        }
    }

    private void getSensorDebugInfo(JSONObject sensorvalues) {
        for ( Entry<SensorPort, EV3Sensor> mapEntry : this.sensors.entrySet() ) {
            int hardwareId = mapEntry.getValue().getComponentType().hashCode();
            SensorPort port = mapEntry.getKey();
            String partKey = port.name() + "-";

            if ( EV3Sensors.EV3_COLOR_SENSOR.hashCode() == hardwareId ) {
                partKey += EV3Sensors.EV3_COLOR_SENSOR.getShortName() + "-";
                sensorvalues.put(partKey + ColorSensorMode.AMBIENTLIGHT, this.hal.getColorSensorAmbient(port));
                sensorvalues.put(partKey + ColorSensorMode.COLOUR, this.hal.getColorSensorColour(port));
                sensorvalues.put(partKey + ColorSensorMode.RED, this.hal.getColorSensorRed(port));
                sensorvalues.put(partKey + ColorSensorMode.RGB, this.hal.getColorSensorRgb(port));
            } else if ( EV3Sensors.EV3_ULTRASONIC_SENSOR.hashCode() == hardwareId ) {
                partKey += EV3Sensors.EV3_ULTRASONIC_SENSOR.getShortName();
                sensorvalues.put(partKey + UltrasonicSensorMode.DISTANCE, this.hal.getUltraSonicSensorDistance(port));
                sensorvalues.put(partKey + UltrasonicSensorMode.PRESENCE, this.hal.getUltraSonicSensorPresence(port));
            } else if ( EV3Sensors.EV3_GYRO_SENSOR.hashCode() == hardwareId ) {
                partKey += EV3Sensors.EV3_GYRO_SENSOR.getShortName();
                sensorvalues.put(partKey + GyroSensorMode.ANGLE, this.hal.getGyroSensorValue(port, GyroSensorMode.ANGLE));
                sensorvalues.put(partKey + GyroSensorMode.RATE, this.hal.getGyroSensorValue(port, GyroSensorMode.RATE));
            } else if ( EV3Sensors.EV3_IR_SENSOR.hashCode() == hardwareId ) {
                partKey += EV3Sensors.EV3_IR_SENSOR.getShortName();
                sensorvalues.put(partKey + InfraredSensorMode.DISTANCE, this.hal.getInfraredSensorDistance(port));
                sensorvalues.put(partKey + InfraredSensorMode.SEEK, this.hal.getInfraredSensorSeek(port));
            } else if ( EV3Sensors.EV3_TOUCH_SENSOR.hashCode() == hardwareId ) {
                partKey += EV3Sensors.EV3_TOUCH_SENSOR.getShortName();
                sensorvalues.put(partKey, this.hal.isPressed(port));
            }
        }
    }

    /**
     * Send a message to the server which block is currently active and add token to it.
     *
     * @param uniqueBlockID
     */
    public void sendActiveBlockInfo(String uniqueBlockID) {
        if ( this.getReadyState() == READYSTATE.OPEN ) {
            JSONObject debugMsg = new JSONObject();
            debugMsg.put("token", this.token);
            debugMsg.put("activeblock", uniqueBlockID);
            this.send(debugMsg.toString());
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Receive: " + message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected " + code + " " + reason);
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }
}

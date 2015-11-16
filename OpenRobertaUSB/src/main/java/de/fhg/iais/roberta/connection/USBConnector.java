package de.fhg.iais.roberta.connection;

import java.io.IOException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import de.fhg.iais.roberta.util.ORAtokenGenerator;

public class USBConnector extends Observable implements Runnable, Connector {

    private String brickIp = "10.0.1.1";
    private String serverIp = "localhost";
    private String serverPort = "1999";
    private final String serverAddress;

    private final EV3Communicator ev3comm;
    private final ServerCommunicator servcomm;
    private final ORAtokenGenerator tokenGenerator;

    private JSONObject brickData = null;

    private static final String KEY_TOKEN = "token";
    public static final String KEY_CMD = "cmd";

    private static final String CMD_REGISTER = "register";
    private static final String CMD_PUSH = "push";
    private static final String CMD_ISRUNNING = "isrunning";

    // cmds receive from server
    private static final String CMD_REPEAT = "repeat";
    private static final String CMD_ABORT = "abort";
    private static final String CMD_UPDATE = "update";
    private static final String CMD_DOWNLOAD = "download";
    private static final String CMD_CONFIGURATION = "configuration";

    private static Logger log = Logger.getLogger("Connector");

    public enum State {
        DISCOVER,
        WAIT_FOR_CONNECT,
        CONNECT,
        WAIT_FOR_CMD,
        WAIT_EXECUTION,
        DISCONNECT,
        WAIT_FOR_SERVER,
        UPDATE,
        UPDATE_SUCCESS,
        UPDATE_FAIL,
        ERROR_HTTP,
        ERROR_UPDATE,
        ERROR_BRICK,
        TOKEN_TIMEOUT
    }

    private State state = State.DISCOVER; // First state when program starts
    private String token = "";
    private boolean userDisconnect = false;

    private final String[] fwfiles = {
        "runtime",
        "shared",
        "jsonlib",
        "ev3menu"
    };

    public USBConnector(ResourceBundle serverProps) {
        if ( serverProps != null ) {
            this.brickIp = serverProps.getString("brickIp");
            this.serverIp = serverProps.getString("serverIp");
            this.serverPort = serverProps.getString("serverPort");
        }
        this.serverAddress = this.serverIp + ":" + this.serverPort;

        log.config("Robot ip " + this.brickIp);
        log.config("Server address " + this.serverAddress);

        this.ev3comm = new EV3Communicator(this.brickIp);
        this.servcomm = new ServerCommunicator(this.serverAddress);
        this.tokenGenerator = new ORAtokenGenerator();
    }

    @Override
    public void run() {
        while ( true ) {
            switch ( this.state ) {
                case DISCOVER:
                    try {
                        switch ( this.ev3comm.checkBrickState(CMD_ISRUNNING) ) {
                            case "true": // program is running
                                break;
                            case "false": // brick available and no program running
                                this.state = State.WAIT_FOR_CONNECT;
                                break;
                        }
                        Thread.sleep(1000);
                    } catch ( IOException e ) {
                        // ok
                    } catch ( InterruptedException e ) {
                        // ok
                    }
                    notifyConnectionStateChanged(this.state);
                    break;
                case WAIT_EXECUTION:
                    this.state = State.WAIT_EXECUTION;
                    notifyConnectionStateChanged(this.state);
                    try {
                        switch ( this.ev3comm.checkBrickState(CMD_ISRUNNING) ) {
                            case "true": // program is running
                                this.state = State.WAIT_EXECUTION;
                                notifyConnectionStateChanged(this.state);
                                break;
                            case "false": // brick available and no program running
                                this.state = State.WAIT_FOR_CMD;
                                notifyConnectionStateChanged(this.state);
                                break;
                        }
                        Thread.sleep(1000);
                    } catch ( IOException e ) {
                        // ok
                    } catch ( InterruptedException e ) {
                        // ok
                    }
                    break;
                case WAIT_FOR_CONNECT:
                    try {
                        switch ( this.ev3comm.checkBrickState(CMD_ISRUNNING) ) {
                            case "true":
                                this.state = State.DISCOVER;
                                notifyConnectionStateChanged(State.DISCOVER);
                                break;
                            case "false":
                                // wait for user
                                break;
                            default:
                                break;
                        }
                        Thread.sleep(1000);
                    } catch ( IOException brickerror ) {
                        this.state = State.DISCOVER;
                        notifyConnectionStateChanged(State.DISCOVER);
                    } catch ( InterruptedException e ) {
                        // ok
                    }
                    break;
                case CONNECT:
                    this.token = this.tokenGenerator.generateToken();
                    this.state = State.WAIT_FOR_SERVER;
                    notifyConnectionStateChanged(State.WAIT_FOR_SERVER);
                    try {
                        this.brickData = this.ev3comm.pushToBrick(CMD_REGISTER);
                        this.brickData.put(KEY_TOKEN, this.token);
                        this.brickData.put(KEY_CMD, CMD_REGISTER);
                    } catch ( IOException brickerror ) {
                        notifyConnectionStateChanged(State.ERROR_BRICK);
                        notifyConnectionStateChanged(State.DISCOVER);
                        this.state = State.DISCOVER;
                        break;
                    }
                    try {
                        JSONObject serverResponse = this.servcomm.pushRequest(this.brickData);
                        String command = serverResponse.getString("cmd");
                        switch ( command ) {
                            case CMD_REPEAT:
                                try {
                                    this.brickData = this.ev3comm.pushToBrick(CMD_REPEAT);
                                } catch ( IOException brickerror ) {
                                    notifyConnectionStateChanged(State.ERROR_BRICK);
                                    notifyConnectionStateChanged(State.DISCOVER);
                                    this.state = State.DISCOVER;
                                }
                                this.state = State.WAIT_FOR_CMD;
                                notifyConnectionStateChanged(State.WAIT_FOR_CMD);
                                break;
                            case CMD_ABORT:
                                notifyConnectionStateChanged(State.TOKEN_TIMEOUT);
                                notifyConnectionStateChanged(State.DISCOVER);
                                this.state = State.DISCOVER;
                                break;
                            default:
                                log.info("Command " + command + " unknown");
                                notifyConnectionStateChanged(State.DISCOVER);
                                this.state = State.DISCOVER;
                                break;
                        }
                    } catch ( IOException servererror ) {
                        if ( !this.userDisconnect ) {
                            notifyConnectionStateChanged(State.ERROR_HTTP);
                            notifyConnectionStateChanged(State.DISCOVER);
                        }
                        this.state = State.DISCOVER;
                        this.userDisconnect = false;
                    }
                    break;
                case WAIT_FOR_CMD:
                    try {
                        this.brickData = this.ev3comm.pushToBrick(CMD_REPEAT);
                        this.brickData.put(KEY_TOKEN, this.token);
                        this.brickData.put(KEY_CMD, CMD_PUSH);
                    } catch ( IOException e ) {
                        notifyConnectionStateChanged(State.ERROR_BRICK);
                        notifyConnectionStateChanged(State.DISCOVER);
                        this.state = State.DISCOVER;
                        break;
                    }
                    String command = "default";
                    try {
                        command = this.servcomm.pushRequest(this.brickData).getString(KEY_CMD);
                    } catch ( IOException | JSONException servererror ) {
                        // continue to default block
                    }
                    switch ( command ) {
                        case CMD_REPEAT:
                            break;
                        case CMD_ABORT:
                            try {
                                this.ev3comm.disconnectBrick();
                            } catch ( IOException brickerror ) {
                                notifyConnectionStateChanged(State.ERROR_BRICK);
                            }
                            this.state = State.DISCOVER;
                            notifyConnectionStateChanged(this.state);
                            break;
                        case CMD_UPDATE:
                            log.info("Execute firmware update");
                            try {
                                for ( int i = 0; i < this.fwfiles.length; i++ ) {
                                    byte[] binaryfile = this.servcomm.downloadFirmwareFile(this.brickData, this.fwfiles[i]);
                                    this.ev3comm.uploadFirmwareFile(binaryfile, this.servcomm.getFilename());
                                }
                                this.ev3comm.restartBrick();
                                log.info("Firmware update successful. Restarting EV3 now!");
                                this.state = State.DISCOVER;
                                notifyConnectionStateChanged(this.state);
                                try {
                                    Thread.sleep(3000);
                                } catch ( InterruptedException e ) {
                                    // ok;
                                }
                            } catch ( IOException e ) {
                                notifyConnectionStateChanged(State.ERROR_UPDATE);
                                notifyConnectionStateChanged(State.WAIT_FOR_CMD);
                            }
                            break;
                        case CMD_DOWNLOAD:
                            log.info("Download user program");
                            try {
                                byte[] binaryfile = this.servcomm.downloadProgram(this.brickData);
                                String filename = this.servcomm.getFilename();
                                this.ev3comm.uploadProgram(binaryfile, filename);
                                this.state = State.WAIT_EXECUTION;
                            } catch ( IOException e ) {
                                // do not give up the brick, try another push request
                                // user has to click on run button again
                                this.state = State.WAIT_FOR_CMD;
                            }
                            break;
                        case CMD_CONFIGURATION:
                            break;
                        default:
                            if ( !this.userDisconnect ) {
                                log.warning("Command " + command + " unknown");
                            } else {
                                notifyConnectionStateChanged(State.DISCOVER);
                                this.state = State.DISCOVER;
                                this.userDisconnect = false;
                            }
                            break;
                    }
                default:
                    break;
            }
        }
    }

    @Override
    public void update() {
        this.state = State.UPDATE;
    }

    @Override
    public void connect() {
        this.state = State.CONNECT;
    }

    @Override
    public void disconnect() {
        this.userDisconnect = true;
        try {
            this.ev3comm.disconnectBrick();
        } catch ( IOException e ) {
            // ok
        }
        this.servcomm.disconnect();
        notifyConnectionStateChanged(State.DISCOVER);
        this.state = State.DISCOVER;
    }

    @Override
    public void notifyConnectionStateChanged(State state) {
        setChanged();
        notifyObservers(state);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getBrickName() {
        String brickname = this.brickData.getString("brickname");
        if ( brickname != null ) {
            return brickname;
        } else {
            return "";
        }
    }

    public State getConnectionState() {
        return this.state;
    }

    @Override
    public void close() {
        disconnect();
        this.servcomm.shutdown();
        this.ev3comm.shutdown();
    }
}

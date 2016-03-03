package de.fhg.iais.roberta.connection;

public interface Connector {

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

    static final String KEY_TOKEN = "token";
    public static final String KEY_CMD = "cmd";

    static final String CMD_REGISTER = "register";
    static final String CMD_PUSH = "push";
    static final String CMD_ISRUNNING = "isrunning";

    static final String CMD_REPEAT = "repeat";
    static final String CMD_ABORT = "abort";
    static final String CMD_UPDATE = "update";
    static final String CMD_DOWNLOAD = "download";
    static final String CMD_CONFIGURATION = "configuration";

    public void connect();

    public void disconnect();

    public void close();

    public void notifyConnectionStateChanged(State state);

    public String getToken();

    public String getBrickName();

    public void update();

    public void updateCustomServerAddress(String customServerAddress);

    public void resetToDefaultServerAddress();
}

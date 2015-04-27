package Connection;

import Connection.USBConnector.State;

public interface Connector {
    public void connect();

    public void disconnect();

    public void close();

    public void notifyConnectionStateChanged(State state);

    public String getToken();

    public String getBrickName();

    public String getBrickBatteryVoltage();
}

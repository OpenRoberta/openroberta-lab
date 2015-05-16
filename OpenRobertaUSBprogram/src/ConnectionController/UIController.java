package ConnectionController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

import Connection.Connector;
import Connection.USBConnector;
import Connection.USBConnector.State;
import ConnectionViews.ConnectionView;
import ConnectionViews.ORAPopup;

public class UIController<ObservableObject> implements Observer {

    private Connector connector;
    private ConnectionView conView;
    private boolean connected;
    private ResourceBundle rb;
    private static Logger log = Logger.getLogger("Connector");

    public UIController(USBConnector usbCon, ConnectionView conView, ResourceBundle rb) {
        this.connector = usbCon;
        this.conView = conView;
        this.rb = rb;
        this.connected = false;
        addListener();
    }

    public void control() {
        this.conView.setVisible(true);
    }

    private void addListener() {
        this.conView.setConnectActionListener(new ConnectActionListener());
        this.conView.setCloseListener(new CloseListener());
        ((Observable) this.connector).addObserver(this);
    }

    public class ConnectActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton b = (AbstractButton) e.getSource();
            if ( b.getActionCommand() == "close" ) {
                log.info("User close");
                closeApplication();
            } else if ( b.getActionCommand() == "about" ) {
                log.info("User about");
                showAboutPopup();
            } else {
                if ( b.isSelected() ) {
                    log.info("User connect");
                    connector.connect();
                    b.setText(rb.getString("disconnect"));
                } else {
                    log.info("User disconnect");
                    connector.disconnect();
                    b.setText(rb.getString("connect"));
                }
            }
        }
    }

    public class CloseListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            log.info("User close");
            closeApplication();
        }

    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    // TODO Maybe expose the temp directory from USBConnector to delete the temporary files before closing the application.
    public void closeApplication() {
        if ( this.connected ) {
            String[] buttons = {
                rb.getString("close"), rb.getString("cancel")
            };
            int n =
                ORAPopup.showPopup(
                    conView,
                    rb.getString("attention"),
                    rb.getString("confirmCloseInfo"),
                    new ImageIcon(getClass().getClassLoader().getResource("resources/Roberta.png")),
                    buttons);
            if ( n == 0 ) {
                connector.close();
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        State state = (State) arg1;
        switch ( state ) {
            case WAIT_FOR_CONNECT:
                //this.conView.setNew(this.connector.getBrickName());
                this.conView.setWaitForConnect();
                break;
            case WAIT_FOR_SERVER:
                this.conView.setNew(this.connector.getToken());
                break;
            case WAIT_FOR_CMD:
                this.connected = true;
                this.conView.setNew(this.connector.getBrickName());
                this.conView.setWaitForCmd(this.connector.getBrickBatteryVoltage());
                break;
            case DISCOVER:
                this.connected = false;
                this.conView.setDiscover();
                break;
            case DISCOVER_CONNECTED:
                this.conView.setDiscoverConnected();
                break;
            case DISCOVER_UPDATE:
                if ( ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("updateInfo"), null) == 0 ) {
                    connector.update();
                }
                break;
            case UPDATE_SUCCESS:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("restartInfo"), null);
                break;
            case UPDATE_FAIL:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("updateFail"), null);
                break;
            case ERROR_HTTP:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("httpErrorInfo"), null);
                break;
            case ERROR_BRICK:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("httpBrickInfo"), null);
                break;
            default:
                break;
        }
    }

    private void showAboutPopup() {
        ORAPopup.showPopup(
            conView,
            rb.getString("about"),
            rb.getString("aboutInfo"),
            new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/iais_logo.gif")).getImage().getScaledInstance(
                100,
                27,
                java.awt.Image.SCALE_AREA_AVERAGING)));
    }
}

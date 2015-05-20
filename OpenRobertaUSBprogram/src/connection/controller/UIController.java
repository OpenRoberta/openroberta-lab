package connection.controller;

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

import connection.views.ConnectionView;
import connection.views.ORAPopup;
import connectionEV3.Connector;
import connectionEV3.USBConnector;
import connectionEV3.USBConnector.State;

public class UIController<ObservableObject> implements Observer {

    private final Connector connector;
    private final ConnectionView conView;
    private boolean connected;
    private final ResourceBundle rb;
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
                    UIController.this.connector.connect();
                    b.setText(UIController.this.rb.getString("disconnect"));
                } else {
                    log.info("User disconnect");
                    UIController.this.connector.disconnect();
                    b.setText(UIController.this.rb.getString("connect"));
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
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    // TODO Maybe expose the temp directory from USBConnector to delete the temporary files before closing the application.
    public void closeApplication() {
        if ( this.connected ) {
            String[] buttons = {
                this.rb.getString("close"), this.rb.getString("cancel")
            };
            int n =
                ORAPopup.showPopup(this.conView, this.rb.getString("attention"), this.rb.getString("confirmCloseInfo"), new ImageIcon(getClass()
                    .getClassLoader()
                    .getResource("resources/Roberta.png")), buttons);
            if ( n == 0 ) {
                this.connector.close();
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
                this.conView.setNew(this.rb.getString("token") + " " + this.connector.getToken());
                break;
            case WAIT_FOR_CMD:
                this.connected = true;
                this.conView.setNew(this.rb.getString("name") + " " + this.connector.getBrickName());
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
                if ( ORAPopup.showPopup(this.conView, this.rb.getString("attention"), this.rb.getString("updateInfo"), null) == 0 ) {
                    this.connector.update();
                }
                break;
            case UPDATE_SUCCESS:
                ORAPopup.showPopup(this.conView, this.rb.getString("attention"), this.rb.getString("restartInfo"), null);
                break;
            case UPDATE_FAIL:
                ORAPopup.showPopup(this.conView, this.rb.getString("attention"), this.rb.getString("updateFail"), null);
                break;
            case ERROR_HTTP:
                ORAPopup.showPopup(this.conView, this.rb.getString("attention"), this.rb.getString("httpErrorInfo"), null);
                break;
            case ERROR_BRICK:
                ORAPopup.showPopup(this.conView, this.rb.getString("attention"), this.rb.getString("httpBrickInfo"), null);
                break;
            case TOKEN_TIMEOUT:
                ORAPopup.showPopup(this.conView, this.rb.getString("attention"), this.rb.getString("tokenTimeout"), null);
            default:
                break;
        }
    }

    private void showAboutPopup() {
        ORAPopup.showPopup(this.conView, this.rb.getString("about"), this.rb.getString("aboutInfo"), new ImageIcon(new ImageIcon(getClass()
            .getClassLoader()
            .getResource("resources/iais_logo.gif")).getImage().getScaledInstance(100, 27, java.awt.Image.SCALE_AREA_AVERAGING)));
    }
}

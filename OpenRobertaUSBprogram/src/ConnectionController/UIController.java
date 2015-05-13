package ConnectionController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

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

    public enum errorTypes {
        UPDATE, RESTART, UPDATESUCCESS, UPDATEFAIL, HTTP
    }

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
                closeApplication();
            } else if ( b.getActionCommand() == "about" ) {
                showAboutPopup();
            } else {
                if ( b.isSelected() ) {
                    connector.connect();
                    b.setText(rb.getString("disconnect"));
                } else {
                    connector.disconnect();
                    b.setText(rb.getString("connect"));
                }
            }
        }
    }

    public class CloseListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
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
                showErrorPopup(errorTypes.UPDATE);
                break;
            case FORCEUPDATE:
                showErrorPopup(errorTypes.UPDATE);
                break;
            case UPDATE_SUCCESS:
                showErrorPopup(errorTypes.UPDATESUCCESS);
                break;
            case UPDATE_FAIL:
                showErrorPopup(errorTypes.UPDATEFAIL);
                break;
            case ERROR_HTTP:
                showErrorPopup(errorTypes.HTTP);
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

    private void showErrorPopup(errorTypes type) {
        switch ( type ) {
            case HTTP:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("httpErrorInfo"), null);
                break;
            case UPDATE:
                if ( ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("updateInfo"), null) == 0 ) {
                    connector.update();
                }
                break;
            case RESTART:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("restartInfo"), null);
                break;
            case UPDATEFAIL:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("updateFail"), null);
                break;
            case UPDATESUCCESS:
                ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("updateSuccess"), null);
                break;
        }
//        if ( type.equals("http") ) {
//            ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("httpErrorInfo"), null);
//        } else if ( type.equals("update") ) {
//            if ( ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("updateInfo"), null) == 0 ) {
//                connector.update();
//            }
//        } else if ( type.equals("restart") ) {
//            ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("restartInfo"), null);
//        } else if ( type.equals("updateFail") ) {
//            ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("updateFail"), null);
//        } else {
//            ORAPopup.showPopup(conView, rb.getString("attention"), rb.getString("httpBrickInfo"), null);
//        }
    }
}

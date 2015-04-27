package ConnectionController;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import Connection.Connector;
import Connection.USBConnector;
import Connection.USBConnector.State;
import ConnectionViews.ConnectionView;

public class UIController<ObservableObject> implements Observer {

    private Connector connector;
    private ConnectionView conView;
    private boolean connected;
    private ResourceBundle rb;

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

    public void closeApplication() {
        if ( connected ) {
            String close = rb.getString("close");
            String cancel = rb.getString("cancel");
            Object[] options = {
                close, cancel
            };
            UIManager.put("OptionPane.background", Color.white);
            UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
            UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
            UIManager.put("OptionPane.buttonBackground", Color.red);
            UIManager.put("Panel.background", Color.white);
            UIManager.put("Panel.button", Color.white);
            int n =
                JOptionPane.showOptionDialog(
                    conView,
                    rb.getString("confirmCloseInfo"),
                    rb.getString("attention"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon(getClass().getClassLoader().getResource("./resources/Roberta.png")),
                    options,
                    options[0]);
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
                this.conView.setNew(this.connector.getBrickName());
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
            case ERROR_HTTP:
                showErrorPopup();
                break;
            default:
                break;
        }
    }

    private void showErrorPopup() {
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonBackground", Color.red);
        UIManager.put("Panel.background", Color.white);
        UIManager.put("Panel.button", Color.white);
        JOptionPane.showMessageDialog(conView, rb.getString("httpErrorInfo"), rb.getString("attention"), JOptionPane.ERROR_MESSAGE);
    }
}

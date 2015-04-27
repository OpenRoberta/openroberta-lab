package ConnectionMain;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import Connection.USBConnector;
import ConnectionController.UIController;
import ConnectionViews.ConnectionView;

public class USBConnectionMain {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ResourceBundle messages = getLocals();
                ResourceBundle serverProbs = getServerProps();
                USBConnector usbCon = new USBConnector(serverProbs);
                Thread thread = new Thread(usbCon, "USBConnector");
                thread.start();
                ConnectionView view = new ConnectionView(messages);
                UIController<?> controller = new UIController<Object>(usbCon, view, messages);
                controller.control();
            }

            private ResourceBundle getServerProps() {
                return ResourceBundle.getBundle("resources/server");
            }

            private ResourceBundle getLocals() {
                ResourceBundle rb;
                try {
                    rb = ResourceBundle.getBundle("resources/messages", Locale.getDefault());
                } catch ( Exception e ) {
                    rb = ResourceBundle.getBundle("resources/messages", Locale.ENGLISH);
                }
                return rb;
            }
        });
    }
}

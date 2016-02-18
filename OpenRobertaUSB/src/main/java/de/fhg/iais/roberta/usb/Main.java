package de.fhg.iais.roberta.usb;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang3.SystemUtils;

import de.fhg.iais.roberta.connection.EV3USBConnector;
import de.fhg.iais.roberta.ui.ConnectionView;
import de.fhg.iais.roberta.ui.UIController;
import de.fhg.iais.roberta.util.ORAFormatter;

public class Main {

    private static final String LOGFILENAME = "OpenRobertaUSB.log";
    private static Logger log = Logger.getLogger("Connector");
    private static ConsoleHandler consoleHandler = new ConsoleHandler();
    private static FileHandler fileHandler = null;

    private static File logFile = null;

    public static void main(String[] args) {

        configureLogger();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                prepareUI();
                ResourceBundle messages = getLocals();
                ResourceBundle serverProps = getServerProps();
                EV3USBConnector ev3usbcon = new EV3USBConnector(serverProps);

                ConnectionView view = new ConnectionView(messages);
                UIController<?> controller = new UIController<Object>(ev3usbcon, view, messages);
                controller.control();
                Thread thread = new Thread(ev3usbcon);
                thread.start();
            }

            private void prepareUI() {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                UIManager.put("MenuBar.background", Color.white);
                UIManager.put("Menu.background", Color.white);
                UIManager.put("Menu.selectionBackground", Color.decode("#afca04"));
                UIManager.put("MenuItem.background", Color.white);
                UIManager.put("MenuItem.selectionBackground", Color.decode("#afca04"));
                UIManager.put("MenuItem.focus", Color.decode("#afca04"));
                UIManager.put("Menu.foreground", Color.decode("#333333"));
                UIManager.put("Menu.Item.foreground", Color.decode("#333333"));
                UIManager.put("Menu.font", new Font("Arial", Font.PLAIN, 12));
                UIManager.put("MenuItem.foreground", Color.decode("#333333"));
                UIManager.put("MenuItem.font", new Font("Arial", Font.PLAIN, 12));
            }

            private ResourceBundle getServerProps() {
                return ResourceBundle.getBundle("OpenRobertaUSB");
            }

            private ResourceBundle getLocals() {
                ResourceBundle rb;
                try {
                    rb = ResourceBundle.getBundle("messages", Locale.getDefault());
                } catch ( Exception e ) {
                    rb = ResourceBundle.getBundle("messages", Locale.ENGLISH);
                }
                log.config("Language " + rb.getLocale());
                return rb;
            }
        });
    }

    public static void stopFileLogger() {
        fileHandler.flush();
        fileHandler.close();
    }

    private static void configureLogger() {
        String path = "";
        try {
            if ( SystemUtils.IS_OS_WINDOWS ) {
                path = System.getenv("APPDATA");
            } else if ( SystemUtils.IS_OS_LINUX ) {
                path = System.getProperty("user.home");
            } else if ( SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX ) {
                // TODO
            }
            logFile = new File(path, "OpenRobertaUSB");
            if ( !logFile.exists() ) {
                logFile.mkdir();
            }
            fileHandler = new FileHandler(new File(logFile, LOGFILENAME).getPath(), false);
            fileHandler.setFormatter(new ORAFormatter());
            fileHandler.setLevel(Level.ALL);
        } catch ( SecurityException | IOException e ) {
            // ok
        }
        consoleHandler.setFormatter(new ORAFormatter());
        consoleHandler.setLevel(Level.ALL);
        log.setLevel(Level.ALL);
        log.addHandler(consoleHandler);
        log.addHandler(fileHandler);
        log.setUseParentHandlers(false);
        log.info("Logging to file: " + new File(logFile, LOGFILENAME).getPath().toString());
    }
}

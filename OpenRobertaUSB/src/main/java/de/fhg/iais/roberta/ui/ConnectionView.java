package de.fhg.iais.roberta.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import de.fhg.iais.roberta.ui.UIController.CloseListener;
import de.fhg.iais.roberta.ui.UIController.ConnectActionListener;

public class ConnectionView extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JMenuBar menu = new JMenuBar();
    private final JMenu mnFile = new JMenu();
    private final JMenuItem mntClose = new JMenuItem();
    private final JMenu mnInfo = new JMenu();
    private final JMenuItem mntAbout = new JMenuItem();
    private final JLabel lblRobot = new JLabel();
    private final JLabel lblGif = new JLabel();
    private final ORAToggleButton butConnect = new ORAToggleButton();
    private final ORAButton butClose = new ORAButton();
    private final JTextArea txtInfo = new JTextArea();
    private final JPanel pnlToken = new JPanel();
    private final JTextField txtToken = new JTextField();
    private final JPanel pnlButton = new JPanel();
    private final JPanel centerPanel = new JPanel();
    private final ResourceBundle messages;
    private ImageIcon icoRobotNotDiscovered;
    private ImageIcon icoRobotConnected;
    private ImageIcon icoRobotDiscovered;
    private ImageIcon gifPlug;
    private ImageIcon gifConnect;
    private ImageIcon gifServer;
    private ImageIcon gifConnected;

    private boolean toggle = true;

    public ConnectionView(ResourceBundle messages) {
        this.messages = messages;
        createIcons();
        intGUI();
        this.setDiscover();
    }

    private void intGUI() {

        this.setSize(250, 400);
        this.setMinimumSize(new Dimension(300, 75));
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.menu.setForeground(Color.decode("#333333"));
        this.menu.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.mnFile.setText(this.messages.getString("file"));
        this.mnInfo.setText(this.messages.getString("info"));
        this.mntClose.setText(this.messages.getString("close"));
        this.mntClose.setActionCommand("close");
        this.mntAbout.setText(this.messages.getString("about"));
        this.mntAbout.setActionCommand("about");
        this.lblRobot.setIcon(this.icoRobotNotDiscovered);
        this.lblGif.setMinimumSize(new Dimension(100, 106));
        this.lblGif.setSize(new Dimension(100, 106));
        this.txtInfo.setLineWrap(true);
        this.txtInfo.setMinimumSize(new Dimension(500, 0));
        this.txtInfo.setWrapStyleWord(true);
        this.txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        this.txtInfo.setForeground(Color.decode("#333333"));
        this.txtInfo.setMargin(new Insets(16, 16, 16, 16));
        this.txtInfo.setEditable(false);
        this.pnlToken.setBorder(null);
        this.pnlToken.setBackground(Color.white);
        this.pnlToken.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.txtToken.setFont(new Font("Arial", Font.PLAIN, 18));
        this.txtToken.setEditable(false);
        this.txtToken.setBackground(Color.white);
        this.txtToken.setBorder(null);
        this.pnlButton.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        this.pnlButton.setBackground(Color.white);
        this.pnlButton.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.Y_AXIS));
        this.centerPanel.setAlignmentX(CENTER_ALIGNMENT);
        this.butConnect.setEnabled(false);
        this.butClose.setText(this.messages.getString("close"));
        this.butClose.setActionCommand("close");
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.add(this.menu, BorderLayout.NORTH);
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.decode("#dddddd"));
        this.centerPanel.add(sep);
        this.centerPanel.setBorder(null);
        this.centerPanel.add(this.pnlToken);
        JPanel a = new JPanel();
        a.add(this.lblGif);
        a.setBackground(Color.white);
        this.centerPanel.add(a);
        this.centerPanel.add(this.txtInfo);
        this.centerPanel.add(Box.createVerticalGlue());
        this.centerPanel.add(this.pnlButton);
        this.pnlButton.add(this.butConnect);
        this.pnlButton.add(Box.createHorizontalStrut(12));
        this.pnlButton.add(this.butClose);
        this.pnlToken.add(this.txtToken);
        this.menu.add(this.mnFile);
        this.menu.add(this.mnInfo);
        this.menu.add(Box.createHorizontalGlue());
        this.menu.add(this.lblRobot);
        this.mnFile.add(this.mntClose);
        this.mnInfo.add(this.mntAbout);
        this.setTitle("USBConnection");
        this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("OR.png")).getImage());
        this.txtInfo.setText(Locale.getDefault().getLanguage());
        this.setTitle(this.messages.getString("title"));
    }

    @SuppressWarnings("rawtypes")
    public void setConnectActionListener(ConnectActionListener connectListener) {
        this.butConnect.addActionListener(connectListener);
        this.mntClose.addActionListener(connectListener);
        this.mntAbout.addActionListener(connectListener);
        this.butClose.addActionListener(connectListener);
    }

    @SuppressWarnings("rawtypes")
    public void setCloseListener(CloseListener closeListener) {
        this.addWindowListener(closeListener);

    }

    public void setWaitForConnect() {
        this.lblRobot.setIcon(this.icoRobotDiscovered);
        this.butConnect.setEnabled(true);
        this.txtInfo.setText(this.messages.getString("connectInfo"));
        this.lblGif.setIcon(this.gifConnect);
    }

    public void setWaitExecution() {
        if ( this.toggle ) {
            this.lblRobot.setIcon(this.icoRobotConnected);
        } else {
            this.lblRobot.setIcon(this.icoRobotDiscovered);
        }
        this.toggle = !this.toggle;
    }

    public void setWaitForCmd() {
        this.butConnect.setEnabled(true);
        this.butConnect.setSelected(true);
        this.lblRobot.setIcon(this.icoRobotConnected);
        this.txtInfo.setText(this.messages.getString("serverInfo"));
        this.lblGif.setIcon(this.gifConnected);
    }

    public void setDiscover() {
        this.txtToken.setText("");
        this.lblRobot.setIcon(this.icoRobotNotDiscovered);
        this.butConnect.setText(this.messages.getString("connect"));
        this.butConnect.setSelected(false);
        this.butConnect.setEnabled(false);
        this.txtInfo.setText(this.messages.getString("plugInInfo"));
        this.lblGif.setIcon(this.gifPlug);
    }

    public void setDiscoverConnected() {
        this.butConnect.setSelected(false);
        this.butConnect.setEnabled(false);
    }

    public void setNew(String token) {
        this.txtToken.setText(token);
        this.txtInfo.setText(this.messages.getString("tokenInfo"));
        this.lblGif.setIcon(this.gifServer);
    }

    private void createIcons() {
        URL imgURL = getClass().getClassLoader().getResource("Roberta_Menu_Icon_green.png");
        if ( imgURL != null ) {
            this.icoRobotConnected = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("Roberta_Menu_Icon_red.png");
        if ( imgURL != null ) {
            this.icoRobotDiscovered = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("Roberta_Menu_Icon_grey.png");
        if ( imgURL != null ) {
            this.icoRobotNotDiscovered = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("plug.gif");
        if ( imgURL != null ) {
            this.gifPlug = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("connect.gif");
        if ( imgURL != null ) {
            this.gifConnect = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("server.gif");
        if ( imgURL != null ) {
            this.gifServer = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("connected.gif");
        if ( imgURL != null ) {
            this.gifConnected = new ImageIcon(imgURL);
        }
    }

}

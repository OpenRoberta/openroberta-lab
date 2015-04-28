package ConnectionViews;

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

import ConnectionController.UIController.CloseListener;
import ConnectionController.UIController.ConnectActionListener;

public class ConnectionView extends JFrame {

    private JMenuBar menu = new JMenuBar();
    private JMenu mnFile = new JMenu();
    private JMenuItem mntClose = new JMenuItem();
    private JMenu mnInfo = new JMenu();
    private JMenuItem mntAbout = new JMenuItem();
    private JLabel lblRobot = new JLabel();
    private JLabel lblGif = new JLabel();
    private ORAToggleButton butConnect = new ORAToggleButton();
    private ORAButton butClose = new ORAButton();
    private JTextArea txtInfo = new JTextArea();
    private JPanel pnlToken = new JPanel();
    private JTextField txtToken = new JTextField();
    private JPanel pnlButton = new JPanel();
    private JPanel centerPanel = new JPanel();
    private ResourceBundle messages;
    private ImageIcon icoRobotNotDiscovered;
    private ImageIcon icoRobotConnected;
    private ImageIcon icoRobotDiscovered;
    private ImageIcon gifPlug;
    private ImageIcon gifConnect;
    private ImageIcon gifServer;
    private ImageIcon gifConnected;

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
        this.mnFile.setText(messages.getString("file"));
        this.mnInfo.setText(messages.getString("info"));
        this.mntClose.setText(messages.getString("close"));
        this.mntClose.setActionCommand("close");
        this.mntAbout.setText(messages.getString("about"));
        this.lblRobot.setIcon(icoRobotNotDiscovered);
        this.lblGif.setMinimumSize(new Dimension(100, 106));
        this.lblGif.setSize(new Dimension(100, 106));
        this.txtInfo.setLineWrap(true);
        this.txtInfo.setMinimumSize(new Dimension(500, 0));
        this.txtInfo.setWrapStyleWord(true);
        this.txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        this.txtInfo.setForeground(Color.decode("#333333"));
        this.txtInfo.setMargin(new Insets(16, 16, 16, 16));
        this.txtInfo.setEditable(false);
        this.pnlToken.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
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
        this.butClose.setText(messages.getString("close"));
        this.butClose.setActionCommand("close");
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(menu, BorderLayout.NORTH);
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.decode("#dddddd"));
        this.centerPanel.add(sep);
        this.centerPanel.setBorder(null);
        this.centerPanel.add(pnlToken);
        JPanel a = new JPanel();
        a.add(lblGif);
        a.setBackground(Color.white);
        this.centerPanel.add(a);
        this.centerPanel.add(txtInfo);
        this.centerPanel.add(Box.createVerticalGlue());
        this.centerPanel.add(pnlButton);
        this.pnlButton.add(butConnect);
        this.pnlButton.add(Box.createHorizontalStrut(12));
        this.pnlButton.add(butClose);
        this.pnlToken.add(txtToken);
        this.menu.add(mnFile);
        this.menu.add(mnInfo);
        this.menu.add(Box.createHorizontalGlue());
        this.menu.add(lblRobot);
        this.mnFile.add(mntClose);
        this.mnInfo.add(mntAbout);
        this.setTitle("USBConnection");
        this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("./resources/ora_16x16.png")).getImage());
        this.txtInfo.setText(Locale.getDefault().getLanguage());
        this.setTitle(messages.getString("title"));
    }

    public void setConnectActionListener(ConnectActionListener connectListener) {
        this.butConnect.addActionListener(connectListener);
        this.mntClose.addActionListener(connectListener);
        this.butClose.addActionListener(connectListener);
    }

    public void setCloseListener(CloseListener closeListener) {
        this.addWindowListener(closeListener);

    }

    public void setWaitForConnect() {
        this.lblRobot.setIcon(this.icoRobotDiscovered);
        this.butConnect.setEnabled(true);
        this.txtInfo.setText(messages.getString("connectInfo"));
        this.lblGif.setIcon(gifConnect);
    }

    public void setWaitForCmd(String string) {
        this.lblRobot.setIcon(this.icoRobotConnected);
        this.txtInfo.setText(messages.getString("serverInfo"));
        this.lblGif.setIcon(gifConnected);
    }

    public void setDiscover() {
        this.txtToken.setVisible(false);
        this.lblRobot.setIcon(this.icoRobotNotDiscovered);
        this.butConnect.setText(messages.getString("connect"));
        this.butConnect.setSelected(false);
        this.butConnect.setEnabled(false);
        this.txtInfo.setText(messages.getString("plugInInfo"));
        this.lblGif.setIcon(gifPlug);
    }

    public void setNew(String token) {
        this.txtToken.setText(token);
        this.txtToken.setVisible(true);
        this.txtInfo.setText(messages.getString("tokenInfo"));
        this.lblGif.setIcon(gifServer);
    }

    private void createIcons() {
        URL imgURL = getClass().getClassLoader().getResource("./resources/Roberta_Menu_Icon_green.png");
        if ( imgURL != null ) {
            this.icoRobotConnected = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("./resources/Roberta_Menu_Icon_red.png");
        if ( imgURL != null ) {
            this.icoRobotDiscovered = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("./resources/Roberta_Menu_Icon_grey.png");
        if ( imgURL != null ) {
            this.icoRobotNotDiscovered = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("./resources/plug.gif");
        if ( imgURL != null ) {
            this.gifPlug = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("./resources/connect.gif");
        if ( imgURL != null ) {
            this.gifConnect = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("./resources/server.gif");
        if ( imgURL != null ) {
            this.gifServer = new ImageIcon(imgURL);
        }
        imgURL = getClass().getClassLoader().getResource("./resources/connected.gif");
        if ( imgURL != null ) {
            this.gifConnected = new ImageIcon(imgURL);
        }
    }
}

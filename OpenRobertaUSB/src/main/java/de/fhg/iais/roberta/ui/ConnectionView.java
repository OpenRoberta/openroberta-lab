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
import javax.swing.JCheckBox;
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
    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;
    private static final int ADVANCED_HEIGHT = 460;

    private final JMenuBar menu = new JMenuBar();
    private final JMenu mnFile = new JMenu();
    private final JMenuItem mntClose = new JMenuItem();
    private final JMenu mnInfo = new JMenu();
    private final JMenuItem mntAbout = new JMenuItem();
    private final JLabel lblRobot = new JLabel();
    private final JLabel lblMainGif = new JLabel();
    private final ORAToggleButton butConnect = new ORAToggleButton();
    private final ORAButton butClose = new ORAButton();
    private final JTextArea txtInfo = new JTextArea();
    private final JPanel pnlToken = new JPanel();
    private final JTextField txtToken = new JTextField();
    private final JCheckBox checkCustomAddress = new JCheckBox();
    private final JLabel checkCustomDesc = new JLabel();
    private final JTextField customheading = new JTextField();
    private final JLabel customipDesc = new JLabel();
    private final JTextField customip = new JTextField();
    private final JLabel customportDesc = new JLabel();
    private final JTextField customport = new JTextField();
    private final JPanel pnlMainGif = new JPanel();
    private final JPanel pnlButton = new JPanel();
    private final JPanel pnlCustomInfo = new JPanel();
    private final JPanel pnlCustomHeading = new JPanel();
    private final JPanel pnlCustomAddress = new JPanel();
    private final JPanel centerPanel = new JPanel();
    private final JSeparator sep = new JSeparator();
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
        initGUI();
        this.setDiscover();
    }

    private void initGUI() {
        this.setSize(WIDTH, HEIGHT);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.menu.setForeground(Color.decode("#333333"));
        this.menu.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.menu.setMaximumSize(this.menu.getPreferredSize());
        this.mnFile.setText(this.messages.getString("file"));
        this.mnInfo.setText(this.messages.getString("info"));
        this.mntClose.setText(this.messages.getString("close"));
        this.mntClose.setActionCommand("close");
        this.mntAbout.setText(this.messages.getString("about"));
        this.mntAbout.setActionCommand("about");

        this.lblRobot.setIcon(this.icoRobotNotDiscovered);
        this.lblMainGif.setPreferredSize(new Dimension(260, 140));

        this.txtInfo.setLineWrap(true);
        this.txtInfo.setMinimumSize(new Dimension(300, 0));
        this.txtInfo.setWrapStyleWord(true);
        this.txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        this.txtInfo.setForeground(Color.decode("#333333"));
        this.txtInfo.setMargin(new Insets(8, 16, 8, 16));
        this.txtInfo.setEditable(false);
        this.txtInfo.setMaximumSize(new Dimension(WIDTH, 90));
        this.txtInfo.setPreferredSize(new Dimension(WIDTH, 90));

        this.pnlToken.setBorder(null);
        this.pnlToken.setBackground(Color.white);
        this.pnlToken.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.pnlToken.setPreferredSize(new Dimension(300, 30));
        this.pnlToken.setMaximumSize(this.pnlToken.getPreferredSize());

        this.txtToken.setFont(new Font("Arial", Font.PLAIN, 18));
        this.txtToken.setEditable(false);
        this.txtToken.setBackground(Color.white);
        this.txtToken.setBorder(null);

        this.checkCustomAddress.setBackground(Color.white);
        this.checkCustomAddress.setActionCommand("customaddress");

        this.checkCustomDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        this.checkCustomDesc.setBackground(Color.white);
        this.checkCustomDesc.setBorder(null);

        this.pnlButton.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        this.pnlButton.setBackground(Color.white);
        this.pnlButton.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.pnlCustomInfo.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        this.pnlCustomInfo.setBackground(Color.white);
        this.pnlCustomInfo.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.pnlCustomHeading.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        this.pnlCustomHeading.setBackground(Color.white);
        this.pnlCustomHeading.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.pnlCustomHeading.setVisible(false);

        this.pnlCustomAddress.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        this.pnlCustomAddress.setBackground(Color.white);
        this.pnlCustomAddress.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.pnlCustomAddress.setVisible(false);

        this.pnlMainGif.setBackground(Color.white);
        this.pnlMainGif.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.pnlMainGif.setPreferredSize(new Dimension(300, 145));
        this.pnlMainGif.add(this.lblMainGif);

        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.Y_AXIS));
        this.centerPanel.setAlignmentX(CENTER_ALIGNMENT);
        this.centerPanel.setBackground(Color.white);
        this.centerPanel.setPreferredSize(new Dimension(300, 450));
        this.centerPanel.setMaximumSize(this.centerPanel.getPreferredSize());

        this.butConnect.setEnabled(false);
        this.butClose.setText(this.messages.getString("close"));
        this.butClose.setActionCommand("close");

        this.customheading.setFont(new Font("Arial", Font.PLAIN, 14));
        this.customheading.setEditable(false);
        this.customheading.setBackground(Color.white);
        this.customheading.setBorder(null);
        this.customheading.setText(this.messages.getString("customDesc"));

        this.customipDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        this.customipDesc.setBackground(Color.white);
        this.customipDesc.setBorder(null);

        this.customip.setFont(new Font("Arial", Font.PLAIN, 14));
        this.customip.setEditable(true);
        this.customip.setColumns(10);

        this.customportDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        this.customportDesc.setBackground(Color.white);
        this.customportDesc.setBorder(null);

        this.customport.setFont(new Font("Arial", Font.PLAIN, 14));
        this.customport.setEditable(true);
        this.customport.setColumns(4);

        this.add(this.menu, BorderLayout.NORTH);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.sep.setForeground(Color.decode("#dddddd"));
        this.centerPanel.add(this.sep);
        this.centerPanel.setBorder(null);
        this.centerPanel.add(this.pnlToken);
        this.centerPanel.add(this.pnlMainGif);
        this.centerPanel.add(this.txtInfo);
        this.centerPanel.add(this.pnlButton);
        this.centerPanel.add(this.pnlCustomInfo);
        this.centerPanel.add(this.pnlCustomHeading);
        this.centerPanel.add(this.pnlCustomAddress);
        this.pnlButton.add(this.butConnect);
        this.pnlButton.add(Box.createHorizontalStrut(12));
        this.pnlButton.add(this.butClose);
        this.pnlCustomInfo.add(this.checkCustomAddress);
        this.pnlCustomInfo.add(this.checkCustomDesc);
        this.checkCustomDesc.setText(this.messages.getString("checkCustomDesc"));
        this.pnlCustomHeading.add(this.customheading);
        this.pnlCustomAddress.add(this.customipDesc);
        this.customipDesc.setText(this.messages.getString("ip"));
        this.pnlCustomAddress.add(Box.createVerticalGlue());
        this.pnlCustomAddress.add(this.customip);
        this.pnlCustomAddress.add(Box.createHorizontalStrut(12));
        this.pnlCustomAddress.add(this.customportDesc);
        this.customportDesc.setText(this.messages.getString("port"));
        this.pnlCustomAddress.add(this.customport);
        this.pnlToken.add(this.txtToken);
        this.menu.add(this.mnFile);
        this.menu.add(this.mnInfo);
        this.menu.add(Box.createHorizontalGlue());
        this.menu.add(this.lblRobot);
        this.mnFile.add(this.mntClose);
        this.mnInfo.add(this.mntAbout);
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
        this.checkCustomAddress.addActionListener(connectListener);
    }

    @SuppressWarnings("rawtypes")
    public void setCloseListener(CloseListener closeListener) {
        this.addWindowListener(closeListener);

    }

    public void setWaitForConnect() {
        this.lblRobot.setIcon(this.icoRobotDiscovered);
        this.butConnect.setEnabled(true);
        this.txtInfo.setText(this.messages.getString("connectInfo"));
        this.lblMainGif.setIcon(this.gifConnect);
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
        this.lblMainGif.setIcon(this.gifConnected);
    }

    public void setDiscover() {
        this.txtToken.setText("");
        this.lblRobot.setIcon(this.icoRobotNotDiscovered);
        this.butConnect.setText(this.messages.getString("connect"));
        this.butConnect.setSelected(false);
        this.butConnect.setEnabled(false);
        this.txtInfo.setText(this.messages.getString("plugInInfo"));
        this.lblMainGif.setIcon(this.gifPlug);
    }

    public void setDiscoverConnected() {
        this.butConnect.setSelected(false);
        this.butConnect.setEnabled(false);
    }

    public void setNew(String token) {
        this.txtToken.setText(token);
        this.txtInfo.setText(this.messages.getString("tokenInfo"));
        this.lblMainGif.setIcon(this.gifServer);
    }

    public void showAdvancedOptions() {
        if ( this.checkCustomAddress.isSelected() ) {
            this.setSize(new Dimension(WIDTH, ADVANCED_HEIGHT));
            this.setPreferredSize(new Dimension(WIDTH, ADVANCED_HEIGHT));
            this.pnlCustomHeading.setVisible(true);
            this.pnlCustomAddress.setVisible(true);
            this.centerPanel.revalidate();
            this.revalidate();
        } else {
            this.setSize(new Dimension(WIDTH, HEIGHT));
            this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            this.pnlCustomHeading.setVisible(false);
            this.pnlCustomAddress.setVisible(false);
            this.centerPanel.revalidate();
            this.revalidate();
        }
    }

    public String getCustomIP() {
        return this.customip.getText();
    }

    public String getCustomPort() {
        return this.customport.getText();
    }

    public boolean isCustomAddressSelected() {
        return this.checkCustomAddress.isSelected();
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

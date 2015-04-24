package ConnectionViews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import ConnectionController.UIController.CloseListener;
import ConnectionController.UIController.ConnectListener;

public class ConnectionView extends JFrame {

    private ORAToggleButton butConnect = new ORAToggleButton();
    private ORAButton butCancel = new ORAButton();
    private JTextArea txtInfo = new JTextArea();
    private JPanel pnlToken = new JPanel();
    private JTextField txtToken = new JTextField();
    private JPanel pnlButton = new JPanel();
    private JPanel statePanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JLabel roberta = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("./resources/ora_16x16.png")));
    private ResourceBundle messages;

    public ConnectionView(ResourceBundle messages) {
        this.messages = messages;
        this.setSize(300, 300);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.txtInfo.setLineWrap(true);
        this.txtInfo.setWrapStyleWord(true);
        this.txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        this.txtInfo.setMargin(new Insets(16, 16, 16, 16));
        this.pnlToken.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        this.pnlToken.setBackground(Color.white);
        this.pnlToken.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.txtToken.setFont(new Font("Arial", Font.PLAIN, 18));
        this.txtToken.setVisible(false);
        this.txtToken.setEditable(false);
        this.txtToken.setBackground(Color.white);
        this.txtToken.setBorder(null);
        this.pnlButton.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        this.pnlButton.setBackground(Color.white);
        this.pnlButton.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.Y_AXIS));
        this.centerPanel.setAlignmentX(CENTER_ALIGNMENT);
        this.statePanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        this.statePanel.setBackground(Color.white);
        this.statePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.statePanel.add(roberta);
        this.butConnect.setEnabled(false);
        this.butCancel.setText(messages.getString("cancel"));
        this.butCancel.setActionCommand("cancel");
        this.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(pnlButton, BorderLayout.SOUTH);
        this.add(statePanel, BorderLayout.NORTH);
        this.centerPanel.add(pnlToken);
        this.centerPanel.add(txtInfo);
        this.pnlButton.add(butConnect);
        this.pnlButton.add(butCancel);
        this.pnlToken.add(txtToken);
        this.setTitle("USBConnection");
        this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("./resources/ora_16x16.png")).getImage());
        this.txtInfo.setText(Locale.getDefault().getLanguage());
        this.setTitle(messages.getString("title"));
        this.setDiscover();
    }

    public void setConnectListener(ConnectListener connectListener) {
        this.butConnect.addActionListener(connectListener);
        this.butCancel.addActionListener(connectListener);

    }

    public void setCloseListener(CloseListener closeListener) {
        this.addWindowListener(closeListener);
    }

    public void setWaitForConnect(boolean enable) {
        this.butConnect.setEnabled(enable);
        if ( enable ) {
            this.txtInfo.setText(messages.getString("connectInfo"));
        }
    }

    public void setWaitForCmd(String string) {
        this.txtInfo.setText("hallo");//messages.getString("waitServerInfo"));
    }

    public void setConnected() {
        this.txtToken.setVisible(false);
        this.txtInfo.setText("connected");//messages.getString("waitServerInfo"));
    }

    public void setDiscover() {
        this.butConnect.setText(messages.getString("connect"));
        this.butConnect.setSelected(false);
        this.txtInfo.setText(messages.getString("plugInInfo"));
    }

    public void setNew(String token) {
        this.txtToken.setText(token);
        this.txtToken.setVisible(true);
        this.txtInfo.setText("eingeben");//messages.getString("waitServerInfo"));
    }
}

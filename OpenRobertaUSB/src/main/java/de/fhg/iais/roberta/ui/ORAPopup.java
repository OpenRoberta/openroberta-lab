package de.fhg.iais.roberta.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resources;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class ORAPopup extends JOptionPane {

    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 150;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIDefaults defaults = UIManager.getLookAndFeelDefaults();
                Set<Entry<Object, Object>> entries = defaults.entrySet();
                for ( Entry<Object, Object> entry : entries ) {
                    System.out.print(entry.getKey() + " = ");
                    System.out.print(entry.getValue() + "\n");
                }
                String title = "Titel";
                String text = "Text";
                String[] buttons = new String[] {
                    "a",
                    "b",
                    "c"
                };
                System.out.println(ORAPopup.showPopup(null, title, text, null, buttons));
            }
        });
    }

    public static int showPopup(Component component, String title, String text, Icon icon, String[] txtButtons) {
        ORAButton buttons[] = new ORAButton[txtButtons.length];

        for ( int i = 0; i < txtButtons.length; i++ ) {
            final ORAButton oraButton = new ORAButton();
            oraButton.setText(txtButtons[i]);
            oraButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane pane = (JOptionPane) ((JComponent) e.getSource()).getParent().getParent();
                    pane.setValue(oraButton);
                }
            });

            buttons[i] = oraButton;
        }
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        UIManager.put("Panel.background", Color.white);
        text = "<html><body><p style='width: " + WIDTH + "px;'>" + text + "</p></body></html>";

        return JOptionPane.showOptionDialog(component, text, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, buttons, buttons[0]);
    }

    public static int showPopup(Component component, String title, String text, Icon icon) {
        if ( icon == null ) {
            icon = new ImageIcon(Resources.class.getResource("warning-outline.png"));
        }
        return showPopup(component, title, text, icon, new String[] {
            "OK"
        });
    }
}
package connection.views;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ORAToggleButton extends JToggleButton {
    public ORAToggleButton() {

        this.setBackground(Color.decode("#afca04"));
        this.setFont(new Font("Arial", Font.PLAIN, 16));
        this.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        this.setForeground(Color.white);
        this.setRolloverEnabled(true);
        UIManager.put("ToggleButton.select", Color.decode("#afca04"));
        SwingUtilities.updateComponentTreeUI(this);
        this.getModel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel b = (ButtonModel) e.getSource();
                if ( b.isRollover() ) {
                    setBackground(Color.decode("#b7d032"));
                } else {
                    setBackground(Color.decode("#afca04"));
                }
            }
        });
    }
}

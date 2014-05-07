package lejos.ev3.startup;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

// @formatter:off
public class RobertaKeyboard {
    
    private TextLCD lcd = LocalEV3.get().getTextLCD();
    
    int x = 0, y = 5;
    
    String[] upper = {"0123456789        ", 
                      "QWERTYUIOP        ", 
                      "ASDFGHJKL         ", 
                      "ZXCVBNM           ", 
                      "x D           "};
    
    String[] lines = upper;
    
    void display() {
        //LCD.drawString("Keyboard", 4, 0);
        lcd.clear();
        for(int i=0;i<lines.length;i++) {
            lcd.drawString(lines[i], 0, i+1);
        }
        displayCursor(true);
    }
    
    void displayCursor(boolean inverted) {
        lcd.drawString(lines[y-1].substring(x,x+1), x, y, inverted);
    }
    
    String getString() {
        StringBuilder sb = new StringBuilder();
        x = 0;
        y = 5;
        display();
        
        while (true) {
            int b = Button.waitForAnyPress();
            
            displayCursor(false);
            
            if (b == Button.ID_DOWN) {
                if (++y > 5) y = 1;
            } else if (b == Button.ID_UP) {
                if (--y < 1) y = 5;
            } else if (b == Button.ID_LEFT) {
                if (--x < 0) x = 9;
            } else if (b == Button.ID_RIGHT) {
                if (++x > 9) x = 0;
            } else if (b == Button.ID_ENTER) {
                if (y < 5) sb.append(lines[y-1].charAt(x));
                else {
                    switch (lines[4].charAt(x)) {
                    case 'x':
                        if (sb.length() > 0) {
                            sb.deleteCharAt(sb.length()-1);
                            lcd.drawString(" ", sb.length(), 7);
                        } else Sound.buzz();
                        break;
                    case 'D':
                        return sb.toString();
                    }
                }
            } else if (b == Button.ID_ESCAPE) {
                return null;
            }
            
            displayCursor(true);
            String s = sb.toString();
            if (s.length() > 18) s = s.substring(s.length() - 18, s.length());
            lcd.drawString(s, 0, 7);
        }
    }
    
    public static void main(String[] args) {
        RobertaKeyboard k = new RobertaKeyboard();
        
        String s = k.getString();
        System.out.println("String is " + s);
    }
}
// @formatter:on

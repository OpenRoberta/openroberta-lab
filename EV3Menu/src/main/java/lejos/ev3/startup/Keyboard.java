package lejos.ev3.startup;

import lejos.ev3.startup.Utils;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;

public class Keyboard
{

    private static Image ICOK = new Image(
            12,
            16,
            Utils.stringToBytes8("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u000c\u0000\u0006\u0000\u0003\u0080\u0001\u00c0\u0000\u0061\u0000\u0033\u0000\u001e\u0000\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000"));
    private static Image ICDEL = new Image(
            12,
            16,
            Utils.stringToBytes8("\u0000\u0000\u0000\u0000\u0000\u0000\u00f0\u000f\u00f8\u000f\u00dc\r\u00be\u000e\u007f\u000f\u00be\u000e\u00dc\r\u00f8\u000f\u00f0\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"));
    private static Image ICSHIFT = new Image(
            12,
            16,
            Utils.stringToBytes8("\u0000\u0000\u0000\u0000\u0060\u0000\u00f0\u0000\u0098\u0001\u000c\u0003\u0006\u0006\u000e\u0007\u0008\u0001\u0008\u0001\u0008\u0001\u00f8\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"));
    private static Image ICSHIFTON = new Image(
            12,
            16,
            Utils.stringToBytes8("\u0000\u0000\u0000\u0000\u0060\u0000\u00f0\u0000\u00f8\u0001\u00fc\u0003\u00fe\u0007\u00fe\u0007\u00f8\u0001\u00f8\u0001\u00f8\u0001\u00f8\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"));
    private static Image ICSYMBOL = new Image(
            12,
            16,
            Utils.stringToBytes8("\u0000\u0000\u0082\u0001\u00c3\u0003\u0063\u0007\u0033\u0006\u0033\u000c\u0003\u000c\u0003\u000c\u0003\u0006\u0003\u0003\u0083\u0001\u0083\u0001\u0083\u0001\u0000\u0000\u0000\u0000\u0083\u0001"));

    private static GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();

    private static String[][] keyboards = {
            { "1234567890?", 
               "qwertyuiop<", 
               "asdfghjkl.>", 
               "^zxcvbnm  ^" },
            { "1234567890?", 
               "QWERTYUIOP<", 
               "ASDFGHJKL,>", 
               "^ZXCVBNM  ^" },
            { "[]{}#%^*+=@", 
              "-\\|:;()$@\"<", 
              "~/_`&.,?!'>", 
              "^     <>  ^" } };

    public static String getString()
    {

        String str = "";
        int sx = 0, sy = 0, keyboard = 0;

        lcd.setFont(Font.getDefaultFont());
        int chWidth = lcd.getFont().width;
        int chHeight = lcd.getFont().getHeight();
        int maxChars = 160 / chWidth - 2;
        lcd.clear();
        do
        {
            lcd.setColor(GraphicsLCD.WHITE);
            lcd.fillRect(8, 20, 162, 98);
            lcd.setColor(GraphicsLCD.BLACK);
            lcd.drawRect(7, 19, 164, 100);
            lcd.drawLine(6, 20, 6, 118);
            lcd.drawLine(172, 20, 172, 118);

            lcd.setStrokeStyle(GraphicsLCD.DOTTED);
            lcd.setFont(Font.getDefaultFont());

            String substr = str;
            if (str.length() > maxChars)
            {
                substr = str.substring(str.length() - maxChars);
                lcd.drawString("<", 16, 38, GraphicsLCD.BOTTOM
                        | GraphicsLCD.RIGHT);
            }
            lcd.drawString(substr, 15, 38, GraphicsLCD.BOTTOM);
            lcd.drawString(" ", 15 + lcd.getFont().stringWidth(substr), 38,
                    GraphicsLCD.BOTTOM, true);

            lcd.drawLine(15, 40, 163, 40);

            for (int yi = 0; yi < 4; yi++)
            {
                for (int xi = 0; xi < 11; xi++)
                {
                    int x = xi * (chWidth + 4) + 16;
                    int y = yi * (lcd.getFont().getHeight() + 3) + 43;

                    /* Draw Key Character */
                    lcd.drawChar(keyboards[keyboard][yi].charAt(xi), x, y, 0);

                    /* Draw Finish Key */
                    if (yi == 1 && xi == 10)
                        lcd.drawImage(ICOK, x - 1, y, 0);

                    /* Draw Symbol Key */
                    if (yi == 2 && xi == 10)
                        lcd.drawImage(ICSYMBOL, x - 1, y, 0);

                    /* Draw Del Key */
                    if (yi == 0 && xi == 10)
                        lcd.drawImage(ICDEL, x - 1, y, 0);

                    /* Draw Shift Key */
                    if (yi == 3 && (xi == 0 || xi == 10))
                        lcd.drawImage((keyboard == 1) ? ICSHIFTON : ICSHIFT,
                                x - 1, y, 0);

                    /* Draw Space Bar */
                    if (yi == 3 && xi == 8)
                    {
                        lcd.drawRect(x, y + 1, chWidth * 2 + 4, chHeight - 2);
                        if (sy == 3 && sx >= 8 && sx <= 9)
                            lcd.fillRect(x - 1, y, chWidth * 2 + 6, chHeight);
                    }

                    /* Invert Key if Selected */
                    if (sx == xi && sy == yi
                            && !(sy == 3 && sx >= 8 && sx <= 9))
                        lcd.drawRegionRop(null, x - 1, y, chWidth + 2,
                                chHeight, x - 1, y, 0,
                                GraphicsLCD.ROP_COPYINVERTED);
                }
            }
            lcd.refresh();
            switch (Button.waitForAnyPress())
            {
            case Button.ID_RIGHT:
                if (sy == 3 && sx == 8)
                    sx++;
                if (sx < 10)
                    sx++;
                else
                    sx = 0;
                break;
            case Button.ID_LEFT:
                if (sy == 3 && sx == 9)
                    sx--;
                if (sx > 0)
                    sx--;
                else
                    sx = 10;
                break;
            case Button.ID_UP:
                if (sy > 0)
                    sy--;
                else
                    sy = 3;
                break;
            case Button.ID_DOWN:
                if (sy < 3)
                    sy++;
                else
                    sy = 0;
                break;
            case Button.ID_ENTER:
                if (sy == 3 && (sx == 0 || sx == 10))
                { // Shift Key Pressed
                    if (keyboard == 0)
                        keyboard = 1;
                    else if (keyboard == 1)
                        keyboard = 0;
                }
                else if (sy == 2 && sx == 10)
                { // Symbols Key Pressed
                    if (keyboard != 2)
                        keyboard = 2;
                    else
                        keyboard = 0;
                }
                else if (sy == 0 && sx == 10)
                { // Backspace Key Pressed
                    if (str.length() > 0)
                        str = str.substring(0, str.length() - 1);
                }
                else if (sy == 1 && sx == 10)
                { // Finish Key Pressed
                    return str;
                }
                else
                    // Character Key Pressed
                    str += keyboards[keyboard][sy].substring(sx, sx + 1);
                break;
            case Button.ID_ESCAPE:
                return null;
            }
            lcd.refresh();

        } while (true);
    }

    public static void main(String[] args)
    {
        String s = Keyboard.getString();
        System.out.println("String is " + s);
    }

}

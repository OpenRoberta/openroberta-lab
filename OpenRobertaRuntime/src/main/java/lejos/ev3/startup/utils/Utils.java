package lejos.ev3.startup.utils;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;

public class Utils {

    private static GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
    private static TextLCD lcd = LocalEV3.get().getTextLCD();

    public static byte[] stringToBytes8(String str) {
        int len = str.length();
        byte[] r = new byte[len];
        for ( int i = 0; i < len; i++ ) {
            r[i] = (byte) str.charAt(i);
        }
        return r;
    }

    public static byte[] stringToBytes16(String str, int off, int len) {
        byte[] r = new byte[len << 1];
        for ( int i = 0; i < len; i++ ) {
            char c = str.charAt(off + i);
            int j = i << 1;
            r[j] = (byte) (c >> 8);
            r[j + 1] = (byte) c;
        }
        return r;
    }

    public static byte[] textToBytes(String text) {
        int len = text.length();
        int blen = 0;
        int fw = lcd.getFont().width;
        if ( len > 0 ) {
            blen = len * (fw + 1) - 1;
        }

        byte[] b = new byte[blen];
        byte[] f = lcd.getFont().glyphs;

        for ( int i = 0; i < len; i++ ) {
            char c = text.charAt(i);
            int i1 = c * fw;
            int i2 = i * (fw + 1);

            if ( i1 < f.length ) {
                System.arraycopy(f, i1, b, i2, fw);
            }
        }

        return b;
    }

    public static String versionToString(int version) {
        //very short byte code
        return new StringBuilder().append(version >>> 16).append('.').append((version >>> 8) & 0xFF).append('.').append(version & 0xFF).toString();
    }

    public static void drawRect(int x, int y, int width, int height) {
        g.drawRect(x, y, width, height);
    }

    /**
     * Return the extension part of a filename
     *
     * @param fileName
     * @return the file extension
     */
    public static String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if ( dot < 0 ) {
            return "";
        }

        return fileName.substring(dot + 1, fileName.length());
    }

    /**
     * Return the base part (no extension) of a filename
     *
     * @param fileName
     * @return the base part of the name
     */
    public static String getBaseName(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if ( dot < 0 ) {
            return fileName;
        }

        return fileName.substring(0, dot);
    }
}

package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.Image;

public interface RMIGraphicsLCD extends Remote {
	
    public void setPixel(int x, int y, int color) throws RemoteException;
    
    public int getPixel(int x, int y) throws RemoteException;
    
    public void drawString(String str, int x, int y, int anchor, boolean inverted) throws RemoteException;
    
    public void drawString(String str, int x, int y, int anchor) throws RemoteException;
    
    public void drawSubstring(String str, int offset, int len,
            int x, int y, int anchor) throws RemoteException;

    public void drawChar(char character, int x, int y, int anchor) throws RemoteException;

    public void drawChars(char[] data, int offset, int length,
            int x, int y, int anchor) throws RemoteException;

    public int getStrokeStyle() throws RemoteException;

    public void setStrokeStyle(int style) throws RemoteException;

    public void drawRegionRop(Image src, int sx, int sy, int w, int h, int x, int y, int anchor, int rop) throws RemoteException;
    
    public void drawRegionRop(Image src, int sx, int sy, int w, int h, int transform, int x, int y, int anchor, int rop) throws RemoteException;

    public void drawRegion(Image src,
            int sx, int sy,
            int w, int h,
            int transform,
            int x, int y,
            int anchor) throws RemoteException;

    public void drawImage(Image src, int x, int y, int anchor) throws RemoteException;

    public void drawLine(int x0, int y0, int x1, int y1) throws RemoteException;

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) throws RemoteException;

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) throws RemoteException;

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) throws RemoteException;
 
    public void drawRect(int x, int y, int width, int height) throws RemoteException;

    public void fillRect(int x, int y, int w, int h) throws RemoteException;

    public void copyArea(int sx, int sy,
            int w, int h,
            int x, int y, int anchor) throws RemoteException;

    public Font getFont() throws RemoteException;

    public void setFont(Font f) throws RemoteException;

    public void translate(int x, int y) throws RemoteException;

    public int getTranslateX() throws RemoteException;

    public int getTranslateY() throws RemoteException;

    public void setColor(int rgb) throws RemoteException;

    public void setColor(int red, int green, int blue) throws RemoteException;
    
    public void refresh() throws RemoteException;
    
    public void clear() throws RemoteException;
    
    public int getWidth() throws RemoteException;
    
    public int getHeight() throws RemoteException;
    
    public byte[] getDisplay() throws RemoteException;
    
    public byte[] getHWDisplay() throws RemoteException;
    
    public void setContrast(int contrast) throws RemoteException;
    
    public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx, int dy, int w, int h, int rop) throws RemoteException;
    
    public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte dst[], int dw, int dh, int dx, int dy, int w, int h, int rop) throws RemoteException;
    
    public void setAutoRefresh(boolean on) throws RemoteException;
    
    public int setAutoRefreshPeriod(int period) throws RemoteException;

}

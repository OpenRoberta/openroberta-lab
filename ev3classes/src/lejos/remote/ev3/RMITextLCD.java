package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.hardware.lcd.Font;

public interface RMITextLCD extends Remote {

	public void refresh() throws RemoteException;

	public void clear()  throws RemoteException;

	public int getWidth()  throws RemoteException;

	public int getHeight()  throws RemoteException;

	public byte[] getDisplay()  throws RemoteException;

	public byte[] getHWDisplay()  throws RemoteException;

	public void setContrast(int contrast)  throws RemoteException;

	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx,
			int dy, int w, int h, int rop)  throws RemoteException;

	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte[] dst,
			int dw, int dh, int dx, int dy, int w, int h, int rop)  throws RemoteException;

	public void setAutoRefresh(boolean on)  throws RemoteException;

	public int setAutoRefreshPeriod(int period)  throws RemoteException;

	public void drawChar(char c, int x, int y)  throws RemoteException;

	public void drawString(String str, int x, int y, boolean inverted)  throws RemoteException;

	public void drawString(String str, int x, int y)  throws RemoteException;

	public void drawInt(int i, int x, int y)  throws RemoteException;

	public void drawInt(int i, int places, int x, int y)  throws RemoteException;

	public void clear(int x, int y, int n) throws RemoteException;

	public void clear(int y)  throws RemoteException;

	public void scroll()  throws RemoteException;

	public Font getFont()  throws RemoteException;

	public int getTextWidth()  throws RemoteException;

	public int getTextHeight()  throws RemoteException;

}

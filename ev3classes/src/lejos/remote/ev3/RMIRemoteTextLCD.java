package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.TextLCD;

public class RMIRemoteTextLCD  extends UnicastRemoteObject implements RMITextLCD {

	private static final long serialVersionUID = -7900852313387010501L;
	private TextLCD lcd = LocalEV3.get().getTextLCD();
	
	protected RMIRemoteTextLCD() throws RemoteException {
		super(0);
	}
	
	public void setFont(Font f) {
		lcd = LocalEV3.get().getTextLCD(f);
	}

	@Override
	public void refresh() throws RemoteException {
		lcd.refresh();	
	}

	@Override
	public void clear() throws RemoteException {
		lcd.clear();	
	}

	@Override
	public int getWidth() throws RemoteException {
		return lcd.getWidth();
	}

	@Override
	public int getHeight() throws RemoteException {
		return lcd.getHeight();
	}

	@Override
	public byte[] getDisplay() throws RemoteException {
		return lcd.getDisplay();
	}

	@Override
	public byte[] getHWDisplay() throws RemoteException {
		return lcd.getHWDisplay();
	}

	@Override
	public void setContrast(int contrast) throws RemoteException {
		lcd.setContrast(contrast);	
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx,
			int dy, int w, int h, int rop) throws RemoteException {
		lcd.bitBlt(src, sw, sh, sx, sy, dx, dy, w, h, rop);	
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte[] dst,
			int dw, int dh, int dx, int dy, int w, int h, int rop)
			throws RemoteException {
		lcd.bitBlt(src, sw, sh, sx, sy, dst, dw, dh, dx, dy, w, h, rop);
	}

	@Override
	public void setAutoRefresh(boolean on) throws RemoteException {
		lcd.setAutoRefresh(on);	
	}

	@Override
	public int setAutoRefreshPeriod(int period) throws RemoteException {
		return lcd.setAutoRefreshPeriod(period);
	}

	@Override
	public void drawChar(char c, int x, int y) throws RemoteException {
		lcd.drawChar(c, x, y);	
	}

	@Override
	public void drawString(String str, int x, int y, boolean inverted)
			throws RemoteException {
		lcd.drawString(str, x, y, inverted);
	}

	@Override
	public void drawString(String str, int x, int y) throws RemoteException {
		lcd.drawString(str, x, y);	
	}

	@Override
	public void drawInt(int i, int x, int y) throws RemoteException {
		lcd.drawInt(i, x, y);	
	}

	@Override
	public void drawInt(int i, int places, int x, int y) throws RemoteException {
		lcd.drawInt(i, places, x, y);
	}

	@Override
	public void clear(int x, int y, int n) throws RemoteException {
		lcd.clear(x, y, n);
		
	}

	@Override
	public void clear(int y) throws RemoteException {
		lcd.clear(y);
	}

	@Override
	public void scroll() throws RemoteException {
		lcd.scroll();
	}

	@Override
	public Font getFont() throws RemoteException {
		return lcd.getFont();
	}

	@Override
	public int getTextWidth() throws RemoteException {
		return lcd.getTextWidth();
	}

	@Override
	public int getTextHeight() throws RemoteException {
		return lcd.getTextHeight();
	}
}

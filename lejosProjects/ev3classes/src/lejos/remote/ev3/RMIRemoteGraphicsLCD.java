package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;

public class RMIRemoteGraphicsLCD  extends UnicastRemoteObject implements RMIGraphicsLCD {

	private static final long serialVersionUID = 1561883712422890550L;
	private GraphicsLCD g = LocalEV3.get().getGraphicsLCD();

	protected RMIRemoteGraphicsLCD() throws RemoteException {
		super(0);
	}

	@Override
	public void setPixel(int x, int y, int color) throws RemoteException {
		g.setPixel(x, y, color);	
	}

	@Override
	public int getPixel(int x, int y) throws RemoteException {
		return g.getPixel(x, y);
	}

	@Override
	public void drawString(String str, int x, int y, int anchor,
			boolean inverted) throws RemoteException {
		g.drawString(str, x, y, anchor, inverted);
	}

	@Override
	public void drawString(String str, int x, int y, int anchor)
			throws RemoteException {
		g.drawString(str, x, y, anchor);
	}

	@Override
	public void drawSubstring(String str, int offset, int len, int x, int y,
			int anchor) throws RemoteException {
		g.drawSubstring(str, offset, len, x, y, anchor);
	}

	@Override
	public void drawChar(char character, int x, int y, int anchor)
			throws RemoteException {
		g.drawChar(character, x, y, anchor);
	}

	@Override
	public void drawChars(char[] data, int offset, int length, int x, int y,
			int anchor) throws RemoteException {
		g.drawChars(data, offset, length, x, y, anchor);
	}

	@Override
	public int getStrokeStyle() throws RemoteException {
		return g.getStrokeStyle();
	}

	@Override
	public void setStrokeStyle(int style) throws RemoteException {
		g.setStrokeStyle(style);
	}

	@Override
	public void drawRegionRop(Image src, int sx, int sy, int w, int h, int x,
			int y, int anchor, int rop) throws RemoteException {
		g.drawRegionRop(src, sx, sy, w, h, x, y, anchor, rop);
	}

	@Override
	public void drawRegionRop(Image src, int sx, int sy, int w, int h,
			int transform, int x, int y, int anchor, int rop)
			throws RemoteException {
		g.drawRegionRop(src, sx, sy, w, h, transform, x, y, anchor, rop);
	}

	@Override
	public void drawRegion(Image src, int sx, int sy, int w, int h,
			int transform, int x, int y, int anchor) throws RemoteException {
		g.drawRegion(src, sx, sy, w, h, transform, x, y, anchor);
	}

	@Override
	public void drawImage(Image src, int x, int y, int anchor)
			throws RemoteException {
		g.drawImage(src, x, y, anchor);
	}

	@Override
	public void drawLine(int x0, int y0, int x1, int y1) throws RemoteException {
		g.drawLine(x0, y0, x1, y1);	
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) throws RemoteException {
		g.drawArc(x, y, width, height, startAngle, arcAngle);	
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) throws RemoteException {
		g.fillArc(x, y, width, height, startAngle, arcAngle);	
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) throws RemoteException {
		g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void drawRect(int x, int y, int width, int height)
			throws RemoteException {
		g.drawRect(x, y, width, height);
	}

	@Override
	public void fillRect(int x, int y, int w, int h) throws RemoteException {
		g.fillRect(x, y, w, h);
	}

	@Override
	public void copyArea(int sx, int sy, int w, int h, int x, int y, int anchor)
			throws RemoteException {
		g.copyArea(sx, sy, w, h, x, y, anchor);
	}

	@Override
	public Font getFont() throws RemoteException {
		return g.getFont();
	}

	@Override
	public void setFont(Font f) throws RemoteException {
		g.setFont(f);	
	}

	@Override
	public void translate(int x, int y) throws RemoteException {
		g.translate(x, y);
	}

	@Override
	public int getTranslateX() throws RemoteException {
		return g.getTranslateX();
	}

	@Override
	public int getTranslateY() throws RemoteException {
		return g.getTranslateY();
	}

	@Override
	public void setColor(int rgb) throws RemoteException {
		g.setColor(rgb);
	}

	@Override
	public void setColor(int red, int green, int blue) throws RemoteException {
		g.setColor(red, green, blue);
	}

	@Override
	public void refresh() throws RemoteException {
		g.refresh();
	}

	@Override
	public void clear() throws RemoteException {
		g.clear();
	}

	@Override
	public int getWidth() throws RemoteException {
		return g.getWidth();
	}

	@Override
	public int getHeight() throws RemoteException {
		return g.getHeight();
	}

	@Override
	public byte[] getDisplay() throws RemoteException {
		return g.getDisplay();
	}

	@Override
	public byte[] getHWDisplay() throws RemoteException {
		return g.getHWDisplay();
	}

	@Override
	public void setContrast(int contrast) throws RemoteException {
		g.setContrast(contrast);
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx,
			int dy, int w, int h, int rop) throws RemoteException {
		g.bitBlt(src, sw, sh, sx, sy, dx, dy, w, h, rop);
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte[] dst,
			int dw, int dh, int dx, int dy, int w, int h, int rop)
			throws RemoteException {
		g.bitBlt(src, sw, sh, sx, sy, dst, dw, dh, dx, dy, w, h, rop);
	}

	@Override
	public void setAutoRefresh(boolean on) throws RemoteException {
		g.setAutoRefresh(on);	
	}

	@Override
	public int setAutoRefreshPeriod(int period) throws RemoteException {
		return g.setAutoRefreshPeriod(period);
	}
}

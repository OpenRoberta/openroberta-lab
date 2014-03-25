package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.port.PortException;

public class RemoteGraphicsLCD implements GraphicsLCD {
	private RMIGraphicsLCD g;
	
	public RemoteGraphicsLCD(RMIGraphicsLCD g) {
		this.g = g;
	}
	
	@Override
	public void refresh() {
		try {
			g.refresh();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void clear() {
		try {
			g.clear();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getWidth() {
		try {
			return g.getWidth();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getHeight() {
		try {
			return g.getHeight();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public byte[] getDisplay() {
		try {
			return g.getDisplay();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public byte[] getHWDisplay() {
		try {
			return g.getHWDisplay();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setContrast(int contrast) {
		try {
			g.setContrast(contrast);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx,
			int dy, int w, int h, int rop) {
		try {
			g.bitBlt(src, sw, sh, sx, sy, dx, dy, w, h, rop);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte[] dst,
			int dw, int dh, int dx, int dy, int w, int h, int rop) {
		try {
			g.bitBlt(src, sw, sh, sx, sy, dst, dw, dh, dx, dy, w, h, rop);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void setAutoRefresh(boolean on) {
		try {
			g.setAutoRefresh(on);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int setAutoRefreshPeriod(int period) {
		try {
			return g.setAutoRefreshPeriod(period);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setPixel(int x, int y, int color) {
		try {
			g.setPixel(x, y, color);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public int getPixel(int x, int y) {
		try {
			return g.getPixel(x, y);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawString(String str, int x, int y, int anchor,
			boolean inverted) {
		try {
			g.drawString(str, x, y, anchor, inverted);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawString(String str, int x, int y, int anchor) {
		try {
			g.drawString(str, x, y, anchor);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawSubstring(String str, int offset, int len, int x, int y,
			int anchor) {
		try {
			g.drawSubstring(str, offset, len, x, y, anchor);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawChar(char character, int x, int y, int anchor) {
		try {
			g.drawChar(character, x, y, anchor);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void drawChars(char[] data, int offset, int length, int x, int y,
			int anchor) {
		try {
			g.drawChars(data, offset, length, x, y, anchor);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getStrokeStyle() {
		try {
			return g.getStrokeStyle();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setStrokeStyle(int style) {
		try {
			g.setStrokeStyle(style);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawRegionRop(Image src, int sx, int sy, int w, int h, int x,
			int y, int anchor, int rop) {
		try {
			g.drawRegionRop(src, sx, sy, w, h, x, y, anchor, rop);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void drawRegionRop(Image src, int sx, int sy, int w, int h,
			int transform, int x, int y, int anchor, int rop) {
		try {
			g.drawRegionRop(src, sx, sy, w, h, transform, x, y, anchor, rop);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawRegion(Image src, int sx, int sy, int w, int h,
			int transform, int x, int y, int anchor) {
		try {
			g.drawRegion(src, sx, sy, w, h, transform, x, y, anchor);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawImage(Image src, int x, int y, int anchor) {
		try {
			g.drawImage(src, x, y, anchor);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawLine(int x0, int y0, int x1, int y1) {
		try {
			g.drawLine(x0, y0, x1, y1);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		try {
			g.drawArc(x, y, width, height, startAngle, arcAngle);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		try {
			g.fillArc(x, y, width, height, startAngle, arcAngle);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		try {
			g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		try {
			g.drawRect(x, y, width, height);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void fillRect(int x, int y, int w, int h) {
		try {
			g.fillRect(x, y, w, h);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void copyArea(int sx, int sy, int w, int h, int x, int y, int anchor) {
		try {
			g.copyArea(sx, sy, w, h, x, y, anchor);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public Font getFont() {
		try {
			return g.getFont();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setFont(Font f) {
		try {
			g.setFont(f);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void translate(int x, int y) {
		try {
			g.translate(x, y);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getTranslateX() {
		try {
			return g.getTranslateX();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getTranslateY() {
		try {
			return g.getTranslateY();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setColor(int rgb) {
		try {
			g.setColor(rgb);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void setColor(int red, int green, int blue) {
		try {
			g.setColor(red, green, blue);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
}

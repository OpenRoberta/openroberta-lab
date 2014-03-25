package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.PortException;

public class RemoteTextLCD implements TextLCD {
	private RMITextLCD lcd;
	
	public RemoteTextLCD(RMITextLCD lcd) {
		this.lcd = lcd;
	}

	@Override
	public void refresh() {
		try {
			lcd.refresh();
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void clear() {
		try {
			lcd.clear();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getWidth() {
		try {
			return lcd.getWidth();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getHeight() {
		try {
			return lcd.getHeight();
		} catch (RemoteException e) {
			throw new PortException(e);
		}			
	}

	@Override
	public byte[] getDisplay() {
		try {
			return lcd.getDisplay();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public byte[] getHWDisplay() {
		try {
			return lcd.getHWDisplay();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setContrast(int contrast) {
		try {
			lcd.setContrast(contrast);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx,
			int dy, int w, int h, int rop) {
		try {
			lcd.bitBlt(src, sw, sh, sx, sy, dx, dy, w, h, rop);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte[] dst,
			int dw, int dh, int dx, int dy, int w, int h, int rop) {
		try {
			lcd.bitBlt(src, sw, sh, sx, sy, dst, dw, dh, dx, dy, w, h, rop);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void setAutoRefresh(boolean on) {
		try {
			lcd.setAutoRefresh(on);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public int setAutoRefreshPeriod(int period) {
		try {
			return lcd.setAutoRefreshPeriod(period);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawChar(char c, int x, int y) {
		try {
			lcd.drawChar(c, x, y);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawString(String str, int x, int y, boolean inverted) {
		try {
			lcd.drawString(str, x, y, inverted);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawString(String str, int x, int y) {
		try {
			lcd.drawString(str, x, y);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawInt(int i, int x, int y) {
		try {
			lcd.drawInt(i, x, y);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void drawInt(int i, int places, int x, int y) {
		try {
			lcd.drawInt(i, places, x, y);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void clear(int x, int y, int n) {
		try {
			lcd.clear(x, y, n);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void clear(int y) {
		try {
			lcd.clear(y);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void scroll() {
		try {
			lcd.scroll();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public Font getFont() {
		try {
			return lcd.getFont();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getTextWidth() {
		try {
			return lcd.getTextWidth();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getTextHeight() {
		try {
			return lcd.getTextHeight();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
}

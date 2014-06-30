package lejos.remote.ev3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;

public class RemoteRequestGraphicsLCD implements GraphicsLCD {
	private ObjectInputStream is;
	private ObjectOutputStream os;

	public RemoteRequestGraphicsLCD(ObjectInputStream is, ObjectOutputStream os) {
		this.is = is;
		this.os = os;
	}

	@Override
	public void refresh() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_REFRESH;
		sendRequest(req, false);
	}

	@Override
	public void clear() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_CLEAR;
		sendRequest(req, false);
	}

	@Override
	public int getWidth() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_GET_WIDTH;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			EV3Reply reply = (EV3Reply) is.readObject();
			return reply.reply;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int getHeight() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_GET_HEIGHT;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			EV3Reply reply = (EV3Reply) is.readObject();
			return reply.reply;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public byte[] getDisplay() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_GET_DISPLAY;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			EV3Reply reply = (EV3Reply) is.readObject();
			return reply.contents;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public byte[] getHWDisplay() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_GET_HW_DISPLAY;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			EV3Reply reply = (EV3Reply) is.readObject();
			return reply.contents;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void setContrast(int contrast) {
		// Not implemented
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, int dx,
			int dy, int w, int h, int rop) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_BITBLT_1;
		req.byteData = src;
		req.intValue = sw;
		req.intValue2 = sh;
		req.intValue3 = sx;
		req.intValue4 = sy;
		req.intValue5 = dx;
		req.intValue6 = dy;
		req.intValue7 = w;
		req.intValue8 = h;
		req.intValue9 = rop;
		sendRequest(req, false);
	}

	@Override
	public void bitBlt(byte[] src, int sw, int sh, int sx, int sy, byte[] dst,
			int dw, int dh, int dx, int dy, int w, int h, int rop) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_BITBLT_2;
		req.byteData = src;
		req.intValue = sw;
		req.intValue2 = sh;
		req.intValue3 = sx;
		req.intValue4 = sy;
		req.byteData2 = dst;
		req.intValue5 = dw;
		req.intValue6 = dh;
		req.intValue7 = dx;
		req.intValue8 = dy;
		req.intValue9 = w;
		req.intValue10 = h;
		req.intValue11 = rop;
		sendRequest(req, false);
	}

	@Override
	public void setAutoRefresh(boolean on) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_SET_AUTO_REFRESH;
		req.flag = on;
		sendRequest(req, false);
	}

	@Override
	public int setAutoRefreshPeriod(int period) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_SET_AUTO_REFRESH;
		req.intValue = period;
		try {
			os.writeObject(req);
			return 0;
		} catch (IOException e) {
			return 0;
		}
	}

	@Override
	public void setPixel(int x, int y, int color) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = color;
		req.request = EV3Request.Request.LCD_G_SET_PIXEL;
		sendRequest(req, false);
	}

	@Override
	public int getPixel(int x, int y) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.request = EV3Request.Request.LCD_G_GET_PIXEL;
		return sendRequest(req, true).reply;
	}

	@Override
	public void drawString(String str, int x, int y, int anchor,
			boolean inverted) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = anchor;
		req.str = str;
		req.flag = inverted;
		req.request = EV3Request.Request.LCD_G_DRAW_STRING_INVERTED;
		sendRequest(req, false);
	}

	@Override
	public void drawString(String str, int x, int y, int anchor) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = anchor;
		req.str = str;
		req.request = EV3Request.Request.LCD_G_DRAW_STRING;
		sendRequest(req, false);
	}

	@Override
	public void drawSubstring(String str, int offset, int len, int x, int y,
			int anchor) {
		EV3Request req = new EV3Request();
		req.intValue = offset;
		req.intValue2 = x;
		req.intValue3 = y;
		req.intValue4 = anchor;
		req.str = str;
		req.request = EV3Request.Request.LCD_G_DRAW_SUBSTRING;
		sendRequest(req, false);
	}

	@Override
	public void drawChar(char character, int x, int y, int anchor) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = anchor;
		req.ch = character;
		req.request = EV3Request.Request.LCD_G_DRAW_CHAR;
		sendRequest(req, false);
	}

	@Override
	public void drawChars(char[] data, int offset, int length, int x, int y,
			int anchor) {
		EV3Request req = new EV3Request();
		req.intValue = offset;
		req.intValue2 = x;
		req.intValue3 = y;
		req.intValue4 = anchor;
		req.chars = data;
		req.request = EV3Request.Request.LCD_G_DRAW_CHARS;
		sendRequest(req, false);
	}

	@Override
	public int getStrokeStyle() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_G_GET_STROKE_STYLE;
		return sendRequest(req, true).reply;
	}

	@Override
	public void setStrokeStyle(int style) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_G_SET_STROKE_STYLE;
		sendRequest(req, false);
	}

	@Override
	public void drawRegionRop(Image src, int sx, int sy, int w, int h, int x,
			int y, int anchor, int rop) {
		EV3Request req = new EV3Request();
		req.image = src;
		req.intValue = sx;
		req.intValue2 = sy;
		req.intValue3 = w;
		req.intValue4 = h;
		req.intValue5 = x;
		req.intValue6 = y;
		req.intValue7 = anchor;
		req.intValue8 = rop;
		req.request = EV3Request.Request.LCD_G_DRAW_REGION_ROP;
		sendRequest(req, false);
	}

	@Override
	public void drawRegionRop(Image src, int sx, int sy, int w, int h,
			int transform, int x, int y, int anchor, int rop) {
		EV3Request req = new EV3Request();
		req.image = src;
		req.intValue = sx;
		req.intValue2 = sy;
		req.intValue3 = w;
		req.intValue4 = h;
		req.intValue5 = transform;
		req.intValue6 = x;
		req.intValue7 = y;
		req.intValue8 = anchor;
		req.intValue9 = rop;
		req.request = EV3Request.Request.LCD_G_DRAW_REGION_ROP_TRANSFORM;
		sendRequest(req, false);
	}

	@Override
	public void drawRegion(Image src, int sx, int sy, int w, int h,
			int transform, int x, int y, int anchor) {
		EV3Request req = new EV3Request();
		req.image = src;
		req.intValue = sx;
		req.intValue2 = sy;
		req.intValue3 = w;
		req.intValue4 = h;
		req.intValue5 = transform;
		req.intValue6 = x;
		req.intValue7 = y;
		req.intValue8 = anchor;
		req.request = EV3Request.Request.LCD_G_DRAW_REGION;
		sendRequest(req, false);
	}

	@Override
	public void drawImage(Image src, int x, int y, int anchor) {
		EV3Request req = new EV3Request();
		req.image = src;
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = anchor;
		req.request = EV3Request.Request.LCD_G_DRAW_IMAGE;
		sendRequest(req, false);
	}

	@Override
	public void drawLine(int x0, int y0, int x1, int y1) {
		EV3Request req = new EV3Request();
		req.intValue = x0;
		req.intValue2 = y0;
		req.intValue3 = x1;
		req.intValue4 = y1;
		req.request = EV3Request.Request.LCD_G_DRAW_LINE;
		sendRequest(req, false);
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = width;
		req.intValue4 = height;
		req.intValue5 = startAngle;
		req.intValue6 = arcAngle;
		req.request = EV3Request.Request.LCD_G_DRAW_ARC;
		sendRequest(req, false);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = width;
		req.intValue4 = height;
		req.intValue5 = startAngle;
		req.intValue6 = arcAngle;
		req.request = EV3Request.Request.LCD_G_FILL_ARC;
		sendRequest(req, false);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = width;
		req.intValue4 = height;
		req.intValue5 = arcWidth;
		req.intValue6 = arcHeight;
		req.request = EV3Request.Request.LCD_G_DRAW_ROUND_RECT;
		sendRequest(req, false);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = width;
		req.intValue4 = height;
		req.request = EV3Request.Request.LCD_G_DRAW_RECT;
		sendRequest(req, false);
	}

	@Override
	public void fillRect(int x, int y, int w, int h) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = w;
		req.intValue4 = h;
		req.request = EV3Request.Request.LCD_G_FILL_RECT;
		sendRequest(req, false);
	}

	@Override
	public void copyArea(int sx, int sy, int w, int h, int x, int y, int anchor) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.intValue3 = w;
		req.intValue4 = h;
		req.intValue5 = anchor;
		req.request = EV3Request.Request.LCD_G_COPY_AREA;
		sendRequest(req, false);
	}

	@Override
	public Font getFont() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFont(Font f) {
		// TODO Auto-generated method stub
	}

	@Override
	public void translate(int x, int y) {
		EV3Request req = new EV3Request();
		req.intValue = x;
		req.intValue2 = y;
		req.request = EV3Request.Request.LCD_G_TRANSLATE;
		sendRequest(req, false);
	}

	@Override
	public int getTranslateX() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_G_GET_TRANSLATE_X;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			EV3Reply reply = (EV3Reply) is.readObject();
			return reply.reply;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int getTranslateY() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.LCD_G_GET_TRANSLATE_Y;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			EV3Reply reply = (EV3Reply) is.readObject();
			return reply.reply;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void setColor(int rgb) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setColor(int red, int green, int blue) {
		// TODO Auto-generated method stub
	}
	
	private EV3Reply sendRequest(EV3Request req, boolean replyRequired) {
		EV3Reply reply = null;
		req.replyRequired = replyRequired;
		try {
			os.reset();
			os.writeObject(req);
			if (replyRequired) {
				reply = (EV3Reply) is.readObject();
				if (reply.e != null) throw new RemoteRequestException(reply.e);
			}
			return reply;
		} catch (Exception e) {
			throw new RemoteRequestException(e);
		}
	}
}

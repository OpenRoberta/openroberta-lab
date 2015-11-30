package lejos.ev3.startup;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

public class Keyboard {
	
	private TextLCD lcd = LocalEV3.get().getTextLCD();
	
	int x = 0, y = 5;
	
	String[] lower = {"!@#$%^&*()_+`~    ", 
					  "1234567890-=      ",
					  "qwertyuiop[]\\{}|  ",
					  "asdfghjkl;':\"     ",
					  "zxcvbnm,./<>?     ",
					  "U l x D           "};
	
	String[] upper = {"!@#$%^&*()_+`~    ", 
					  "1234567890-=      ",
					  "QWERTYUIOP[]\\{}|  ",
					  "ASDFGHJKL;':\"     ",
					  "ZXCVBNM,./<>?     ",
					  "U l x D           "};
	
	String[] lines = lower;
	
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
				if (++y > 6) y = 1;
			} else if (b == Button.ID_UP) {
				if (--y < 1) y = 6;
			} else if (b == Button.ID_LEFT) {
				if (--x < 0) x = 17;
			} else if (b == Button.ID_RIGHT) {
				if (++x > 17) x = 0;
			} else if (b == Button.ID_ENTER) {
				if (y < 6) sb.append(lines[y-1].charAt(x));
				else {
					switch (lines[5].charAt(x)) {
					case 'U': 
						lines = upper;
						display();
					    break;
					case 'l':
						lines = lower;
						display();
						break;
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
		Keyboard k = new Keyboard();
		
		String s = k.getString();
		System.out.println("String is " + s);
	}
}

package lejos.ev3.startup;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

// @formatter:off
// formatter would change keyboard layout
/**
 * Keyboard based on {@link Keyboard} , lejos class for wifi password input.
 * Robertakeyboard for token input, only capital characters and numbers.
 * 
 * @author dpyka
 */
public class RobertaKeyboard
{

	private final TextLCD lcd = LocalEV3.get().getTextLCD();

	public RobertaKeyboard()
	{
		//
	}

	int x = 0, y = 5;

	String[] upper = { "0123456789        ",
	                   "QWERTZUIOP        ",
	                   "ASDFGHJKL         ",
	                   "YXCVBNM           ",
			           "x D           " };

	String[] lines = this.upper;

	void display()
	{
		// LCD.drawString("Keyboard", 4, 0);
		this.lcd.clear();
		for (int i = 0; i < this.lines.length; i++)
		{
			this.lcd.drawString(this.lines[i], 0, i + 1);
		}
		displayCursor(true);
	}

	void displayCursor(boolean inverted)
	{
		this.lcd.drawString(this.lines[this.y - 1].substring(this.x, this.x + 1), this.x, this.y, inverted);
	}

	/**
	 * Returns the token that was created via RobertaKeyboard.
	 * 
	 * @return token as String
	 */
	String getString()
	{
		StringBuilder sb = new StringBuilder();
		this.x = 0;
		this.y = 5;
		display();

		while (true)
		{
			int b = Button.waitForAnyPress();

			displayCursor(false);

			if (b == Button.ID_DOWN)
			{
				if (++this.y > 5)
				{
					this.y = 1;
				}
			}
			else if (b == Button.ID_UP)
			{
				if (--this.y < 1)
				{
					this.y = 5;
				}
			}
			else if (b == Button.ID_LEFT)
			{
				if (--this.x < 0)
				{
					this.x = 9;
				}
			}
			else if (b == Button.ID_RIGHT)
			{
				if (++this.x > 9)
				{
					this.x = 0;
				}
			}
			else if (b == Button.ID_ENTER)
			{
				if (this.y < 5)
				{
					sb.append(this.lines[this.y - 1].charAt(this.x));
				}
				else
				{
					switch (this.lines[4].charAt(this.x))
					{
					case 'x':
						if (sb.length() > 0)
						{
							sb.deleteCharAt(sb.length() - 1);
							this.lcd.drawString(" ", sb.length(), 7);
						}
						else
						{
							Sound.buzz();
						}
						break;
					case 'D':
						return sb.toString();
					}
				}
			}
			else if (b == Button.ID_ESCAPE)
			{
				return "";
			}

			displayCursor(true);
			String s = sb.toString();
			if (s.length() > 18)
			{
				s = s.substring(s.length() - 18, s.length());
			}
			this.lcd.drawString(s, 0, 7);
		}
	}

}
// @formatter:on

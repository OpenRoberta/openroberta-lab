package de.fraunhofer.roberta;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

public class RobertaGUI
{

	private final GraphicsLCD display;
	private final int width;
	private final int height;
	
	public RobertaGUI()
	{
		this.display = LocalEV3.get().getGraphicsLCD();
		width = display.getWidth();
		height = display.getHeight();
	}
	
	public void clearDisplay()
	{
		display.clear();
		display.refresh();
	}
	
	private void displayLogo()
	{
		display.setFont(Font.getLargeFont());
		display.drawString("Roberta", width/2, height/2, GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
	}
	
	public void refresh()
	{
		display.refresh();
	}
	
	public void robertaActive()
	{
		clearDisplay();
		Button.LEDPattern(2);
		displayLogo();
	}
	
	public void robertaPassive()
	{
		Button.LEDPattern(0);
		clearDisplay();
	}
}

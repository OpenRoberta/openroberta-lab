import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class EV3HelloWorld
{
    public static void main(String[] args)
    {
    	LCD.drawString("Beispiel", 0, 2);
    	LCD.drawString("Programm", 0, 3);
    	Button.LEDPattern(4);
    	Delay.msDelay(3000);
    }
}

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class EV3HelloWorld {
    public static void main(String[] args) {
        LCD.clear();
        LCD.drawString("Hallo Welt", 0, 3);
        Button.waitForAnyPress();
        LCD.clear();
        LCD.refresh();
    }
}

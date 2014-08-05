import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class Main {

    public static void main(String[] args) {
        LCD.clear();
        LCD.drawString("Hello World!", 0, 3);
        Button.waitForAnyPress();
        LCD.clear();
        LCD.refresh();
        System.out.println("Hello World!");
    }
}

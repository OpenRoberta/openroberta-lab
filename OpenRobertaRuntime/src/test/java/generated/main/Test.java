package generated.main;

import de.fhg.iais.roberta.ast.syntax.action.BlinkMode;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.brickconfiguration.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class Test {
    private final EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder().build();

    public static void main(String[] args) {
        new Test().run();
    }

    public void run() {
        Hal hal = new Hal(this.brickConfiguration);
        hal.ledOn(BrickLedColor.GREEN, BlinkMode.FLASH);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}
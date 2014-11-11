package generated.main;

import de.fhg.iais.roberta.ast.syntax.EV3BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.action.BlinkMode;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class blinker2 {
    private final EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder().build();

    public static void main(String[] args) {
        new blinker2().run();
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

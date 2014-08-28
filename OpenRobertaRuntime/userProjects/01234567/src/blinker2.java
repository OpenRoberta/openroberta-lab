package generated.main;
 
import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.codegen.lejos.Hal;
 
public class blinker2 {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
 
    public static void main(String[] args) {
        new blinker2().run();
    }
 
    public void run() {
        Hal hal = new Hal(this.brickConfiguration);
        hal.ledOn(BrickLedColor.GREEN, true);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}
package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.codegen.lejos.Hal;

public class test999 {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .build();

    public static void main(String[] args) {
        new test999().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        
        hal.drawText("Hallo", 0, 0);
        hal.playTone(300, 2000);
        hal.ledOn(BrickLedColor.GREEN, false);
        hal.setVolume(50);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}

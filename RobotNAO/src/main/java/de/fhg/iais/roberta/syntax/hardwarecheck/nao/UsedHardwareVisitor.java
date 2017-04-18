package de.fhg.iais.roberta.syntax.hardwarecheck.nao;

import java.util.ArrayList;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class UsedHardwareVisitor extends CheckVisitor {

    private boolean radioUsed;
    private boolean accelerometerUsed;
    private boolean greyScale;

    public UsedHardwareVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        check(phrasesSet);
    }

    private void check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(phrasesSet.size() >= 1);
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(this);
            }
        }
    }

    public boolean isRadioUsed() {
        return this.radioUsed;
    }

    public boolean isAccelerometerUsed() {
        return this.accelerometerUsed;
    }

    public boolean isGreyScale() {
        return this.greyScale;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.accelerometerUsed = true;
        return null;
    }

}

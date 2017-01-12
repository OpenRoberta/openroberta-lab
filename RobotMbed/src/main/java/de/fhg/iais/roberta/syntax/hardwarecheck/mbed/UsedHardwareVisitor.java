package de.fhg.iais.roberta.syntax.hardwarecheck.mbed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinWriteValueSensor;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.expr.Image;
import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.PinValueSensor;
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

    private Set<Integer> pinsUsed = new HashSet<Integer>();

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

    public Set<Integer> getUsedPins() {
        return this.pinsUsed;
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        displayTextAction.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        displayImageAction.getValuesToDisplay().visit(this);
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        radioSendAction.getMsg().visit(this);
        this.radioUsed = true;
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        this.radioUsed = true;
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.accelerometerUsed = true;
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.accelerometerUsed = true;
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        this.greyScale = true;
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().visit(this);
        rgbColor.getG().visit(this);
        rgbColor.getB().visit(this);
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        this.pinsUsed.add(pinTouchSensor.getPinNumber());
        return null;
    }

    @Override
    public Void visitPinValueSensor(PinValueSensor<Void> pinValueSensor) {
        this.pinsUsed.add(pinValueSensor.getPinNumber());
        return null;
    }

    @Override
    public Void visitPinWriteValueSensor(PinWriteValueSensor<Void> pinWriteValueSensor) {
        this.pinsUsed.add(pinWriteValueSensor.getPinNumber());
        return null;
    }

}

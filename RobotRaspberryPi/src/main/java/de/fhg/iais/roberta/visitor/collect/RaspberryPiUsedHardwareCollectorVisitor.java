package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedBlinkAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedDimAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedGetAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedSetAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorHexString;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class RaspberryPiUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IRaspberryPiVisitor<Void> {

    public RaspberryPiUsedHardwareCollectorVisitor(
        UsedHardwareBean.Builder builder,
        ArrayList<ArrayList<Phrase<Void>>> phrasesSet,
        ConfigurationAst brickConfiguration) {
        super(builder, brickConfiguration);
    }

    @Override
    public Void visitColorHexString(ColorHexString<Void> colorHexString) {
        return null;
    }

    @Override
    public Void visitLedSetAction(LedSetAction<Void> ledBarSetAction) {
        return null;
    }

    @Override
    public Void visitLedBlinkAction(LedBlinkAction<Void> ledBlinkAction) {
        return null;
    }

    @Override
    public Void visitLedDimAction(LedDimAction<Void> ledDimAction) {
        return null;
    }

    @Override
    public Void visitLedGetAction(LedGetAction<Void> ledGetAction) {
        return null;
    }

}

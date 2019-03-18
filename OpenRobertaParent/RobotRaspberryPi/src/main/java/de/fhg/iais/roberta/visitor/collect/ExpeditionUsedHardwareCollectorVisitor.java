package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.expedition.LedBlinkAction;
import de.fhg.iais.roberta.syntax.action.expedition.LedDimAction;
import de.fhg.iais.roberta.syntax.action.expedition.LedGetAction;
import de.fhg.iais.roberta.syntax.action.expedition.LedSetAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorHexString;
import de.fhg.iais.roberta.visitor.hardware.IExpeditionVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class ExpeditionUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IExpeditionVisitor<Void> {

    public ExpeditionUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration brickConfiguration) {
        super(brickConfiguration);
        check(phrasesSet);
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

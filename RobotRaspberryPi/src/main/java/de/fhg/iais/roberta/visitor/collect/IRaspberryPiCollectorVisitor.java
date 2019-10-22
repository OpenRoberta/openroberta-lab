package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.action.raspberrypi.LedBlinkAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedDimAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedGetAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedSetAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorHexString;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;

/**
 * Collector for the Raspberry Pi.
 * Adds the blocks missing from the defaults of {@link ICollectorVisitor}.
 * Defines the specific parent implementation to use (the one of the collector) due to unrelated defaults.
 */
public interface IRaspberryPiCollectorVisitor extends ICollectorVisitor, IRaspberryPiVisitor<Void> {

    @Override
    default Void visitColorHexString(ColorHexString<Void> colorHexString) {
        return null;
    }

    @Override
    default Void visitLedSetAction(LedSetAction<Void> ledSetAction) {
        ledSetAction.getBrightness().accept(this);
        return null;
    }

    @Override
    default Void visitLedBlinkAction(LedBlinkAction<Void> ledBlinkAction) {
        ledBlinkAction.getDuration().accept(this);
        ledBlinkAction.getFrequency().accept(this);
        return null;
    }

    @Override
    default Void visitLedDimAction(LedDimAction<Void> ledDimAction) {
        ledDimAction.getDuration().accept(this);
        ledDimAction.getFrom().accept(this);
        ledDimAction.getTo().accept(this);
        return null;
    }

    @Override
    default Void visitLedGetAction(LedGetAction<Void> ledGetAction) {
        return null;
    }

    // following methods are used to specify unrelated defaults
}

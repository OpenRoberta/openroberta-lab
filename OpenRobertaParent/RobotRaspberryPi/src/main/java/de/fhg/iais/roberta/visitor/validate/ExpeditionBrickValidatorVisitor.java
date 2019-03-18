package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.expedition.LedBlinkAction;
import de.fhg.iais.roberta.syntax.action.expedition.LedDimAction;
import de.fhg.iais.roberta.syntax.action.expedition.LedGetAction;
import de.fhg.iais.roberta.syntax.action.expedition.LedSetAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorHexString;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.visitor.hardware.IExpeditionVisitor;

public final class ExpeditionBrickValidatorVisitor extends AbstractBrickValidatorVisitor implements IExpeditionVisitor<Void> {

    public ExpeditionBrickValidatorVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
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

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
    }
}

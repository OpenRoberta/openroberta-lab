package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class RaspberryPiUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IRaspberryPiCollectorVisitor {
    public RaspberryPiUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }

    @Override
    public Void visitIntentStmt(IntentStmt<Void> intentStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSlotSensor(SlotSensor<Void> slotSensor) {
        // TODO Auto-generated method stub
        return null;
    }
}

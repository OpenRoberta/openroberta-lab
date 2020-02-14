package de.fhg.iais.roberta.visitor.collect;
import de.fhg.iais.roberta.bean.UsedMethodBean;

public class BotnrollUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IBotnrollCollectorVisitor {
    public BotnrollUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}

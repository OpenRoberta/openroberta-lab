package de.fhg.iais.roberta.visitor.collect;
import de.fhg.iais.roberta.bean.UsedMethodBean;

public class MbotUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IMbotCollectorVisitor {
    public MbotUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}

package de.fhg.iais.roberta.visitor.collect;
import de.fhg.iais.roberta.bean.UsedMethodBean;

public class ArduinoUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IArduinoCollectorVisitor {
    public ArduinoUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}

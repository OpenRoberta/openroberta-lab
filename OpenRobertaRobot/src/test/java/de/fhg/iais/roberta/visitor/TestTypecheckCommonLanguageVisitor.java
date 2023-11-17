package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;

public class TestTypecheckCommonLanguageVisitor extends TypecheckCommonLanguageVisitor {
    public TestTypecheckCommonLanguageVisitor(UsedHardwareBean usedHardwareBean) {
        super(usedHardwareBean);
    }
}

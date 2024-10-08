package de.fhg.iais.roberta.worker.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.Ev3TypecheckVisitor;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;
import de.fhg.iais.roberta.worker.AbstractTypecheckWorker;

public class Ev3TypecheckWorker extends AbstractTypecheckWorker {
    @Override
    public TypecheckCommonLanguageVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new Ev3TypecheckVisitor(usedHardwareBean);
    }
}

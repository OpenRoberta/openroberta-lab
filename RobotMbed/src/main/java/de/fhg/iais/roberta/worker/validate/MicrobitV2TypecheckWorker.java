package de.fhg.iais.roberta.worker.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.MicrobitV2TypecheckVisitor;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;
import de.fhg.iais.roberta.worker.AbstractTypecheckWorker;

public class MicrobitV2TypecheckWorker extends AbstractTypecheckWorker {
    @Override
    protected TypecheckCommonLanguageVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new MicrobitV2TypecheckVisitor(usedHardwareBean);
    }
}

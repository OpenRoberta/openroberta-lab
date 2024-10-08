package de.fhg.iais.roberta.worker.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;
import de.fhg.iais.roberta.visitor.validate.WedoTypecheckVisitor;
import de.fhg.iais.roberta.worker.AbstractTypecheckWorker;

public class WedoTypecheckWorker extends AbstractTypecheckWorker {

    @Override
    public TypecheckCommonLanguageVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new WedoTypecheckVisitor(usedHardwareBean);
    }
}

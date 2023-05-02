package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.MbedThree2ThreeOneTransformerVisitor;
import de.fhg.iais.roberta.visitor.TransformerVisitor;

public class MbedThree2ThreeOneTransformerWorker extends Three2ThreeOneTransformerWorker {

    @Override
    protected TransformerVisitor getVisitor(Project project, NewUsedHardwareBean.Builder builder, ConfigurationAst configuration) {
        return new MbedThree2ThreeOneTransformerVisitor(builder, project.getRobotFactory().getBlocklyDropdownFactory(), configuration);
    }
}

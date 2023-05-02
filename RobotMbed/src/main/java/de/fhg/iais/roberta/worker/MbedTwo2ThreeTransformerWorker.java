package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.MbedTwo2ThreeTransformerVisitor;
import de.fhg.iais.roberta.visitor.TransformerVisitor;

public class MbedTwo2ThreeTransformerWorker extends Two2ThreeTransformerWorker {

    @Override
    protected TransformerVisitor getVisitor(Project project, NewUsedHardwareBean.Builder builder, ConfigurationAst configuration) {
        return new MbedTwo2ThreeTransformerVisitor(
            new MbedTwo2ThreeTransformerHelper(project.getRobotFactory().getBlocklyDropdownFactory(), configuration),
            builder,
            project.getRobotFactory().getBlocklyDropdownFactory(),
            configuration);
    }
}

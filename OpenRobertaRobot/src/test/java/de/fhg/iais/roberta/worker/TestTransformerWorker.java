package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.TestTransformerVisitor;
import de.fhg.iais.roberta.visitor.TransformerVisitor;

public class TestTransformerWorker extends AbstractTransformerWorker {

    public TestTransformerWorker() {
        super("0.0", "2.0", "4.0");
    }

    @Override
    protected TransformerVisitor getVisitor(Project project, NewUsedHardwareBean.Builder builder, ConfigurationAst configuration) {
        return new TestTransformerVisitor(project.getRobotFactory().getBlocklyDropdownFactory());
    }
}

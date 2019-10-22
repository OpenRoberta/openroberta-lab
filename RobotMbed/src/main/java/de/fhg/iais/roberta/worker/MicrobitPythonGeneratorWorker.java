package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.MicrobitConfiguration;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.MicrobitPythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public class MicrobitPythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        return new MicrobitPythonVisitor(
            usedHardwareBean,
            codeGeneratorSetupBean,
            new MicrobitConfiguration.Builder().build(),
            project.getProgramAst().getTree());
    }
}

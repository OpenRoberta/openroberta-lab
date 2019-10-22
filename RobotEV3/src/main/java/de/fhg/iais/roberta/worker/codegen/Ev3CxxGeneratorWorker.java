package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.Ev3C4ev3Visitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.worker.AbstractLanguageGeneratorWorker;

public final class Ev3CxxGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        return new Ev3C4ev3Visitor(
            usedHardwareBean,
            codeGeneratorSetupBean,
            project.getProgramName(),
            project.getConfigurationAst(),
            project.getProgramAst().getTree(),
            project.getLanguage());
    }
}

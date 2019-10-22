package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.CalliopeConfiguration;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.CalliopeCppVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public class CalliopeCxxGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        return new CalliopeCppVisitor(usedHardwareBean, codeGeneratorSetupBean, new CalliopeConfiguration.Builder().build(), project.getProgramAst().getTree());
    }
}

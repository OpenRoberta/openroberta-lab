package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.visitor.codegen.VorwerkPythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public final class VorwerkPythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        // TODO workaround: Vorwerk currently has a combination of a hard coded configuration in VorwerkConfiguration and one in the blockly
        // TODO configuration block (ip, port, username, password). The front end configuration is read and created in the project. However, for the hardware
        // TODO checking the actual hardcoded configuration is needed. This should be removed once the configuration is correctly saved in the default xml.
        return new VorwerkPythonVisitor(
            usedHardwareBean,
            codeGeneratorSetupBean,
            new VorwerkConfiguration(project.getConfigurationAst().getIpAddress(),
                                     project.getConfigurationAst().getPortNumber(),
                                     project.getConfigurationAst().getUserName(),
                                     project.getConfigurationAst().getPassword()),
            project.getProgramAst().getTree());
    }
}

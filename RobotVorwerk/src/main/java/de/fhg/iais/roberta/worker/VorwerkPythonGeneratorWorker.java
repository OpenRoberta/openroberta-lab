package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.visitor.codegen.VorwerkPythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public final class VorwerkPythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean> beans) {
        // TODO workaround: Vorwerk currently has a combination of a hard coded configuration in VorwerkConfiguration and one in the blockly
        // TODO configuration block (ip, port, username, password). The front end configuration is read and created in the project. However, for the hardware
        // TODO checking the actual hardcoded configuration is needed. This should be removed once the configuration is correctly saved in the default xml.
        return new VorwerkPythonVisitor(
            project.getProgramAst().getTree(),
            new ConfigurationAst.Builder()
                .setIpAddress(project.getConfigurationAst().getIpAddress())
                .setUserName(project.getConfigurationAst().getUserName())
                .setPassword(project.getConfigurationAst().getPassword())
                .build(VorwerkConfiguration.class),
            beans);
    }
}

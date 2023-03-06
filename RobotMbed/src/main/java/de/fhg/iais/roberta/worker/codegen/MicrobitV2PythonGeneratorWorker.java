package de.fhg.iais.roberta.worker.codegen;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.MicrobitPythonVisitor;
import de.fhg.iais.roberta.visitor.codegen.MicrobitV2PythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.worker.AbstractLanguageGeneratorWorker;

public class MicrobitV2PythonGeneratorWorker extends AbstractLanguageGeneratorWorker {
    @Override
    protected AbstractLanguageVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean> beans) {
        return new MicrobitV2PythonVisitor(project.getProgramAst().getTree(), project.getConfigurationAst(), beans);
    }
}

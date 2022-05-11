package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.RobotinoROSPythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public final class RobotinoROSPythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean> beans) {
        return new RobotinoROSPythonVisitor(project.getProgramAst().getTree(), beans, project.getConfigurationAst());
    }
}

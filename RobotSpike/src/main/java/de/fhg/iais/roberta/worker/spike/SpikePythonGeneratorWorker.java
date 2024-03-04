package de.fhg.iais.roberta.worker.spike;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.visitor.spike.SpikePythonVisitor;
import de.fhg.iais.roberta.worker.AbstractLanguageGeneratorWorker;

public final class SpikePythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean> beans) {
        return new SpikePythonVisitor(project.getProgramAst().getTree(), beans, project.getConfigurationAst());
    }
}

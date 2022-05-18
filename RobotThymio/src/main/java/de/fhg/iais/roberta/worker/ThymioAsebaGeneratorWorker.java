package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.AbstractAsebaVisitor;
import de.fhg.iais.roberta.visitor.ThymioAsebaVisitor;

public final class ThymioAsebaGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractAsebaVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean> beans) {
        return new ThymioAsebaVisitor(project.getProgramAst().getTree(), beans, project.getConfigurationAst());
    }
}

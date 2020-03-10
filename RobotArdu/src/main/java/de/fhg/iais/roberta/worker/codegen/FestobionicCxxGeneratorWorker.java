package de.fhg.iais.roberta.worker.codegen;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.FestobionicCppVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.worker.AbstractLanguageGeneratorWorker;

public class FestobionicCxxGeneratorWorker extends AbstractLanguageGeneratorWorker {
    @Override
    protected AbstractLanguageVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean> beans) {
        return new FestobionicCppVisitor(project.getProgramAst().getTree(), project.getConfigurationAst(), beans);
    }
}

package de.fhg.iais.roberta.worker.codegen;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.MbedV2TextlyJavaVisitor;
import de.fhg.iais.roberta.visitor.codegen.MicrobitV2PythonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractTextlyJavaVisitor;
import de.fhg.iais.roberta.worker.AbstractLanguageGeneratorWorker;
import de.fhg.iais.roberta.worker.RegenerateNepoWorker;

public class MicrobitV2RegenerateNepoWorker extends RegenerateNepoWorker {
    @Override
    protected AbstractTextlyJavaVisitor getVisitorForTextlyJava(Project project) {
        return new MbedV2TextlyJavaVisitor(project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}

package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.WeDoRegenerateTextlyJavaVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractRegenerateTextlyJavaVisitor;
import de.fhg.iais.roberta.worker.RegenerateNepoWorker;

public class WedoRegenerateNepoWorker extends RegenerateNepoWorker {
    @Override
    protected AbstractRegenerateTextlyJavaVisitor getVisitorForTextlyJava(Project project) {
        return new WeDoRegenerateTextlyJavaVisitor(project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}

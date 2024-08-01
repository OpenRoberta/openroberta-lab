package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.Ev3RegenerateTextlyJavaVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractRegenerateTextlyJavaVisitor;
import de.fhg.iais.roberta.worker.RegenerateNepoWorker;

public class Ev3RegenerateNepoWorker extends RegenerateNepoWorker {
    @Override
    protected AbstractRegenerateTextlyJavaVisitor getVisitorForTextlyJava(Project project) {
        return new Ev3RegenerateTextlyJavaVisitor(project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}

package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.collect.AbstractUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;

/**
 * Uses the {@link AbstractUsedHardwareCollectorVisitor} to visit the current AST and collect all used hardware.
 * Data collected is stored in the {@link UsedHardwareBean}.
 * Currently does more than just collect the used hardware, e.g. also handles global variables.
 */
public abstract class AbstractUsedHardwareCollectorWorker implements IWorker {

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic.
     * Could be removed in the future, when visitors are specified in the properties as well, or inferred from the worker name.
     *
     * @param builder the used hardware bean builder
     * @param project the project
     * @return the appropriate visitor for the current robot
     */
    protected abstract AbstractCollectorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project);

    @Override
    public void execute(Project project) {
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();
        AbstractCollectorVisitor visitor = getVisitor(builder, project);
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        collectGlobalVariables(tree, visitor);
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                    builder.setProgramEmpty(phrases.size() == 2);
                } else {
                    phrase.accept(visitor);
                }
            }
        }
        UsedHardwareBean bean = builder.build();
        project.addWorkerResult("CollectedHardware", bean);
    }

    protected static void collectGlobalVariables(List<ArrayList<Phrase<Void>>> phrasesSet, IVisitor<Void> visitor) {
        for ( List<Phrase<Void>> phrases : phrasesSet ) {
            Phrase<Void> phrase = phrases.get(1);
            if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                phrase.accept(visitor);
            }
        }
    }
}

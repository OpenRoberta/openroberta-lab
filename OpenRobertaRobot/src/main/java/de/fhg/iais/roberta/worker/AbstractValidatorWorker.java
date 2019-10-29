package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;

/**
 * Uses the {@link AbstractProgramValidatorVisitor} to visit the current AST and validate the hardware.
 * May also annotate the AST to add information about inconsistencies in it.
 */
public abstract class AbstractValidatorWorker implements IWorker {

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic.
     * Could be removed in the future, when visitors are specified in the properties as well, or inferred from the worker name.
     *
     * @param builder the used hardware bean builder
     * @param project the project
     * @return the appropriate visitor for the current robot
     */
    protected abstract AbstractProgramValidatorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project);

    /**
     * Returns the bean name. Used by subclasses to keep the execute method generic.
     * Specifies the bean name to be used by the abstract execute.
     *
     * @return the bean name
     */
    protected abstract String getBeanName();

    @Override
    public void execute(Project project) {
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();
        AbstractProgramValidatorVisitor visitor = getVisitor(builder, project);
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        // workaround: because methods in the tree may use global variables before the main task is
        // reached within the tree, the variables may not exist yet and show up as not declared
        collectGlobalVariables(tree, visitor);
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.accept(visitor);
            }
        }

        int errorCounter = visitor.getErrorCount();
        if ( errorCounter > 0 ) {
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            project.addToErrorCounter(errorCounter);
        }
    }

    private void collectGlobalVariables(List<ArrayList<Phrase<Void>>> phrasesSet, IVisitor<Void> visitor) {
        for ( List<Phrase<Void>> phrases : phrasesSet ) {
            Phrase<Void> phrase = phrases.get(1);
            if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                phrase.accept(visitor);
            }
        }
    }
}

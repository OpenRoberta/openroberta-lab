package de.fhg.iais.roberta.worker;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
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

    // TODO should be final, check "ArduinoConfigurationValidatorWorker"
    @Override
    public void execute(Project project) {
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();
        AbstractProgramValidatorVisitor visitor = this.getVisitor(project, ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        List<List<Phrase<Void>>> tree = project.getProgramAst().getTree();
        // workaround: because methods in the tree may use global variables before the main task is
        // reached within the tree, the variables may not exist yet and show up as not declared
        collectGlobalVariables(tree, visitor);
        for ( Iterable<Phrase<Void>> phrases : tree ) {
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

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic.
     * Could be removed in the future, when visitors are specified in the properties as well, or inferred from the worker name.
     *
     * @param project the project
     * @param beanBuilders a map of available bean builders, may be empty
     * @return the appropriate visitor for the current robot
     */
    protected abstract AbstractProgramValidatorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders);

    private void collectGlobalVariables(Iterable<List<Phrase<Void>>> phrasesSet, IVisitor<Void> visitor) {
        for ( List<Phrase<Void>> phrases : phrasesSet ) {
            Phrase<Void> phrase = phrases.get(1);
            if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                phrase.accept(visitor);
            }
        }
    }
}

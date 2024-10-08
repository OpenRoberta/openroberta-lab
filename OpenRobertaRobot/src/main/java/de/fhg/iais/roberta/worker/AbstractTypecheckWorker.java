package de.fhg.iais.roberta.worker;

import java.util.List;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.NepoInfoProcessor;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.TypecheckCommonLanguageVisitor;

/**
 * Uses the {@link TypecheckCommonLanguageVisitor} to visit the current AST and typecheck all items contained.
 */
public abstract class AbstractTypecheckWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        UsedHardwareBean usedHardwareBean = project.getWorkerResult(UsedHardwareBean.class);
        TypecheckCommonLanguageVisitor visitor = this.getVisitor(project, usedHardwareBean);

        int errors = 0;
        for ( List<Phrase> listOfPhrases : project.getProgramAst().getTree() ) {
            for ( Phrase phrase : listOfPhrases ) {
                phrase.accept(visitor);
                int errorsOfThisPhrase = NepoInfoProcessor.collectNepoErrors(phrase).size();
                errors += errorsOfThisPhrase;
            }
        }
        if ( errors > 0 ) {
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            project.addToErrorCounter(errors, null);
        } else {
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
        }
    }

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic. Could be removed in the future, when visitors are
     * specified in the properties as well, or inferred from the worker name.
     *
     * @param project the project
     * @param usedHardwareBean the used hardware bean
     * @return the appropriate visitor for the current robot
     */
    public abstract TypecheckCommonLanguageVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean);
}

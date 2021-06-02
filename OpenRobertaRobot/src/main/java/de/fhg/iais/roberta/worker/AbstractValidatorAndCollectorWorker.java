package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap.Builder;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.IProjectBean.IBuilder;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

import java.util.Collections;
import java.util.List;

/**
 * Uses the {@link AbstractProgramValidatorVisitor} to visit the current AST and validate the hardware. May also annotate the AST to add information about
 * inconsistencies in it.
 */
public abstract class AbstractValidatorAndCollectorWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        Builder<IProjectBean.IBuilder<?>> mapBuilder = new ImmutableClassToInstanceMap.Builder<>();
        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();
        mapBuilder.put(UsedMethodBean.Builder.class, usedMethodBeanBuilder);
        UsedHardwareBean.Builder usedHardwareBeanBuilder = new UsedHardwareBean.Builder();
        mapBuilder.put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder);
        ImmutableClassToInstanceMap<IBuilder<?>> map = mapBuilder.build();
        CommonNepoValidatorAndCollectorVisitor visitor = this.getVisitor(project, mapBuilder.build());
        List<List<Phrase<Void>>> tree = project.getProgramAst().getTree();
        // workaround: because methods in the tree may use global variables before the main task is
        // reached within the tree, the variables may not exist yet and show up as not declared
        collectGlobalVariables(tree, visitor);
        for ( List<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                    usedHardwareBeanBuilder.setProgramEmpty(phrases.size() == 2);
                } else {
                    phrase.accept(visitor);
                }
            }
        }
        usedMethodBeanBuilder.addAdditionalEnums(getAdditionalMethodEnums());

        project.addWorkerResult(map.get(UsedMethodBean.Builder.class).build());
        project.addWorkerResult(map.get(UsedHardwareBean.Builder.class).build());

        int errorCounter = visitor.getErrorCount();
        if ( errorCounter > 0 ) {
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            project.addToErrorCounter(errorCounter, visitor.getErrorAndWarningMessages());
        }
    }

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic. Could be removed in the future, when visitors are
     * specified in the properties as well, or inferred from the worker name.
     *
     * @param project the project
     * @param beanBuilders a map of available bean builders, may be empty
     * @return the appropriate visitor for the current robot
     */
    protected abstract CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders);

    private void collectGlobalVariables(Iterable<List<Phrase<Void>>> phrasesSet, IVisitor<Void> visitor) {
        for ( List<Phrase<Void>> phrases : phrasesSet ) {
            Phrase<Void> phrase = phrases.get(1);
            if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                phrase.accept(visitor);
            }
        }
    }

    /**
     * Returns additional enums which should be added to the used method collection. Used by subclasses to keep the execute method generic.
     *
     * @return the additional enums required
     */
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.emptyList();
    }
}

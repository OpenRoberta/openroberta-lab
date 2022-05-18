package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap.Builder;

import de.fhg.iais.roberta.bean.ErrorAndWarningBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.IProjectBean.IBuilder;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

/**
 * Uses the {@link AbstractProgramValidatorVisitor} to visit the current AST and validate it. May also annotate the AST to add information about
 * inconsistencies in it.
 */
public abstract class AbstractValidatorAndCollectorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        Builder<IProjectBean.IBuilder> mapBuilder = new ImmutableClassToInstanceMap.Builder();

        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();
        mapBuilder.put(UsedMethodBean.Builder.class, usedMethodBeanBuilder);

        UsedHardwareBean.Builder usedHardwareBeanBuilder = new UsedHardwareBean.Builder();
        mapBuilder.put(UsedHardwareBean.Builder.class, usedHardwareBeanBuilder);

        ErrorAndWarningBean.Builder errorAndWarningBeanBuilder = new ErrorAndWarningBean.Builder();
        mapBuilder.put(ErrorAndWarningBean.Builder.class, errorAndWarningBeanBuilder);

        NNBean.Builder nnBeanBuilder = new NNBean.Builder();
        mapBuilder.put(NNBean.Builder.class, nnBeanBuilder);

        ImmutableClassToInstanceMap<IBuilder> map = mapBuilder.build();

        CommonNepoValidatorAndCollectorVisitor visitor = this.getVisitor(project, map);
        List<List<Phrase>> tree = project.getProgramAst().getTree();
        // workaround: because methods in the tree may use global variables before the main task is
        // reached within the tree, the variables may not exist yet and show up as not declared
        collectGlobalVariables(tree, visitor);
        for ( List<Phrase> phrases : tree ) {
            for ( Phrase phrase : phrases ) {
                if ( phrase.hasName("MAIN_TASK") ) {
                    usedHardwareBeanBuilder.setProgramEmpty(phrases.size() == 2);
                } else {
                    phrase.accept(visitor);
                }
            }
        }
        usedMethodBeanBuilder.addAdditionalEnums(getAdditionalMethodEnums());

        project.addWorkerResult(map.get(UsedMethodBean.Builder.class).build());
        project.addWorkerResult(map.get(UsedHardwareBean.Builder.class).build());
        project.addWorkerResult(map.get(NNBean.Builder.class).build());

        ErrorAndWarningBean errorAndWarningBean = errorAndWarningBeanBuilder.build();
        int errorCounter = errorAndWarningBean.getErrorCount();
        if ( errorCounter > 0 ) {
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            project.addToErrorCounter(errorCounter, errorAndWarningBean.getErrorAndWarningMessages());
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
    protected abstract CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders);

    protected void collectGlobalVariables(Iterable<List<Phrase>> phrasesSet, IVisitor<Void> visitor) {
        for ( List<Phrase> phrases : phrasesSet ) {
            Phrase phrase = phrases.get(1);
            if ( phrase.hasName("MAIN_TASK") ) {
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

package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.collect.AbstractUsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;

/**
 * Uses the {@link AbstractUsedMethodCollectorVisitor} to visit the current AST and collect all used methods.
 * Data collected is stored in the {@link UsedMethodBean}.
 */
public abstract class AbstractUsedMethodCollectorWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        UsedMethodBean.Builder builder = new UsedMethodBean.Builder();
        ICollectorVisitor visitor = this.getVisitor(builder);
        Iterable<List<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( Iterable<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.accept(visitor);
            }
        }
        builder.addAdditionalEnums(this.getAdditionalMethodEnums());
        UsedMethodBean bean = builder.build();
        project.appendWorkerResult(bean);
    }

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic.
     * Could be removed in the future, when visitors are specified in the properties as well, or inferred from the worker name.
     *
     * @param builder the used hardware bean builder
     * @return the appropriate visitor for the current robot
     */
    protected abstract ICollectorVisitor getVisitor(UsedMethodBean.Builder builder);

    /**
     * Returns additional enums which should be added to the used method collection. Used by subclasses to keep the execute method generic.
     *
     * @return the additional enums required
     */
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.emptyList();
    }
}

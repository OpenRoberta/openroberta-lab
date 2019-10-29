package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean.Builder;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.collect.AbstractUsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;

/**
 * Uses the {@link AbstractUsedMethodCollectorVisitor} to visit the current AST and collect all used methods.
 * Data collected is stored in the {@link UsedHardwareBean}.
 */
public abstract class AbstractUsedMethodCollectorWorker implements IWorker {

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
    protected List<Class<? extends Enum<?>>> getAdditionalEnums() {
        return Collections.emptyList();
    }

    @Override
    public void execute(Project project) {
        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();
        ICollectorVisitor visitor = getVisitor(usedMethodBeanBuilder);
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.accept(visitor);
            }
        }
        CodeGeneratorSetupBean.Builder codeGenSetupBeanBuilder = new CodeGeneratorSetupBean.Builder();
        codeGenSetupBeanBuilder.setFileExtension(project.getSourceCodeFileExtension());
        codeGenSetupBeanBuilder.setHelperMethodFile(project.getRobotFactory().getPluginProperties().getStringProperty("robot.helperMethods"));
        codeGenSetupBeanBuilder.addAdditionalEnums(getAdditionalEnums());
        codeGenSetupBeanBuilder.addUsedMethods(usedMethodBeanBuilder.build().getUsedMethods());
        project.addWorkerResult("CodeGeneratorSetup", codeGenSetupBeanBuilder.build());
    }
}

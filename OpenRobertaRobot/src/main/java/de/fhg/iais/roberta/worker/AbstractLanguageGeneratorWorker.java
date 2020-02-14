package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

/**
 * Uses the {@link AbstractLanguageVisitor} to visit the current AST and generate the robot's specific source code.
 */
public abstract class AbstractLanguageGeneratorWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        UsedHardwareBean usedHardwareBean = project.getWorkerResult(UsedHardwareBean.class);
        UsedMethodBean usedMethodBean = project.getWorkerResult(UsedMethodBean.class);

        AbstractLanguageVisitor visitor;
        // Prepare bean for the code generation visitor
        CodeGeneratorSetupBean.Builder codeGenSetupBeanBuilder = new CodeGeneratorSetupBean.Builder();
        codeGenSetupBeanBuilder.setFileExtension(project.getSourceCodeFileExtension());
        codeGenSetupBeanBuilder.setHelperMethodFile(project.getRobotFactory().getPluginProperties().getStringProperty("robot.helperMethods"));
        codeGenSetupBeanBuilder.addAdditionalEnums(usedMethodBean.getAdditionalEnums());
        codeGenSetupBeanBuilder.addUsedMethods(usedMethodBean.getUsedMethods());
        ClassToInstanceMap<IProjectBean> beans =
            ImmutableClassToInstanceMap
                .<IProjectBean> builder()
                .put(UsedHardwareBean.class, usedHardwareBean)
                .put(CodeGeneratorSetupBean.class, codeGenSetupBeanBuilder.build())
                .build();
        visitor = this.getVisitor(project, beans);
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(project.isWithWrapping());
        project.setResult(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
    }

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic.
     * Could be removed in the future, when visitors are specified in the properties as well, or inferred from the worker name.
     *
     * @param project the project
     * @param beans a map of available beans, may be empty
     * @return the appropriate visitor for the current robot
     */
    protected abstract AbstractLanguageVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean> beans);
}

package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.collect.EdisonMethods;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;

public class EdisonUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {
    @Override
    protected ICollectorVisitor getVisitor(UsedMethodBean.Builder builder) {
        return new EdisonUsedMethodCollectorVisitor(builder);
    }

    /**
     * Edison has additional methods that need to be generated.
     * @return the additional methods
     */
    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalEnums() {
        return Collections.singletonList(EdisonMethods.class);
    }

    @Override
    public void execute(Project project) {
        UsedMethodBean.Builder usedMethodBeanBuilder = new UsedMethodBean.Builder();

        // workaround: currently the used hardware worker can also generate used methods (currently only for edison -> e.g. driving need methods)
        // here the previously generated bean is combined with the actual one
        UsedMethodBean usedMethodsInHardwareBean = (UsedMethodBean) project.getWorkerResult("UsedMethodsInHardware");
        for ( Enum<?> usedMethod : usedMethodsInHardwareBean.getUsedMethods() ) {
            usedMethodBeanBuilder.addUsedMethod(usedMethod);
        }

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

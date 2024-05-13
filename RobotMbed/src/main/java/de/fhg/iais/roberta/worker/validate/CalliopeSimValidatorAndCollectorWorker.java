package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CalliopeValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class CalliopeSimValidatorAndCollectorWorker extends CalliopeValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        boolean hasBlueTooth = project.getRobotFactory().getPluginProperties().getBooleanProperty("robot.has.bluetooth");
        return new CalliopeValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, true, isDisplaySwitchUsed(project), hasBlueTooth);
    }

}

package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.Nano33BleValidatorAndCollectorVisitor;

public class Nano33BleValidatorAndCollectorWorker extends ArduinoValidatorAndCollectorWorker {
    public Nano33BleValidatorAndCollectorWorker() {
        super();
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new Nano33BleValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

}

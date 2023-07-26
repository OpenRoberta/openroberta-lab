package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.ArduinoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.SenseboxValidatorAndCollectorVisitor;

public class SenseboxValidatorAndCollectorWorker extends ArduinoValidatorAndCollectorWorker {
    public SenseboxValidatorAndCollectorWorker() {
        super();
    }

    @Override
    protected ArduinoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new SenseboxValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, project.getSSID(), project.getPassword());
    }
}

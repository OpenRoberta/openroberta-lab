package de.fhg.iais.roberta.worker.validate;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.ArduinoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

public class ArduinoValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {
    @Override
    public void execute(Project project) {
        ArduinoConfigurationValidator arduinoConfigurationValidator = new ArduinoConfigurationValidator(project);
        List<String> freePins = project.getRobotFactory().getFreePins();
        arduinoConfigurationValidator.validateConfiguration(freePins);
        super.execute(project);
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new ArduinoValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }


}

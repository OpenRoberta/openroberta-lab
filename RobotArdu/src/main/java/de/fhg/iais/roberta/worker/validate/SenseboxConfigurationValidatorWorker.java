package de.fhg.iais.roberta.worker.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.SenseboxBrickValidatorVisitor;

public class SenseboxConfigurationValidatorWorker extends ArduinoConfigurationValidatorWorker {
    public SenseboxConfigurationValidatorWorker() {
        super(Stream.of("LED_BUILTIN", "0", "1", "2", "3", "4", "5", "6", "7", "8").collect(Collectors.toList()));
    }

    @Override
    protected AbstractProgramValidatorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new SenseboxBrickValidatorVisitor(project.getConfigurationAst(), beanBuilders, project.getSSID(), project.getPassword());
    }
}

package de.fhg.iais.roberta.worker;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.MicrobitValidatorAndCollectorVisitor;

public class MicrobitValidatorAndCollectorWorker extends MbedValidatorAndCollectorWorker {


    public MicrobitValidatorAndCollectorWorker() {
        super(Stream.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "19", "20").collect(Collectors.toList()),
            Stream.of("KEY", "ACCELEROMETER", "COMPASS", "TEMPERATURE", "LIGHT", "ROBOT").collect(Collectors.toList()));
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new MicrobitValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

package de.fhg.iais.roberta.worker.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.Nano33BleValidatorAndCollectorVisitor;

public class Nano33BleValidatorAndCollectorWorker extends ArduinoValidatorAndCollectorWorker {
    public Nano33BleValidatorAndCollectorWorker() {
        super(
            Stream
                .of(
                    "LED_BUILTIN",
                    "LSM9DS1",
                    "APDS9960",
                    "LPS22HB",
                    "HTS221",
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "A0",
                    "A1",
                    "A2",
                    "A3",
                    "A4",
                    "A5")
                .collect(Collectors.toList()));
    }
    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new Nano33BleValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

}

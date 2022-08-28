package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.MbedValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public abstract class MbedValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {
    private final List<String> freePins;
    private final List<String> defaultProperties;
    private final List<String> existingPins;
    private final HashMap<String, String> mapCorrectConfigPins;

    /**
     * @param freePins All the pins that can be used on the calliope/microbit by the configuration blocks. Also used for checking if the configuration blocks only
     *     contain existing pins (variable existingPins)
     * @param defaultProps The default properties to get the pins from the calliope/microbit.
     * @param mapCorrectConfigPins There are a few configuration blocks that either have no property to get the pins or the pins aren't mapped correctly so this
     *     hashmap maps them to their specific pins, so they can be checked.
     */
    public MbedValidatorAndCollectorWorker(List<String> freePins, List<String> defaultProps, HashMap<String, String> mapCorrectConfigPins) {
        this.freePins = Collections.unmodifiableList(freePins);
        this.defaultProperties = Collections.unmodifiableList(defaultProps);
        this.existingPins = Collections.unmodifiableList(freePins);
        this.mapCorrectConfigPins = mapCorrectConfigPins;
    }

    @Override
    public void execute(Project project) {
        MbedConfigurationValidatorWorker mbedConfigurationValidatorWorker = new MbedConfigurationValidatorWorker(project);
        mbedConfigurationValidatorWorker.validateConfiguration(freePins, defaultProperties, existingPins, mapCorrectConfigPins);
        super.execute(project);
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new MbedValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

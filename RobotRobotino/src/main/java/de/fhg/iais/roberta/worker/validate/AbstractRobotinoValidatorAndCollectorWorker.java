package de.fhg.iais.roberta.worker.validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.RobotinoMethods;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

public abstract class AbstractRobotinoValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    private static final List<String> NON_BLOCKING_PROPERTIES = Collections.unmodifiableList(Arrays.asList("BN", "BU", "M1", "M2", "M3"));

    @Override
    abstract protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders);

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(RobotinoMethods.class);
    }


    @Override
    public void execute(Project project) {
        validateConfig(project);
        super.execute(project);
    }

    private void validateConfig(Project project) {
        List<String> takenPins = new ArrayList<>();
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> checkIfPinTaken(project, v, takenPins));
    }

    private void checkIfPinTaken(Project project, ConfigurationComponent configurationComponent, List<String> takenPins) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        for ( Map.Entry<String, String> property : componentProperties.entrySet() ) {
            if ( NON_BLOCKING_PROPERTIES.contains(property.getKey()) ) {
                continue;
            }
            if ( takenPins.contains(property.getValue()) ) {
                String blockId = configurationComponent.getProperty().getBlocklyId();
                project.addToErrorCounter(1, null);
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
                break;
            }
            takenPins.add(property.getValue());
        }
    }
}

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
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.OrbValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

public class OrbValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

	private static final List<String> sensors = Collections.unmodifiableList(Arrays.asList("COLOR", "TOUCH", "ULTRASONIC", "GYRO"));


	@Override
	protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
		return new OrbValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
	}

	@Override
	public void execute(Project project) {
		validateConfig(project);
		super.execute(project);
	}
	private void duplicatedPorts(Project project, ConfigurationComponent component){
		String blockId = component.getProperty().getBlocklyId();
		project.addToErrorCounter(1, null);
		project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
		project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
	}

	private void validateConfig(Project project) {
		Map<String, ConfigurationComponent> configComponents = project.getConfigurationAst().getConfigurationComponents();
		List<String> takenPortsMotor = new ArrayList<>();
		List<String> takenPortsSensor = new ArrayList<>();
		for ( ConfigurationComponent component : configComponents.values() ) {
			if ( component.componentType.equals(SC.DIFFERENTIALDRIVE) ) {
				if (component.getOptProperty("MOTOR_R").equals(component.getOptProperty("MOTOR_L"))){
					duplicatedPorts(project, component);
					break;
				}
			}
			if ( component.componentType.equals(SC.MOTOR) ){
				if (takenPortsMotor.contains(component.getOptProperty(SC.MOTOR))){
					duplicatedPorts(project, component);
					break;
				}
				takenPortsMotor.add(component.getOptProperty(SC.MOTOR));
			}
			if (sensors.contains(component.componentType)){
				if (takenPortsSensor.contains(component.getOptProperty("CONNECTOR"))){
					duplicatedPorts(project, component);
					break;
				}
				takenPortsSensor.add(component.getOptProperty("CONNECTOR"));
			}

		}

	}

}

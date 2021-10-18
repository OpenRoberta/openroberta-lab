package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.DifferentialMotorValidatorAndCollectorVisitor;

public class OrbValidatorAndCollectorVisitor extends Ev3ValidatorAndCollectorVisitor {

	private final DifferentialMotorValidatorAndCollectorVisitor differentialMotorValidatorAndCollectorVisitor;

	public OrbValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration,
			ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
		super(robotConfiguration, beanBuilders);
		this.differentialMotorValidatorAndCollectorVisitor = new DifferentialMotorValidatorAndCollectorVisitor(this,
				robotConfiguration, beanBuilders);
	}

	@Override
	public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
		addWarningToPhrase(clearDisplayAction, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
		addWarningToPhrase(compassSensor, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
		addWarningToPhrase(irSeekerSensor, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitLightAction(LightAction<Void> lightAction) {
		addWarningToPhrase(lightAction, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
		addWarningToPhrase(lightStatusAction, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
		addWarningToPhrase(playFileAction, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
		addWarningToPhrase(playNoteAction, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
		addWarningToPhrase(sayTextAction, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
		addWarningToPhrase(showPictureAction, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
		addWarningToPhrase(soundSensor, "BLOCK_NOT_SUPPORTED");
		return null;
	}

	@Override
	public Void visitToneAction(ToneAction<Void> toneAction) {
		addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
		return null;
	}

	@Override
	public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
		addWarningToPhrase(volumeAction, "BLOCK_NOT_EXECUTED");
		return null;
	}

	protected void checkSensorPort(ExternalSensor<Void> sensor) {
		final ConfigurationComponent usedSensor = this.robotConfiguration
				.optConfigurationComponent(sensor.getUserDefinedPort());
		if (usedSensor == null) {
			addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
		} else {
			final String type = usedSensor.getComponentType();
			switch (sensor.getKind().getName()) {
			case "COLOR_SENSING":
				if (!type.equals("COLOR")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "TOUCH_SENSING":
				if (!type.equals("TOUCH")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "ULTRASONIC_SENSING":
				if (!type.equals("ULTRASONIC")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "INFRARED_SENSING":
				if (!type.equals("INFRARED")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "GYRO_SENSING":
				if (!type.equals("GYRO")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "SOUND_SENSING":
				if (!type.equals("SOUND")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "LIGHT_SENSING":
				if (!type.equals("LIGHT")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "COMPASS_SENSING":
				if (!type.equals("COMPASS")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "IRSEEKER_SENSING":
				if (!type.equals("IRSEEKER")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			case "HTCOLOR_SENSING":
				if (!type.equals("HT_COLOR")) {
					addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
				}
				break;
			default:
				break;
			}
		}
	}

}

package de.fhg.iais.roberta.visitor.validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.INxtVisitor;
import de.fhg.iais.roberta.visitor.NxtMethods;

public class NxtValidatorAndCollectorVisitor extends DifferentialMotorOldConfValidatorAndCollectorVisitor implements INxtVisitor<Void> {

    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("COLOR_SENSING", SC.COLOR);
        put("HTCOLOR_SENSING", SC.HT_COLOR);
        put("LIGHT_SENSING", SC.LIGHT);
        put("SOUND_SENSING", SC.SOUND);
        put("TOUCH_SENSING", SC.TOUCH);
        put("ULTRASONIC_SENSING", SC.ULTRASONIC);
    }});

    public NxtValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        requiredComponentVisited(bluetoothCheckConnectAction, bluetoothCheckConnectAction.getConnection());
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        requiredComponentVisited(bluetoothReceiveAction, bluetoothReceiveAction.getConnection());
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        requiredComponentVisited(bluetoothSendAction, bluetoothSendAction.getConnection(), bluetoothSendAction.getMsg());
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        checkSensorPort(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        ConfigurationComponent configurationComponent = robotConfiguration.optConfigurationComponent(encoderSensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderSensor, "CONFIGURATION_ERROR_MOTOR_MISSING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), configurationComponent.getComponentType()));
        }
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        checkSensorPort(htColorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(htColorSensor.getUserDefinedPort(), SC.HT_COLOR, htColorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        ConfigurationComponent configurationComponent = robotConfiguration.optConfigurationComponent(lightAction.getPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(lightAction, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else if ( !configurationComponent.getComponentType().equals(SC.COLOR) ) {
            addErrorToPhrase(lightAction, "CONFIGURATION_ERROR_SENSOR_WRONG");
        } else {
            usedHardwareBuilder.addUsedSensor(new UsedSensor(lightAction.getPort(), configurationComponent.getComponentType(), SC.COLOR));
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorPort(lightSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        super.visitMathOnListFunct(mathOnListFunct);
        if ( mathOnListFunct.getFunctName() == FunctionNames.STD_DEV ) {
            usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
            usedMethodBuilder.addUsedMethod(FunctionNames.STD_DEV);
        }
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        super.visitMathSingleFunct(mathSingleFunct);
        switch ( mathSingleFunct.getFunctName() ) {
            case ACOS:
                usedMethodBuilder.addUsedMethod(FunctionNames.ASIN);
                usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
                usedMethodBuilder.addUsedMethod(NxtMethods.FACTORIAL);
                break;
            case ASIN:
            case COS:
            case SIN:
                usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
                usedMethodBuilder.addUsedMethod(NxtMethods.FACTORIAL);
                break;
            case ATAN:
            case EXP:
            case LN:
                usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
                break;
            case LOG10:
                usedMethodBuilder.addUsedMethod(FunctionNames.LN);
                usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
                break;
            case ROUND:
            case ROUNDUP:
                usedMethodBuilder.addUsedMethod(FunctionNames.ROUNDDOWN);
                break;
            case TAN:
                usedMethodBuilder.addUsedMethod(FunctionNames.SIN);
                usedMethodBuilder.addUsedMethod(FunctionNames.COS);
                usedMethodBuilder.addUsedMethod(FunctionNames.POWER);
                usedMethodBuilder.addUsedMethod(NxtMethods.FACTORIAL);
                break;
            default:
                break; // no action necessary
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.SOUND));
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.x, showTextAction.y);
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        checkSensorPort(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        checkSensorPort(touchSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(touchSensor.getUserDefinedPort(), SC.TOUCH, touchSensor.getMode()));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        if ( volumeAction.getMode() == VolumeAction.Mode.SET ) {
            requiredComponentVisited(volumeAction, volumeAction.getVolume());
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.SOUND));
        return null;
    }

    private void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent configurationComponent = robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.getComponentType()) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
    }
}

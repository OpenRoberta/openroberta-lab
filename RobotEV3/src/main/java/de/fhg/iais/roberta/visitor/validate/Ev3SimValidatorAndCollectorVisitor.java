package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;

public class Ev3SimValidatorAndCollectorVisitor extends Ev3ValidatorAndCollectorVisitor {
    public Ev3SimValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        addWarningToPhrase(bluetoothReceiveAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitBluetoothReceiveAction(bluetoothReceiveAction);
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        addWarningToPhrase(compassSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        addWarningToPhrase(soundSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        addWarningToPhrase(htColorSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

}

package de.fhg.iais.roberta.visitor;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.thymio.LedProxVOnAction;
import de.fhg.iais.roberta.syntax.action.thymio.PlayRecordingAction;
import de.fhg.iais.roberta.syntax.action.thymio.RecordStartAction;
import de.fhg.iais.roberta.syntax.action.thymio.RecordStopAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.basic.C;

public class ThymioSimValidatorAndCollectorVisitor extends ThymioValidatorAndCollectorVisitor {
    public ThymioSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitRecordStartAction(RecordStartAction recordStartAction) {
        super.visitRecordStartAction(recordStartAction);
        addWarningToPhrase(recordStartAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitRecordStopAction(RecordStopAction recordStopAction) {
        super.visitRecordStopAction(recordStopAction);
        addWarningToPhrase(recordStopAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        addErrorToPhrase(accelerometerSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        addErrorToPhrase(temperatureSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitLedProxVOnAction(LedProxVOnAction ledProxVOnAction) {
        super.visitLedProxVOnAction(ledProxVOnAction);
        addWarningToPhrase(ledProxVOnAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        super.visitPlayFileAction(playFileAction);
        addWarningToPhrase(playFileAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitPlayRecordingAction(PlayRecordingAction playRecordingAction) {
        super.visitPlayRecordingAction(playRecordingAction);
        addWarningToPhrase(playRecordingAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        super.visitInfraredSensor(infraredSensor);
        String mode = infraredSensor.getMode().toLowerCase();
        if ( mode.equals(C.AMBIENTLIGHT) ) {
            addErrorToPhrase(infraredSensor, "SIM_BLOCK_NOT_SUPPORTED");
        }
        return null;
    }
}

package de.fhg.iais.roberta.visitor.validate;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;

public class Ev3BrickValidatorVisitor extends AbstractBrickValidatorVisitor implements IEv3Visitor<Void> {

    public Ev3BrickValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        super.checkSensorPort(sensor);
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            // should be handled by super implementation
        } else {
            String type = usedSensor.getComponentType();
            switch ( sensor.getKind().getName() ) {
                case "HTCOLOR_SENSING":
                    if ( !type.equals("HT_COLOR") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        super.visitMotorOnAction(motorOnAction);
        if ( motorOnAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(motorOnAction.getUserDefinedPort());
            boolean duration = motorOnAction.getParam().getDuration() != null;
            if ( usedConfigurationBlock == null ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            } else {
                if ( SC.OTHER.equals(usedConfigurationBlock.getComponentType()) && duration ) {
                    motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED"));
                }
            }
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        super.visitListCreate(listCreate);
        if ( listCreate.getTypeVar() == BlocklyType.CONNECTION ) {
            List<Expr<Void>> expressions = listCreate.getValue().get();
            for ( Expr<?> expr : expressions ) {
                if ( expr.getVarType() == BlocklyType.NULL ) {
                    listCreate.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        super.visitListRepeat(listRepeat);
        if ( listRepeat.getTypeVar() == BlocklyType.CONNECTION && listRepeat.getParam().get(0).getVarType() == BlocklyType.NULL ) {
            listRepeat.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        super.visitCompassSensor(compassSensor);
        if ( this.robotConfiguration.getRobotName().equals("ev3dev") && (compassSensor.getMode().equals(SC.CALIBRATE)) ) {
            compassSensor.addInfo(NepoInfo.warning("BLOCK_NOT_SUPPORTED"));
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        throw new DbcException("Weg hier!");
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        throw new DbcException("Weg hier!");
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        throw new DbcException("Weg hier!");
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        return super.visitPinGetValueSensor(pinGetValueSensor);
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return super.visitGetSampleSensor(sensorGetSample);
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        return super.visitMoistureSensor(moistureSensor);
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        return super.visitHumiditySensor(humiditySensor);
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        return super.visitMotionSensor(motionSensor);
    }

    @Override
    public Void visitDropSensor(DropSensor<Void> dropSensor) {
        return super.visitDropSensor(dropSensor);
    }

    @Override
    public Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        return super.visitPulseSensor(pulseSensor);
    }

    @Override
    public Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        return super.visitRfidSensor(rfidSensor);
    }

    @Override
    public Void visitVemlLightSensor(VemlLightSensor<Void> vemlLightSensor) {
        return super.visitVemlLightSensor(vemlLightSensor);
    }

    @Override
    public Void visitParticleSensor(ParticleSensor<Void> particleSensor) {
        return super.visitParticleSensor(particleSensor);
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        super.visitSayTextAction(sayTextAction);
        if ( this.robotConfiguration.getRobotName().equals("ev3lejosv0") ) {
            sayTextAction.addInfo(NepoInfo.warning("BLOCK_NOT_SUPPORTED"));
        }
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        showPictureAction.getX().accept(this);
        showPictureAction.getY().accept(this);
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        checkSensorPort(htColorSensor);
        String mode = htColorSensor.getMode();
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(htColorSensor.getUserDefinedPort(), SC.HT_COLOR, mode));
        return null;
    }
}

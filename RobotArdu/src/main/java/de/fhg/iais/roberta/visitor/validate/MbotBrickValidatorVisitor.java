package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

public final class MbotBrickValidatorVisitor extends AbstractBrickValidatorVisitor implements IMbotVisitor<Void> {

    private boolean main = false;

    public MbotBrickValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        if ( sensor.getKind().getName().equals("LIGHT_SENSING") && sensor.getPort().equals("6") ) {
            return;
        }
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent("ORT_" + sensor.getPort());
        if ( usedConfigurationBlock == null ) {
            sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else {
            switch ( sensor.getKind().getName() ) {
                case "INFRARED_SENSING":
                    if ( !usedConfigurationBlock.getComponentType().equals("INFRARED") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "LIGHT_SENSING":
                    if ( !usedConfigurationBlock.getComponentType().equals("LIGHT") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( !usedConfigurationBlock.getComponentType().equals("ULTRASONIC") ) {
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
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        checkSensorPort(temperatureSensor);
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometer) {
        checkSensorPort(accelerometer);
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        // TODO checkSensorPort(flameSensor);       
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        checkSensorPort(voltageSensor);
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        serialWriteAction.getValue().accept(this);
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        sendIRAction.getMessage().accept(this);
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction<Void> ledMatrixImageAction) {
        ledMatrixImageAction.getValuesToDisplay().accept(this);
        checkActorPort(ledMatrixImageAction, ledMatrixImageAction.getPort());
        return null;
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
            } else if ( SC.OTHER.equals(usedConfigurationBlock.getComponentType()) && duration ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED"));
            }
        }
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction<Void> ledMatrixTextAction) {
        ledMatrixTextAction.getMsg().accept(this);
        checkActorPort(ledMatrixTextAction, ledMatrixTextAction.getPort());
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction) {
        ledMatrixImageShiftFunction.getImage().accept(this);
        ledMatrixImageShiftFunction.getPositions().accept(this);
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<Void> ledMatrixImageInverFunction) {
        ledMatrixImageInverFunction.getImage().accept(this);
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<Void> ledMatrixSetBrightnessAction) {
        ledMatrixSetBrightnessAction.getBrightness().accept(this);
        checkActorPort(ledMatrixSetBrightnessAction, ledMatrixSetBrightnessAction.getPort());
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        checkActorPort(clearDisplayAction, clearDisplayAction.getPort());
        return null;
    }

    private void checkActorPort(Action<Void> action, String port) {
        if ( action.getInfos().getErrorCount() == 0 ) {
            // TODO check why "ORT_" is needed
            ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent("ORT_" + port);
            if ( usedConfigurationBlock == null ) {
                action.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            } else {
                if ( !SC.LED_MATRIX.equals(usedConfigurationBlock.getComponentType()) ) {
                    action.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                    this.errorCount++;
                }
            }
        }
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        if ( turnAction.getParam().getDuration() != null ) {
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        super.visitRepeatStmt(repeatStmt);
        if ( repeatStmt.getMode() == Mode.FOREVER_ARDU ) {
            this.main = true;
        }
        repeatStmt.getList().accept(this);
        this.main = false;
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        super.visitMethodVoid(methodVoid);
        this.main = true;
        methodVoid.getParameters().accept(this);
        methodVoid.getBody().accept(this);
        this.main = false;
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        super.visitMethodReturn(methodReturn);
        this.main = true;
        methodReturn.getParameters().accept(this);
        methodReturn.getBody().accept(this);
        methodReturn.getReturnValue().accept(this);
        this.main = false;
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        super.visitListCreate(listCreate);
        listCreate.getValue().accept(this);
        if ( this.main && listCreate.getTypeVar() == BlocklyType.IMAGE ) {
            listCreate.addInfo(NepoInfo.error("BLOCK_NOT_SUPPORTED"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            indexOfFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitIndexOfFunct(indexOfFunct);
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            listGetIndex.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitListGetIndex(listGetIndex);
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            listSetIndex.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitListSetIndex(listSetIndex);
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            lengthOfIsEmptyFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitLengthOfIsEmptyFunct(lengthOfIsEmptyFunct);
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            mathOnListFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitMathOnListFunct(mathOnListFunct);
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        if ( getSubFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            getSubFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitGetSubFunct(getSubFunct);
    }
}

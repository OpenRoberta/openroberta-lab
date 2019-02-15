package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinReadValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public class SenseboxBrickValidatorVisitor extends AbstractBrickValidatorVisitor implements ISensorVisitor<Void>, IArduinoVisitor<Void> {

    public SenseboxBrickValidatorVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        return null;
    }

    @Override
    public Void visitPinReadValueAction(PinReadValueAction<Void> pinReadValueActor) {
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LED) ) {
            this.addError("CONFIGURATION_ERROR_DEPENDENCY_MISSING", lightAction);
        }
        return null;
    }

    @Override
    public Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        //Send data action block can be used only with conjunction with wi-fi block from configuration:
        if ( !this.robotConfiguration.isComponentTypePresent(SC.WIRELESS) ) {
            this.addError("CONFIGURATION_ERROR_DEPENDENCY_MISSING", sendDataAction);
            return null;
        }
        for ( Pair<String, Expr<Void>> value : sendDataAction.getId2Phenomena() ) {
            value.getSecond().visit(this);
        }
        return null;
    }

    @Override
    public Void visitVemlLightSensor(VemlLightSensor<Void> vemlLightSensor) {
        checkSensorPort(vemlLightSensor);
        switch ( vemlLightSensor.getMode() ) {
            case SC.LIGHT:
                break;
            case SC.UVLIGHT:
                break;
            default:
                vemlLightSensor.addInfo(NepoInfo.error("ILLEGAL_MODE_USED"));
                this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        checkSensorPort(temperatureSensor);
        switch ( temperatureSensor.getMode() ) {
            case SC.TEMPERATURE:
                break;
            case SC.PRESSURE:
                break;
            default:
                temperatureSensor.addInfo(NepoInfo.error("ILLEGAL_MODE_USED"));
                this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        checkSensorPort(humiditySensor);
        switch ( humiditySensor.getMode() ) {
            case SC.HUMIDITY:
                break;
            case SC.TEMPERATURE:
                break;
            default:
                humiditySensor.addInfo(NepoInfo.error("ILLEGAL_MODE_USED"));
                this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> button) {
        checkSensorPort(button);
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorPort(lightSensor);
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> potentiometer) {
        checkSensorPort(potentiometer);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        return null;
    }

}

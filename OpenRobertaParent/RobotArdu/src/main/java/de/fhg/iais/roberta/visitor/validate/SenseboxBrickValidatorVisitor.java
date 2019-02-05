package de.fhg.iais.roberta.visitor.validate;

import java.util.List;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.actors.arduino.PinReadValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.DbcException;
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
    public Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.WIRELESS) ) {
            //TODO okay, really, replace this with a suiting error message:
            sendDataAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
            //throw new DbcException("Send data action block can be used only with conjunction with wi-fi block from configuration.");
        }
        List<Expr<Void>> listOfSensors = sendDataAction.getParam().get();
        for ( Expr<Void> sensor : listOfSensors ) {
            checkSensorPort((ExternalSensor<Void>) (((SensorExpr<Void>) sensor).getSens()));
            String sensorName = null;
            try {
                sensorName = ((SensorExpr<Void>) sensor).getSens().getKind().getName();
            } catch ( ClassCastException e ) {
                throw new DbcException("Expressions in the send data block are restricted to sensor values. Affected expression is " + sensor);
            }
            switch ( sensorName ) {
                case "HUMIDITY_SENSING":
                    if ( !this.robotConfiguration.isComponentTypePresent(SC.HUMIDITY) ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
                        this.errorCount++;
                        //throw new DbcException("A block is used for which the corresponding configuration block is not present: " + sensorName);
                    }
                    break;
                case "TEMPERATURE_SENSING":
                    if ( !this.robotConfiguration.isComponentTypePresent(SC.TEMPERATURE) ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
                        this.errorCount++;
                        //throw new DbcException("A block is used for which the corresponding configuration block is not present: " + sensorName);
                    }
                    break;
                case "VEMLLIGHT_SENSING":
                    if ( !this.robotConfiguration.isComponentTypePresent(SC.LIGHTVEML) ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
                        this.errorCount++;
                        //throw new DbcException("A block is used for which the corresponding configuration block is not present: " + sensorName);
                    }
                    break;
                default:
                    sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
                    this.errorCount++;
                    //throw new DbcException("An invalid sensor has been detected: " + sensorName);
            }
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

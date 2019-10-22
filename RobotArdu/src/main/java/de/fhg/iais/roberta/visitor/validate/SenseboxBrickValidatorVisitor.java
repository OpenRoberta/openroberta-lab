package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public class SenseboxBrickValidatorVisitor extends AbstractBrickValidatorVisitor implements ISensorVisitor<Void>, IArduinoVisitor<Void> {

    private final String SSID;
    private final String password;

    public SenseboxBrickValidatorVisitor(UsedHardwareBean.Builder builder, ConfigurationAst brickConfiguration, String SSID, String password) {
        super(builder, brickConfiguration);
        this.SSID = SSID;
        this.password = password;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( lightAction.getMode().toString().equals(BlocklyConstants.ON) && !this.robotConfiguration.isComponentTypePresent(SC.LED) ) {
            lightAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        } else if ( lightAction.getMode().toString().equals(BlocklyConstants.DEFAULT) && !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            lightAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        } else {
            // no errors, all blocks in place.
        }
        return null;
    }

    @Override
    public Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        if ( SSID.equals("") || password.equals("") ) {
            sendDataAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_WLAN_CREDENTIALS_MISSING"));
            return null;
        }
        if ( (sendDataAction.getDestination().equals("SENSEMAP") && !this.robotConfiguration.isComponentTypePresent(SC.WIRELESS)) ) {
            sendDataAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            return null;
        }
        if ( (sendDataAction.getDestination().equals("SDCARD") && !this.robotConfiguration.isComponentTypePresent(SC.SENSEBOX_SDCARD)) ) {
            sendDataAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            return null;
        }
        for ( Pair<String, Expr<Void>> value : sendDataAction.getId2Phenomena() ) {
            if ( value.getSecond() instanceof EmptyExpr ) {
                // well, here can be an annotation if we encounter an empty input.
                // this key, ACTION_ERROR_EMPTY_INPUT should be added to blockly, maybe.
                sendDataAction.addInfo(NepoInfo.error("ACTION_ERROR_EMPTY_INPUT"));
                return null;
            }
            value.getSecond().accept(this);
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

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.BUZZER) ) {
            toneAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.RGBLED) ) {
            lightStatusAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitPlotPointAction(PlotPointAction<Void> plotPointAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            plotPointAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitPlotClearAction(PlotClearAction<Void> plotClearAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            plotClearAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            showTextAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            clearDisplayAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
        return null;
    }
}

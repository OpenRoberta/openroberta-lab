package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.visitor.hardware.ISenseboxVisitor;

public class SenseboxValidatorAndCollectorVisitor extends ArduinoValidatorAndCollectorVisitor implements ISenseboxVisitor<Void> {

    private final String SSID;
    private final String password;

    public SenseboxValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        String SSID,
        String password) {
        super(brickConfiguration, beanBuilders);
        this.SSID = SSID;
        this.password = password;
    }

    @Override
    public Void visitSendDataAction(SendDataAction sendDataAction) {
        if ( this.SSID.equals("") || this.password.equals("") ) {
            addErrorToPhrase(sendDataAction, "CONFIGURATION_ERROR_WLAN_CREDENTIALS_MISSING");
            return null;
        }
        if ( (sendDataAction.destination.equals("SENSEMAP") && !this.robotConfiguration.isComponentTypePresent(SC.WIRELESS)) ) {
            addErrorToPhrase(sendDataAction, "CONFIGURATION_ERROR_WLAN_MISSING");
            return null;
        }
        if ( (sendDataAction.destination.equals("SDCARD") && !this.robotConfiguration.isComponentTypePresent(SC.SENSEBOX_SDCARD)) ) {
            addErrorToPhrase(sendDataAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            return null;
        }
        for ( Pair<String, Expr> value : sendDataAction.id2Phenomena ) {
            requiredComponentVisited(sendDataAction, value.getSecond());
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.NONE, SC.SEND_DATA));
        return null;
    }

    @Override
    public Void visitEnvironmentalSensor(EnvironmentalSensor environmentalSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.ENVIRONMENTAL) ) {
            addErrorToPhrase(environmentalSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(environmentalSensor.getUserDefinedPort(), SC.ENVIRONMENTAL, environmentalSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGpsSensor(GpsSensor gpsSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.GPS) ) {
            addErrorToPhrase(gpsSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gpsSensor.getUserDefinedPort(), SC.GPS, gpsSensor.getMode()));
        return null;
    }

    @Override
    public Void visitParticleSensor(ParticleSensor particleSensor) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.PARTICLE) ) {
            addErrorToPhrase(particleSensor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(particleSensor.getUserDefinedPort(), SC.PARTICLE, particleSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPlotClearAction(PlotClearAction plotClearAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            addErrorToPhrase(plotClearAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(plotClearAction.port, SC.SENSEBOX_PLOTTING));
        return null;
    }

    @Override
    public Void visitPlotPointAction(PlotPointAction plotPointAction) {
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            addErrorToPhrase(plotPointAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(plotPointAction, plotPointAction.value, plotPointAction.tickmark);
        usedHardwareBuilder.addUsedActor(new UsedActor(plotPointAction.port, SC.SENSEBOX_PLOTTING));
        return null;
    }


    @Override
    public Void visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        checkSensorPort(vemlLightSensor);
        switch ( vemlLightSensor.getMode() ) {
            case SC.LIGHT:
            case SC.UVLIGHT:
                break;
            default:
                addErrorToPhrase(vemlLightSensor, "ILLEGAL_MODE_USED");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(vemlLightSensor.getUserDefinedPort(), SC.LIGHTVEML, vemlLightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        checkSensorPort(compassSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        checkSensorPort(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        super.visitShowTextAction(showTextAction);
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            addErrorToPhrase(showTextAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        super.visitClearDisplayAction(clearDisplayAction);
        if ( !this.robotConfiguration.isComponentTypePresent(SC.LCDI2C) ) {
            addErrorToPhrase(clearDisplayAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return null;
    }
}

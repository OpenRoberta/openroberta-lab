package de.fhg.iais.roberta.visitor.collect;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author VinArt
 */
public final class SenseboxUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IArduinoVisitor<Void> {

    public SenseboxUsedHardwareCollectorVisitor(List<List<Phrase<Void>>> phrasesSet, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(null, beanBuilders);
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
    public Void visitVemlLightSensor(VemlLightSensor<Void> lightSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(lightSensor.getPort(), SC.LIGHTVEML, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        // TODO check that WiFi config block is used, otherwise throw an exception
        // and show user the error, that they must use this block in conjunction
        // with WiFi/ethernet/LoRa
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.NONE, SC.SEND_DATA));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(temperatureSensor.getPort(), SC.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(humiditySensor.getPort(), SC.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(compassSensor.getPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPlotPointAction(PlotPointAction<Void> plotPointAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(plotPointAction.getPort(), SC.SENSEBOX_PLOTTING));
        plotPointAction.getValue().accept(this);
        return null;
    }

    @Override
    public Void visitPlotClearAction(PlotClearAction<Void> plotClearAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(plotClearAction.getPort(), SC.SENSEBOX_PLOTTING));
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( !lightAction.getMode().toString().equals(BlocklyConstants.DEFAULT) ) {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(lightAction.getPort(), SC.LED));
        } else {
            this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(lightAction.getPort(), SC.RGBLED));
        }
        return null;
    }

    @Override
    public Void visitParticleSensor(ParticleSensor<Void> particleSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(particleSensor.getPort(), SC.PARTICLE, particleSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGpsSensor(GpsSensor<Void> gpsSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(gpsSensor.getPort(), SC.GPS, gpsSensor.getMode()));
        return null;
    }
    
    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        serialWriteAction.getValue().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.SERIAL, SC.SERIAL));
        return null;
    }

    @Override
    public Void visitEnvironmentalSensor(EnvironmentalSensor<Void> environmentalSensor) {
        this
            .getBuilder(UsedHardwareBean.Builder.class)
            .addUsedSensor(new UsedSensor(environmentalSensor.getPort(), SC.ENVIRONMENTAL, environmentalSensor.getMode()));
        return null;
    }
}

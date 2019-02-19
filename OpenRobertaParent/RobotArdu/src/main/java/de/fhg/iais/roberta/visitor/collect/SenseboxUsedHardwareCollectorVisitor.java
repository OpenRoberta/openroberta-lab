package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.actors.arduino.PinReadValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author VinArt
 */
public final class SenseboxUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IArduinoVisitor<Void> {
    private boolean isListsUsed = false;

    @Override
    public boolean isListsUsed() {
        return isListsUsed;
    }

    public SenseboxUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        super(null);
        this.check(phrasesSet);
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitPinReadValueAction(PinReadValueAction<Void> pinReadValueActor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitVemlLightSensor(VemlLightSensor<Void> lightSensor) {
        this.usedSensors.add(new UsedSensor(lightSensor.getPort(), SC.LIGHTVEML, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        // TODO check that WiFi config block is used, otherwise throw an exception
        // and show user the error, that they must use this block in conjunction
        // with WiFi/ethernet/LoRa
        this.usedActors.add(new UsedActor(SC.NONE, SC.SEND_DATA));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.usedSensors.add(new UsedSensor(temperatureSensor.getPort(), SC.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        this.usedSensors.add(new UsedSensor(humiditySensor.getPort(), SC.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    //TODO Put this in the abstract collector AbstractCollectorVisitor.java if it does not affect other robots
    // 29.01.2019 - Artem Vinokurov
    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        if ( var.isGlobal() ) {
            this.visitedVars.add(var);
        }
        if ( var.getVarType().equals(BlocklyType.ARRAY)
            || var.getVarType().equals(BlocklyType.ARRAY_BOOLEAN)
            || var.getVarType().equals(BlocklyType.ARRAY_NUMBER)
            || var.getVarType().equals(BlocklyType.ARRAY_COLOUR)
            || var.getVarType().equals(BlocklyType.ARRAY_CONNECTION)
            || var.getVarType().equals(BlocklyType.ARRAY_IMAGE)
            || var.getVarType().equals(BlocklyType.ARRAY_STRING) ) {
            this.isListsUsed = true;
        }
        var.getValue().visit(this);
        this.globalVariables.add(var.getName());
        this.declaredVariables.add(var.getName());
        return null;
    }

}

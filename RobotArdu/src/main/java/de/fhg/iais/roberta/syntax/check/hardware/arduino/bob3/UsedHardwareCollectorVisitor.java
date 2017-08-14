package de.fhg.iais.roberta.syntax.check.hardware.arduino.bob3;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.arduino.Bob3Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.action.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.action.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.ExternalLedOffAction;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.ExternalLedOnAction;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.expr.arduino.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.Bob3TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.bob3.Bob3TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.visitor.arduino.Bob3AstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author VinArt
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor implements Bob3AstVisitor<Void> {

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Bob3Configuration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitTouchSensor(Bob3TouchSensor<Void> touchSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLightSensor(AmbientLightSensor<Void> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBodyLEDAction(BodyLEDAction<Void> bodyLEDAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBob3TemperatureSensor(Bob3TemperatureSensor<Void> temperatureSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBob3CodePadSensor(CodePadSensor<Void> codePadSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBob3GetSampleSensor(GetSampleSensor<Void> bob3GetSampleSensor) {
        this.isTimerSensorUsed = true;
        return null;
    }

    @Override
    public Void visitExternalLedOnAction(ExternalLedOnAction<Void> externalLedOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitExternalLedOffAction(ExternalLedOffAction<Void> externalLedOffAction) {
        // TODO Auto-generated method stub
        return null;
    }
}

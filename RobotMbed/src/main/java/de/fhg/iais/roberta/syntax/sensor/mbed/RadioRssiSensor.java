package de.fhg.iais.roberta.syntax.sensor.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoBasic(name = "RADIO_RSSI", category = "SENSOR", blocklyNames = {"mbedSensors_getRssi", "robsensors_rssi_getsample"})
public final class RadioRssiSensor extends ExternalSensor {

    private RadioRssiSensor(ExternalSensorBean externalSensorBean, BlocklyProperties properties) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

    public static  RadioRssiSensor make(ExternalSensorBean externalSensorBean, BlocklyProperties properties) {
        return new RadioRssiSensor(externalSensorBean, properties);
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        ExternalSensorBean sensorData = extractPortAndModeAndSlot(block, helper);
        return RadioRssiSensor.make(sensorData, Jaxb2Ast.extractBlocklyProperties(block));
    }
}

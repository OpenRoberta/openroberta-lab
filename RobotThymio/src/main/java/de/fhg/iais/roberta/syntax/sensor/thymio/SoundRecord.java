//package de.fhg.iais.roberta.syntax.sensor.thymio;
//
//import de.fhg.iais.roberta.blockly.generated.Hide;
//import de.fhg.iais.roberta.syntax.sensor.Sensor;
//import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
//import de.fhg.iais.roberta.transformer.forField.NepoField;
//import de.fhg.iais.roberta.transformer.forField.NepoHide;
//import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
//import de.fhg.iais.roberta.util.ast.BlocklyComment;
//import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
//import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
//
//@NepoPhrase(category = "SENSOR", blocklyNames = {"robSensors_sound_record"}, name = "SOUND_RECORD")
//public class SoundRecord<V> extends Sensor<V> implements WithUserDefinedPort<V> {
//    @NepoField(name = BlocklyConstants.MODE)
//    public final String mode;
//    @NepoField(name = BlocklyConstants.SENSORPORT)
//    public final String sensorPort;
//    @NepoHide
//    public final Hide hide;
//
//    public SoundRecord(BlocklyBlockProperties properties, BlocklyComment comment, String mode, String sensorPort, Hide hide) {
//        super(properties, comment);
//        this.mode = mode;
//        this.sensorPort = sensorPort;
//        this.hide = hide;
//        setReadOnly();
//    }
//
//    @Override
//    public String getUserDefinedPort() {
//        return this.sensorPort;
//    }
//
//
//}

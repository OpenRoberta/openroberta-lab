//package de.fhg.iais.roberta.syntax.sensor.thymio;
//
//import de.fhg.iais.roberta.blockly.generated.Hide;
//import de.fhg.iais.roberta.syntax.sensor.Sensor;
//import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
//import de.fhg.iais.roberta.transformer.forField.NepoField;
//import de.fhg.iais.roberta.transformer.forField.NepoHide;
//import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
//import de.fhg.iais.roberta.util.ast.BlocklyComment;
//import de.fhg.iais.roberta.util.dbc.Assert;
//import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
//import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
//
///**
// * This class represents the <b>robSensors_gyro_axis_reset</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
// * stopping every movement of the robot.<br/>
// * <br/>
// */
//
//@NepoPhrase(category = "SENSOR", blocklyNames = {"robSensors_gyro_reset_axis"}, name = "GYRO_RESET_AXIS")
//public class GyroResetAxis<V> extends Sensor<V> implements WithUserDefinedPort<V> {
//    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
//    public final String port;
//    @NepoField(name = BlocklyConstants.SLOT, value = BlocklyConstants.EMPTY_SLOT)
//    public final String slot;
//    @NepoHide
//    public final Hide hide;
//
//    public GyroResetAxis(BlocklyBlockProperties properties, BlocklyComment comment, String port, String slot, Hide hide) {
//        super(properties, comment);
//        Assert.nonEmptyString(port);
//        this.port = port;
//        this.slot = slot;
//        this.hide = hide;
//        setReadOnly();
//    }
//
//    @Override
//    public String getUserDefinedPort() {
//        return this.port;
//    }
//}

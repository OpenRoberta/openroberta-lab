package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>naoSensors_getSample</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorType}. See enum {@link SensorType} for all possible type of sensors.<br>
 * <br>
 * To create an instance from this class use the method {@link #make}.<br>
 */
public class NaoGetSampleSensor<V> extends Sensor<V> {
    private final Sensor<V> sensor;
    private final String sensorPort;
    private final String slot;
    private final GetSampleType sensorType;

    private NaoGetSampleSensor(
        GetSampleType sensorType,
        String port,
        String slot,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        super(BlockTypeContainer.getByName("GET_SAMPLE"), properties, comment);
        Assert.notNull(sensorType);
        Assert.notNull(port);
        this.sensorPort = port;
        this.slot = slot;
        //TODO: reimplemnt it in a better way
        if ( port.equals("") ) {
            port = BlocklyConstants.NO_PORT;
        }
        this.sensorType = sensorType;
        SensorMetaDataBean sensorMetaDataBean;
        switch ( sensorType.getSensorType() ) {
            //            case BlocklyConstants.NAO_TOUCHSENSOR:
            //                String sensor = helper.extractField(fields, BlocklyConstants.POSITION);
            //                String sensorSide = helper.extractField(fields, BlocklyConstants.SIDE);
            //                Sensor<V> touchSensor =
            //                    Touchsensors.make(Touchsensors.SensorType.valueOf(sensor), Touchsensors.TouchSide.valueOf(sensorSide), blockProperties, blockComment);
            //                return NaoGetSampleSensor.make(sensorType, touchSensor);
            //            case BlocklyConstants.NAO_DETECTFACE:
            //                Sensor<V> detectFace = DetectFace.make(blockProperties, blockComment);
            //                return NaoGetSampleSensor.make(sensorType, detectFace);
            //            case BlocklyConstants.NAO_NAOMARK:
            //                Sensor<V> markSensor = NaoMark.make(blockProperties, blockComment);
            //                return NaoGetSampleSensor.make(sensorType, markSensor);
            //            case BlocklyConstants.NAO_SONAR:
            //                Sensor<V> sonar = Sonar.make(blockProperties, blockComment);
            //                return NaoGetSampleSensor.make(sensorType, sonar);
            //            case BlocklyConstants.NAO_GYROMETER:
            //                coordinate = helper.extractField(fields, BlocklyConstants.COORDINATE);
            //                Sensor<V> gyroSensor = Gyrometer.make(Gyrometer.Coordinate.valueOf(coordinate), blockProperties, blockComment);
            //                return NaoGetSampleSensor.make(sensorType, gyroSensor);
            case BlocklyConstants.ACCELEROMETER:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getAxis(BlocklyConstants.DEFAULT),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT));
                this.sensor = AccelerometerSensor.make(sensorMetaDataBean, properties, comment);
                break;
            //            case BlocklyConstants.NAO_FSR:
            //                sensorSide = helper.extractField(fields, BlocklyConstants.SIDE);
            //                Sensor<V> fsrSensor = ForceSensor.make(ForceSensor.Side.valueOf(sensorSide), blockProperties, blockComment);
            //                return NaoGetSampleSensor.make(sensorType, fsrSensor);
            //            case BlocklyConstants.NAO_RECOGNIZEWORD:
            //                values = helper.extractValues(block, (short) 1);
            //                Phrase<V> dictionary = helper.extractValue(values, new ExprParam(BlocklyConstants.WORD, BlocklyType.ARRAY_STRING));
            //                Sensor<V> recognizeWord = RecognizeWord.make(helper.convertPhraseToExpr(dictionary), blockProperties, blockComment);
            //                return NaoGetSampleSensor.make(sensorType, recognizeWord);
            default:
                throw new DbcException("Invalid sensor " + sensorType.getSensorType() + "!");
        }
        setReadOnly();
    }

    /**
     * @return the sensor
     */
    public Sensor<V> getSensor() {
        return this.sensor;
    }

    /**
     * @return name of the port
     */
    public String getSensorPort() {
        return this.sensorPort;
    }

    /**
     * @return type of the sensor who will get the sample
     */
    public GetSampleType getSensorType() {
        return this.sensorType;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitNaoGetSampleSensor(this);
    }

    @Override
    public String toString() {
        return "NaoGetSampleSensor [" + this.sensor + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 4);
        String modeName = helper.extractField(fields, BlocklyConstants.SENSORTYPE);
        String portName = helper.extractField(fields, GetSampleType.get(modeName).getPortTypeName());
        String slot = helper.extractField(fields, GetSampleType.get(modeName).getValues()[0]);
        return GetSampleSensor
            .make(GetSampleType.get(modeName), portName, slot, helper.extractBlockProperties(block), helper.extractComment(block), helper.getModeFactory());
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this.sensor, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setInput(getSensorType().name());
        jaxbDestination.setMutation(mutation);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, getSensorType().name());
        switch ( this.sensorType.getSensorType() ) {
            case BlocklyConstants.NAO_TOUCHSENSOR:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.POSITION, ((Touchsensors<V>) this.sensor).getSensor().toString());
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, ((Touchsensors<V>) this.sensor).getSide().toString());
                break;
            //            case BlocklyConstants.NAO_GYROMETER:
            //                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COORDINATE, ((Gyrometer<V>) this.sensor).getCoordinate().toString());
            //                break;
            case BlocklyConstants.ACCELEROMETER:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COORDINATE, ((AccelerometerSensor<V>) this.sensor).getPort().toString());
                break;
            case BlocklyConstants.NAO_FSR:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, ((ForceSensor<V>) this.sensor).getSide().toString());
                break;
            case BlocklyConstants.NAO_RECOGNIZEWORD:
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.WORD, ((RecognizeWord<V>) this.sensor).getVocabulary());
            default:
                break;
        }

        return jaxbDestination;
    }

}

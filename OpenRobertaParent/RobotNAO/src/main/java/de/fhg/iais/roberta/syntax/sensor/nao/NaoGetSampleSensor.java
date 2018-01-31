package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.sensor.nao.GetSensorSampleType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
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
    private final GetSensorSampleType sensorType;

    private NaoGetSampleSensor(GetSensorSampleType sensorType, Sensor<V> sensor) {
        super(BlockTypeContainer.getByName("GET_SAMPLE"), sensor.getProperty(), sensor.getComment());
        Assert.isTrue(sensorType != null);
        this.sensorType = sensorType;
        this.sensor = sensor;
        setReadOnly();
    }

    private static <V> NaoGetSampleSensor<V> make(GetSensorSampleType sensorType, Sensor<V> sensor) {
        return new NaoGetSampleSensor<V>(sensorType, sensor);

    }

    public GetSensorSampleType getSensorType() {
        return this.sensorType;
    }

    public Sensor<V> getSensor() {
        return this.sensor;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitNaoGetSampleSensor(this);
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
        GetSensorSampleType sensorType = GetSensorSampleType.get(helper.extractField(fields, BlocklyConstants.SENSORTYPE));

        List<Value> values;
        String coordinate;
        BlocklyBlockProperties blockProperties = helper.extractBlockProperties(block);
        BlocklyComment blockComment = helper.extractComment(block);
        switch ( sensorType ) {
            case NAO_TOUCHSENSOR:
                String sensor = helper.extractField(fields, BlocklyConstants.POSITION);
                String sensorSide = helper.extractField(fields, BlocklyConstants.SIDE);
                Sensor<V> touchSensor =
                    Touchsensors.make(Touchsensors.SensorType.valueOf(sensor), Touchsensors.TouchSide.valueOf(sensorSide), blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, touchSensor);
            case NAO_DETECTFACE:
                Sensor<V> detectFace = DetectFace.make(blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, detectFace);
            case NAO_NAOMARK:
                Sensor<V> markSensor = NaoMark.make(blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, markSensor);
            case NAO_SONAR:
                Sensor<V> sonar = Sonar.make(blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, sonar);
            case NAO_GYROMETER:
                coordinate = helper.extractField(fields, BlocklyConstants.COORDINATE);
                Sensor<V> gyroSensor = Gyrometer.make(Gyrometer.Coordinate.valueOf(coordinate), blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, gyroSensor);
            case NAO_ACCELEROMETER:
                coordinate = helper.extractField(fields, BlocklyConstants.COORDINATE);
                Sensor<V> accelerometer = Accelerometer.make(Accelerometer.Coordinate.valueOf(coordinate), blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, accelerometer);
            case NAO_FSR:
                sensorSide = helper.extractField(fields, BlocklyConstants.SIDE);
                Sensor<V> fsrSensor = ForceSensor.make(ForceSensor.Side.valueOf(sensorSide), blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, fsrSensor);
            case NAO_RECOGNIZEWORD:
                values = helper.extractValues(block, (short) 1);
                Phrase<V> dictionary = helper.extractValue(values, new ExprParam(BlocklyConstants.WORD, BlocklyType.ARRAY_STRING));
                Sensor<V> recognizeWord = RecognizeWord.make(helper.convertPhraseToExpr(dictionary), blockProperties, blockComment);
                return NaoGetSampleSensor.make(sensorType, recognizeWord);
            default:
                return null;
        }

    }

    @Override
    public String toString() {
        return "NaoGetSampleSensor [" + this.sensor + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this.sensor, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setInput(getSensorType().name());
        jaxbDestination.setMutation(mutation);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, getSensorType().name());
        switch ( this.sensorType ) {
            case NAO_TOUCHSENSOR:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.POSITION, ((Touchsensors<V>) this.sensor).getSensor().toString());
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, ((Touchsensors<V>) this.sensor).getSide().toString());
                break;
            case NAO_GYROMETER:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COORDINATE, ((Gyrometer<V>) this.sensor).getCoordinate().toString());
                break;
            case NAO_ACCELEROMETER:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COORDINATE, ((Accelerometer<V>) this.sensor).getCoordinate().toString());
                break;
            case NAO_FSR:
                JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, ((ForceSensor<V>) this.sensor).getSide().toString());
                break;
            case NAO_RECOGNIZEWORD:
                JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.WORD, ((RecognizeWord<V>) this.sensor).getVocabulary());
            default:
                break;
        }

        return jaxbDestination;
    }

}

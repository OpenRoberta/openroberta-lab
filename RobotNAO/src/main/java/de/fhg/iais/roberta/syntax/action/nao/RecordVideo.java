package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Camera;
import de.fhg.iais.roberta.mode.action.nao.Resolution;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from
 * this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class RecordVideo<V> extends Action<V> {

    @Override
    public String toString() {
        return "RecordVideo [" + this.resolution + ", " + this.camera + ", " + this.duration + ", " + this.videoName + "]";
    }

    private final Resolution resolution;
    private final Camera camera;
    private final Expr<V> duration;
    private final Expr<V> videoName;

    private RecordVideo(Resolution resolution, Camera camera, Expr<V> duration, Expr<V> videoName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RECORD_VIDEO"), properties, comment);
        Assert.notNull(resolution, "Missing resolution in RecordVideo block!");
        Assert.notNull(camera, "Missing camera in RecordVideo block!");
        this.resolution = resolution;
        this.camera = camera;
        this.duration = duration;
        this.videoName = videoName;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RecordVideo}. This instance is read only and can not be modified.
     *
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RecordVideo}
     */
    private static <V> RecordVideo<V> make(
        Resolution resolution,
        Camera camera,
        Expr<V> duration,
        Expr<V> videoName,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new RecordVideo<>(resolution, camera, duration, videoName, properties, comment);
    }

    public Resolution getResolution() {
        return this.resolution;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Expr<V> getDuration() {
        return this.duration;
    }

    public Expr<V> getVideoName() {
        return this.videoName;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitRecordVideo(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        List<Value> values = helper.extractValues(block, (short) 2);

        String resolution = helper.extractField(fields, BlocklyConstants.RESOLUTION);
        String camera = helper.extractField(fields, BlocklyConstants.CAMERA);
        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, BlocklyType.NUMBER_INT));
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.FILENAME, BlocklyType.NUMBER_INT));

        return RecordVideo
            .make(
                Resolution.get(resolution),
                Camera.get(camera),
                helper.convertPhraseToExpr(duration),
                helper.convertPhraseToExpr(msg),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.RESOLUTION, this.resolution.getValues()[0]);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.CAMERA, this.camera.getValues()[0]);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.DURATION, this.duration);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.FILENAME, this.videoName);

        return jaxbDestination;
    }
}
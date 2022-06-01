package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Camera;
import de.fhg.iais.roberta.mode.action.nao.Resolution;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);

        String resolution = Jaxb2Ast.extractField(fields, BlocklyConstants.RESOLUTION);
        String camera = Jaxb2Ast.extractField(fields, BlocklyConstants.CAMERA);
        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, BlocklyType.NUMBER_INT));
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.FILENAME, BlocklyType.NUMBER_INT));

        return RecordVideo
            .make(
                Resolution.get(resolution),
                Camera.get(camera),
                Jaxb2Ast.convertPhraseToExpr(duration),
                Jaxb2Ast.convertPhraseToExpr(msg),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.RESOLUTION, this.resolution.getValues()[0]);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.CAMERA, this.camera.getValues()[0]);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.DURATION, this.duration);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FILENAME, this.videoName);

        return jaxbDestination;
    }
}
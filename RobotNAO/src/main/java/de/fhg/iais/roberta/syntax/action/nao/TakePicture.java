package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Camera;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from
 * this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class TakePicture<V> extends Action<V> {

    private final Camera camera;
    private final Expr<V> pictureName;

    private TakePicture(Camera camera, Expr<V> pictureName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TAKE_PICTURE"), properties, comment);
        Assert.notNull(camera, "Missing camera in TakePicture block!");
        Assert.isTrue(pictureName != null);
        this.camera = camera;
        this.pictureName = pictureName;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "TakePicture [" + this.camera + ", " + this.pictureName + "]";
    }

    /**
     * Creates instance of {@link TakePicture}. This instance is read only and can not be modified.
     *
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link TakePicture}
     */
    private static <V> TakePicture<V> make(Camera camera, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TakePicture<>(camera, msg, properties, comment);
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Expr<V> getPictureName() {
        return this.pictureName;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        String camera = Jaxb2Ast.extractField(fields, BlocklyConstants.CAMERA);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.FILENAME, BlocklyType.NUMBER_INT));

        return TakePicture.make(Camera.get(camera), Jaxb2Ast.convertPhraseToExpr(msg), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.CAMERA, this.camera.getValues()[0]);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FILENAME, this.pictureName);

        return jaxbDestination;
    }
}
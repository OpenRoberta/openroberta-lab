package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
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
 * This class represents the <b>robActions_display_image</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for showing a image on display.<br/>
 * <br>
 * The client must provide the {@link DisplayImageMode} and {@link Expr} (image(s) to be displayed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(DisplayImageMode, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class LEDMatrixImageAction<V> extends Action<V> {

    private final String port;
    private final Expr<V> valuesToDisplay;
    private final String displayImageMode;

    private LEDMatrixImageAction(String port, String displayImageMode, Expr<V> valuesToDisplay, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_MATRIX_IMAGE_ACTION"), properties, comment);
        Assert.isTrue(port != null && displayImageMode != null && valuesToDisplay != null);
        this.port = port;
        this.displayImageMode = displayImageMode;
        this.valuesToDisplay = valuesToDisplay;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LEDMatrixImageAction}. This instance is read only and can not be modified.
     *
     * @param mode {@link DisplayImageMode} how an image to be displayed; must <b>not</b> be null,
     * @param param {@link Expr} image(s) to be displayed; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LEDMatrixImageAction}
     */
    private static <V> LEDMatrixImageAction<V> make(
        String port,
        String displayImageMode,
        Expr<V> valuesToDisplay,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new LEDMatrixImageAction<>(port, displayImageMode, valuesToDisplay, properties, comment);
    }

    public String getPort() {
        return this.port;
    }

    /**
     * @return {@link DisplayImageMode} mode in which images will be displayed
     */
    public String getDisplayImageMode() {
        return this.displayImageMode;
    }

    /**
     * @return {@link Expr} image(s) to be displayed.
     */
    public Expr<V> getValuesToDisplay() {
        return this.valuesToDisplay;
    }

    @Override
    public String toString() {
        return "DisplayImageAction [" + this.port + ", " + this.displayImageMode + ", " + this.valuesToDisplay + "]";
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
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        final String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
        String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
        Phrase<V> image = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        return LEDMatrixImageAction.make(port, mode, Jaxb2Ast.convertPhraseToExpr(image), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setType(this.displayImageMode);
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.displayImageMode);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.valuesToDisplay);

        return jaxbDestination;
    }
}

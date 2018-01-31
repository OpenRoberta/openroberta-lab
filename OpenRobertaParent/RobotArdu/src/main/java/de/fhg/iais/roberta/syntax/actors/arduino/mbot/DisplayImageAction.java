package de.fhg.iais.roberta.syntax.actors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.MbotAstVisitor;

/**
 * This class represents the <b>mbedActions_display_image</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for showing a image on display.<br/>
 * <br>
 * The client must provide the {@link DisplayImageMode} and {@link Expr} (image(s) to be displayed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(DisplayImageMode, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class DisplayImageAction<V> extends Action<V> {

    private final Expr<V> valuesToDisplay;

    private final IActorPort displayPort;

    private DisplayImageAction(IActorPort displayPort, Expr<V> valuesToDisplay, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DISPLAY_IMAGE_ACTION"), properties, comment);
        this.valuesToDisplay = valuesToDisplay;
        this.displayPort = displayPort;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DisplayImageAction}. This instance is read only and can not be modified.
     *
     * @param mode {@link DisplayImageMode} how an image to be displayed; must <b>not</b> be null,
     * @param param {@link Expr} image(s) to be displayed; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplayImageAction}
     */
    private static <V> DisplayImageAction<V> make(IActorPort displayPort, Expr<V> valuesToDisplay, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DisplayImageAction<>(displayPort, valuesToDisplay, properties, comment);
    }

    /**
     * @return {@link Expr} image(s) to be displayed.
     */
    public Expr<V> getValuesToDisplay() {
        return this.valuesToDisplay;
    }

    @Override
    public String toString() {
        return "DisplayImageAction [" + this.valuesToDisplay + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbotAstVisitor<V>) visitor).visitDisplayImageAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Value> values = helper.extractValues(block, (short) 1);
        List<Field> fields = helper.extractFields(block, (short) 1);
        String port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        Phrase<V> image = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        return DisplayImageAction
            .make(factory.getActorPort(port), helper.convertPhraseToExpr(image), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = getPort().getValues()[0];
        Mutation mutation = new Mutation();
        jaxbDestination.setMutation(mutation);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.valuesToDisplay);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;

    }

    public IActorPort getPort() {
        return this.displayPort;
    }
}

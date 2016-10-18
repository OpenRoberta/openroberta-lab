package de.fhg.iais.roberta.syntax.action.calliope;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.mode.action.calliope.DisplayImageMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>mbedActions_display_image</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for showing a image on display.<br/>
 * <br>
 * The client must provide the {@link DisplayImageMode} and {@link Expr} (image(s) to be displayed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(DisplayImageMode, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class DisplayImageAction<V> extends Action<V> {

    private final DisplayImageMode displayImageMode;
    private final Expr<V> valuesToDisplay;

    private DisplayImageAction(DisplayImageMode displayImageMode, Expr<V> valuesToDisplay, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DISPLAY_IMAGE_ACTION"), properties, comment);
        Assert.isTrue(displayImageMode != null && valuesToDisplay != null);
        this.displayImageMode = displayImageMode;
        this.valuesToDisplay = valuesToDisplay;
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
    private static <V> DisplayImageAction<V> make(
        DisplayImageMode displayImageMode,
        Expr<V> valuesToDisplay,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new DisplayImageAction<>(displayImageMode, valuesToDisplay, properties, comment);
    }

    /**
     * @return {@link DisplayImageMode} mode in which images will be displayed
     */
    public DisplayImageMode getDisplayImageMode() {
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
        return "DisplayImageAction [" + this.displayImageMode + ", " + this.valuesToDisplay + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return null;
        //        return visitor.visitDriveAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        //        List<Field> fields;
        //        String mode;
        //        List<Value> values;
        //        MotionParam<V> mp;
        //        Phrase<V> power;
        //        IRobotFactory factory = helper.getModeFactory();
        //        fields = helper.extractFields(block, (short) 1);
        //        mode = helper.extractField(fields, BlocklyConstants.DIRECTION);
        //
        //        if ( !block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON) ) {
        //            values = helper.extractValues(block, (short) 2);
        //            power = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
        //            Phrase<V> distance = helper.extractValue(values, new ExprParam(BlocklyConstants.DISTANCE, Integer.class));
        //            MotorDuration<V> md = new MotorDuration<>(factory.getMotorMoveMode("DISTANCE"), helper.convertPhraseToExpr(distance));
        //            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(power)).duration(md).build();
        //        } else {
        //            values = helper.extractValues(block, (short) 1);
        //            power = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
        //            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(power)).build();
        //        }
        //        return DisplayImageAction.make(factory.getDriveDirection(mode), mp, helper.extractBlockProperties(block), helper.extractComment(block));
        return null;
    }

    @Override
    public Block astToBlock() {
        //        Block jaxbDestination = new Block();
        //        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        //
        //        if ( getProperty().getBlockType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON_FOR) ) {
        //            JaxbTransformerHelper
        //                .addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString() == "FOREWARD" ? getDirection().toString() : "BACKWARDS");
        //        } else {
        //            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString());
        //        }
        //        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER, getParam().getSpeed());
        //
        //        if ( getParam().getDuration() != null ) {
        //            JaxbTransformerHelper.addValue(jaxbDestination, getParam().getDuration().getType().toString(), getParam().getDuration().getValue());
        //        }
        //        return jaxbDestination;
        return null;
    }
}

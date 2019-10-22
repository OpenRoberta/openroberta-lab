package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import org.checkerframework.checker.units.qual.degrees;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Joint;
import de.fhg.iais.roberta.mode.action.nao.RelativeAbsolute;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
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
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for making
 * the robot walk for a distance.<br/>
 * <br/>
 * The client must provide the {@link joint} and {@link degrees} (direction and distance to walk).
 */
public final class MoveJoint<V> extends Action<V> {

    private final Joint joint;
    private final RelativeAbsolute relativeAbsolute;
    private final Expr<V> degrees;

    private MoveJoint(Joint joint, RelativeAbsolute relativeAbsolute, Expr<V> degrees, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MOVE_JOINT"), properties, comment);
        Assert.notNull(joint, "Missing joint in MoveJoint block!");
        Assert.notNull(relativeAbsolute, "Missing relativeAbsolute in MoveJoint block!");
        this.joint = joint;
        this.relativeAbsolute = relativeAbsolute;
        this.degrees = degrees;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MoveJoint}. This instance is read only and can not be modified.
     *
     * @param direction {@link joint} the robot will walk,
     * @param distance {@link degrees} the robot will walk for,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MoveJoint}
     */
    private static <V> MoveJoint<V> make(
        Joint joint,
        RelativeAbsolute relativeAbsolute,
        Expr<V> degrees,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new MoveJoint<V>(joint, relativeAbsolute, degrees, properties, comment);
    }

    public RelativeAbsolute getRelativeAbsolute() {
        return this.relativeAbsolute;
    }

    public Joint getJoint() {
        return this.joint;
    }

    public Expr<V> getDegrees() {
        return this.degrees;
    }

    @Override
    public String toString() {
        return "MoveJoint [" + this.joint + ", " + this.relativeAbsolute + ", " + this.degrees + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitMoveJoint(this);
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
        List<Value> values = helper.extractValues(block, (short) 1);

        String joint = helper.extractField(fields, BlocklyConstants.JOINT);
        String relativeAbsolute = helper.extractField(fields, BlocklyConstants.MODE);

        Phrase<V> walkDistance = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));

        return MoveJoint
            .make(
                Joint.get(joint),
                RelativeAbsolute.get(relativeAbsolute),
                helper.convertPhraseToExpr(walkDistance),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.JOINT, this.joint.toString());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, this.relativeAbsolute.toString());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.POWER, this.degrees);

        return jaxbDestination;
    }
}

package de.fhg.iais.roberta.syntax.action.vorwerk;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
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
 * This class represents the <b>vorwerkActions_brush_on</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for applying a posture<br/>
 * <br/>
 * The client must provide the {@link Expr} speed.
 */
public final class VacuumOn<V> extends Action<V> {
    private final Expr<V> speed;

    private VacuumOn(Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("VACUUM_ON"), properties, comment);
        Assert.notNull(speed, "Missing speed!");
        this.speed = speed;
        setReadOnly();
    }

    /**
     * Creates instance of {@link VacuumOn}. This instance is read only and can not be modified.
     *
     * @param speed {@link Expr} speed of the brush,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link VacuumOn}
     */
    private static <V> VacuumOn<V> make(Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new VacuumOn<V>(speed, properties, comment);
    }

    public Expr<V> getSpeed() {
        return this.speed;
    }

    @Override
    public String toString() {
        return "VacuumOn [" + this.speed + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        Phrase<V> speed = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.NUMBER));
        return VacuumOn.make(Jaxb2Ast.convertPhraseToExpr(speed), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.OUT, this.speed);

        return jaxbDestination;
    }
}

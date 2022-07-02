package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "WALK_ASYNC", category = "ACTOR", blocklyNames = {"naoActions_walk_async"})
public final class WalkAsync<V> extends Action<V> {

    public final Expr<V> XSpeed;
    public final Expr<V> YSpeed;
    public final Expr<V> ZSpeed;

    public WalkAsync(Expr<V> XSpeed, Expr<V> YSpeed, Expr<V> ZSpeed, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        this.XSpeed = XSpeed;
        this.YSpeed = YSpeed;
        this.ZSpeed = ZSpeed;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "WalkTo [" + this.XSpeed + ", " + this.YSpeed + ", " + this.ZSpeed + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 3);

        Phrase<V> XSpeed = helper.extractValue(values, new ExprParam(BlocklyConstants.X + BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));
        Phrase<V> YSpeed = helper.extractValue(values, new ExprParam(BlocklyConstants.Y + BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));
        Phrase<V> ZSpeed = helper.extractValue(values, new ExprParam(BlocklyConstants.Z + BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));

        return new WalkAsync<V>(Jaxb2Ast.convertPhraseToExpr(XSpeed), Jaxb2Ast.convertPhraseToExpr(YSpeed), Jaxb2Ast.convertPhraseToExpr(ZSpeed), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.X + BlocklyConstants.SPEED, this.XSpeed);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Y + BlocklyConstants.SPEED, this.YSpeed);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Z + BlocklyConstants.SPEED, this.ZSpeed);

        return jaxbDestination;
    }
}

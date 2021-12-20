package de.poulter.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

public final class DcMotorSetAction<V> extends Action<V> {

    private final String port;
    private final Expr<V> speed;

    private DcMotorSetAction(String port, Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DC_MOTOR_SET_ACTION"), properties, comment);

        Assert.notNull(port);
        Assert.notNull(speed);

        this.port = port;
        this.speed = speed;

        setReadOnly();
    }

    public static <V> DcMotorSetAction<V> make(String port, Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DcMotorSetAction<>(port, speed, properties, comment);
    }

    public String getPort() {
        return this.port;
    }

    public Expr<V> getSpeed() {
        return this.speed;
    }

    @Override
    public String toString() {
        return "DcMotorSetAction [" + this.port + ", " + this.speed + "]";
    }
    
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        Phrase<V> speed = helper.extractValue(values, new ExprParam("POWER", BlocklyType.NUMBER_INT));
        String port = Jaxb2Ast.extractField(fields, "PORT");
        
        return DcMotorSetAction.make(
            port, 
            Jaxb2Ast.convertPhraseToExpr(speed),
            Jaxb2Ast.extractBlockProperties(block),
            Jaxb2Ast.extractComment(block)
        );
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, "PORT", this.port);
        Ast2Jaxb.addValue(jaxbDestination, "POWER", this.speed);

        return jaxbDestination;
    }
}
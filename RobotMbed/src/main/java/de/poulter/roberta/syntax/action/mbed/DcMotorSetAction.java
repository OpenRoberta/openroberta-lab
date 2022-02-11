package de.poulter.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
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

    private final String actorPort;
    private final Direction direction;
    private final Expr<V> motor;
    private final Expr<V> speed;    

    private DcMotorSetAction(String actorPort, Expr<V> motor, Direction direction, Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DC_MOTOR_SET_ACTION"), properties, comment);

        Assert.notNull(motor);
        Assert.notNull(direction);
        Assert.notNull(speed);

        this.actorPort = actorPort;
        this.motor = motor;
        this.direction = direction;
        this.speed = speed;

        setReadOnly();
    }

    public static <V> DcMotorSetAction<V> make(String actorPort, Expr<V> motor, Direction direction, Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DcMotorSetAction<>(actorPort, motor, direction, speed, properties, comment);
    }
    
    public String getActorPort() {
        return actorPort;
    }
    
    public Expr<V> getMotor() {
        return motor;
    }

    public Direction getDirection() {
        return direction;
    }
    
    public Expr<V> getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "DcMotorSetAction [" + actorPort + ", " + motor + ", " + direction + ", " + speed + "]";
    }
    
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String actorPort = Jaxb2Ast.optField(fields, BlocklyConstants.ACTORPORT);
        Phrase<V> motor = helper.extractValue(values, new ExprParam("MOTOR", BlocklyType.NUMBER_INT));
        Phrase<V> speed = helper.extractValue(values, new ExprParam("SPEED", BlocklyType.NUMBER_INT));
        String direction = Jaxb2Ast.optField(fields, "DIRECTION");
        
        return DcMotorSetAction.make(
            Jaxb2Ast.sanitizePort(actorPort),
            Jaxb2Ast.convertPhraseToExpr(motor),
            Direction.fromString(direction),
            Jaxb2Ast.convertPhraseToExpr(speed),
            Jaxb2Ast.extractBlockProperties(block),
            Jaxb2Ast.extractComment(block)
        );
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);        
        Ast2Jaxb.addValue(jaxbDestination, "MOTOR", motor);
        Ast2Jaxb.addField(jaxbDestination, "DIRECTION", direction.toString());
        Ast2Jaxb.addValue(jaxbDestination, "SPEED", speed);

        if ( actorPort != null ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, actorPort);
        }
        
        return jaxbDestination;
    }
}
package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.poulter.roberta.syntax.action.mbed.Direction;


@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_dcmotor_set"}, name = "DC_MOTOR_SET_ACTION")
public final class DcMotorSetAction extends Action {

    @NepoField(name = "ACTORPORT")    
    public final String actorPort;
    
    @NepoValue(name = "MOTOR", type = BlocklyType.NUMBER_INT)
    public final Expr motor;

    @NepoField(name = "DIRECTION")
    public final Direction direction;
        
    @NepoValue(name = "SPEED", type = BlocklyType.NUMBER_INT)
    public final Expr speed;    
   
    public DcMotorSetAction(BlocklyProperties properties, String actorPort, Expr motor, Direction direction, Expr speed) {
        super(properties);
        
        Assert.notNull(actorPort);
        Assert.notNull(motor);
        Assert.notNull(direction);
        Assert.notNull(speed);
        
        this.actorPort = actorPort;
        this.motor = motor;
        this.direction = direction;
        this.speed = speed;
        
        setReadOnly();
    }
    
    @Override
    public String toString() {
        return "DcMotorSetAction [" + this.actorPort + ", " + this.motor + ", " + this.direction + ", " + this.speed + "]";
    }

    public static Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String actorPort = Jaxb2Ast.optField(fields, BlocklyConstants.ACTORPORT);
        Phrase motor = helper.extractValue(values, new ExprParam("MOTOR", BlocklyType.NUMBER_INT));
        Phrase speed = helper.extractValue(values, new ExprParam("SPEED", BlocklyType.NUMBER_INT));
        String direction = Jaxb2Ast.optField(fields, "DIRECTION");
  
        return new DcMotorSetAction(
                Jaxb2Ast.extractBlocklyProperties(block),
                Jaxb2Ast.sanitizePort(actorPort),
                Jaxb2Ast.convertPhraseToExpr(motor),
                Direction.fromString(direction),
                Jaxb2Ast.convertPhraseToExpr(speed)
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





















//import de.fhg.iais.roberta.blockly.generated.Block;
//import de.fhg.iais.roberta.blockly.generated.Field;
//import de.fhg.iais.roberta.blockly.generated.Value;
////import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
////import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
////import de.fhg.iais.roberta.util.syntax.BlocklyComment;
////import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
//import de.fhg.iais.roberta.syntax.Phrase;
//import de.fhg.iais.roberta.syntax.action.Action;
//import de.fhg.iais.roberta.syntax.lang.expr.Expr;
//import de.fhg.iais.roberta.transformer.Ast2Jaxb;
//import de.fhg.iais.roberta.transformer.ExprParam;
//import de.fhg.iais.roberta.transformer.Jaxb2Ast;
//import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
//import de.fhg.iais.roberta.typecheck.BlocklyType;
//import de.fhg.iais.roberta.util.dbc.Assert;
//
//public final class DcMotorSetAction extends Action {
//
//    private final String actorPort;
//    private final Direction direction;
//    private final Expr motor;
//    private final Expr speed;    
//
//    private DcMotorSetAction(String actorPort, Expr motor, Direction direction, Expr speed, BlocklyBlockProperties properties, BlocklyComment comment) {
//        super(BlockTypeContainer.getByName("DC_MOTOR_SET_ACTION"), properties, comment);
//
//        Assert.notNull(motor);
//        Assert.notNull(direction);
//        Assert.notNull(speed);
//
//        this.actorPort = actorPort;
//        this.motor = motor;
//        this.direction = direction;
//        this.speed = speed;
//
//        setReadOnly();
//    }
//
//    public static DcMotorSetAction make(String actorPort, Expr motor, Direction direction, Expr speed, BlocklyBlockProperties properties, BlocklyComment comment) {
//        return new DcMotorSetAction(actorPort, motor, direction, speed, properties, comment);
//    }
//    
//    public String getActorPort() {
//        return actorPort;
//    }
//    
//    public Expr getMotor() {
//        return motor;
//    }
//
//    public Direction getDirection() {
//        return direction;
//    }
//    
//    public Expr getSpeed() {
//        return speed;
//    }
//
//    @Override
//    public String toString() {
//        return "DcMotorSetAction [" + actorPort + ", " + motor + ", " + direction + ", " + speed + "]";
//    }
//    
//    public static Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
//        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
//        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
//        String actorPort = Jaxb2Ast.optField(fields, BlocklyConstants.ACTORPORT);
//        Phrase motor = helper.extractValue(values, new ExprParam("MOTOR", BlocklyType.NUMBER_INT));
//        Phrase speed = helper.extractValue(values, new ExprParam("SPEED", BlocklyType.NUMBER_INT));
//        String direction = Jaxb2Ast.optField(fields, "DIRECTION");
//        
//        return DcMotorSetAction.make(
//            Jaxb2Ast.sanitizePort(actorPort),
//            Jaxb2Ast.convertPhraseToExpr(motor),
//            Direction.fromString(direction),
//            Jaxb2Ast.convertPhraseToExpr(speed),
//            Jaxb2Ast.extractBlockProperties(block),
//            Jaxb2Ast.extractComment(block)
//        );
//    }
//
//    @Override
//    public Block astToBlock() {
//        Block jaxbDestination = new Block();
//        Ast2Jaxb.setBasicProperties(this, jaxbDestination);        
//        Ast2Jaxb.addValue(jaxbDestination, "MOTOR", motor);
//        Ast2Jaxb.addField(jaxbDestination, "DIRECTION", direction.toString());
//        Ast2Jaxb.addValue(jaxbDestination, "SPEED", speed);
//
//        if ( actorPort != null ) {
//            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, actorPort);
//        }
//        
//        return jaxbDestination;
//    }
//}
package de.fhg.iais.roberta.ast.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
<<<<<<< HEAD
=======
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.BlinkMode;
import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;
import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotionParam;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDuration;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorMoveMode;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPicture;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnDirection;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyList;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.ListCreate;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Unary.Op;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.ast.syntax.functions.FunctionNames;
import de.fhg.iais.roberta.ast.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.ast.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.ast.syntax.functions.LenghtOfIsEmptyFunct;
import de.fhg.iais.roberta.ast.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.ast.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.ast.syntax.methods.MethodCall;
import de.fhg.iais.roberta.ast.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickKey;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GetSampleSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.MotorTachoMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorType;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
>>>>>>> Initial strukture for the Bluetooth gui
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * JAXB to AST transformer. Client should provide tree of jaxb objects.
 */
public class JaxbBlocklyProgramTransformer<V> extends JaxbAstTransformer<V> {

    /**
     * Converts object of type {@link BlockSet} to AST tree.
     *
     * @param program
     */
    public void transform(BlockSet set) {
        List<Instance> instances = set.getInstance();
        for ( Instance instance : instances ) {
            instanceToAST(instance);
        }
    }

    private void instanceToAST(Instance instance) {
        List<Block> blocks = instance.getBlock();
        Location<V> location = Location.make(instance.getX(), instance.getY());
        ArrayList<Phrase<V>> range = new ArrayList<Phrase<V>>();
        range.add(location);
        for ( Block block : blocks ) {
            range.add(blockToAST(block));
        }
        this.tree.add(range);
    }

    @Override
    protected Phrase<V> blockToAST(Block block) {
        return invokeJaxbToAstTransform(block);
    }

    private Phrase<V> invokeJaxbToAstTransform(Block block) {
        if ( block == null ) {
            throw new DbcException("Invalid block: " + block);
        }
        String sUpper = block.getType().trim();
        for ( Phrase.Kind co : Phrase.Kind.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return invokeMethod(block, co.getAstClass().getName());
            }
            for ( String value : co.getBlocklyNames() ) {
                if ( sUpper.equals(value) ) {
                    return invokeMethod(block, co.getAstClass().getName());
                }
<<<<<<< HEAD
            }
        }
        throw new DbcException("Invalid Block: " + block.getType());
    }

    @SuppressWarnings("unchecked")
    private Phrase<V> invokeMethod(Block block, String className) {
        Method method;
        try {
            method = Class.forName(className).getMethod("jaxbToAst", Block.class, JaxbAstTransformer.class);
            return (Phrase<V>) method.invoke(null, block, this);
        } catch ( NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | DbcException e ) {
            throw new DbcException(e.getCause().getMessage());
=======
                StmtList<V> listOfVariables = StmtList.make();
                listOfVariables.setReadOnly();
                return MainTask.make(listOfVariables, properties, comment);

            case "robControls_activity":
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.ACTIVITY, String.class));
                return ActivityTask.make(convertPhraseToExpr(expr), properties, comment);

            case "robControls_start_activity":
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.ACTIVITY, String.class));
                return StartActivityTask.make(convertPhraseToExpr(expr), properties, comment);

            case "robProcedures_defnoreturn":
                fields = extractFields(block, (short) 1);
                name = extractField(fields, BlocklyConstants.NAME);

                statements = extractStatements(block, (short) 2);
                exprList = statementsToExprs(statements, BlocklyConstants.ST);
                statement = extractStatement(statements, BlocklyConstants.STACK);

                return MethodVoid.make(name, exprList, statement, properties, comment);

            case "robProcedures_defreturn":
                fields = extractFields(block, (short) 2);
                name = extractField(fields, BlocklyConstants.NAME);

                List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
                values = new ArrayList<Value>();
                statements = new ArrayList<Statement>();
                convertStmtValList(values, statements, valAndStmt);
                exprList = statementsToExprs(statements, BlocklyConstants.ST);
                statement = extractStatement(statements, BlocklyConstants.STACK);
                expr = extractValue(values, new ExprParam(BlocklyConstants.RETURN, NullConst.class));

                return MethodReturn.make(
                    name,
                    exprList,
                    statement,
                    BlocklyType.get(extractField(fields, BlocklyConstants.TYPE)),
                    convertPhraseToExpr(expr),
                    properties,
                    comment);

            case "robProcedures_ifreturn":
                values = extractValues(block, (short) 2);
                left = extractValue(values, new ExprParam(BlocklyConstants.CONDITION, Boolean.class));
                right = extractValue(values, new ExprParam(BlocklyConstants.VALUE, NullConst.class));
                mode = block.getMutation().getReturnType() == null ? "void" : block.getMutation().getReturnType();
                return MethodIfReturn.make(convertPhraseToExpr(left), BlocklyType.get(mode), convertPhraseToExpr(right), properties, comment);

            case "robProcedures_callnoreturn":
            case "robProcedures_callreturn":
                BlocklyType outputType = block.getMutation().getOutputType() == null ? null : BlocklyType.get(block.getMutation().getOutputType());
                String methodName = block.getMutation().getName();
                List<Arg> arguments = block.getMutation().getArg();
                ExprList<V> parameters = argumentsToExprList(arguments);

                values = extractValues(block, (short) arguments.size());

                ExprList<V> parametersValues = valuesToExprList(values, EmptyExpr.class, arguments.size(), BlocklyConstants.ARG);

                return MethodCall.make(methodName, parameters, parametersValues, outputType, properties, comment);
            
            case "com_startConnection":
                values = extractValues(block, (short) 1);
                Phrase<V> bluetoothConnectAddress = extractValue(values, new ExprParam(BlocklyConstants.ADDRESS, String.class));
                return BluetoothConnectAction.make(convertPhraseToExpr(bluetoothConnectAddress), properties, comment);
                
            case "com_sendBlock":
                values = extractValues(block, (short) 2);
                Phrase<V> bluetoothSendMessage = extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, String.class));
                Phrase<V> bluetoothSendConnection = extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, null));
                return BluetoothSendAction.make(convertPhraseToExpr(bluetoothSendConnection), convertPhraseToExpr(bluetoothSendMessage), properties, comment);
                
            case "com_receiveBlock":
                values = extractValues(block, (short) 1);
                Phrase<V> bluetoothRecieveConnection = extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, null));
                return BluetoothReceiveAction.make(convertPhraseToExpr(bluetoothRecieveConnection), properties, comment);
             
            default:
                throw new DbcException("Invalid Block: " + block.getType());
>>>>>>> Initial strukture for the Bluetooth gui
        }
    }
}

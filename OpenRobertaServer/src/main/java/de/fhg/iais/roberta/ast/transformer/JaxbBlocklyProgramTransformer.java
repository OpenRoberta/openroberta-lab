package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.IndexLocation;
import de.fhg.iais.roberta.ast.syntax.ListElementOperations;
import de.fhg.iais.roberta.ast.syntax.Phrase;
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
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * JAXB to AST transformer. Client should provide tree of jaxb objects.
 */
public class JaxbBlocklyProgramTransformer<V> extends JaxbAstTransformer<V> {

    private int variable_counter = 0;

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
        this.tree.add(location);
        for ( Block block : blocks ) {
            this.tree.add(blockToAST(block));
        }
    }

    @Override
    protected Phrase<V> blockToAST(Block block) {
        BlocklyComment comment = extractComment(block);
        BlocklyBlockProperties properties = extractBlockProperties(block);

        List<Value> values;
        List<Statement> statements;
        List<Field> fields;
        List<ExprParam> exprParams;
        List<String> strParams;
        List<Expr<V>> params;
        ExprList<V> exprList;

        Phrase<V> left;
        Phrase<V> right;
        Phrase<V> expr;
        Phrase<V> var;

        String mode;
        String port;
        String op;
        MotionParam<V> mp;
        MotorDuration<V> md;

        switch ( block.getType() ) {
        // ACTION
            case "robActions_motor_on":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.MOTORPORT);
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
                mp = new MotionParam.Builder<V>().speed(convertPhraseToExpr(expr)).build();
                return MotorOnAction.make(ActorPort.get(port), mp, properties, comment);

            case "robActions_motor_on_for":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, BlocklyConstants.MOTORPORT);
                mode = extractField(fields, BlocklyConstants.MOTORROTATION);
                values = extractValues(block, (short) 2);
                left = extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
                right = extractValue(values, new ExprParam(BlocklyConstants.VALUE, Integer.class));
                md = new MotorDuration<V>(MotorMoveMode.get(mode), convertPhraseToExpr(right));
                mp = new MotionParam.Builder<V>().speed(convertPhraseToExpr(left)).duration(md).build();
                return MotorOnAction.make(ActorPort.get(port), mp, properties, comment);

            case "robActions_motorDiff_on":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, BlocklyConstants.DIRECTION);
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
                mp = new MotionParam.Builder<V>().speed(convertPhraseToExpr(expr)).build();
                return DriveAction.make(DriveDirection.get(mode), mp, properties, comment);

            case "robActions_motorDiff_on_for":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, BlocklyConstants.DIRECTION);
                values = extractValues(block, (short) 2);
                left = extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
                right = extractValue(values, new ExprParam(BlocklyConstants.DISTANCE, Integer.class));
                md = new MotorDuration<V>(MotorMoveMode.DISTANCE, (Expr<V>) right);
                mp = new MotionParam.Builder<V>().speed(convertPhraseToExpr(left)).duration(md).build();
                return DriveAction.make(DriveDirection.get(mode), mp, properties, comment);

            case "robActions_motorDiff_turn":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, BlocklyConstants.DIRECTION);
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
                mp = new MotionParam.Builder<V>().speed(convertPhraseToExpr(expr)).build();
                return TurnAction.make(TurnDirection.get(mode), mp, properties, comment);

            case "robActions_motorDiff_turn_for":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, BlocklyConstants.DIRECTION);
                values = extractValues(block, (short) 2);
                left = extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
                right = extractValue(values, new ExprParam(BlocklyConstants.DEGREE, Integer.class));
                md = new MotorDuration<V>(MotorMoveMode.DEGREE, convertPhraseToExpr(right));
                mp = new MotionParam.Builder<V>().speed(convertPhraseToExpr(left)).duration(md).build();
                return TurnAction.make(TurnDirection.get(mode), mp, properties, comment);

            case "robActions_motorDiff_stop":
                return MotorDriveStopAction.make(properties, comment);

            case "robActions_motor_getPower":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.MOTORPORT);
                return MotorGetPowerAction.make(ActorPort.get(port), properties, comment);

            case "robActions_motor_setPower":
                fields = extractFields(block, (short) 1);
                values = extractValues(block, (short) 1);
                port = extractField(fields, BlocklyConstants.MOTORPORT);
                left = extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
                return MotorSetPowerAction.make(ActorPort.get(port), convertPhraseToExpr(left), properties, comment);

            case "robActions_motor_stop":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, BlocklyConstants.MOTORPORT);
                mode = extractField(fields, BlocklyConstants.MODE_);
                return MotorStopAction.make(ActorPort.get(port), MotorStopMode.get(mode), properties, comment);

            case "robActions_display_text":
                values = extractValues(block, (short) 3);
                Phrase<V> msg = extractValue(values, new ExprParam(BlocklyConstants.OUT, String.class));
                Phrase<V> col = extractValue(values, new ExprParam(BlocklyConstants.COL_, Integer.class));
                Phrase<V> row = extractValue(values, new ExprParam(BlocklyConstants.ROW_, Integer.class));
                return ShowTextAction.make(convertPhraseToExpr(msg), convertPhraseToExpr(col), convertPhraseToExpr(row), properties, comment);

            case "robActions_display_picture":
                fields = extractFields(block, (short) 1);
                values = extractValues(block, (short) 2);
                String pic = extractField(fields, BlocklyConstants.PICTURE);
                Phrase<V> x = extractValue(values, new ExprParam(BlocklyConstants.X_, Integer.class));
                Phrase<V> y = extractValue(values, new ExprParam(BlocklyConstants.Y_, Integer.class));
                return ShowPictureAction.make(ShowPicture.get(pic), convertPhraseToExpr(x), convertPhraseToExpr(y), properties, comment);

            case "robActions_display_clear":
                return ClearDisplayAction.make(properties, comment);

            case "robActions_play_tone":
                values = extractValues(block, (short) 2);
                left = extractValue(values, new ExprParam(BlocklyConstants.FREQUENCE, Integer.class));
                right = extractValue(values, new ExprParam(BlocklyConstants.DURATION, Integer.class));
                return ToneAction.make(convertPhraseToExpr(left), convertPhraseToExpr(right), properties, comment);

            case "robActions_play_file":
                fields = extractFields(block, (short) 1);
                String filename = extractField(fields, BlocklyConstants.FILE);
                return PlayFileAction.make(filename, properties, comment);

            case "robActions_play_setVolume":
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.VOLUME, Integer.class));
                return VolumeAction.make(VolumeAction.Mode.SET, convertPhraseToExpr(expr), properties, comment);

            case "robActions_play_getVolume":
                expr = NullConst.make(properties, comment);
                return VolumeAction.make(VolumeAction.Mode.GET, convertPhraseToExpr(expr), properties, comment);

            case "robActions_brickLight_on":
                fields = extractFields(block, (short) 2);
                String color = extractField(fields, BlocklyConstants.SWITCH_COLOR);
                String blink = extractField(fields, BlocklyConstants.SWITCH_BLINK);
                return LightAction.make(BrickLedColor.get(color), BlinkMode.get(blink), properties, comment);

            case "robActions_brickLight_off":
                return LightStatusAction.make(LightStatusAction.Status.OFF, properties, comment);

            case "robActions_brickLight_reset":
                return LightStatusAction.make(LightStatusAction.Status.RESET, properties, comment);

                // Sensoren
            case "robSensors_touch_isPressed":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.SENSORPORT);
                return TouchSensor.make(SensorPort.get(port), properties, comment);

            case "robSensors_ultrasonic_getSample":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, BlocklyConstants.SENSORPORT);
                mode = extractField(fields, BlocklyConstants.MODE_);
                return UltrasonicSensor.make(UltrasonicSensorMode.get(mode), SensorPort.get(port), properties, comment);

            case "robSensors_colour_getSample":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, BlocklyConstants.SENSORPORT);
                mode = extractField(fields, BlocklyConstants.MODE_);
                return ColorSensor.make(ColorSensorMode.get(mode), SensorPort.get(port), properties, comment);

            case "robSensors_infrared_getSample":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, BlocklyConstants.SENSORPORT);
                mode = extractField(fields, BlocklyConstants.MODE_);
                return InfraredSensor.make(InfraredSensorMode.get(mode), SensorPort.get(port), properties, comment);

            case "robSensors_encoder_getSample":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, BlocklyConstants.MOTORPORT);
                mode = extractField(fields, BlocklyConstants.MODE_);
                return EncoderSensor.make(MotorTachoMode.get(mode), ActorPort.get(port), properties, comment);

            case "robSensors_encoder_reset":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.MOTORPORT);
                return EncoderSensor.make(MotorTachoMode.RESET, ActorPort.get(port), properties, comment);

            case "robSensors_key_isPressed":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.KEY);
                return BrickSensor.make(BrickSensor.Mode.IS_PRESSED, BrickKey.get(port), properties, comment);

            case "robSensors_gyro_getSample":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, BlocklyConstants.SENSORPORT);
                mode = extractField(fields, BlocklyConstants.MODE_);
                return GyroSensor.make(GyroSensorMode.get(mode), SensorPort.get(port), properties, comment);

            case "robSensors_gyro_reset":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.SENSORPORT);
                return GyroSensor.make(GyroSensorMode.RESET, SensorPort.get(port), properties, comment);

            case "robSensors_timer_getSample":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.SENSORNUM);
                return TimerSensor.make(TimerSensorMode.GET_SAMPLE, Integer.valueOf(port), properties, comment);

            case "robSensors_timer_reset":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, BlocklyConstants.SENSORNUM);
                return TimerSensor.make(TimerSensorMode.RESET, Integer.valueOf(port), properties, comment);

            case "robSensors_getSample":
                fields = extractFields(block, (short) 2);
                mode = extractField(fields, BlocklyConstants.SENSORTYPE);
                port = extractField(fields, SensorType.get(mode).getPortTypeName());
                return GetSampleSensor.make(SensorType.get(mode), port, properties, comment);

                // Logik
            case "logic_compare":
                return blockToBinaryExpr(
                    block,
                    new ExprParam(BlocklyConstants.A, Integer.class),
                    new ExprParam(BlocklyConstants.B, Integer.class),
                    BlocklyConstants.OP_);

            case "logic_operation":
                return blockToBinaryExpr(
                    block,
                    new ExprParam(BlocklyConstants.A, Boolean.class),
                    new ExprParam(BlocklyConstants.B, Boolean.class),
                    BlocklyConstants.OP_);

            case "logic_negate":
                return blockToUnaryExpr(block, new ExprParam(BlocklyConstants.BOOL, Boolean.class), BlocklyConstants.NOT);

            case "logic_boolean":
                return blockToConst(block, BlocklyConstants.BOOL);

            case "logic_null":
                return NullConst.make(properties, comment);

            case "logic_ternary":
                values = block.getValue();
                Assert.isTrue(values.size() <= 3, "Number of values is not less or equal to 3!");
                Phrase<V> ifExpr = extractValue(values, new ExprParam(BlocklyConstants.IF, Boolean.class));
                Phrase<V> thenStmt = extractValue(values, new ExprParam(BlocklyConstants.THEN, Stmt.class));
                Phrase<V> elseStmt = extractValue(values, new ExprParam(BlocklyConstants.ELSE, Stmt.class));
                StmtList<V> thenList = StmtList.make();
                thenList.addStmt(ExprStmt.make(convertPhraseToExpr(thenStmt)));
                thenList.setReadOnly();
                StmtList<V> elseList = StmtList.make();
                elseList.addStmt(ExprStmt.make(convertPhraseToExpr(elseStmt)));
                elseList.setReadOnly();
                return IfStmt.make(convertPhraseToExpr(ifExpr), thenList, elseList, properties, comment, 0, 0);

                // Mathematik
            case "math_number":
                return blockToConst(block, BlocklyConstants.NUM);

            case "math_arithmetic":
                op = extractOperation(block, BlocklyConstants.OP_);
                if ( op.equals(BlocklyConstants.POWER) ) {
                    exprParams = new ArrayList<ExprParam>();
                    exprParams.add(new ExprParam(BlocklyConstants.A, Integer.class));
                    exprParams.add(new ExprParam(BlocklyConstants.B, Integer.class));
                    params = extractExprParameters(block, exprParams);
                    return MathPowerFunct.make(FunctionNames.POWER, params, extractBlockProperties(block), extractComment(block));
                }
                return blockToBinaryExpr(
                    block,
                    new ExprParam(BlocklyConstants.A, Integer.class),
                    new ExprParam(BlocklyConstants.B, Integer.class),
                    BlocklyConstants.OP_);

            case "math_constant":
                return blockToConst(block, BlocklyConstants.CONSTANT);

            case "math_number_property":
                boolean divisorInput = block.getMutation().isDivisorInput();
                op = extractOperation(block, BlocklyConstants.PROPERTY);
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.NUMBER_TO_CHECK, Integer.class));

                if ( op.equals(BlocklyConstants.DIVISIBLE_BY) ) {
                    Assert.isTrue(divisorInput, "Divisor input is not equal to true!");
                    exprParams.add(new ExprParam(BlocklyConstants.DIVISOR, Integer.class));
                }
                params = extractExprParameters(block, exprParams);
                return MathNumPropFunct.make(FunctionNames.get(op), params, extractBlockProperties(block), extractComment(block));

            case "math_change":
                values = extractValues(block, (short) 1);
                left = extractVar(block);
                right = extractValue(values, new ExprParam(BlocklyConstants.DELTA, Integer.class));
                return Binary.make(Binary.Op.MATH_CHANGE, convertPhraseToExpr(left), convertPhraseToExpr(right), properties, comment);

            case "math_single":
            case "math_round":
            case "math_trig":
                if ( getOperation(block, BlocklyConstants.OP_).equals(BlocklyConstants.NEG) ) {
                    return blockToUnaryExpr(block, new ExprParam(BlocklyConstants.NUM, Integer.class), BlocklyConstants.OP_);
                }
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.NUM, Integer.class));
                op = getOperation(block, BlocklyConstants.OP_);
                params = extractExprParameters(block, exprParams);
                return MathSingleFunct.make(FunctionNames.get(op), params, extractBlockProperties(block), extractComment(block));

            case "math_on_list":
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.LIST_, ArrayList.class));
                op = getOperation(block, BlocklyConstants.OP_);
                params = extractExprParameters(block, exprParams);
                return MathOnListFunct.make(FunctionNames.get(op), params, extractBlockProperties(block), extractComment(block));

            case "math_modulo":
                return blockToBinaryExpr(
                    block,
                    new ExprParam("DIVIDEND", Integer.class),
                    new ExprParam(BlocklyConstants.DIVISOR, Integer.class),
                    BlocklyConstants.MOD);

            case "math_constrain":
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.VALUE, Integer.class));
                exprParams.add(new ExprParam(BlocklyConstants.LOW, Integer.class));
                exprParams.add(new ExprParam(BlocklyConstants.HIGH, Integer.class));
                params = extractExprParameters(block, exprParams);
                return MathConstrainFunct.make(params, extractBlockProperties(block), extractComment(block));

            case "math_random_int":
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.FROM_, Integer.class));
                exprParams.add(new ExprParam(BlocklyConstants.TO_, Integer.class));
                params = extractExprParameters(block, exprParams);
                return MathRandomIntFunct.make(params, extractBlockProperties(block), extractComment(block));

            case "math_random_float":
                return MathRandomFloatFunct.make(extractBlockProperties(block), extractComment(block));

                // TEXT
            case "text":
                return blockToConst(block, BlocklyConstants.TEXT);

            case "robText_join":
            case "text_join":
                exprList = blockToExprList(block, String.class);
                List<Expr<V>> textList = new ArrayList<Expr<V>>();
                textList.add(exprList);
                return TextJoinFunct.make(textList, properties, comment);

            case "text_append":
                values = extractValues(block, (short) 2);
                left = extractValue(values, new ExprParam(BlocklyConstants.VAR, String.class));
                right = extractValue(values, new ExprParam(BlocklyConstants.TEXT, String.class));
                return Binary.make(Binary.Op.TEXT_APPEND, convertPhraseToExpr(left), convertPhraseToExpr(right), properties, comment);

            case "lists_length":
            case "lists_isEmpty":
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.VALUE, String.class));
                params = extractExprParameters(block, exprParams);
                return LenghtOfIsEmptyFunct.make(FunctionNames.get(block.getType()), params, extractBlockProperties(block), extractComment(block));

            case "lists_indexOf":
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.VALUE, String.class));
                exprParams.add(new ExprParam(BlocklyConstants.FIND, String.class));
                op = getOperation(block, BlocklyConstants.END);
                params = extractExprParameters(block, exprParams);
                return IndexOfFunct.make(IndexLocation.get(op), params, extractBlockProperties(block), extractComment(block));

            case "text_print":
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.TEXT, String.class));
                params = extractExprParameters(block, exprParams);
                return TextPrintFunct.make(params, extractBlockProperties(block), extractComment(block));

                // LISTEN
            case "lists_create_empty":
                fields = extractFields(block, (short) 1);
                filename = extractField(fields, BlocklyConstants.LIST_TYPE);
                return EmptyList.make(BlocklyType.get(filename), extractBlockProperties(block), extractComment(block));

            case "lists_create_with":
            case "robLists_create_with":
                fields = extractFields(block, (short) 1);
                filename = extractField(fields, BlocklyConstants.LIST_TYPE);
                return ListCreate
                    .make(BlocklyType.get(filename), blockToExprList(block, ArrayList.class), extractBlockProperties(block), extractComment(block));

            case "lists_repeat":
                fields = extractFields(block, (short) 1);
                filename = extractField(fields, BlocklyConstants.LIST_TYPE);
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.ITEM, List.class));
                exprParams.add(new ExprParam(BlocklyConstants.NUM, Integer.class));
                params = extractExprParameters(block, exprParams);
                return ListRepeat.make(BlocklyType.get(filename), params, extractBlockProperties(block), extractComment(block));

            case "lists_getIndex":
                fields = extractFields(block, (short) 2);
                exprParams = new ArrayList<ExprParam>();
                op = extractField(fields, BlocklyConstants.MODE_);
                exprParams.add(new ExprParam(BlocklyConstants.VALUE, String.class));
                if ( block.getMutation().isAt() ) {
                    exprParams.add(new ExprParam(BlocklyConstants.AT, Integer.class));
                }
                params = extractExprParameters(block, exprParams);
                return ListGetIndex.make(
                    ListElementOperations.get(op),
                    IndexLocation.get(extractField(fields, BlocklyConstants.WHERE)),
                    params,
                    extractBlockProperties(block),
                    extractComment(block));

            case "lists_setIndex":
                fields = extractFields(block, (short) 2);
                op = extractField(fields, BlocklyConstants.MODE_);

                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.LIST_, String.class));
                exprParams.add(new ExprParam(BlocklyConstants.TO_, Integer.class));
                if ( block.getMutation().isAt() ) {
                    exprParams.add(new ExprParam(BlocklyConstants.AT, Integer.class));
                }
                params = extractExprParameters(block, exprParams);
                return ListSetIndex.make(
                    ListElementOperations.get(op),
                    IndexLocation.get(extractField(fields, BlocklyConstants.WHERE)),
                    params,
                    extractBlockProperties(block),
                    extractComment(block));

            case "lists_getSublist":
                fields = extractFields(block, (short) 2);
                strParams = new ArrayList<String>();
                strParams.add(extractField(fields, BlocklyConstants.WHERE1));
                strParams.add(extractField(fields, BlocklyConstants.WHERE2));
                exprParams = new ArrayList<ExprParam>();
                exprParams.add(new ExprParam(BlocklyConstants.LIST_, String.class));
                if ( block.getMutation().isAt1() ) {
                    exprParams.add(new ExprParam(BlocklyConstants.AT1, Integer.class));
                }
                if ( block.getMutation().isAt2() ) {
                    exprParams.add(new ExprParam(BlocklyConstants.AT2, Integer.class));
                }
                params = extractExprParameters(block, exprParams);
                return GetSubFunct.make(FunctionNames.GET_SUBLIST, strParams, params, extractBlockProperties(block), extractComment(block));

            case "robColour_picker":
                return blockToConst(block, BlocklyConstants.COLOUR);

                // VARIABLEN
            case "variables_set":
                values = extractValues(block, (short) 1);
                Phrase<V> p = extractValue(values, new ExprParam(BlocklyConstants.VALUE, EmptyExpr.class));
                expr = convertPhraseToExpr(p);
                return AssignStmt.make((Var<V>) extractVar(block), convertPhraseToExpr(expr), properties, comment);

            case "variables_get":
                return extractVar(block);

            case "variables_declare":
                fields = extractFields(block, (short) 2);
                values = extractValues(block, (short) 1);
                BlocklyType typeVar = BlocklyType.get(extractField(fields, BlocklyConstants.TYPE));
                String name = extractField(fields, BlocklyConstants.VAR);
                expr = extractValue(values, new ExprParam(BlocklyConstants.VALUE, Integer.class));
                boolean next = block.getMutation().isNext();
                return VarDeclaration.make(typeVar, name, convertPhraseToExpr(expr), next, properties, comment);

                // KONTROLLE
            case "controls_if":
            case "robControls_if":
            case "robControls_ifElse":
                Mutation mutation = block.getMutation();
                int _else = getElse(mutation);
                int _elseIf = getElseIf(mutation);

                return blocksToIfStmt(block, _else, _elseIf);

            case "robControls_wait_for":
            case "robControls_wait":
                StmtList<V> statement;
                StmtList<V> list = StmtList.make();
                int mutat = block.getMutation() == null ? 0 : block.getMutation().getWait().intValue();
                if ( mutat == 0 ) {
                    values = extractValues(block, (short) (mutat + 1));
                    statements = extractStatements(block, (short) (mutat + 1));
                } else {
                    List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
                    values = new ArrayList<Value>();
                    statements = new ArrayList<Statement>();
                    convertStmtValList(values, statements, valAndStmt);
                }
                for ( int i = 0; i <= mutat; i++ ) {
                    expr = extractValue(values, new ExprParam(BlocklyConstants.WAIT + i, Boolean.class));
                    statement = extractStatement(statements, BlocklyConstants.DO + i);
                    list.addStmt(RepeatStmt.make(Mode.WAIT, convertPhraseToExpr(expr), statement, extractBlockProperties(block), extractComment(block)));
                }
                list.setReadOnly();
                return WaitStmt.make(list, properties, comment);

            case "robControls_wait_time":
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.WAIT, Integer.class));
                return WaitTimeStmt.make(convertPhraseToExpr(expr), extractBlockProperties(block), extractComment(block));

            case "robControls_loopForever":
                expr = BoolConst.make(true, properties, comment);
                return extractRepeatStatement(block, expr, RepeatStmt.Mode.FOREVER.toString());

            case "controls_whileUntil":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, BlocklyConstants.MODE_);
                values = extractValues(block, (short) 1);
                if ( RepeatStmt.Mode.UNTIL == RepeatStmt.Mode.get(mode) ) {
                    expr =
                        Unary.make(Op.NOT, convertPhraseToExpr(extractValue(values, new ExprParam(BlocklyConstants.BOOL, Boolean.class))), properties, comment);
                } else {
                    expr = extractValue(values, new ExprParam(BlocklyConstants.BOOL, Boolean.class));
                }
                return extractRepeatStatement(block, expr, mode);

            case "controls_for":
                var = extractVar(block);
                values = extractValues(block, (short) 3);
                exprList = ExprList.make();

                Phrase<V> from = extractValue(values, new ExprParam(BlocklyConstants.FROM_, Integer.class));
                Phrase<V> to = extractValue(values, new ExprParam(BlocklyConstants.TO_, Integer.class));
                Phrase<V> by = extractValue(values, new ExprParam(BlocklyConstants.BY_, Integer.class));
                VarDeclaration<V> var1 =
                    VarDeclaration.make(BlocklyType.NUMERIC_INT, ((Var<V>) var).getValue(), convertPhraseToExpr(from), false, properties, comment);

                Binary<V> exprCondition = Binary.make(Binary.Op.LTE, convertPhraseToExpr(var), convertPhraseToExpr(to), properties, comment);
                Binary<V> exprBy = Binary.make(Binary.Op.ADD_ASSIGNMENT, convertPhraseToExpr(var), convertPhraseToExpr(by), properties, comment);
                exprList.addExpr(var1);
                exprList.addExpr(exprCondition);
                exprList.addExpr(exprBy);
                exprList.setReadOnly();
                return extractRepeatStatement(block, exprList, BlocklyConstants.FOR);

            case "controls_forEach":
                var = extractVar(block);
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam(BlocklyConstants.LIST_, List.class));

                Binary<V> exprBinary = Binary.make(Binary.Op.IN, convertPhraseToExpr(var), convertPhraseToExpr(expr), properties, comment);
                return extractRepeatStatement(block, exprBinary, BlocklyConstants.FOR_EACH);

            case "controls_flow_statements":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, BlocklyConstants.FLOW);
                return StmtFlowCon.make(Flow.get(mode), properties, comment);

            case "controls_repeat_ext":
                values = extractValues(block, (short) 1);
                exprList = ExprList.make();
                from = NumConst.make("0", properties, comment);
                to = extractValue(values, new ExprParam(BlocklyConstants.TIMES, Integer.class));
                by = NumConst.make("1", properties, comment);
                var1 = VarDeclaration.make(BlocklyType.NUMERIC_INT, "i" + this.variable_counter, convertPhraseToExpr(from), false, properties, comment);
                var = Var.make(BlocklyType.NUMERIC_INT, "i" + this.variable_counter, properties, comment);
                exprCondition = Binary.make(Binary.Op.LT, convertPhraseToExpr(var), convertPhraseToExpr(to), properties, comment);
                Unary<V> increment = Unary.make(Unary.Op.POSTFIX_INCREMENTS, convertPhraseToExpr(var), properties, comment);
                exprList.addExpr(var1);
                exprList.addExpr(exprCondition);
                exprList.addExpr(increment);
                exprList.setReadOnly();

                this.variable_counter++;
                return extractRepeatStatement(block, exprList, BlocklyConstants.TIMES);

            case "robControls_start":
                if ( block.getMutation().isDeclare() == true ) {
                    statements = extractStatements(block, (short) 1);
                    statement = extractStatement(statements, BlocklyConstants.ST);
                    return MainTask.make(statement, properties, comment);
                }
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

            default:
                throw new DbcException("Invalid Block: " + block.getType());
        }
    }
}

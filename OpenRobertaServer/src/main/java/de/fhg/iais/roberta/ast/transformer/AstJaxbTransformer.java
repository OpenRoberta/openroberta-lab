package de.fhg.iais.roberta.ast.transformer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
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
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class AstJaxbTransformer<V> {

    public Block astToBlock(Phrase<V> astSource) {
        String fieldValue;
        Phrase<V> value;
        String blockType;
        Block jaxbDestination;

        switch ( astSource.getKind() ) {
            case BOOL_CONST:
                jaxbDestination = new Block();

                fieldValue = String.valueOf(((BoolConst<V>) astSource).isValue()).toUpperCase();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "BOOL", fieldValue);

                return jaxbDestination;

            case PICK_COLOR_CONST:
                jaxbDestination = new Block();

                fieldValue = ((ColorConst<V>) astSource).getValue().getHex();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "COLOUR", fieldValue);

                return jaxbDestination;

            case MATH_CONST:
                jaxbDestination = new Block();

                fieldValue = ((MathConst<V>) astSource).getMathConst().name();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "CONSTANT", fieldValue);

                return jaxbDestination;

            case NULL_CONST:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case STRING_CONST:
                jaxbDestination = new Block();

                fieldValue = ((StringConst<V>) astSource).getValue();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "TEXT", fieldValue);

                return jaxbDestination;

            case NUM_CONST:
                jaxbDestination = new Block();

                fieldValue = ((NumConst<V>) astSource).getValue();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "NUM", fieldValue);

                return jaxbDestination;

            case VAR:
                jaxbDestination = new Block();

                fieldValue = ((Var<V>) astSource).getValue();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "VAR", fieldValue);

                return jaxbDestination;

            case UNARY:
                jaxbDestination = new Block();

                value = ((Unary<V>) astSource).getExpr();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                if ( blockType.equals("math_single") ) {
                    fieldValue = ((Unary<V>) astSource).getOp().name();
                    addField(jaxbDestination, "OP", fieldValue);
                    addValue(jaxbDestination, "NUM", value);
                } else {
                    addValue(jaxbDestination, "BOOL", value);
                }

                return jaxbDestination;

            case BINARY:
                jaxbDestination = new Block();

                Binary<V> binary = (Binary<V>) astSource;

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                switch ( binary.getOp() ) {
                    case MATH_CHANGE:
                        addField(jaxbDestination, "VAR", ((Var<V>) binary.getLeft()).getValue());
                        addValue(jaxbDestination, "DELTA", binary.getRight());
                        return jaxbDestination;
                    case TEXT_APPEND:
                        addField(jaxbDestination, "VAR", ((Var<V>) binary.getLeft()).getValue());
                        addValue(jaxbDestination, "TEXT", binary.getRight());
                        return jaxbDestination;

                    default:
                        addField(jaxbDestination, "OP", binary.getOp().name());
                        addValue(jaxbDestination, "A", binary.getLeft());
                        addValue(jaxbDestination, "B", binary.getRight());
                        return jaxbDestination;

                }

            case SENSOR_EXPR:
                return astToBlock(((SensorExpr<V>) astSource).getSens());

            case EMPTY_LIST:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case CLEAR_DISPLAY_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case DRIVE_ACTION:
                jaxbDestination = new Block();

                DriveAction<V> driveAction = (DriveAction<V>) astSource;

                fieldValue = driveAction.getDirection().name();
                value = driveAction.getParam().getSpeed();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "DIRECTION", fieldValue);
                addValue(jaxbDestination, "POWER", value);

                if ( driveAction.getParam().getDuration() != null ) {
                    addValue(jaxbDestination, driveAction.getParam().getDuration().getType().name(), driveAction.getParam().getDuration().getValue());
                }

                return jaxbDestination;

            case LIGHT_ACTION:
                jaxbDestination = new Block();

                LightAction<V> lightAction = (LightAction<V>) astSource;

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "SWITCH_COLOR", lightAction.getColor().name());
                addField(jaxbDestination, "SWITCH_BLINK", lightAction.getBlinkMode().name());

                return jaxbDestination;

            case LIGHT_STATUS_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case STOP_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                return jaxbDestination;

            case MOTOR_GET_POWER_ACTION:
                jaxbDestination = new Block();

                fieldValue = ((MotorGetPowerAction<V>) astSource).getPort().name();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "MOTORPORT", fieldValue);

                return jaxbDestination;

            case MOTOR_ON_ACTION:
                jaxbDestination = new Block();

                MotorOnAction<V> motorOnAction = (MotorOnAction<V>) astSource;
                fieldValue = motorOnAction.getPort().name();
                value = motorOnAction.getParam().getSpeed();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "MOTORPORT", fieldValue);
                addValue(jaxbDestination, "POWER", value);

                if ( motorOnAction.getParam().getDuration() != null ) {
                    addField(jaxbDestination, "MOTORROTATION", motorOnAction.getDurationMode().name());
                    addValue(jaxbDestination, "VALUE", motorOnAction.getDurationValue());
                }

                return jaxbDestination;

            case MOTOR_SET_POWER_ACTION:
                jaxbDestination = new Block();

                MotorSetPowerAction<V> motorSetPowerAction = (MotorSetPowerAction<V>) astSource;
                fieldValue = motorSetPowerAction.getPort().name();
                value = motorSetPowerAction.getPower();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "MOTORPORT", fieldValue);
                addValue(jaxbDestination, "POWER", value);

                return jaxbDestination;

            case MOTOR_STOP_ACTION:
                jaxbDestination = new Block();

                MotorStopAction<V> motorStopAction = (MotorStopAction<V>) astSource;
                fieldValue = motorStopAction.getPort().name();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "MOTORPORT", fieldValue);
                addField(jaxbDestination, "MODE", motorStopAction.getMode().name());

                return jaxbDestination;

            case PLAY_FILE_ACTION:
                jaxbDestination = new Block();

                fieldValue = ((PlayFileAction<V>) astSource).getFileName();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "FILE", fieldValue);

                return jaxbDestination;

            case SHOW_PICTURE_ACTION:
                jaxbDestination = new Block();

                fieldValue = ((ShowPictureAction<V>) astSource).getPicture().name();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "PICTURE", fieldValue);
                addValue(jaxbDestination, "X", ((ShowPictureAction<V>) astSource).getX());
                addValue(jaxbDestination, "Y", ((ShowPictureAction<V>) astSource).getY());

                return jaxbDestination;

            case SHOW_TEXT_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addValue(jaxbDestination, "OUT", ((ShowTextAction<V>) astSource).getMsg());
                addValue(jaxbDestination, "COL", ((ShowTextAction<V>) astSource).getX());
                addValue(jaxbDestination, "ROW", ((ShowTextAction<V>) astSource).getY());

                return jaxbDestination;

            case TONE_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addValue(jaxbDestination, "FREQUENCE", ((ToneAction<V>) astSource).getFrequency());
                addValue(jaxbDestination, "DURATION", ((ToneAction<V>) astSource).getDuration());

                return jaxbDestination;

            case TURN_ACTION:
                jaxbDestination = new Block();

                TurnAction<V> turnAction = (TurnAction<V>) astSource;
                fieldValue = turnAction.getDirection().name();
                value = turnAction.getParam().getSpeed();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "DIRECTION", fieldValue);
                addValue(jaxbDestination, "POWER", value);

                if ( turnAction.getParam().getDuration() != null ) {
                    addValue(jaxbDestination, turnAction.getParam().getDuration().getType().name(), turnAction.getParam().getDuration().getValue());
                }

                return jaxbDestination;

            case VOLUME_ACTION:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                if ( ((VolumeAction<V>) astSource).getMode() == VolumeAction.Mode.SET ) {
                    addValue(jaxbDestination, "VOLUME", ((VolumeAction<V>) astSource).getVolume());
                }

                return jaxbDestination;

            case TOUCH_SENSING:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = ((TouchSensor<V>) astSource).getPort().getPortNumber();

                addField(jaxbDestination, "SENSORPORT", fieldValue);

                return jaxbDestination;

            case ULTRASONIC_SENSING:
                jaxbDestination = new Block();

                UltrasonicSensor<V> ultrasonicSensor = (UltrasonicSensor<V>) astSource;

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = ultrasonicSensor.getPort().getPortNumber();

                addField(jaxbDestination, "SENSORPORT", fieldValue);
                if ( ultrasonicSensor.getMode() != UltrasonicSensorMode.GET_MODE && ultrasonicSensor.getMode() != UltrasonicSensorMode.GET_SAMPLE ) {
                    addField(jaxbDestination, "MODE", ultrasonicSensor.getMode().name());
                }

                return jaxbDestination;

            case COLOR_SENSING:
                jaxbDestination = new Block();

                ColorSensor<V> colorSensor = (ColorSensor<V>) astSource;

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = colorSensor.getPort().getPortNumber();

                addField(jaxbDestination, "SENSORPORT", fieldValue);
                if ( colorSensor.getMode() != ColorSensorMode.GET_MODE && colorSensor.getMode() != ColorSensorMode.GET_SAMPLE ) {
                    addField(jaxbDestination, "MODE", colorSensor.getMode().name());
                }

                return jaxbDestination;

            case INFRARED_SENSING:
                jaxbDestination = new Block();

                InfraredSensor<V> infraredSensor = (InfraredSensor<V>) astSource;

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = infraredSensor.getPort().getPortNumber();

                addField(jaxbDestination, "SENSORPORT", fieldValue);
                if ( infraredSensor.getMode() != InfraredSensorMode.GET_MODE && infraredSensor.getMode() != InfraredSensorMode.GET_SAMPLE ) {
                    addField(jaxbDestination, "MODE", infraredSensor.getMode().name());
                }

                return jaxbDestination;

            case ENCODER_SENSING:
                jaxbDestination = new Block();

                EncoderSensor<V> encoderSensor = (EncoderSensor<V>) astSource;

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = encoderSensor.getMotor().name();

                addField(jaxbDestination, "MOTORPORT", fieldValue);
                if ( encoderSensor.getMode() == MotorTachoMode.DEGREE || encoderSensor.getMode() == MotorTachoMode.ROTATION ) {
                    addField(jaxbDestination, "MODE", encoderSensor.getMode().name());
                }

                return jaxbDestination;

            case BRICK_SENSIG:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = ((BrickSensor<V>) astSource).getKey().name();

                addField(jaxbDestination, "KEY", fieldValue);

                return jaxbDestination;

            case GYRO_SENSIG:
                jaxbDestination = new Block();

                GyroSensor<V> gyroSensor = (GyroSensor<V>) astSource;

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = gyroSensor.getPort().getPortNumber();

                addField(jaxbDestination, "SENSORPORT", fieldValue);
                if ( gyroSensor.getMode() == GyroSensorMode.ANGLE || gyroSensor.getMode() == GyroSensorMode.RATE ) {
                    addField(jaxbDestination, "MODE", gyroSensor.getMode().name());
                }

                return jaxbDestination;

            case TIMER_SENSING:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                fieldValue = String.valueOf(((TimerSensor<V>) astSource).getTimer());

                addField(jaxbDestination, "SENSORNUM", fieldValue);

                return jaxbDestination;

            case SENSOR_GET_SAMPLE:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                GetSampleSensor<V> getSampleSensor = (GetSampleSensor<V>) astSource;

                addField(jaxbDestination, "SENSORTYPE", getSampleSensor.getSensorType().name());
                addField(jaxbDestination, getSampleSensor.getSensorType().getPortTypeName(), getSampleSensor.getSensorPort());

                return jaxbDestination;

            case EXPR_STMT:
                ExprStmt<V> exprStmt = (ExprStmt<V>) astSource;

                return astToBlock(exprStmt.getExpr());

            case AKTION_STMT:
                ActionStmt<V> actionStmt = (ActionStmt<V>) astSource;

                return astToBlock(actionStmt.getAction());

            case SENSOR_STMT:
                SensorStmt<V> sensorStmt = (SensorStmt<V>) astSource;

                return astToBlock(sensorStmt.getSensor());

            case IF_STMT:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                IfStmt<V> ifStmt = (IfStmt<V>) astSource;
                int _else = ifStmt.get_else();
                int _elseIf = ifStmt.get_elseIf();

                StmtList<V> elseList = ifStmt.getElseList();
                int expr = 0;

                boolean repetition = false;

                expr = ifStmt.getExpr().size();

                if ( _else != 0 || _elseIf != 0 ) {
                    Mutation mutation = new Mutation();
                    if ( _else != 0 ) {
                        mutation.setElse(BigInteger.ONE);
                    }
                    if ( _elseIf > 0 ) {
                        mutation.setElseif(BigInteger.valueOf(_elseIf));
                    }
                    jaxbDestination.setMutation(mutation);
                    repetition = true;
                }
                if ( repetition ) {
                    Repetitions repetitions = new Repetitions();
                    for ( int i = 0; i < expr; i++ ) {
                        addValue(repetitions, "IF" + i, ifStmt.getExpr().get(i));
                        addStatement(repetitions, "DO" + i, ifStmt.getThenList().get(i));
                    }
                    if ( elseList.get().size() != 0 ) {
                        addStatement(repetitions, "ELSE", ifStmt.getElseList());
                    }
                    jaxbDestination.setRepetitions(repetitions);
                } else {
                    for ( int i = 0; i < expr; i++ ) {
                        addValue(jaxbDestination, "IF" + i, ifStmt.getExpr().get(i));
                        addStatement(jaxbDestination, "DO" + i, ifStmt.getThenList().get(i));
                    }
                    if ( elseList.get().size() != 0 ) {
                        addStatement(jaxbDestination, "ELSE", ifStmt.getElseList());
                    }
                }

                return jaxbDestination;

            case REPEAT_STMT:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                RepeatStmt<V> repeatStmt = (RepeatStmt<V>) astSource;

                switch ( repeatStmt.getMode() ) {
                    case TIMES:
                        addValue(jaxbDestination, "TIMES", ((Binary<V>) ((ExprList<V>) repeatStmt.getExpr()).get().get(1)).getRight());
                        break;

                    case WAIT:
                    case UNTIL:
                        addField(jaxbDestination, "MODE", repeatStmt.getMode().name());
                        addValue(jaxbDestination, "BOOL", ((Unary<V>) repeatStmt.getExpr()).getExpr());
                        break;

                    case WHILE:
                        addField(jaxbDestination, "MODE", repeatStmt.getMode().name());
                        addValue(jaxbDestination, "BOOL", (repeatStmt.getExpr()));
                        break;

                    case FOR:
                        ExprList<V> exprList = (ExprList<V>) repeatStmt.getExpr();
                        addField(jaxbDestination, "VAR", ((Var<V>) ((Binary<V>) exprList.get().get(0)).getLeft()).getValue());
                        addValue(jaxbDestination, "FROM", ((Binary<V>) exprList.get().get(0)).getRight());
                        addValue(jaxbDestination, "TO", ((Binary<V>) exprList.get().get(1)).getRight());
                        addValue(jaxbDestination, "BY", ((Binary<V>) exprList.get().get(2)).getRight());
                        break;

                    case FOR_EACH:
                        Binary<V> exprBinary = (Binary<V>) repeatStmt.getExpr();
                        addField(jaxbDestination, "VAR", ((Var<V>) exprBinary.getLeft()).getValue());
                        addValue(jaxbDestination, "LIST", exprBinary.getRight());
                        break;
                    default:
                        break;
                }
                addStatement(jaxbDestination, "DO", repeatStmt.getList());

                return jaxbDestination;

            case STMT_FLOW_CONTROL:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                addField(jaxbDestination, "FLOW", ((StmtFlowCon<V>) astSource).getFlow().name());

                return jaxbDestination;

            case ASSIGN_STMT:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                AssignStmt<V> assignStmt = (AssignStmt<V>) astSource;

                addField(jaxbDestination, "VAR", assignStmt.getName().getValue());
                addValue(jaxbDestination, "VALUE", assignStmt.getExpr());

                return jaxbDestination;

            case WAIT_STMT:
                jaxbDestination = new Block();

                blockType = astSource.getProperty().getBlockType();
                setProperties(astSource, jaxbDestination, blockType);
                addComment(astSource, jaxbDestination);

                WaitStmt<V> waitStmt = (WaitStmt<V>) astSource;
                int numOfWait = waitStmt.getStatements().get().size();
                if ( numOfWait > 1 ) {
                    Mutation mutation = new Mutation();
                    mutation.setWait(BigInteger.valueOf(numOfWait - 1));
                    jaxbDestination.setMutation(mutation);
                }

                StmtList<V> waitStmtList = waitStmt.getStatements();
                for ( int i = 0; i < numOfWait; i++ ) {
                    addValue(jaxbDestination, "WAIT" + i, ((RepeatStmt<V>) waitStmtList.get().get(i)).getExpr());
                    addStatement(jaxbDestination, "DO" + i, ((RepeatStmt<V>) waitStmtList.get().get(i)).getList());

                }
                return jaxbDestination;

            case EMPTY_EXPR:
            case LOCATION:
                return null;
            default:
                throw new DbcException("Invalid AST object!");
        }

    }

    private List<Block> extractStmtList(Phrase<V> phrase) {
        List<Block> result = new ArrayList<Block>();
        Assert.isTrue(phrase.getKind() == Kind.STMT_LIST, "Phrase is not StmtList!");
        StmtList<V> stmtList = (StmtList<V>) phrase;
        for ( Stmt<V> stmt : stmtList.get() ) {
            result.add(astToBlock(stmt));
        }
        return result;
    }

    private void addStatement(Block block, String name, Phrase<V> value) {
        Assert.isTrue(value.getKind() == Phrase.Kind.STMT_LIST, "Phrase is not STMT_LIST");
        if ( ((StmtList<V>) value).get().size() != 0 ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractStmtList(value));
            block.getStatement().add(statement);
        }
    }

    private void addStatement(Repetitions repetitions, String name, Phrase<V> value) {
        Assert.isTrue(value.getKind() == Phrase.Kind.STMT_LIST, "Phrase is not STMT_LIST");
        if ( ((StmtList<V>) value).get().size() != 0 ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractStmtList(value));
            repetitions.getValueAndStatement().add(statement);
        }
    }

    private void addValue(Block block, String name, Phrase<V> value) {
        if ( value.getKind() != Kind.EMPTY_EXPR ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            blockValue.setBlock(astToBlock(value));
            block.getValue().add(blockValue);
        }
    }

    private void addValue(Repetitions repetitions, String name, Phrase<V> value) {
        if ( value.getKind() != Kind.EMPTY_EXPR ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            blockValue.setBlock(astToBlock(value));
            repetitions.getValueAndStatement().add(blockValue);
        }
    }

    private void addField(Block block, String name, String value) {
        Field field = new Field();
        field.setName(name);
        field.setValue(value);
        block.getField().add(field);
    }

    private void setProperties(Phrase<V> astObject, Block block, String type) {
        block.setType(type);
        block.setId(astObject.getProperty().getBlocklyId());
        setDisabled(astObject, block);
        setCollapsed(astObject, block);
        setInline(astObject, block);
    }

    private void setInline(Phrase<V> astObject, Block block) {
        if ( astObject.getProperty().isInline() != null ) {
            block.setInline(astObject.getProperty().isInline());
        }
    }

    private void setCollapsed(Phrase<V> astObject, Block block) {
        if ( astObject.getProperty().isCollapsed() ) {
            block.setCollapsed(astObject.getProperty().isCollapsed());
        }
    }

    private void setDisabled(Phrase<V> astObject, Block block) {
        if ( astObject.getProperty().isDisabled() ) {
            block.setDisabled(astObject.getProperty().isDisabled());
        }
    }

    private void addComment(Phrase<V> astObject, Block block) {
        if ( astObject.getComment() != null ) {
            Comment comment = new Comment();
            comment.setValue(astObject.getComment().getComment());
            comment.setPinned(astObject.getComment().isPinned());
            comment.setH(astObject.getComment().getHeight());
            comment.setW(astObject.getComment().getWidth());
            block.setComment(comment);
        }
    }
}
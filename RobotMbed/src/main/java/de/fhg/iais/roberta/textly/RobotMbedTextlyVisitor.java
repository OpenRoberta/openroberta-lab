package de.fhg.iais.roberta.textly;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public class RobotMbedTextlyVisitor<T> extends CommonTextlyVisitor<T> {

    @Override
    public T visitMicrobitv2SensorExpr(ExprlyParser.Microbitv2SensorExprContext ctx) {
        String sensor = ctx.start.getText();
        ExprList parameters = new ExprList();

        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        Mutation mutation = new Mutation();

        switch ( sensor ) {
            case "accelerometerSensor":


                hide.setName("SENSORPORT");
                hide.setValue("_A");
                listHide.add(hide);
                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanAcce = new ExternalSensorBean("_A", "VALUE", ctx.NAME().getText().toUpperCase(), mutation, listHide);
                SensorExpr sensorExprAccelerometer = new SensorExpr(new AccelerometerSensor(mkInlineProperty(ctx, "robSensors_accelerometer_getSample"), externalSensorBeanAcce));
                return (T) checkValidationName(sensorExprAccelerometer, ctx.NAME().getText(), NameType.ACCELEROMETERPORT);

            case "logoTouchSensor":

                hide.setName("SENSORPORT");
                hide.setValue("_LO");
                listHide.add(hide);
                mutation.setMode("PRESSED");


                ExternalSensorBean externalSensorBeanLogo = new ExternalSensorBean("_LO", "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new LogoTouchSensor(mkInlineProperty(ctx, "robsensors_logotouch_getsample"), externalSensorBeanLogo));

            case "compassSensor":

                hide.setName("SENSORPORT");
                hide.setValue("_C");
                listHide.add(hide);
                mutation.setMode("ANGLE");


                ExternalSensorBean externalSensorBeanCompass = new ExternalSensorBean("_C", "ANGLE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new CompassSensor(mkInlineProperty(ctx, "robSensors_compass_getSample"), externalSensorBeanCompass));

            case "gestureSensor":
                String gesture = ctx.NAME().getText();
                String specialGesture = gesture.equalsIgnoreCase("facedown") ? "FACE_DOWN" :
                    gesture.equalsIgnoreCase("faceup") ? "FACE_UP" : null;
                String gestureToUse = specialGesture == null ? gesture.toUpperCase() : specialGesture;

                mutation.setMode(gestureToUse);

                ExternalSensorBean externalSensorBeanGesture = new ExternalSensorBean("- EMPTY_PORT -", gestureToUse, "- EMPTY_SLOT -", mutation, listHide);
                SensorExpr sensorExprGesture = new SensorExpr(new GestureSensor(mkInlineProperty(ctx, "robSensors_gesture_getSample"), externalSensorBeanGesture));
                return (T) checkValidationName(sensorExprGesture, specialGesture != null ? gesture : gestureToUse, NameType.GESTURESENSOROP);

            case "keysSensor":
                mutation.setMode("PRESSED");
                //validateName(ctx.NAME().getText(), NameType.BUTTON);
                ExternalSensorBean externalSensorBeanKey = new ExternalSensorBean(ctx.NAME().getText(), "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                SensorExpr sensorExprKey = new SensorExpr(new KeysSensor(mkInlineProperty(ctx, "robSensors_key_getSample"), externalSensorBeanKey));
                return (T) checkValidationName(sensorExprKey, ctx.NAME().getText(), NameType.BUTTON);

            case "lightSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_L");
                listHide.add(hide);
                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanLight = new ExternalSensorBean("_L", "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new LightSensor(mkInlineProperty(ctx, "robSensors_light_getSample"), externalSensorBeanLight));

            case "pinGetValueSensor":

                mutation.setMode(ctx.op.getText().toUpperCase());
                validateName(ctx.NAME().getText(), NameType.PORT);
                ExternalSensorBean externalSensorBeanPin = new ExternalSensorBean(ctx.NAME().getText(), ctx.op.getText().toUpperCase(), "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new PinGetValueSensor(mkInlineProperty(ctx, "robSensors_pin_getSample"), externalSensorBeanPin));

            case "pinTouchSensor":

                mutation.setMode("PRESSED");
                ExternalSensorBean externalSensorBeanPinT = new ExternalSensorBean(ctx.INT().getText(), "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new PinTouchSensor(mkInlineProperty(ctx, "robSensors_pintouch_getSample"), externalSensorBeanPinT));

            case "soundSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_S");
                listHide.add(hide);
                mutation.setMode("SOUND");
                ExternalSensorBean externalSensorBeanSound = new ExternalSensorBean("_S", "SOUND", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new SoundSensor(mkInlineProperty(ctx, "robSensors_sound_getSample"), externalSensorBeanSound));

            case "temperatureSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_T");
                listHide.add(hide);
                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanTemperature = new ExternalSensorBean("_T", "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new TemperatureSensor(mkInlineProperty(ctx, "robSensors_temperature_getSample"), externalSensorBeanTemperature));

            case "timerSensor":

                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanPinTimer = new ExternalSensorBean("1", "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new TimerSensor(mkInlineProperty(ctx, "robSensors_timer_getSample"), externalSensorBeanPinTimer));

            default:
                Expr result = new EmptyExpr(BlocklyType.NOTHING);
                result.addTcError("invalid function name " + sensor, false);
                return (T) result;
        }
    }

    @Override
    public T visitMicrobitv2SensorStmt(ExprlyParser.Microbitv2SensorStmtContext ctx) {
        String sensor = ctx.start.getText();

        switch ( sensor ) {
            case "pinSetTouchMode":
                PinSetTouchMode p = new PinSetTouchMode(mkInlineProperty(ctx, "robSensors_set_pin_mode"), ctx.op.getText().toUpperCase(), ctx.INT().getText());
                SensorStmt stmt = new SensorStmt(p);
                return (T) stmt;
            case "timerReset":
                TimerReset reset = new TimerReset(mkInlineProperty(ctx, "mbedSensors_timer_reset"), "1");
                SensorStmt stmt2 = new SensorStmt(reset);
                return (T) stmt2;
            default:
                StmtList statementList = new StmtList();
                statementList.setReadOnly();
                statementList.addTcError("invalid sensor" + sensor, false);
                return (T) statementList;
        }
    }

    @Override
    public T visitMicrobitv2ActuatorStmt(ExprlyParser.Microbitv2ActuatorStmtContext ctx) {
        String actuator = ctx.start.getText();
        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        switch ( actuator ) {
            case "showText":

                Expr msgT = (Expr) visit(ctx.expr(0));
                msgT.setReadOnly();
                DisplayTextAction displayTextActionText = new DisplayTextAction(mkInlineProperty(ctx, "mbedActions_display_text"), "TEXT", msgT);
                ActionStmt actionStmtText = new ActionStmt(displayTextActionText);
                return (T) actionStmtText;
            case "showCharacter":
                Expr msgC = (Expr) visit(ctx.expr(0));
                msgC.setReadOnly();
                DisplayTextAction displayTextActionChar = new DisplayTextAction(mkInlineProperty(ctx, "mbedActions_display_text"), "CHARACTER", msgC);
                ActionStmt actionStmtChar = new ActionStmt(displayTextActionChar);
                return (T) actionStmtChar;
            case "pitch":
                Expr freq = (Expr) visit(ctx.expr(0));
                freq.setReadOnly();
                Expr duration = (Expr) visit(ctx.expr(1));
                duration.setReadOnly();
                hide.setName("ACTORPORT");
                hide.setValue("_B");
                ToneAction toneAction = new ToneAction(mkInlineProperty(ctx, "mbedActions_play_tone"), freq, duration, "_B", hide);
                ActionStmt actionStmtTone = new ActionStmt(toneAction);
                return (T) actionStmtTone;

            case "playFile":
                String fileName = ctx.NAME().getText().toUpperCase();
                hide.setName("ACTORPORT");
                hide.setValue("_B");
                PlayFileAction playFileAction = new PlayFileAction(mkInlineProperty(ctx, "actions_play_file"), "- EMPTY_PORT -", fileName, hide);
                ActionStmt actionStmtPlayFile = new ActionStmt(playFileAction);
                return (T) checkValidationName(actionStmtPlayFile, ctx.NAME().getText(), NameType.PLAYFILE);
        }
        return null;
    }

    @Override
    public T visitRobotMicrobitv2SensorStatement(ExprlyParser.RobotMicrobitv2SensorStatementContext ctx) {
        return visitMicrobitv2SensorStmt(ctx.microbitv2SensorStmt());
    }

    @Override
    public T visitRobotMicrobitv2ActuatorStatement(ExprlyParser.RobotMicrobitv2ActuatorStatementContext ctx) {
        return visitMicrobitv2ActuatorStmt(ctx.microbitv2ActuatorStmt());
    }

    @Override
    public T visitRobotMicrobitv2Expression(ExprlyParser.RobotMicrobitv2ExpressionContext ctx) {
        return visitMicrobitv2SensorExpr(ctx.microbitv2SensorExpr());
    }


    // TODO: catch expection for other robots
    @Override
    public T visitRobotWeDoExpression(ExprlyParser.RobotWeDoExpressionContext ctx) throws UnsupportedOperationException {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTcError("this expression is only for Wedo Robot " + ctx.getText(), false);
        return (T) result;
    }

    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }
}

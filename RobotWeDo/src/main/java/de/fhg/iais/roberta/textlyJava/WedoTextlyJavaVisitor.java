package de.fhg.iais.roberta.textlyJava;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyJavaVisitor;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;

public class WedoTextlyJavaVisitor<T> extends CommonTextlyJavaVisitor<T> {
    private static final Map<String, String> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put("#pink", "#ff1493");
        COLOR_MAP.put("#purple", "#800080");
        COLOR_MAP.put("#blue", "#4876ff");
        COLOR_MAP.put("#cyan", "#00ffff");
        COLOR_MAP.put("#lightgreen", "#90ee90");
        COLOR_MAP.put("#green", "#008000");
        COLOR_MAP.put("#yellow", "#ffff00");
        COLOR_MAP.put("#orange", "#ffa500");
        COLOR_MAP.put("#red", "#ff0000");
        COLOR_MAP.put("#white", "#fffffe");
    }
    @Override
    public T visitWeDoSensorExpr(TextlyJavaParser.WeDoSensorExprContext ctx) {
        String sensor = ctx.start.getText();
        ExprList parameters = new ExprList();
        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        Mutation mutation = new Mutation();

        switch ( sensor ) {
            case "gyroSensor":
                String gyroSlot = GyroSensorSlot.getAstName(ctx.NAME(1).getText());
                if ( gyroSlot != null ) {
                    mutation.setMode("TILTED");
                    ExternalSensorBean externalSensorBeanGyro = new ExternalSensorBean(ctx.NAME(0).getText(), "TILTED", gyroSlot, mutation, listHide);
                    SensorExpr sensorExprGyro = new SensorExpr(new GyroSensor(mkInlineProperty(ctx, "robSensors_gyro_getSample"), externalSensorBeanGyro));
                    return (T) checkValidationName(sensorExprGyro, ctx.NAME(0).getText(), NameType.PORT); // Only check the port name
                } else {
                    Expr result = new EmptyExpr(BlocklyType.NOTHING);
                    result.addTextlyError("Invalid gyro sensor slot: " + ctx.NAME(1).getText(), true);
                    return (T) result;
                }

            case "infraredSensor":
                mutation.setMode("DISTANCE");
                validatePattern(ctx.NAME(0).getText(), NameType.PORT);
                ExternalSensorBean externalSensorBeanInfra = new ExternalSensorBean(ctx.NAME(0).getText(), "DISTANCE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new InfraredSensor(mkInlineProperty(ctx, "robSensors_infrared_getSample"), externalSensorBeanInfra));

            case "keysSensor":
                mutation.setMode("PRESSED");
                validatePattern(ctx.NAME(0).getText(), NameType.PORT);
                ExternalSensorBean externalSensorBeanKey = new ExternalSensorBean(ctx.NAME(0).getText(), "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new KeysSensor(mkInlineProperty(ctx, "robSensors_key_getSample"), externalSensorBeanKey));

            case "timerSensor":
                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanPinTimer = new ExternalSensorBean("1", "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new TimerSensor(mkInlineProperty(ctx, "robSensors_timer_getSample"), externalSensorBeanPinTimer));
        }
        return null;
    }

    @Override
    public T visitCol(TextlyJavaParser.ColContext ctx) {
        String colorText = ctx.COLOR().getText().toLowerCase();
        if ( colorText.startsWith("#rgb(") && colorText.endsWith(")") ) {
            colorText = colorText.substring(5, colorText.length() - 1);
            colorText = "#" + colorText;
        }

        String colorHex = COLOR_MAP.getOrDefault(colorText, colorText);
        if ( !COLOR_MAP.containsValue(colorHex) ) {
            Expr result = new EmptyExpr(BlocklyType.NUMBER);
            result.addTextlyError("This Colour is not supported for WeDo", true);
            return (T) result;
        }

        return (T) new ColorConst(mkInlineProperty(ctx, "robColour_picker"), colorHex);
    }

    @Override
    public T visitWedoSensorStmt(TextlyJavaParser.WedoSensorStmtContext ctx) {
        String sensor = ctx.start.getText();

        switch ( sensor ) {

            case "timerReset":
                TimerReset reset = new TimerReset(mkInlineProperty(ctx, "robSensors_timer_reset"), "1");
                SensorStmt stmt2 = new SensorStmt(reset);
                return (T) stmt2;
            default:
                StmtList statementList = new StmtList();
                statementList.setReadOnly();
                statementList.addTextlyError("invalid sensor" + sensor, false);
                return (T) statementList;

        }
    }

    @Override
    public T visitWedoActuatorStmt(TextlyJavaParser.WedoActuatorStmtContext ctx) {
        String actuator = ctx.start.getText();
        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();

        switch ( actuator ) {
            case "showText":
                Expr msg = (Expr) visit(ctx.expr(0));
                msg.setReadOnly();
                Expr x = new EmptyExpr(BlocklyType.NUMBER_INT);
                Expr y = new EmptyExpr(BlocklyType.NUMBER_INT);
                ShowTextAction showTextAction = new ShowTextAction(mkInlineProperty(ctx, "robActions_display_text"), msg, x, y, "- EMPTY_PORT -", null);
                ActionStmt actionStmtShowText = new ActionStmt(showTextAction);
                return (T) actionStmtShowText;

            case "motor.move":
                String port = ctx.NAME().getText();
                Expr speed = (Expr) visit(ctx.expr(0));
                MotorDuration motorDuration;
                if ( ctx.expr(1) != null ) {
                    Expr time = (Expr) visit(ctx.expr(1));
                    motorDuration = new MotorDuration(null, time);
                } else {
                    motorDuration = null;
                }

                MotionParam motionParam = new MotionParam.Builder().speed(speed).duration(motorDuration).build();
                MotorOnAction motorOnAction = new MotorOnAction(port, motionParam, mkInlineProperty(ctx, "robActions_motor_on_for"));
                ActionStmt actionStmtMotorOnAction = new ActionStmt(motorOnAction);
                return (T) checkValidationName(actionStmtMotorOnAction, port, NameType.PORT);

            case "motor.stop":
                String portS = ctx.NAME().getText();
                MotorStopAction motorStopAction = new MotorStopAction(portS, null, mkInlineProperty(ctx, "robActions_motor_stop"));
                ActionStmt actionStmtMotorStopAction = new ActionStmt(motorStopAction);
                return (T) checkValidationName(actionStmtMotorStopAction, portS, NameType.PORT);

            case "clearDisplay":
                ClearDisplayAction clearDisplayAction = new ClearDisplayAction(mkExternalProperty(ctx, "robActions_display_clear"), "- EMPTY_PORT -", hide);
                ActionStmt actionStmtClearDisplay = new ActionStmt(clearDisplayAction);
                return (T) actionStmtClearDisplay;

            case "pitch":
                String portPitch = ctx.NAME().getText();
                Expr freq = (Expr) visit(ctx.expr(0));
                freq.setReadOnly();
                Expr duration = (Expr) visit(ctx.expr(1));
                duration.setReadOnly();
                ToneAction toneAction = new ToneAction(mkInlineProperty(ctx, "robActions_play_tone"), freq, duration, portPitch, hide);
                ActionStmt actionStmtTone = new ActionStmt(toneAction);
                return (T) checkValidationName(actionStmtTone, portPitch, NameType.PORT);

            case "turnRgbOn":
                String portRgb = ctx.NAME().getText();
                Expr color = (Expr) visit(ctx.expr(0));
                color.setReadOnly();
                RgbLedOnAction rgbLedOnAction = new RgbLedOnAction(mkExternalProperty(ctx, "actions_rgbLed_on"), portRgb, color);
                ActionStmt actionStmtRgbLedOn = new ActionStmt(rgbLedOnAction);
                return (T) checkValidationName(actionStmtRgbLedOn, portRgb, NameType.PORT);

            case "turnRgbOff":
                String portRgbOff = ctx.NAME().getText();
                RgbLedOffAction rgbLedOffAction = new RgbLedOffAction(mkExternalProperty(ctx, "actions_rgbLed_off"), portRgbOff);
                ActionStmt actionStmtRgbLedOff = new ActionStmt(rgbLedOffAction);
                return (T) checkValidationName(actionStmtRgbLedOff, portRgbOff, NameType.PORT);
        }
        return null;
    }


    @Override
    public T visitRobotWeDoExpression(TextlyJavaParser.RobotWeDoExpressionContext ctx) {
        return visitWeDoSensorExpr(ctx.weDoSensorExpr());
    }

    @Override
    public T visitRobotWeDoSensorStatement(TextlyJavaParser.RobotWeDoSensorStatementContext ctx) {
        return visitWedoSensorStmt(ctx.wedoSensorStmt());
    }

    @Override
    public T visitRobotWedoActuatorStatement(TextlyJavaParser.RobotWedoActuatorStatementContext ctx) {
        return visitWedoActuatorStmt(ctx.wedoActuatorStmt());
    }


    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }

    private static BlocklyProperties mkExternalProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", false, ctx);
    }

    private enum GyroSensorSlot {
        UP("up"),
        DOWN("down"),
        BACK("back"),
        FRONT("front"),
        NO("no"),
        ANY("any");

        private final String name;

        GyroSensorSlot(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstName(String textlyName) {
            for ( GyroSensorSlot slot : values() ) {
                if ( slot.name.equalsIgnoreCase(textlyName) ) {
                    return slot.name();
                }
            }
            return null;
        }
    }
}
package de.fhg.iais.roberta.textly;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
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
                ExternalSensorBean externalSensorBeanAcce = new ExternalSensorBean("_A", "VALUE", ctx.op.getText().toUpperCase(), mutation, listHide);
                return (T) new SensorExpr(new AccelerometerSensor(mkInlineProperty(ctx, "robSensors_accelerometer_getSample"), externalSensorBeanAcce));

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
                String op = ctx.op.getText();
                String transformedOp;

                switch ( op ) {
                    case "faceDown":
                        transformedOp = "FACE_DOWN";
                        break;
                    case "faceUp":
                        transformedOp = "FACE_UP";
                        break;
                    default:
                        transformedOp = op.toUpperCase();
                        break;
                }

                mutation.setMode(transformedOp);


                ExternalSensorBean externalSensorBeanGesture = new ExternalSensorBean("- EMPTY_PORT -", transformedOp, "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new GestureSensor(mkInlineProperty(ctx, "robSensors_gesture_getSample"), externalSensorBeanGesture));

            case "keysSensor":
                mutation.setMode("PRESSED");
                validateName(ctx.NAME().getText(), NameType.BUTTON);
                ExternalSensorBean externalSensorBeanKey = new ExternalSensorBean(ctx.NAME().getText(), "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new KeysSensor(mkInlineProperty(ctx, "robSensors_key_getSample"), externalSensorBeanKey));

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
    public T visitRobotMicrobitv2Statement(ExprlyParser.RobotMicrobitv2StatementContext ctx) {
        return visitMicrobitv2SensorStmt(ctx.microbitv2SensorStmt());
    }

    @Override
    public T visitRobotMicrobitv2Expression(ExprlyParser.RobotMicrobitv2ExpressionContext ctx) {
        return visitMicrobitv2SensorExpr(ctx.microbitv2SensorExpr());
    }

    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }
}

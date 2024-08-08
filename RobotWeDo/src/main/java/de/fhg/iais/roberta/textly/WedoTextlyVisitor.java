package de.fhg.iais.roberta.textly;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public class WedoTextlyVisitor<T> extends CommonTextlyVisitor<T> {

    @Override
    public T visitWeDoSensorExpr(ExprlyParser.WeDoSensorExprContext ctx) {
        String sensor = ctx.start.getText();
        ExprList parameters = new ExprList();

        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        Mutation mutation = new Mutation();

        switch ( sensor ) {
            case "gyroSensor":

                mutation.setMode("TILTED");

                ExternalSensorBean externalSensorBeanGyro = new ExternalSensorBean(ctx.NAME(0).getText(), "TILTED", ctx.NAME(1).getText().toUpperCase(), mutation, listHide);

                SensorExpr sensorExprGyro = new SensorExpr(new GyroSensor(mkInlineProperty(ctx, "robSensors_gyro_getSample"), externalSensorBeanGyro));
                checkValidationName(sensorExprGyro, ctx.NAME(0).getText(), NameType.PORT);
                sensorExprGyro = checkValidationName(sensorExprGyro, ctx.NAME(0).getText(), NameType.PORT);
                return (T) checkValidationName(sensorExprGyro, ctx.NAME(1).getText(), NameType.GYROSENSORSLOT);

            case "infraredSensor":

                mutation.setMode("DISTANCE");
                validateName(ctx.NAME(0).getText(), NameType.PORT);
                ExternalSensorBean externalSensorBeanInfra = new ExternalSensorBean(ctx.NAME(0).getText(), "DISTANCE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new InfraredSensor(mkInlineProperty(ctx, "robSensors_infrared_getSample"), externalSensorBeanInfra));

            case "keysSensor":
                mutation.setMode("PRESSED");
                validateName(ctx.NAME(0).getText(), NameType.PORT);
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
    public T visitWedoSensorStmt(ExprlyParser.WedoSensorStmtContext ctx) {
        String sensor = ctx.start.getText();

        switch ( sensor ) {

            case "timerReset":
                TimerReset reset = new TimerReset(mkInlineProperty(ctx, "robSensors_timer_reset"), "1");
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
    public T visitWedoActuatorStmt(ExprlyParser.WedoActuatorStmtContext ctx) {
        return null;
    }


    @Override
    public T visitRobotWeDoExpression(ExprlyParser.RobotWeDoExpressionContext ctx) {
        return visitWeDoSensorExpr(ctx.weDoSensorExpr());
    }

    @Override
    public T visitRobotWeDoSensorStatement(ExprlyParser.RobotWeDoSensorStatementContext ctx) {
        return visitWedoSensorStmt(ctx.wedoSensorStmt());
    }

    @Override
    public T visitRobotWedoActuatorStatement(ExprlyParser.RobotWedoActuatorStatementContext ctx) {
        return visitWedoActuatorStmt(ctx.wedoActuatorStmt());
    }


    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }
}
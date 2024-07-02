package de.fhg.iais.roberta.textly;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.MbedRobotsBaseVisitor;
import de.fhg.iais.roberta.MbedRobotsParser;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public class RobotMbedTextlyVisitor<T> extends MbedRobotsBaseVisitor<T> {


    public T visitMicrobitV2SensorExpression(MbedRobotsParser.MicrobitV2SensorExpressionContext ctx) {

        String sensor = ctx.MICROBITV2_SENSORSEXPR().toString();
        ExprList parameters = new ExprList();
        for ( MbedRobotsParser.ExprContext expr : ctx.expr() ) {
            Expr param = (Expr) visit(expr);
            parameters.addExpr(param);
            param.setReadOnly();
        }
        parameters.setReadOnly();

        switch ( sensor ) {
            case "accelerometerSensor":

                Hide hide = new Hide();
                hide.setName("SENSORPORT");
                hide.setValue("_A");
                List<Hide> listHide = new LinkedList();
                listHide.add(hide);
                Mutation mutation = new Mutation();
                mutation.setMode("VALUE");

                ExternalSensorBean externalSensorBean = new ExternalSensorBean("_A", "VALUE", "X", mutation, listHide);
                return (T) new SensorExpr(new AccelerometerSensor(mkInlineProperty(ctx, "robSensors_accelerometer_getSample"), externalSensorBean));
        }
        return null;
    }

    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }
}

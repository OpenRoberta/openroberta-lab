package de.fhg.iais.roberta.conf.transformer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationBaseVisitor;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationLexer;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.ActorContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.ActorStmtContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.ConfContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.MotorSpecContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.SensorStmtContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.SizesContext;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Actors;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Sensors;
import de.fhg.iais.roberta.ev3.components.EV3Actor;
import de.fhg.iais.roberta.ev3.components.EV3Sensor;
import de.fhg.iais.roberta.util.Formatter;

public class ConfigurationParseTree2ConfigurationVisitor extends BrickConfigurationBaseVisitor<Void> {
    EV3BrickConfiguration.Builder builder = new EV3BrickConfiguration.Builder();
    ActorPort nextActorToAttach = null;

    /**
     * take a brick configuration program as String, parse it, create a visitor as an instance of this class and visit the parse tree to create an AST.<br>
     * Factory method
     */
    public static BrickConfiguration startWalkForVisiting(String stmt) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(stmt.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        BrickConfigurationLexer lex = new BrickConfigurationLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        BrickConfigurationParser parser = new BrickConfigurationParser(tokens);
        ConfContext tree = parser.conf();
        ConfigurationParseTree2ConfigurationVisitor visitor = new ConfigurationParseTree2ConfigurationVisitor();
        visitor.visit(tree);
        return visitor.builder.build();
    }

    @Override
    public Void visitSizes(SizesContext ctx) {
        try {
            this.builder.setWheelDiameter(Formatter.s2d(ctx.RATIONAL(0).getText()));
            this.builder.setTrackWidth(Formatter.s2d(ctx.RATIONAL(1).getText()));
        } catch ( ParseException e ) {
            throw new RuntimeException("Keys.E1", e);
        }
        return null;
    }

    @Override
    public Void visitActorStmt(ActorStmtContext ctx) {
        this.nextActorToAttach = ActorPort.get(ctx.ACTORPORT().getText());
        return visitChildren(ctx);
    }

    @Override
    public Void visitActor(ActorContext ctx) {
        EV3Actors motorKind;
        if ( "large".equalsIgnoreCase(ctx.MOTORKIND().getText()) ) {
            motorKind = EV3Actors.EV3_LARGE_MOTOR;
        } else {
            motorKind = EV3Actors.EV3_MEDIUM_MOTOR;
        }
        MotorSpecContext motorSpec = ctx.motorSpec();
        boolean regulated = "regulated".equalsIgnoreCase(motorSpec.REGULATION().getText());
        DriveDirection direction;
        if ( "foreward".equalsIgnoreCase(motorSpec.ROTATION().getText()) ) {
            direction = DriveDirection.FOREWARD;
        } else {
            direction = DriveDirection.BACKWARD;
        }
        TerminalNode leftOrRight = motorSpec.LEFTORRIGHT();
        MotorSide motorSide;
        if ( leftOrRight == null ) {
            motorSide = MotorSide.NONE;
        } else if ( "left".equalsIgnoreCase(leftOrRight.getText()) ) {
            motorSide = MotorSide.LEFT;
        } else {
            motorSide = MotorSide.RIGHT;
        }
        EV3Actor actor = new EV3Actor(motorKind, regulated, direction, motorSide);
        this.builder.addActor(this.nextActorToAttach, actor);
        this.nextActorToAttach = null;
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmtContext ctx) {
        SensorPort port = SensorPort.get(ctx.SENSORPORT().getText());
        String sensorShortName = ctx.SENSOR().getText();
        EV3Sensors attachedSensor = null;
        for ( EV3Sensors s : EV3Sensors.SENSORS ) {
            if ( s.getShortName().equalsIgnoreCase(sensorShortName) ) {
                attachedSensor = s;
                break;
            }
        }
        if ( attachedSensor == null ) {
            throw new RuntimeException("Keys.E2");
        }
        this.builder.addSensor(port, new EV3Sensor(attachedSensor));
        return null;
    }
}
package de.fhg.iais.roberta.conf.transformer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.fhg.iais.roberta.ast.syntax.BrickConfigurationOld;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationBaseVisitor;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationLexer;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.ActorStmtContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.ConnectorlContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.MotorContext;
import de.fhg.iais.roberta.brickConfiguration.generated.BrickConfigurationParser.SensorStmtContext;

public class BrickConfigurationTreeToAst extends BrickConfigurationBaseVisitor<Void> {
    BrickConfigurationOld.Builder builder = new BrickConfigurationOld.Builder();

    /**
     * take a brick configuration program as String, parse it, create a visitor as an instance of this class and visit the parse tree to create an AST.<br>
     * Factory method
     */
    public static BrickConfigurationOld startWalkForVisiting(String stmt) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(stmt.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        BrickConfigurationLexer lex = new BrickConfigurationLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        BrickConfigurationParser parser = new BrickConfigurationParser(tokens);
        ConnectorlContext tree = parser.connectorl();
        new BrickConfigurationTreeToAst().visit(tree);
        BrickConfigurationTreeToAst visitor = new BrickConfigurationTreeToAst();
        visitor.visit(tree);
        return visitor.builder.build();
    }

    @Override
    public Void visitActorStmt(ActorStmtContext ctx) {
        super.visitActorStmt(ctx);
        this.builder.visitingActorPort(ctx.ACTORPORT().getText());
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmtContext ctx) {
        this.builder.visiting(ctx.ATTACHSENSOR().getText());
        this.builder.visitingSensorPort(ctx.SENSORPORT().getText());
        return null;
    }

    @Override
    public Void visitMotor(MotorContext ctx) {
        TerminalNode regulation = ctx.REGULATION();
        this.builder.visiting(ctx.MOTORTYPE().getText(), regulation == null ? "regulated" : regulation.getText());
        return null;
    }
}
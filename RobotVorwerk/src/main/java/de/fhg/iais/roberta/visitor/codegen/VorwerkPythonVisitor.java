package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOn;
import de.fhg.iais.roberta.syntax.action.vorwerk.SideBrush;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOn;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IVorwerkVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class VorwerkPythonVisitor extends AbstractPythonVisitor implements IVorwerkVisitor<Void> {
    protected final ConfigurationAst brickConfiguration;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public VorwerkPythonVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);

        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.wait(50)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("hal.wait(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("hal.play_sound(" + playFileAction.getFileName() + ")");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String userDefinedPort = motorOnAction.getUserDefinedPort();
        String port = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getInternalPortName();
        this.sb.append("hal." + port + "_motor_on(");
        motorOnAction.getParam().getSpeed().accept(this);
        this.sb.append(", ");
        motorOnAction.getDurationValue().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String userDefinedPort = motorStopAction.getUserDefinedPort();
        String port = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getInternalPortName();
        this.sb.append("hal." + port + "_motor_stop()");
        return null;
    }

    @Override
    public Void visitBrushOn(BrushOn<Void> brushOn) {
        this.sb.append("hal.brush_on(");
        brushOn.getSpeed().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBrushOff(BrushOff<Void> brushOff) {
        this.sb.append("hal.brush_off()");
        return null;
    }

    @Override
    public Void visitSideBrush(SideBrush<Void> sideBrush) {
        this.sb.append("hal.side_brush_" + sideBrush.getWorkingState().toString().toLowerCase() + "()");
        return null;
    }

    @Override
    public Void visitVacuumOn(VacuumOn<Void> vacuumOn) {
        this.sb.append("hal.vacuum_on(");
        vacuumOn.getSpeed().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitVacuumOff(VacuumOff<Void> vacuumOff) {
        this.sb.append("hal.vacuum_off()");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.sb.append("hal.drive_distance(" + quote(driveAction.getDirection().toString()) + ", ");
        driveAction.getParam().getSpeed().accept(this);
        this.sb.append(", ");
        if ( driveAction.getParam().getDuration() == null ) {
            this.sb.append("100");
        } else {
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("hal.stop_motors()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort();
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("hal.getTimerValue(" + timerNumber + ")");
                break;
            case SC.RESET:
                this.sb.append("hal.resetTimer(" + timerNumber + ")");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        String port = getDevicePortName(touchSensor);
        this.sb.append("hal.sample_touch_sensor(" + port);
        this.sb.append(", ");
        this.sb.append(quote(touchSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = getDevicePortName(ultrasonicSensor);
        this.sb.append("hal.sample_ultrasonic_sensor(").append(port);
        this.sb.append(", ");
        this.sb.append(quote(ultrasonicSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        String port = getDevicePortName(accelerometerSensor);
        this.sb.append("hal.sample_accelerometer_sensor(").append(port).append(")");
        return null;
    }

    @Override
    public Void visitDropOffSensor(DropOffSensor<Void> dropOffSensor) {
        String port = getDevicePortName(dropOffSensor);
        this.sb.append("hal.sample_dropoff_sensor(").append(port).append(")");
        return null;
    }

    @Override
    public Void visitWallSensor(WallSensor<Void> wallSensor) {
        this.sb.append("hal.sample_wall_sensor()");
        return null;
    }

    private String getDevicePortName(ExternalSensor<Void> sensor) {
        String userDefinedPort = sensor.getPort();
        String port = this.brickConfiguration.getConfigurationComponent(userDefinedPort).getInternalPortName();
        return "'" + port + "'";
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.usedGlobalVarInFunctions.clear();
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.sb.append("from __future__ import absolute_import");
        nlIndent();
        this.sb.append("from roberta import Hal");
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        nlIndent();
        this.sb.append("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.sb.append("class ContinueLoop(Exception): pass");
        nlIndent();
        nlIndent();
        this.sb.append("hal = Hal()");
        nlIndent();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.sb.append("def main():");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("run()");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("print('Fehler im Vorwerk')");
        nlIndent();
        this.sb.append("print(e.__class__.__name__)");
        nlIndent();
        // FIXME: we can only print about 30 chars
        this.sb.append("print(e)");
        decrIndentation();
        decrIndentation();
        nlIndent();
        nlIndent();
        this.sb.append("if __name__ == \"__main__\":");
        incrIndentation();
        nlIndent();
        this.sb.append("main()");
    }

    private String quote(String value) {
        return "'" + value.toLowerCase() + "'";
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}

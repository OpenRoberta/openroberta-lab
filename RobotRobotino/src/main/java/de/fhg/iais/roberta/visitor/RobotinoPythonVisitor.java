package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.constants.RobotinoConstants;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveDistanceAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidrivePositionAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.CameraSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.CameraThreshold;
import de.fhg.iais.roberta.syntax.sensor.robotino.ColourBlob;
import de.fhg.iais.roberta.syntax.sensor.robotino.MarkerInformation;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensorReset;
import de.fhg.iais.roberta.syntax.sensor.robotino.OpticalSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class RobotinoPythonVisitor extends AbstractPythonVisitor implements IRobotinoVisitor<Void> {

    private final ConfigurationAst configurationAst;
    private final List<VarDeclaration> varDeclarations;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public RobotinoPythonVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
        this.configurationAst = configurationAst;
        varDeclarations = new ArrayList<>();
    }

    private void generateVariables() {
        this.src.add("sys.stdout = io.StringIO()\nsys.stderr = io.StringIO()");
        nlIndent();
        this.src.add("ROBOTINOIP = \"127.0.0.1:80\"");
        nlIndent();
        this.src.add("PARAMS = {'sid':'robertaProgram'}");
        nlIndent();
        this.src.add("MAXSPEED = 0.5");
        nlIndent();
        this.src.add("MAXROTATION = 0.57");
        generateOptionalVariables();
    }

    private void generateOptionalVariables() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            nlIndent();
            this.src.add("_digitalPinValues = [0 for i in range(8)]");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            nlIndent();
            this.src.add("currentSpeed = [0, 0, 0]");
        }
    }

    private ConfigurationComponent getOmnidrive() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(RobotinoConstants.OMNIDRIVE) ) {
                return component;
            }
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.usedGlobalVarInFunctions.add(var.getCodeSafeName());
        this.src.add(var.getCodeSafeName());
        if ( var.getBlocklyType().getBlocklyName().contains("Array") ) {
            this.src.add(" = []");
        } else {
            this.src.add(" = None");
        }
        this.varDeclarations.add(var);
        return null;
    }

    @Override
    protected void visitorGenerateImports() {
        this.src.add("#!/usr/bin/env python3");
        nlIndent();
        this.src.add("import math, random, time, requests, threading, sys, io");
        nlIndent();
    }

    @Override
    protected void visitorGenerateGlobalVariables() {
        generateVariables();
        nlIndent();
        generateTimerVariables(false);
        nlIndent();
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        visitorGenerateUserVariablesAndMethods(mainTask);
        nlIndent();

        this.src.add("def run(RV):");
        incrIndentation();
        generateGlobalVariables();
        this.src.add("time.sleep(1)");
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.ODOMETRY) ) {
            nlIndent();
            //odometrieReset
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.RESETODOMETRY), "(RV, 0, 0, 0)");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.CAMERA) ) {
            //default threshold
            nlIndent();
            this.src.add("RV.writeFloat(4, 100) ");
            nlIndent();
            this.src.add("time.sleep(0.05)");
        }
        generateTimerVariables(true);
        for ( VarDeclaration var : varDeclarations ) {
            nlIndent();
            declareGlobalVariable(var);
        }

        nlIndent();
        return null;
    }

    private boolean hasUserdefinedMethods() {
        return this.programPhrases
            .stream().anyMatch(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"));
    }

    private void generateMain() {
        this.src.add("def main(RV):");
        incrIndentation();
        nlIndent();
        this.src.add("try:");
        incrIndentation();
        nlIndent();
        this.src.add("run(RV)");
        decrIndentation();
        nlIndent();
        this.src.add("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.src.add("print(e)");
        nlIndent();
        this.src.add("raise");
        decrIndentation();
        nlIndent();
        generateFinally();
        decrIndentation();
        nlIndent();
    }

    private void generateStart() {
        this.src.add("def start(RV):");
        incrIndentation();
        nlIndent();
        this.src.add("motorDaemon2 = threading.Thread(target=main, daemon=True, args=(RV,), name='mainProgram')\n    motorDaemon2.start()");
        decrIndentation();
        nlIndent();
    }

    private void declareGlobalVariable(VarDeclaration var) {
        super.visitVarDeclaration(var);
    }

    private void generateGlobalVariables() {
        //add usermade global variables
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
        }
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
        appendViewMethods();
        decrIndentation();
        nlIndent();
    }

    private void appendViewMethods() {
        this.src.add("def step(RV):");
        incrIndentation();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            nlIndent();
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.POSTVEL), "()");
        } else {
            nlIndent();
            this.src.add("pass");
        }
        decrIndentation();
        nlIndent();
        nlIndent();
        generateMain();
        generateStart();
        nlIndent();
        this.src.add("def stop(RV):");
        incrIndentation();
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        nlIndent();
        nlIndent();
        this.src.add("def cleanup(RV):");
        incrIndentation();
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        nlIndent();
    }

    private void generateFinally() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) || this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            this.src.add("finally:");
            incrIndentation();
            nlIndent();
            if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
                this.src.add("global _digitalPinValues");
                nlIndent();
            }
        }

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.OMNIDRIVESPEED));
            this.src.add("(0,0,0)");
            nlIndent();
        }

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            this.src.add("_digitalPinValues = [0 for i in range(8)]");
            nlIndent();
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.SETDIGITALPIN), "(1, False)");
            nlIndent();
        }

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) || this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            decrIndentation();
            decrIndentation();
            nlIndent();
        }
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.src.add("((time.time() - _timer", timerSensor.getUserDefinedPort(), ")/1000)");
                break;
            case SC.RESET:
                this.src.add("_timer", timerSensor.getUserDefinedPort(), " = time.time()");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.add("_timer", timerReset.sensorPort, " = time.time()");
        return null;
    }

    private void generateTimerVariables(boolean decleration) {
        this.getBean(UsedHardwareBean.class)
            .getUsedSensors()
            .stream()
            .filter(usedSensor -> usedSensor.getType().equals(SC.TIMER))
            .collect(Collectors.groupingBy(UsedSensor::getPort))
            .keySet()
            .forEach(port -> {
                this.usedGlobalVarInFunctions.add("_timer" + port);
                nlIndent();
                if ( decleration ) {
                    this.src.add("_timer", port, " = time.time()");
                } else {
                    this.src.add("_timer", port, " = None");
                }
            });
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.src.add("time.sleep(0.2)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("time.sleep(");
        waitTimeStmt.time.accept(this);
        this.src.add("/1000)");
        return null;
    }


    @Override
    public Void visitOmnidriveAction(OmnidriveAction omnidriveAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.OMNIDRIVESPEED));
        this.src.add("(");
        omnidriveAction.xVel.accept(this);
        this.src.add(", ");
        omnidriveAction.yVel.accept(this);
        this.src.add(", ");
        omnidriveAction.thetaVel.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.OMNIDRIVESPEED));
        this.src.add("(0, 0, 0)");
        return null;
    }

    @Override
    public Void visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.DRIVEFORDISTANCE));
        this.src.add("(RV, ");
        omnidriveDistanceAction.xVel.accept(this);
        this.src.add(", ");
        omnidriveDistanceAction.yVel.accept(this);
        this.src.add(", ");
        omnidriveDistanceAction.distance.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitOmnidrivePositionAction(OmnidrivePositionAction omnidrivePositionAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.DRIVETOPOSITION));
        this.src.add("(RV, ");
        omnidrivePositionAction.x.accept(this);
        this.src.add(", ");
        omnidrivePositionAction.y.accept(this);
        this.src.add(", ");
        omnidrivePositionAction.power.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.TURNFORDEGREES), "(RV, ");
        if ( turnAction.direction == TurnDirection.RIGHT ) {
            this.src.add("-");
        }
        turnAction.param.getSpeed().accept(this);
        this.src.add(", ");
        turnAction.param.getDuration().getValue().accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMarkerInformation(MarkerInformation markerInformation) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETMARKERINFO));
        this.src.add("(RV, ");
        markerInformation.markerId.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectMarkSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETMARKERS));
        this.src.add("(RV)");
        return null;
    }

    @Override
    public Void visitCameraSensor(CameraSensor cameraSensor) {
        if ( cameraSensor.getMode().equals("LINE") ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETCAMERALINE), "(RV)");
        }
        return null;
    }

    @Override
    public Void visitOdometrySensor(OdometrySensor odometrySensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETODOMETRY));
        if ( odometrySensor.getSlot().equals("THETA") ) {
            this.src.add("('rot') * (180 / math.pi)");
        } else {
            this.src.add("('", odometrySensor.getSlot().toLowerCase(), "') * 100");
        }
        return null;
    }

    @Override
    public Void visitOdometrySensorReset(OdometrySensorReset odometrySensorReset) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.RESETODOMETRY));
        switch ( odometrySensorReset.slot ) {
            case "ALL":
                this.src.add("(RV, 0, 0, 0)");
                break;
            case "X":
                this.src.add("(RV, 0, RV.readFloatVector(1)[1], RV.readFloatVector(1)[2])");
                break;
            case "Y":
                this.src.add("(RV, RV.readFloatVector(1)[0], 0, RV.readFloatVector(1)[2])");
                break;
            case "THETA":
                this.src.add("(RV, RV.readFloatVector(1)[0], RV.readFloatVector(1)[1], 0)");
                break;
            default:
                throw new DbcException("Invalid Odometry Mode!");
        }
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        String port = configurationAst.getConfigurationComponent(pinGetValueSensor.getUserDefinedPort()).getComponentProperties().get("OUTPUT").substring(2);

        if ( pinGetValueSensor.getMode().equals(SC.DIGITAL) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETDIGITALPIN), "(", port, ")");
        } else if ( pinGetValueSensor.getMode().equals(SC.ANALOG) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETANALOGPIN), "(", port, ")");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.ISBUMPED), "()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETDISTANCE), "(", port, ")");
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.SETDIGITALPIN),
            "(",
            configurationAst.getConfigurationComponent(pinWriteValueAction.port).getComponentProperties().get("INPUT").substring(2),
            ", ");
        pinWriteValueAction.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitColourBlob(ColourBlob colourBlob) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETCOLOURBLOB), "(RV, [");
        colourBlob.minHue.accept(this);
        this.src.add(", ");
        colourBlob.maxHue.accept(this);
        this.src.add(", ");
        colourBlob.minSat.accept(this);
        this.src.add(", ");
        colourBlob.maxSat.accept(this);
        this.src.add(", ");
        colourBlob.minVal.accept(this);
        this.src.add(", ");
        colourBlob.maxVal.accept(this);
        this.src.add("])");
        return null;
    }

    @Override
    public Void visitCameraThreshold(CameraThreshold cameraThreshold) {
        this.src.add("RV.writeFloat(4, ");
        cameraThreshold.threshold.accept(this);
        this.src.add(")");
        this.nlIndent();
        this.src.add("time.sleep(0.005)");
        return null;
    }

    @Override
    public Void visitOpticalSensor(OpticalSensor opticalSensor) {
        String port = "";
        if ( opticalSensor.getMode().equals("OPENING") ) {
            port = configurationAst.getConfigurationComponent(opticalSensor.getUserDefinedPort()).getComponentProperties().get("BK").substring(2);
        } else {
            port = configurationAst.getConfigurationComponent(opticalSensor.getUserDefinedPort()).getComponentProperties().get("WH").substring(2);
        }
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETDIGITALPIN), "(", port, ")");
        return null;
    }

}
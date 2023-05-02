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
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
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
    private List<VarDeclaration> varDeclarations;

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

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        this.sb.append("#!/usr/bin/env python3");
        nlIndent();
        this.sb.append("import math, random, time, requests, threading, sys, io");
        nlIndent();
        generateVariables();
        nlIndent();
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this.getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
        }
        generateTimerVariables(false);
    }

    private void generateVariables() {
        this.sb.append("sys.stdout = io.StringIO()\n" +
            "sys.stderr = io.StringIO()");
        nlIndent();
        this.sb.append("ROBOTINOIP = \"127.0.0.1:80\"");
        nlIndent();
        this.sb.append("PARAMS = {'sid':'robertaProgram'}");
        nlIndent();
        this.sb.append("MAXSPEED = 0.5");
        nlIndent();
        this.sb.append("MAXROTATION = 0.57");
        generateOptionalVariables();
    }

    private void generateOptionalVariables() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            nlIndent();
            this.sb.append("_digitalPinValues = [0 for i in range(8)]");
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            nlIndent();
            this.sb.append("currentSpeed = [0, 0, 0]");
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
        this.sb.append(var.getCodeSafeName());
        if ( var.getVarType().getBlocklyName().contains("Array") ) {
            this.sb.append(" = []");
        } else {
            this.sb.append(" = None");
        }
        this.varDeclarations.add(var);
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        variables.accept(this);
        generateUserDefinedMethods();
        if ( hasUserdefinedMethods() ) {
            nlIndent();
        } else if ( varDeclarations.size() > 0 ) {
            nlIndent();
            nlIndent();
        }
        nlIndent();
        this.sb.append("def run(RV):");
        incrIndentation();
        generateGlobalVariables();
        this.sb.append("time.sleep(1)");
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.ODOMETRY) ) {
            nlIndent();
            //odometrieReset
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.RESETODOMETRY))
                .append("(RV, 0, 0, 0)");
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.CAMERA) ) {
            //default threshold
            nlIndent();
            this.sb.append("RV.writeFloat(4, 100) ");
            nlIndent();
            this.sb.append("time.sleep(0.05)");
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
        this.sb.append("def main(RV):");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("run(RV)");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("print(e)");
        nlIndent();
        this.sb.append("raise");
        decrIndentation();
        nlIndent();
        generateFinally();
        decrIndentation();
        nlIndent();
    }

    private void generateStart() {
        this.sb.append("def start(RV):");
        incrIndentation();
        nlIndent();
        this.sb.append("motorDaemon2 = threading.Thread(target=main, daemon=True, args=(RV,), name='mainProgram')\n" +
            "    motorDaemon2.start()");
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
            this.sb.append("global ").append(String.join(", ", this.usedGlobalVarInFunctions));
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
        this.sb.append("def step(RV):");
        incrIndentation();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            nlIndent();
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).
                    getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.POSTVEL))
                .append("()");
        } else {
            nlIndent();
            this.sb.append("pass");
        }
        decrIndentation();
        nlIndent();
        nlIndent();
        generateMain();
        generateStart();
        nlIndent();
        this.sb.append("def stop(RV):");
        incrIndentation();
        nlIndent();
        this.sb.append("pass");
        decrIndentation();
        nlIndent();
        nlIndent();
        this.sb.append("def cleanup(RV):");
        incrIndentation();
        nlIndent();
        this.sb.append("pass");
        decrIndentation();
        nlIndent();
    }

    private void generateFinally() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) || this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            this.sb.append("finally:");
            incrIndentation();
            nlIndent();
            if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
                this.sb.append("global _digitalPinValues");
                nlIndent();
            }
        }

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.OMNIDRIVESPEED));
            this.sb.append("(0,0,0)");
            nlIndent();
        }

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            this.sb.append("_digitalPinValues = [0 for i in range(8)]");
            nlIndent();
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.SETDIGITALPIN))
                .append("(1, False)");
            nlIndent();
        }

        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) || this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            decrIndentation();
            decrIndentation();
            nlIndent();
        }
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("((time.time() - _timer").append(timerSensor.getUserDefinedPort()).append(")/1000)");
                break;
            case SC.RESET:
                this.sb.append("_timer").append(timerSensor.getUserDefinedPort()).append(" = time.time()");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.sb.append("_timer").append(timerReset.sensorPort).append(" = time.time()");
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
                    this.sb.append("_timer").append(port).append(" = time.time()");
                } else {
                    this.sb.append("_timer").append(port).append(" = None");
                }
            });
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.sb.append("time.sleep(0.2)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("time.sleep(");
        waitTimeStmt.time.accept(this);
        this.sb.append("/1000)");
        return null;
    }


    @Override
    public Void visitOmnidriveAction(OmnidriveAction omnidriveAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.OMNIDRIVESPEED));
        this.sb.append("(");
        omnidriveAction.xVel.accept(this);
        this.sb.append(", ");
        omnidriveAction.yVel.accept(this);
        this.sb.append(", ");
        omnidriveAction.thetaVel.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.OMNIDRIVESPEED));
        this.sb.append("(0, 0, 0)");
        return null;
    }

    @Override
    public Void visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.DRIVEFORDISTANCE));
        this.sb.append("(RV, ");
        omnidriveDistanceAction.xVel.accept(this);
        this.sb.append(", ");
        omnidriveDistanceAction.yVel.accept(this);
        this.sb.append(", ");
        omnidriveDistanceAction.distance.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitOmnidrivePositionAction(OmnidrivePositionAction omnidrivePositionAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.DRIVETOPOSITION));
        this.sb.append("(RV, ");
        omnidrivePositionAction.x.accept(this);
        this.sb.append(", ");
        omnidrivePositionAction.y.accept(this);
        this.sb.append(", ");
        omnidrivePositionAction.power.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.TURNFORDEGREES))
            .append("(RV, ");
        if ( turnAction.direction == TurnDirection.RIGHT ) {
            this.sb.append("-");
        }
        turnAction.param.getSpeed().accept(this);
        this.sb.append(", ");
        turnAction.param.getDuration().getValue().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMarkerInformation(MarkerInformation markerInformation) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETMARKERINFO));
        this.sb.append("(RV, ");
        markerInformation.markerId.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectMarkSensor) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETMARKERS));
        this.sb.append("(RV)");
        return null;
    }

    @Override
    public Void visitCameraSensor(CameraSensor cameraSensor) {
        if ( cameraSensor.getMode().equals("LINE") ) {
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETCAMERALINE))
                .append("(RV)");
        }
        return null;
    }

    @Override
    public Void visitOdometrySensor(OdometrySensor odometrySensor) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETODOMETRY));
        if ( odometrySensor.getSlot().equals("THETA") ) {
            this.sb.append("('rot')")
                .append(" * (180 / math.pi)");
        } else {
            this.sb.append("('")
                .append(odometrySensor.getSlot().toLowerCase())
                .append("') * 100");
        }
        return null;
    }

    @Override
    public Void visitOdometrySensorReset(OdometrySensorReset odometrySensorReset) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.RESETODOMETRY));
        switch ( odometrySensorReset.slot ) {
            case "ALL":
                this.sb.append("(RV, 0, 0, 0)");
                break;
            case "X":
                this.sb.append("(RV, 0, RV.readFloatVector(1)[1], RV.readFloatVector(1)[2])");
                break;
            case "Y":
                this.sb.append("(RV, RV.readFloatVector(1)[0], 0, RV.readFloatVector(1)[2])");
                break;
            case "THETA":
                this.sb.append("(RV, RV.readFloatVector(1)[0], RV.readFloatVector(1)[1], 0)");
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
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETDIGITALPIN)).append("(" + port + ")");
        } else if ( pinGetValueSensor.getMode().equals(SC.ANALOG) ) {
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETANALOGPIN)).append("(" + port + ")");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.ISBUMPED))
            .append("()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETDISTANCE) + "(")
            .append(port)
            .append(")");
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.SETDIGITALPIN))
            .append("(")
            .append(configurationAst.getConfigurationComponent(pinWriteValueAction.port).getComponentProperties().get("INPUT").substring(2))
            .append(", ");
        pinWriteValueAction.value.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitColourBlob(ColourBlob colourBlob) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETCOLOURBLOB))
            .append("(RV, [");
        colourBlob.minHue.accept(this);
        this.sb.append(", ");
        colourBlob.maxHue.accept(this);
        this.sb.append(", ");
        colourBlob.minSat.accept(this);
        this.sb.append(", ");
        colourBlob.maxSat.accept(this);
        this.sb.append(", ");
        colourBlob.minVal.accept(this);
        this.sb.append(", ");
        colourBlob.maxVal.accept(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitCameraThreshold(CameraThreshold cameraThreshold) {
        this.sb.append("RV.writeFloat(4, ");
        cameraThreshold.threshold.accept(this);
        this.sb.append(")");
        this.nlIndent();
        this.sb.append("time.sleep(0.005)");
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
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETDIGITALPIN)).append("(" + port + ")");
        return null;
    }

}
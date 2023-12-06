package de.fhg.iais.roberta.visitor;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.constants.FischertechnikConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MotorOmniOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class Txt4PythonVisitor extends AbstractPythonVisitor implements ITxt4Visitor<Void> {

    private final ConfigurationAst configurationAst;
    private String frontLeftMotor;
    private String frontRightMotor;
    private String rearLeftMotor;
    private String rearRightMotor;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public Txt4PythonVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
        this.configurationAst = configurationAst;
        setMotorPorts();
    }

    private void setMotorPorts() {
        ConfigurationComponent omniDrive = getOmniDrive();
        if ( omniDrive != null ) {
            frontLeftMotor = omniDrive.getOptProperty("MOTOR_FL");
            frontRightMotor = omniDrive.getOptProperty("MOTOR_FR");
            rearLeftMotor = omniDrive.getOptProperty("MOTOR_RL");
            rearRightMotor = omniDrive.getOptProperty("MOTOR_RR");
        } else {
            frontLeftMotor = "M1";
            frontRightMotor = "M2";
            rearLeftMotor = "M3";
            rearRightMotor = "M4";

        }
    }

    private ConfigurationComponent getOmniDrive() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(FischertechnikConstants.OMNIDRIVE) ) {
                return component;
            }
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        if ( !variables.get().isEmpty() ) {
            variables.accept(this);
            nlIndent();
        }
        if ( this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
            .count() > 0 ) {
            generateUserDefinedMethods();
        }
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this.getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.src.add(helperMethodImpls);
        }
        nlIndent();
        this.src.add("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String motorPort = getPortFromConfig(motorOnAction.port);
        this.src.add("TXT_M_", motorPort, "_encodermotor.set_speed(int(");
        motorOnAction.power.accept(this);
        this.src.add("),Motor.CCW)");
        nlIndent();
        this.src.add("TXT_M_", motorPort, "_encodermotor.start()");
        return null;
    }

    @Override
    public Void visitMotorOmniOnAction(MotorOmniOnAction motorOmniOnAction) {

        //TODO maybe make this global?
        String motorFL = "TXT_M_" + this.frontLeftMotor + "_encodermotor";
        String motorFR = "TXT_M_" + this.frontRightMotor + "_encodermotor";
        String motorRL = "TXT_M_" + this.rearLeftMotor + "_encodermotor";
        String motorRR = "TXT_M_" + this.rearRightMotor + "_encodermotor";

        String speedMultiplier = "";
        //replace with forward, backward, left, right, forward left, backward right, forward right, backward left,
        switch ( motorOmniOnAction.direction ) {
            case "HEART_SMALL": //backward
                speedMultiplier = "-";
            case "HEART": //forward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEVERTICAL));
                break;
            case "SMILE": //right
                speedMultiplier = "-";
            case "HAPPY": //left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEHORIZONTAL));
                break;
            case "SUPRISED": //backward right
                speedMultiplier = "-";
            case "CONFUSED": //forward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTL));
                break;
            case "ANGRY": //backward left
                speedMultiplier = "-";
            case "ASLEEP": //forward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTR));
                break;
            default:
                break;
        }
        this.src.add("(", motorFL, ", ", motorFR, ", ", motorRL, ", ", motorRR, ", ");
        this.src.add(speedMultiplier);
        motorOmniOnAction.power.accept(this);
        this.src.add(")");


        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.src.add("(");
        rgbColor.R.accept(this);
        this.src.add(", ");
        rgbColor.G.accept(this);
        this.src.add(", ");
        rgbColor.B.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.add("timer.reset()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("timer.now() * 1000");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String color = "";
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#E701A7":
                color = "'pink'";
                break;
            case "#571CC1":
                color = "'violet'";
                break;
            case "#3590F5":
                color = "'blue'";
                break;
            case "#77E7FF":
                color = "'azure'";
                break;
            case "#0FCB54":
                color = "'cyan'";
                break;
            case "#0BA845":
                color = "'green'";
                break;
            case "#F7F700":
                color = "'yellow'";
                break;
            case "#FAAC01":
                color = "'orange'";
                break;
            case "#FA010C":
                color = "'red'";
                break;
            case "#000000":
                color = "'black'";
                break;
            case "#FFFFFF":
                color = "'white'";
                break;
            case "#EBC300":
                color = "None";
                break;
            default:
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        this.src.add(color);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
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
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        UsedHardwareBean usedHardwareBean = this.getBean(UsedHardwareBean.class);
        this.src.add("import math").nlI();
        this.src.add("import time").nlI();
        if ( usedHardwareBean.isActorUsed(C.RANDOM) || usedHardwareBean.isActorUsed(C.RANDOM_DOUBLE) ) {
            this.src.add("import random").nlI();
        }
        if ( !usedHardwareBean.getUsedActors().isEmpty() && !usedHardwareBean.getUsedSensors().isEmpty() ) {
            nlIndent();
        }

    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.src.add("def main():");
        incrIndentation();
        nlIndent();
        this.src.add("try:");
        incrIndentation();
        nlIndent();
        this.src.add("run()");
        decrIndentation();
        nlIndent();
        this.src.add("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        //TODO finally close open ports
        decrIndentation();
        nlIndent();
        nlIndent();

        this.src.add("main()");
    }

    private String getPortFromConfig(String name) {
        ConfigurationComponent block = configurationAst.getConfigurationComponent(name);
        return block.getComponentProperties().get("PORT");
    }
}
package de.fhg.iais.roberta.visitor;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
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
public final class RobotinoROSPythonVisitor extends AbstractPythonVisitor implements IRobotinoVisitor<Void> {

    private final ConfigurationAst configurationAst;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public RobotinoROSPythonVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
        this.configurationAst = configurationAst;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        this.sb.append("#!/usr/bin/env python3");
        nlIndent();
        this.sb.append("import rospy");
        nlIndent();
        this.sb.append("import math, random");
        nlIndent();
        generateImports();
        nlIndent();
        this.sb.append("rospy.init_node('robotino_go', anonymous=True)");
        nlIndent();
        generatePublishers();
        generateTimerVariables();
        nlIndent();
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this.getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
        }
    }

    private void generateImports() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TOUCH) ) {
            this.sb.append("from std_msgs.msg import Bool");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            this.sb.append("from geometry_msgs.msg import Twist");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.INFRARED) ) {
            this.sb.append("from sensor_msgs.msg import PointCloud");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.DIGITAL_INPUT) ) {
            this.sb.append("from std_msgs.msg import Int8MultiArray");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.ANALOG_INPUT) || this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.ODOMETRY) ) {
            this.sb.append("from std_msgs.msg import Float32MultiArray");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.ODOMETRY) ) {
            this.sb.append("from nav_msgs.msg import Odometry");
            nlIndent();
        }
    }

    private void generatePublishers() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            this.sb.append("_motorPub = rospy.Publisher('cmd_vel', Twist, queue_size=10)");
            nlIndent();
            this.sb.append("_speed = Twist()\n_safetySetting = True\n");
            nlIndent();
            this.sb.append("_weight = " + Double.parseDouble(getOmnidrive().getComponentProperties().get("WEIGHT_KG")));
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            this.sb.append("_digitalPinPub = rospy.Publisher('set_digital_output', Int8MultiArray, queue_size=10)");
            nlIndent();
            this.sb.append("_digitalPinValues = [0 for i in range(8)]");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.ODOMETRY) ) {
            this.sb.append("_resetOdomPub = rospy.Publisher('reset_odometry', Float32MultiArray, queue_size=10)");
            nlIndent();
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
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        generateGlobalVariables();
        this.sb.append("print(\"starting roberta node...\")");
        nlIndent();
        generateInitializers();
        //cannot immediately publish after determining publishers so waiting time is added at the beginning
        this.sb.append("rospy.sleep(0.3)");
        nlIndent();
        nlIndent();

        return null;
    }

    private void generateGlobalVariables() {
        //add usermade global variables
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global ").append(String.join(", ", this.usedGlobalVarInFunctions));
        }
        nlIndent();
    }

    private void generateInitializers() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            this.sb.append("_omnidrive = rospy.Timer(rospy.Duration(0.1),_publishVel)");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(RobotinoConstants.ODOMETRY) ) {
            this.sb.append("rospy.sleep(0.3)");
            nlIndent();
            this.sb.append("_resetOdomPub.publish(Float32MultiArray())");
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
        this.sb.append("raise");
        decrIndentation();
        nlIndent();
        decrIndentation();
        generateFinally();
        decrIndentation();
        nlIndent();

        this.sb.append("main()");
    }

    private void generateFinally() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) || this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            this.sb.append("finally:");
        }
        incrIndentation();
        incrIndentation();

        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(RobotinoConstants.OMNIDRIVE) ) {
            this.sb.append("_setSpeedOmnidrive(0, 0, 0, False)");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIGITAL_PIN) ) {
            this.sb.append("int8Array = Int8MultiArray()");
            nlIndent();
            this.sb.append("int8Array.data = [0 for i in range(0, 8)]");
            nlIndent();
            this.sb.append("_digitalPinPub.publish(int8Array)");
            nlIndent();
        }
        decrIndentation();
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
                this.sb.append("((rospy.get_time() - _timer").append(timerSensor.getUserDefinedPort()).append(")*1000)");
                break;
            case SC.RESET:
                this.sb.append("_timer").append(timerSensor.getUserDefinedPort()).append(" = rospy.get_time()");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        return null;
    }

    private void generateTimerVariables() {
        this.getBean(UsedHardwareBean.class)
            .getUsedSensors()
            .stream()
            .filter(usedSensor -> usedSensor.getType().equals(SC.TIMER))
            .collect(Collectors.groupingBy(UsedSensor::getPort))
            .keySet()
            .forEach(port -> {
                this.usedGlobalVarInFunctions.add("_timer" + port);
                this.sb.append("_timer").append(port).append(" = rospy.get_time()");
                nlIndent();
            });
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.sb.append("rospy.sleep(0.2)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("rospy.sleep(");
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
        this.sb.append(", ");
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TOUCH) ) {
            this.sb.append("False");
        } else {
            this.sb.append("True");
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.OMNIDRIVESPEED));
        this.sb.append("(0, 0, 0, False)");
        return null;
    }

    @Override
    public Void visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.DRIVEFORDISTANCE));
        this.sb.append("(");
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
        this.sb.append("(");
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
            .append("(");
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
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectMarkSensor) {
        return null;
    }

    @Override
    public Void visitCameraSensor(CameraSensor cameraSensor) {
        return null;
    }

    @Override
    public Void visitOdometrySensor(OdometrySensor odometrySensor) {
        if ( odometrySensor.getSlot().equals("THETA") ) {
            this.sb.append("(")
                .append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.GETORIENTATION))
                .append("() * 180 / math.pi)");
        } else {
            this.sb.append("rospy.wait_for_message(\"odom\", Odometry).pose.pose.position.")
                .append(odometrySensor.getSlot().toLowerCase());
        }
        return null;
    }

    @Override
    public Void visitOdometrySensorReset(OdometrySensorReset odometrySensorReset) {
        switch ( odometrySensorReset.slot ) {
            case "ALL":
                this.sb.append("_resetOdomPub.publish(Float32MultiArray())");
                break;
            case "X":
                this.sb.append("_resetOdometry(")
                    .append("0, ")
                    .append("rospy.wait_for_message(\"odom\", Odometry).pose.pose.position.y, ")
                    .append("_getOrientation())");
                break;
            case "Y":
                this.sb.append("_resetOdometry(")
                    .append("rospy.wait_for_message(\"odom\", Odometry).pose.pose.position.x, ")
                    .append("0, ")
                    .append("_getOrientation())");
                break;
            case "THETA":
                this.sb.append("_resetOdometry(")
                    .append("rospy.wait_for_message(\"odom\", Odometry).pose.pose.position.x, ")
                    .append("rospy.wait_for_message(\"odom\", Odometry).pose.pose.position.y, ")
                    .append("0)");
                break;
            default:
                throw new DbcException("Invalid Odometry Mode!");

        }
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        this.sb.append("rospy.wait_for_message(");
        if ( pinGetValueSensor.getMode().equals(SC.DIGITAL) ) {
            this.sb.append("\"digital_inputs\", Int8MultiArray");
        } else if ( pinGetValueSensor.getMode().equals(SC.ANALOG) ) {
            this.sb.append("\"analog_inputs\", Float32MultiArray");
        }
        this.sb.append(").data[").append(configurationAst.getConfigurationComponent(pinGetValueSensor.getUserDefinedPort()).getComponentProperties().get("OUTPUT")).append("-1]");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.sb.append("rospy.wait_for_message(\"bumper\", Bool).data");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        this.sb.append("_getDistance(")
            .append(port)
            .append(")");
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(RobotinoMethods.SETDIGITALPIN))
            .append("(")
            .append(configurationAst.getConfigurationComponent(pinWriteValueAction.port).getComponentProperties().get("INPUT"))
            .append(", ");
        pinWriteValueAction.value.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitColourBlob(ColourBlob colourBlob) {
        return null;
    }

    @Override
    public Void visitCameraThreshold(CameraThreshold cameraThreshold) {
        return null;
    }

    @Override
    public Void visitOpticalSensor(OpticalSensor opticalSensor) {
        return null;
    }


}
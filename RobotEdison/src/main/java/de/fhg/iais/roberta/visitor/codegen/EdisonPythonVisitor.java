package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.codegen.HelperMethodGenerator;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.*;
import de.fhg.iais.roberta.syntax.lang.functions.*;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IEdisonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

import java.util.ArrayList;
import java.util.Set;

/**
 * This class visits the Blockly blocks for the Edison robot and translates them into EdPy Python2 code (https://github.com/Bdanilko/EdPy)
 * Many methods are not supported (N O P) because the Edison robot does not allow the import of any Python module (f.e. math).
 * Also the edison robot only supports integers and has no suport for nested statements. (f.e. "if (a and b):" with a,b being booleans)
 */
public class EdisonPythonVisitor extends AbstractPythonVisitor implements IEdisonVisitor<Void> {

    protected final Configuration brickConfig;
    protected final Set<UsedSensor> usedSensors;
    protected final Set<UsedActor> usedActors;
    protected final Set<EdisonUsedHardwareCollectorVisitor.Method> usedMethods;
    protected ILanguage lang;
    private String newLine = System.getProperty("line.separator");
    private int soundFileName = 0;

    /**
     * initialize the Python code generator visitor.
     * The UsedHardwareCollector will provide the Python code generator with the used methods, used sensors/actors and used variables.
     * The used sensors/actors are disregarded in the edison robot, since all sensors/actors are fixed.
     * The used methods are collected by the UsedHardwareCollector and then saved as a Set, so that they can be appended in the suffix
     *
     * @param brickConfig    hardware configuration of the robot (fixed)
     * @param programPhrases to generate the code from
     * @param indentation    to start with. Will be incremented/decremented depending on block structure
     * @param language       the language
     */
    public EdisonPythonVisitor(Configuration brickConfig, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation, ILanguage language,
        HelperMethodGenerator helperMethodGenerator) {
        super(programPhrases, indentation, helperMethodGenerator, new EdisonUsedMethodCollectorVisitor(programPhrases));
        this.brickConfig = brickConfig;

        EdisonUsedHardwareCollectorVisitor checker = new EdisonUsedHardwareCollectorVisitor(programPhrases, brickConfig);
        this.usedSensors = checker.getUsedSensors();
        this.usedActors = checker.getUsedActors();
        this.usedMethods = checker.getUsedMethods();
        this.usedGlobalVarInFunctions = checker.getMarkedVariablesAsGlobal();
        this.lang = language;
        this.loopsLabels = checker.getloopsLabelContainer();
    }

    /**
     * Generates the program prefix, i.e. all preparations that need to be executed before Blockly generated code is reached
     *
     * @param withWrapping if the source code should be wrapped by prefix/suffix
     */
    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping )
            return;

        this.sb.append("import Ed"); nlIndent();
        this.sb.append("Ed.EdisonVersion = Ed.V2"); nlIndent();
        this.sb.append("Ed.DistanceUnits = Ed.CM"); nlIndent();
        this.sb.append("Ed.Tempo = Ed.TEMPO_SLOW"); nlIndent();
        this.sb.append("obstacleDetectionOn = False"); nlIndent(); //nur wenn benötigt
        this.sb.append("Ed.LineTrackerLed(Ed.ON)"); nlIndent(); //nur wenn benötigt
        this.sb.append("Ed.ReadClapSensor()"); nlIndent(); //zur Sicherheit -- um den Sensor zurückzusetzen
        this.sb.append("Ed.ReadLineState()"); nlIndent();
        this.sb.append("Ed.TimeWait(250, Ed.TIME_MILLISECONDS)"); //möglicherweise überflüssig
    }

    /**
     * Generates the program suffix, i.e. everything that will be appended to the very end of the source .py file
     * In the suffix are NEPO helper methods for things like sum, round, min, max, etc
     *
     * @param withWrapping if the source code should be wrapped by prefix/suffix
     */
    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if (!withWrapping) {
            return;
        }

        decrIndentation(); nlIndent(); //new line for helper methods
        nlIndent();
        this.sb.append("def shorten(num): return ((num+5)/10)"); nlIndent(); //This method is used so often that it comes with every program

        for ( EdisonUsedHardwareCollectorVisitor.Method m : this.usedMethods) {
            this.sb.append(newLine);

            switch (m) {
                case AVG:
                    this.sb.append("def avg(list):");
                    incrIndentation();
                    nlIndent();

                    this.sb.append("returnValue = sum(list) / len(list)");
                    nlIndent();
                    this.sb.append("return returnValue");
                    decrIndentation();
                    nlIndent();
                    break;
                case SUM:
                    this.sb.append("def sum(list):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("sum_of_list = 0");
                    nlIndent();
                    this.sb.append("listLength = len(list)");
                    nlIndent();
                    this.sb.append("for i in range(listLength): sum_of_list = (sum_of_list + list[i])");
                    nlIndent();
                    this.sb.append("return sum_of_list");
                    decrIndentation();
                    nlIndent();
                    break;
                case MIN:
                    this.sb.append("def min(list):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("min_of_list = list[0]");
                    nlIndent();
                    this.sb.append("listLength = len(list)");
                    nlIndent();
                    this.sb.append("for i in range(listLength):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("if list[i] < min_of_list:");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("min_of_list = list[i]");
                    decrIndentation();
                    decrIndentation();
                    nlIndent();
                    this.sb.append("return min_of_list");
                    decrIndentation();
                    nlIndent();
                    break;
                case MAX:
                    this.sb.append("def max(list):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("max_of_list = list[0]");
                    nlIndent();
                    this.sb.append("listLength = len(list)");
                    nlIndent();
                    this.sb.append("for i in range(listLength):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("if list[i] > max_of_list:");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("max_of_list = list[i]");
                    decrIndentation();
                    decrIndentation();
                    nlIndent();
                    this.sb.append("return max_of_list");
                    decrIndentation();
                    nlIndent();
                    break;
                case CREATE_REPEAT:
                    this.sb.append("def create_repeat(item, times):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("list = Ed.List(times)");
                    nlIndent();
                    this.sb.append("listLength = len(list)");
                    nlIndent();
                    this.sb.append("for i in range(listLength): list[i] = item");
                    nlIndent();
                    this.sb.append("return list");
                    decrIndentation();
                    nlIndent();
                    break;
                case PRIME:
                    this.sb.append("def isPrime(number):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("if number <= 1: return False");
                    nlIndent();
                    this.sb.append("newNum = number - 2");
                    nlIndent();
                    this.sb.append("for x in range(newNum):");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("y = (x + 2)");
                    nlIndent();
                    this.sb.append("if (number % y) == 0: return False");
                    decrIndentation();
                    nlIndent();
                    this.sb.append("return True");
                    decrIndentation();
                    nlIndent();
                    break;
                case OBSTACLEDETECTION:
                    this.sb.append("def obstacle_detection(mode):");
                    incrIndentation(); nlIndent();
                    this.sb.append("global obstacleDetectionOn");
                    nlIndent();
                    this.sb.append("if (obstacleDetectionOn == False):");
                    incrIndentation(); nlIndent();
                    this.sb.append("Ed.ObstacleDetectionBeam(Ed.ON)");
                    nlIndent();
                    this.sb.append("obstacleDetectionOn = True");
                    decrIndentation(); nlIndent();
                    this.sb.append("return Ed.ReadObstacleDetection() == mode");
                    decrIndentation(); nlIndent();
                    break;
                case IRSEND:
                    this.sb.append("def ir_send(payload):");
                    incrIndentation(); nlIndent();
                    this.sb.append("global obstacleDetectionOn");
                    nlIndent();
                    this.sb.append("if (obstacleDetectionOn == True):");
                    incrIndentation(); nlIndent();
                    this.sb.append("Ed.ObstacleDetectionBeam(Ed.OFF)");
                    nlIndent();
                    this.sb.append("obstacleDetectionOn = False");
                    decrIndentation(); nlIndent();
                    this.sb.append("Ed.SendIRData(payload)");
                    decrIndentation(); nlIndent();
                    break;
                case IRSEEK:
                    //mode: 0 = edison IR data; 1 = remote control data
                    this.sb.append("def ir_seek(mode):");
                    incrIndentation(); nlIndent();
                    this.sb.append("global obstacleDetectionOn");
                    nlIndent();
                    this.sb.append("if (obstacleDetectionOn == True):");
                    incrIndentation(); nlIndent();
                    this.sb.append("Ed.ObstacleDetectionBeam(Ed.OFF)");
                    nlIndent();
                    this.sb.append("obstacleDetectionOn = False");
                    decrIndentation(); nlIndent();
                    this.sb.append("if (mode == 0): return Ed.ReadIRData()");
                    nlIndent();
                    this.sb.append("elif (mode == 1): return Ed.ReadRemote()");
                    decrIndentation(); decrIndentation(); nlIndent();
                    break;
                case MOTORON:
                    //motor: 0 = LMOTOR; 1 = RMOTOR
                    //distance: Entweder integer oder Ed.DISTANCE_UNLIMITED
                    this.sb.append("def motor_on(motor, power, distance):");
                    incrIndentation(); nlIndent();
                    this.sb.append("if (motor == 0):");
                    incrIndentation(); nlIndent();
                    this.sb.append("if (power < 0): Ed.DriveLeftMotor(Ed.BACKWARD, -shorten(power), distance)");
                    nlIndent();
                    this.sb.append("else: Ed.DriveLeftMotor(Ed.FORWARD, shorten(power), distance)");
                    decrIndentation(); nlIndent();
                    this.sb.append("if (motor == 1):");
                    incrIndentation(); nlIndent();
                    this.sb.append("if (power < 0): Ed.DriveRightMotor(Ed.BACKWARD, -shorten(power), distance)");
                    nlIndent();
                    this.sb.append("else: Ed.DriveRightMotor(Ed.FORWARD, shorten(power), distance)");
                    decrIndentation(); decrIndentation(); nlIndent();
                    break;
                case ROUND:
                    this.sb.append("def round(num): return ((num+5)/10)*10");
                    nlIndent();
                    break;
                case ROUND_UP:
                    this.sb.append("def round_up(num): return ((num/10)+1)*10");
                    nlIndent();
                    break;
                case ROUND_DOWN:
                    this.sb.append("def round_down(num): return (num/10)");
                    nlIndent();
                    break;
                case ABSOLUTE:
                    this.sb.append("def absolute(num):");
                    incrIndentation(); nlIndent();
                    this.sb.append("if (num<0): return -num");
                    nlIndent();
                    this.sb.append("else: return num");
                    decrIndentation(); nlIndent();
                    break;
                case POW10:
                    this.sb.append("def pow10(num):");
                    incrIndentation(); nlIndent();
                    this.sb.append("powered = 10");
                    nlIndent();
                    this.sb.append("newNum = num-1");
                    nlIndent();
                    this.sb.append("for _temp_x in range(newNum):");
                    incrIndentation(); nlIndent();
                    this.sb.append("powered = powered * 10");
                    decrIndentation(); nlIndent();
                    this.sb.append("return powered");
                    decrIndentation(); nlIndent();
                    break;
                case CURVE:
                    this.sb.append("def read_dist(leftspeed, rightspeed):");
                    incrIndentation(); nlIndent();
                    this.sb.append("if (leftspeed > rightspeed):");
                    incrIndentation(); nlIndent();
                    this.sb.append("return Ed.ReadDistance(Ed.MOTOR_LEFT)");
                    decrIndentation(); nlIndent();
                    this.sb.append("else:");
                    incrIndentation(); nlIndent();
                    this.sb.append("return Ed.ReadDistance(Ed.MOTOR_RIGHT)");
                    decrIndentation(); decrIndentation(); nlIndent();
                    break;
                case DIFFDRIVE:
                    this.sb.append("def diff_drive(direction, speed, distance):");
                    incrIndentation(); nlIndent();
                    this.sb.append("if speed < 0:");
                    incrIndentation(); nlIndent();
                    this.sb.append("speed = -speed");
                    nlIndent();
                    this.sb.append("if direction == Ed.FORWARD: Ed.Drive(Ed.BACKWARD, shorten(speed), distance)");
                    nlIndent();
                    this.sb.append("else: Ed.Drive(Ed.FORWARD, shorten(speed), distance)");
                    decrIndentation(); nlIndent();
                    this.sb.append("else: Ed.Drive(direction, shorten(speed), distance)");
                    decrIndentation(); nlIndent();
                case DIFFTURN:
                    this.sb.append("def diff_turn(direction, speed, degree):");
                    incrIndentation(); nlIndent();
                    this.sb.append("if speed < 0:");
                    incrIndentation(); nlIndent();
                    this.sb.append("speed = -speed");
                    nlIndent();
                    this.sb.append("if direction == Ed.SPIN_RIGHT: Ed.Drive(Ed.SPIN_LEFT, shorten(speed), degree)");
                    nlIndent();
                    this.sb.append("else: Ed.Drive(Ed.SPIN_RIGHT, shorten(speed), degree)");
                    decrIndentation(); nlIndent();
                    this.sb.append("else: Ed.Drive(direction, shorten(speed), degree)");
                    decrIndentation(); nlIndent();
                default:
                    break;
            }
        }
    }

    /**
     * Generates source code from a brick configuration and a Blockly program.
     * This method is needed because {@link AbstractPythonVisitor#generateCode(boolean)} and {AbstractPythonVisitor#generateProgramMainBody()} are protected.
     *
     * @param brickCfg       the brick configuration
     * @param programPhrases the program to generate the code from
     * @param withWrapping   wrap the code with prefix/suffix
     * @param language       the locale
     * @return the source code as a String
     */
    public static String generate(
        Configuration brickCfg, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping, ILanguage language,
        HelperMethodGenerator helperMethodGenerator) {
        Assert.notNull(brickCfg);

        EdisonPythonVisitor visitor = new EdisonPythonVisitor(brickCfg, programPhrases, 0, language, helperMethodGenerator);
        visitor.generateCode(withWrapping);

        return visitor.sb.toString();
    }




    // --------------------- unsupported methods --------------------- \\




    /**
     * visit a {@link ConnectConst}.
     * @param connectConst to be visited
     */
    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        //not needed I guess.. (idk what this does) NOP
        return null;
    }

    /**
     * Function to print() text
     * visit a {@link TextPrintFunct}.
     *
     * @param textPrintFunct to be visited
     */
    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        //NOP not needed in Edison
        throw new DbcException("Not supported!");
    }

    /**
     * Function to get a sublist from a list
     * visit a {@link GetSubFunct}.
     *
     * @param getSubFunct to be visited
     */
    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        //NOP Not supported by Edison
        throw new DbcException("Not supported!");
    }

    /**
     * Function to constrain a number (number is between MIN and MAX)
     * visit a {@link MathConstrainFunct}.
     *
     * @param mathConstrainFunct to be visited
     */
    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        //NOP
        throw new DbcException("Not supported!");
    }

    /**
     * Function to get a random float between 0 and 1
     * visit a {@link MathRandomFloatFunct}.
     *
     * @param mathRandomFloatFunct
     */
    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        //NOP not supported by Edison robot
        throw new DbcException("Not supported!");
    }

    /**
     * Function to get a random integer between MIN and MAX
     * visit a {@link MathRandomIntFunct}.
     *
     * @param mathRandomIntFunct to be visited
     */
    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        //NOP not supported by Edison robot
        throw new DbcException("Not supported!");
    }

    /**
     * Function to append text
     * visit a {@link TextJoinFunct}.
     *
     * @param textJoinFunct to be visited
     */
    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        //NOP
        throw new DbcException("Not supported!");
    }

    /**
     * Function to get the index of the first occurrence of an element in a list
     *
     * @param indexOfFunct to be visited
     * @return
     */
    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        //NOP
        throw new DbcException("Not supported!");
    }

    /**
     * Function to get a math constant.
     * This block has been removed from the Edison toolbox and is only here for legacy reasons and compatibility with the common robot tests.
     * visit a {@link MathConst}.
     *
     * @param mathConst to be visited
     */
    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        //NOP
        throw new DbcException("Not supported!");
    }




    // --------------------- supported methods --------------------- \\




    /**
     * Visits the repeat statement ("controls_repeat_ext")
     *
     * @param repeatStmt to be visited
     * @return
     */
    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                appendTry();
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                appendTry();
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                appendTry();
                break;
            default:
                throw new DbcException("Invalid Repeat Statement!");
        }
        incrIndentation();
        appendPassIfEmptyBody(repeatStmt);
        repeatStmt.getList().visit(this);
        if ( !isWaitStmt ) {
            appendExceptionHandling();
        } else {
            appendBreakStmt(repeatStmt);
        }
        decrIndentation();
        return null;
    }


    /**
     * Visit the block "controls_repeat_ext"
     */
    @Override
    protected void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(whitespace());
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        this.sb.append(whitespace() + "in range(");
        expressions.get().get(2).visit(this);
        this.sb.append("):");
    }

    /**
     * visit a {@link GetSampleSensor}
     *
     * @param sensorGetSample to be visited
     */
    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        this.sb.append("(");
        sensorGetSample.getSensor().visit(this);
        this.sb.append(")");
        return null;
    }

    /**
     * Function to get readings from the obstacle detector.
     * visit a {@link InfraredSensor} for the block "robSensors_infrared_getSample"
     * @param infraredSensor to be visited
     */
    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append("obstacle_detection(");

        switch (infraredSensor.getPort()) {
            case "FRONT":
                this.sb.append("Ed.OBSTACLE_AHEAD");
                break;
            case "LEFT":
                this.sb.append("Ed.OBSTACLE_LEFT");
                break;
            case "RIGHT":
                this.sb.append("Ed.OBSTACLE_RIGHT");
                break;
        }

        this.sb.append(")");
        return null;
    }

    /**
     * Function to receive IR data
     * Needs a helper method
     * visit a {@link InfraredSensor} for the block "robSensors_irseeker_getSample"
     *
     * @param irSeekerSensor
     */
    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        switch (irSeekerSensor.getMode()) {
            case "RCCODE":
                this.sb.append("ir_seek(1)");
                break;
            case "EDISON_CODE":
                this.sb.append("ir_seek(0)");
                break;
        }
        return null;
    }

    /**
     * Function to get the light level from a phototransistor/light sensor
     * The light level reported from Edison is between 0 and 32767, but the return value is in percent.
     * That's why it is divided by 32767.
     * The line tracker can also report if the robot is on a line, but for this to work the robot has to be placed on a white surface when the program starts.
     *
     * visit a {@link LightSensor} for the block "robSensors_light_getSample"
     *
     * @param lightSensor
     */
    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        switch (lightSensor.getPort()) {
            case "LLIGHT":
                this.sb.append("Ed.ReadLeftLightLevel() / 32767 * 100");
                break;
            case "RLIGHT":
                this.sb.append("Ed.ReadRightLightLevel() / 32767 * 100");
                break;
            case "LINETRACKER":
                if (lightSensor.getMode().equals("LINE")) {
                    this.sb.append("Ed.LineState() == Ed.LINE_ON_BLACK");
                } else {
                    this.sb.append("Ed.ReadLineTracker() / 32767 * 100");
                }
                break;
        }

        return null;
    }

    /**
     * Function to detect claps from the sound sensor
     * visit a {@link SoundSensor} for the block "robSensors_sound_getSample"
     *
     * @param soundSensor
     */
    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("Ed.ReadClapSensor() == Ed.CLAP_DETECTED");
        return null;
    }

    /**
     * Function to send data via infrared
     * visit a {@link SendIRAction} for the block "edisonCommunication_ir_sendBlock"
     *
     * @param sendIRAction
     * @return
     */
    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.sb.append("ir_send(");
        sendIRAction.getCode().visit(this);
        this.sb.append(")");
        return null;
    }

    /**
     * Function to receive data via infrared
     * visit a {@link SendIRAction} for the block "edisonCommunication_ir_receiveBlock"
     * @param receiveIRAction
     * @return
     */
    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.sb.append("ir_seek(0)");
        return null;
    }

    /**
     * Function to drive straight forward/backward with given power % and time/distance
     * visit a {@link DriveAction} for the block "robActions_motorDiff_on" and "robActions_motorDiff_on_for"
     *
     * @param driveAction to be visited
     */
    @Override public Void visitDriveAction(DriveAction<Void> driveAction) {
        String direction = "Ed.FORWARD";
        switch (driveAction.getDirection().toString()) {
            case "FOREWARD":
                break;
            case "BACKWARD":
                direction = "Ed.BACKWARD";
                break;
        }

        this.sb.append("diff_drive(" + direction + ", ");
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        if (driveAction.getParam().getDuration() != null) {
            driveAction.getParam().getDuration().getValue().visit(this);
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED");
        }

        this.sb.append(")");

        return null;
    }

    /**
     * Function to perform mathematics on a list (sum/average/min/max/...)
     * Visit a {@link MathOnListFunct} for the block "math_on_list"
     *
     * @param mathOnListFunct to be visited
     */
    @Override public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch (mathOnListFunct.getFunctName().getOpSymbol()) {
            case "SUM":
                this.sb.append("sum(");
                break;
            case "MIN":
                this.sb.append("min(");
                break;
            case "MAX":
                this.sb.append("max(");
                break;
            case "AVERAGE":
                this.sb.append("avg(");
                break;
            default:
                break;
        }

        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    /**
     * All Math blocks (Integers and Fractions) are checked here. If the number constant is not an integer an exception will be thrown
     * Only blocks of type "math_integer" should be used with the Edison robot
     *
     * @param numConst
     * @return
     */
    @Override public Void visitNumConst(NumConst<Void> numConst) {
        if ( isInteger(numConst.getValue()) ) {
            super.visitNumConst(numConst);
        } else {
            throw new IllegalArgumentException("Not an integer");
        }
        return null;
    }

    /**
     * Visits the programs main task, i.e. the Blockly-blocks that are (indirectly) connected to the red "robControls_start"-block.
     * In this method, user-generated Blockly-methods are appended also
     *
     * @param mainTask the main task class to be visited
     * @return null
     */
    @Override public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this); //fill usedGlobalVarInFunctions with values

        nlIndent();
        generateUserDefinedMethods(); //Functions created by the user will be defined before the main function


        return null;
    }

    /**
     * Function to check if a number is odd/even/positive/negative/...
     * visit a {@link MathNumPropFunct} for the block "math_number_property"
     *
     * @param mathNumPropFunct to be visited
     */
    @Override public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch (mathNumPropFunct.getFunctName()) {
            case EVEN:
                this.sb.append("((");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % 2) == 0)");
                break;
            case ODD:
                this.sb.append("((");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % 2) != 0)");
                break;
            case PRIME:
                this.sb.append("isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" >= 0)");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" < 0)");
                break;
            case DIVISIBLE_BY:
                this.sb.append("((");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(") == 0)");
                break;
            default:
                break;
        }

        return null;
    }

    /**
     * Function to create a list
     * visit a {@link ListCreate} for the block "robLists_create_with"
     *
     * @param listCreate to be visited
     */
    @Override public Void visitListCreate(ListCreate<Void> listCreate) {
        int listSize = listCreate.getValue().get().size();

        this.sb.append("Ed.List(").append(listSize).append((", ["));
        if (listSize == 0) {
            this.sb.append("]");
        } else {
            for (int i = 0; i < listSize; i++) {
                listCreate.getValue().get().get(i).visit(this);
                if (i < (listSize -1)) {
                    this.sb.append((","));
                } else {
                    this.sb.append("]");
                }
            }
        }
        this.sb.append(")");

        return null;
    }

    /**
     * Function to create a List with repeated item
     * visit a {@link ListRepeat} for the block "robLists_repeat"
     *
     * @param listRepeat to be visited
     */
    @Override public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("create_repeat(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");

        return null;
    }

    /**
     * Function to execute code when a key is pressed
     * visit a {@link KeysSensor} for the block "robSensors_key_getSample"
     *
     * @param keysSensor to be visited
     */
    @Override public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        switch (keysSensor.getPort()) {
            case "REC":
                this.sb.append("Ed.ReadKeypad() == Ed.KEYPAD_ROUND");
                break;
            case "PLAY":
                this.sb.append("Ed.ReadKeypad() == Ed.KEYPAD_TRIANGLE");
                break;
            default:
        }

        return null;
    }

    /**
     * //TODO-MAX komplett neu
     * Function to drive a curve forward/backward.
     * This is still very buggy when used together with sensors (e.g. drive a curve until an obstacle is detected).
     *
     * visit a {@link CurveAction} for the block "robActions_motorDiff_curve_for" and "robActions_motorDiff_curve"
     *
     * @param curveAction to visit
     */
    @Override public Void visitCurveAction(CurveAction<Void> curveAction) {
        String direction;

        //determine the direction
        switch (curveAction.getDirection().toString()) {
            default:
            case "FOREWARD":
                direction = "Ed.FORWARD";
                break;
            case "BACKWARD":
                direction = "Ed.BACKWARD";
                break;
        }

        //first, drive straight forward with the speed-% of the right motor
        //Then, directly set both motors to the correct speed-%
        this.sb.append("Ed.Drive(" + direction + ", shorten(");
        curveAction.getParamRight().getSpeed().visit(this);
        this.sb.append("), Ed.DISTANCE_UNLIMITED)");
        nlIndent();
        this.sb.append("Ed.DriveLeftMotor(" + direction + ", shorten(");
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append("), ");
        this.sb.append("Ed.DISTANCE_UNLIMITED)");
        nlIndent();
        this.sb.append("Ed.DriveRightMotor(" + direction + ", shorten(");
        curveAction.getParamRight().getSpeed().visit(this);
        this.sb.append("), ");
        this.sb.append("Ed.DISTANCE_UNLIMITED)");
        nlIndent();

        //If a duration in cm is wanted, set the distance for both motors
        //and then periodically read the distance until one of them is 0
        if (curveAction.getParamLeft().getDuration() != null) {
            this.sb.append("Ed.SetDistance(Ed.MOTOR_LEFT, ");
            curveAction.getParamLeft().getDuration().getValue().visit(this);
            this.sb.append(")");
            nlIndent();
            this.sb.append("Ed.SetDistance(Ed.MOTOR_RIGHT, ");
            curveAction.getParamRight().getDuration().getValue().visit(this);
            this.sb.append(")");
            nlIndent();
            this.sb.append("while read_dist(");
            curveAction.getParamLeft().getSpeed().visit(this);
            this.sb.append(", ");
            curveAction.getParamRight().getSpeed().visit(this);
            this.sb.append(") > 0:");
            incrIndentation(); nlIndent();
            this.sb.append("pass");
            decrIndentation(); nlIndent();
            this.sb.append("Ed.ResetDistance()");
        }

        return null;
    }

    /**
     * Function to turn the robot
     * visit a {@link TurnAction} for the block "robActions_motorDiff_turn_for" and "robActions_motorDiff_turn"
     *
     * @param turnAction to be visited
     */
    @Override public Void visitTurnAction(TurnAction<Void> turnAction) {
        this.sb.append("diff_turn(Ed.SPIN_" + turnAction.getDirection().toString() + ", ");
        turnAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        if (turnAction.getParam().getDuration() != null) {
            turnAction.getParam().getDuration().getValue().visit(this);
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED");
        }
        this.sb.append(")");

        return null;
    }

    /**
     * Function to set the motors to a specific power-%
     * visit a {@link MotorOnAction} for the block "robActions_motor_on" and "robActions_motor_on_for"
     *
     * @param motorOnAction
     */
    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        switch (motorOnAction.getUserDefinedPort()) {
            case "LMOTOR":
                this.sb.append("motor_on(0, ");
                motorOnAction.getParam().getSpeed().visit(this);
                break;
            case "RMOTOR":
                this.sb.append("motor_on(1, ");
                motorOnAction.getParam().getSpeed().visit(this);
                break;
            default:
                break;
        }
        this.sb.append(", ");

        if (motorOnAction.getDurationValue() != null) {
            motorOnAction.getDurationValue().visit(this);
            this.sb.append(")");
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED)");
        }

        return null;
    }

    /**
     * Function to stop individual motors
     * visit a {@link MotorStopAction} for the block "robActions_motor_stop"
     *
     * @param motorStopAction
     */
    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        switch (motorStopAction.getUserDefinedPort()) {
            case "B":
            case "LMOTOR":
                this.sb.append("Ed.DriveLeftMotor(Ed.STOP, Ed.SPEED_1, 1)");
                break;
            case "C":
            case "RMOTOR":
                this.sb.append("Ed.DriveRightMotor(Ed.STOP, Ed.SPEED_1, 1)");
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * Function to stop driving (stop both motors)
     * visit a {@link MotorDriveStopAction} for the block "robActions_motorDiff_stop"
     *
     * @param stopAction
     */
    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("Ed.Drive(Ed.STOP, Ed.SPEED_1, 1)");
        return null;
    }

    /**
     * Function to find out the length of a list
     * //NOP is empty not supported
     * visit a {@link LengthOfIsEmptyFunct} for the block "robLists_length"
     *
     * @param lengthOfIsEmptyFunct to be visited
     */
    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        this.sb.append("len(");
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);this.sb.append(")");
        return null;
    }

    /**
     * Function to change a single number, for the blocks "math_single" and "math_round"
     *
     * @param mathSingleFunct
     * @return
     */
    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ABS:
                this.sb.append("absolute(");
                break;
            case POW10:
                this.sb.append("pow10(");
                break;
            case ROUND:
                this.sb.append("round(");
                break;
            case ROUNDUP:
                this.sb.append("round_up(");
                break;
            case ROUNDDOWN:
                this.sb.append("round_down(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");

        return null;
    }

    /**
     * Visits the Blockly wait-until-block ("robControls_wait_for")
     *
     * @param waitStmt to be visited
     * @return null
     */
    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("pass");
        decrIndentation();
        return null;
    }

    /**
     * Visits the Blockly wait-block (with user-defined waiting time): "robControls_wait_time"
     *
     * @param waitTimeStmt
     * @return null
     */
    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("Ed.TimeWait(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(", Ed.TIME_MILLISECONDS)");
        return null;
    }

    /**
     * Function to get the n-th element of a list
     * visit a {@link ListGetIndex} for the block "robLists_getIndex"
     *
     * @param listGetIndex to be visited
     */
    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().get(0).visit(this); //Name of list
        this.sb.append("[");
        listGetIndex.getParam().get(1).visit(this); //index (from 0)
        this.sb.append(" - 1]");
        return null;
    }

    /**
     * Function to set the n-th element of a List or insert the element at the n-th place (if supported)
     * visit a {@link ListSetIndex} for the block "robLists_setIndex"
     *
     * @param listSetIndex to be visited
     */
    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        listSetIndex.getParam().get(0).visit(this); //Name of list
        this.sb.append("[");
        listSetIndex.getParam().get(2).visit(this);
        this.sb.append("] = ");
        listSetIndex.getParam().get(1).visit(this);
        return null;
    }

    /**
     * Function to turn on the LEDs
     * visit a {@link LightAction} for the block "robActions_led_on"
     *
     * @param lightAction to be visited
     */
    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        switch (lightAction.getPort()) {
            case "RLED":
                this.sb.append("Ed.RightLed(Ed.ON)");
                break;
            case "LLED":
                this.sb.append("Ed.LeftLed(Ed.ON)");
                break;
            default:
                break;
        }

        return null;
    }

    /**
     * Function to turn off the LEDs
     * visit a {@link LightStatusAction} for the block "robActions_led_off"
     *
     * @param lightStatusAction to be visited
     */
    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch (lightStatusAction.getPort()) {
            case "1":
            case "RLED":
                this.sb.append("Ed.RightLed(Ed.OFF)");
                break;
            case "2":
            case "LLED":
                this.sb.append("Ed.LeftLed(Ed.OFF)");
                break;
            default:
                this.sb.append("Ed.LeftLed(Ed.OFF) #there is an error in your program");
                this.sb.append("Ed.RightLed(Ed.OFF)");
                break;
        }

        return null;
    }

    /**
     * Function to play a tone
     * visit a {@link ToneAction} for the block "robActions_play_tone"
     *
     * @param toneAction to be visited
     */
    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("Ed.PlayTone(8000000/");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(")"); nlIndent();
        this.sb.append("Ed.TimeWait(");
        toneAction.getDuration().visit(this);
        this.sb.append(", Ed.TIME_MILLISECONDS)");
        return null;
    }

    /**
     * Function to play a note
     * visit a {@link PlayNoteAction} for the block "mbedActions_play_note"
     *
     * @param playNoteAction
     */
    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("Ed.PlayTone(4000000/"); //eigentlich 8mio aber die zahlen bei tiefen noten werden zu groß für edison
        this.sb.append(Integer.parseInt(playNoteAction.getFrequency().split("\\.")[0]));
        this.sb.append(", " + playNoteAction.getDuration() + ")");
        nlIndent();
        this.sb.append("Ed.TimeWait(" + playNoteAction.getDuration() + ", Ed.TIME_MILLISECONDS)");

        return null;
    }

    /**
     * //TODO-MAX shorter sound files
     * Function to play a sound file/note file
     * visit a {@link PlayFileAction} for the block "robActions_play_file"
     *
     * @param playFileAction
     */
    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        switch (playFileAction.getFileName().toLowerCase()) {
            case "0":
                this.sb.append("___soundfile1 = Ed.TuneString(7,\"c8e8g8z\")"); //positiv
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile1)");
                break;
            case "1":
//                this.sb.append("___soundfile2 = Ed.TuneString(35, \"c4d8e4d4f4e4d8c8d4a4g4f4e4d4e8c8g2z\")"); //dt. Nationalhymne
//                nlIndent();
//                this.sb.append("Ed.PlayTune(___soundfile2)");
//                break;
                this.sb.append("___soundfile2 = Ed.TuneString(7,\"g8e8c8z\")"); //negativ
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile2)");
                break;
            case "2":
                this.sb.append("___soundfile3 = Ed.TuneString(13,\"g4c4g4c4g4c4z\")"); //warnung
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile3)");
                break;
            case "3":
                this.sb.append("___soundfile4 = Ed.TuneString(21, \"c8e8f4g4o4g4b4g8e4n2z\")"); //MP Tune
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile4)");
                break;
            case "4":
                this.sb.append("___soundfile5 = Ed.TuneString(55, \"c4d4e4f4g2g2a4a4a4a4g2R2a4a4a4a4g2f4f4f4e2e2g4g4g4g4c1z\")"); //Alle meine Entchen
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile5)");
                break;
        }

        nlIndent();
        this.sb.append("while (Ed.ReadMusicEnd() == Ed.MUSIC_NOT_FINISHED):");
        incrIndentation();
        nlIndent();
        this.sb.append("pass");
        decrIndentation();
        this.soundFileName++;

        return null;
    }

    /**
     * Function to reset the sensors
     * visit a {@link ResetSensor} for the block "edisonSensors_sensor_reset"
     *
     * @param resetSensor
     * @return
     */
    @Override public Void visitSensorResetAction(ResetSensor<Void> resetSensor) {
        switch (resetSensor.getSensor()) {
            case "OBSTACLEDETECTOR":
                this.sb.append("Ed.ReadObstacleDetection()");
                break;
            case "KEYPAD":
                this.sb.append("Ed.ReadKeypad()");
                break;
            case "SOUND":
                this.sb.append("Ed.ReadClapSensor()");
                break;
            case "RCCODE":
            case "IRCODE":
                this.sb.append("Ed.ReadRemote()");
                nlIndent();
                this.sb.append("Ed.ReadIRData()");
                break;
            default:
                break;
        }

        return null;
    }
}
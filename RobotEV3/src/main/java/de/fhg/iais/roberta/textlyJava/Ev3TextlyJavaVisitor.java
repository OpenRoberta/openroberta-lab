package de.fhg.iais.roberta.textlyJava;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyJavaVisitor;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.light.BrickLightOffAction;
import de.fhg.iais.roberta.syntax.action.light.BrickLightOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetBias;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetWeight;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassCalibrate;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.BlocklyRegion;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.ast.TextRegion;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;

public class Ev3TextlyJavaVisitor<T> extends CommonTextlyJavaVisitor<T> {
    private static final Map<String, String> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put("#blue", "#0057a6");
        COLOR_MAP.put("#gray", "#585858");
        COLOR_MAP.put("#black", "#000000");
        COLOR_MAP.put("#yellow", "#f7d117");
        COLOR_MAP.put("#green", "#00642e");
        COLOR_MAP.put("#red", "#b30006");
        COLOR_MAP.put("#white", "#ffffff");
        COLOR_MAP.put("#brown", "#532115");
    }
    @Override
    public T visitCol(TextlyJavaParser.ColContext ctx) {
        String colorText = ctx.COLOR().getText().toLowerCase();
        if ( colorText.startsWith("#rgb(") && colorText.endsWith(")") ) {
            colorText = colorText.substring(5, colorText.length() - 1);
            colorText = "#" + colorText;
        }

        String colorHex = COLOR_MAP.getOrDefault(colorText, colorText);
        if ( !COLOR_MAP.containsValue(colorHex) ) {
            Expr result = new EmptyExpr(BlocklyType.NUMBER);
            result.addTextlyError("This Colour is not supported for Ev3", true);
            return (T) result;
        }

        return (T) new ColorConst(mkInlineProperty(ctx, "robColour_picker"), colorHex);
    }

    @Override
    public T visitEv3SensorExpr(TextlyJavaParser.Ev3SensorExprContext ctx) {
        String sensor = ctx.start.getText();
        ExprList parameters = new ExprList();

        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        Mutation mutation = new Mutation();

        switch ( sensor ) {
            case "getSpeedMotor":
                String portMotor = PortsEv3.getPort(ctx.NAME(0).getText());
                T resultValidatePortMotor = validateSensorPortName(ctx, portMotor);
                if ( resultValidatePortMotor != null ) return resultValidatePortMotor;
                MotorGetPowerAction motorGetPowerAction = new MotorGetPowerAction(mkPropertyFromClass(ctx, MotorGetPowerAction.class), portMotor);
                ActionExpr actionExprMotor = new ActionExpr(motorGetPowerAction);
                return (T) actionExprMotor;

            case "getVolume":
                GetVolumeAction getVolumeAction = new GetVolumeAction(mkExternalProperty(ctx, "robActions_play_getVolume"), "- EMPTY_PORT -", null);
                ActionExpr actionExprVolume = new ActionExpr(getVolumeAction);
                return (T) actionExprVolume;

            case "touchSensor":
                String portTouch = ctx.INT().getText();
                T resultValidatedPortTouch = validateSensorPortNumber(ctx, portTouch);
                if ( resultValidatedPortTouch != null ) return resultValidatedPortTouch;
                mutation.setMode("PRESSED");
                ExternalSensorBean externalSensorBeanTouch = new ExternalSensorBean(portTouch, "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                SensorExpr sensorExprTouch = new SensorExpr(new TouchSensor(mkPropertyFromClass(ctx, TouchSensor.class), externalSensorBeanTouch));
                return (T) sensorExprTouch;

            case "ultrasonicSensor":
                String portUltra = ctx.INT().getText();
                T resultValidatePortUltra = validateSensorPortNumber(ctx, portUltra);
                if ( resultValidatePortUltra != null ) return resultValidatePortUltra;
                if ( ctx.children.get(2).getText().equals("getDistance") ) {
                    mutation.setMode("DISTANCE");
                    ExternalSensorBean externalSensorBeanUltra = new ExternalSensorBean(portUltra, "DISTANCE", "- EMPTY_SLOT -", mutation, listHide);
                    SensorExpr sensorExprUltra = new SensorExpr(new UltrasonicSensor(mkPropertyFromClass(ctx, UltrasonicSensor.class), externalSensorBeanUltra));
                    return (T) sensorExprUltra;
                } else {
                    mutation.setMode("PRESENCE");
                    ExternalSensorBean externalSensorBeanUltra = new ExternalSensorBean(portUltra, "PRESENCE", "- EMPTY_SLOT -", mutation, listHide);
                    SensorExpr sensorExprUltra = new SensorExpr(new UltrasonicSensor(mkPropertyFromClass(ctx, UltrasonicSensor.class), externalSensorBeanUltra));
                    return (T) sensorExprUltra;
                }

            case "colorSensor":
                String portColor = ctx.INT().getText();
                T resultValidatePortColor = validateSensorPortNumber(ctx, portColor);
                if ( resultValidatePortColor != null ) return resultValidatePortColor;

                String modeColorSensor = ColorModeEv3.getMode(ctx.NAME(0).getText());
                T resulValidateModeColor = validateSensorModes(ctx, modeColorSensor);
                if ( resulValidateModeColor != null ) return resulValidateModeColor;
                mutation.setMode(modeColorSensor);
                ExternalSensorBean externalSensorBeanColor = new ExternalSensorBean(portColor, modeColorSensor, "- EMPTY_SLOT -", mutation, listHide);
                ColorSensor colorSensor = new ColorSensor(mkInlineProperty(ctx, "robSensors_colour_getSample"), externalSensorBeanColor);
                SensorExpr sensorExprColor = new SensorExpr(colorSensor);

                return (T) sensorExprColor;

            case "infraredSensor":
                String portInfrared = ctx.INT().getText();
                T resultValidatePortInfrared = validateSensorPortNumber(ctx, portInfrared);
                if ( resultValidatePortInfrared != null ) return resultValidatePortInfrared;
                if ( ctx.children.get(2).getText().equals("getDistance") ) {
                    mutation.setMode("DISTANCE");
                    ExternalSensorBean externalSensorBeanInfra = new ExternalSensorBean(portInfrared, "DISTANCE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new InfraredSensor(mkInlineProperty(ctx, "robSensors_infrared_getSample"), externalSensorBeanInfra));
                } else {
                    mutation.setMode("PRESENCE");
                    ExternalSensorBean externalSensorBeanInfra = new ExternalSensorBean(portInfrared, "PRESENCE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new InfraredSensor(mkInlineProperty(ctx, "robSensors_infrared_getSample"), externalSensorBeanInfra));
                }

            case "encoderSensor":
                String portEncoder = ctx.NAME(0).getText();
                T resultValidatePortEncoder = validateSensorPortName(ctx, portEncoder);
                if ( resultValidatePortEncoder != null ) return resultValidatePortEncoder;
                if ( ctx.children.get(2).getText().equals("getDegree") ) {
                    mutation.setMode("DEGREE");
                    ExternalSensorBean externalSensorBeanEncoder = new ExternalSensorBean(portEncoder, "DEGREE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new EncoderSensor(mkPropertyFromClass(ctx, EncoderSensor.class), externalSensorBeanEncoder));
                } else if ( ctx.children.get(2).getText().equals("getRotation") ) {
                    mutation.setMode("ROTATION");
                    ExternalSensorBean externalSensorBeanEncoder = new ExternalSensorBean(portEncoder, "ROTATION", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new EncoderSensor(mkPropertyFromClass(ctx, EncoderSensor.class), externalSensorBeanEncoder));
                } else {
                    mutation.setMode("DISTANCE");
                    ExternalSensorBean externalSensorBeanEncoder = new ExternalSensorBean(portEncoder, "DISTANCE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new EncoderSensor(mkPropertyFromClass(ctx, EncoderSensor.class), externalSensorBeanEncoder));
                }

            case "keysSensor":
                String portKey = Ev3KeyPorts.getAstName(ctx.NAME(0).getText());
                T resultValidatePortKey = validateSensorPortName(ctx, portKey);
                if ( resultValidatePortKey != null ) return resultValidatePortKey;
                mutation.setMode("PRESSED");
                ExternalSensorBean externalSensorBeanKey = new ExternalSensorBean(portKey, "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new KeysSensor(mkInlineProperty(ctx, "robSensors_key_getSample"), externalSensorBeanKey));

            case "gyroSensor":
                String portGyro = ctx.INT().getText();
                T resultValidatePortGyro = validateSensorPortNumber(ctx, portGyro);
                if ( resultValidatePortGyro != null ) return resultValidatePortGyro;
                if ( ctx.children.get(2).getText().equals("getAngle") ) {
                    mutation.setMode("ANGLE");
                    ExternalSensorBean externalSensorBeanGyro = new ExternalSensorBean(portGyro, "ANGLE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new GyroSensor(mkPropertyFromClass(ctx, GyroSensor.class), externalSensorBeanGyro));
                } else {
                    mutation.setMode("RATE");
                    ExternalSensorBean externalSensorBeanGyro = new ExternalSensorBean(portGyro, "RATE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new GyroSensor(mkPropertyFromClass(ctx, GyroSensor.class), externalSensorBeanGyro));
                }
            case "timerSensor":
                String portTimer = ctx.INT().getText();
                if ( portTimer.matches("[1-5]") ) {
                    mutation.setMode("VALUE");
                    ExternalSensorBean externalSensorBeanPinTimer = new ExternalSensorBean(portTimer, "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new TimerSensor(mkInlineProperty(ctx, "robSensors_timer_getSample"), externalSensorBeanPinTimer));
                } else {
                    Expr result = new EmptyExpr(BlocklyType.NOTHING);
                    result.addTextlyError("Invalid Port for this sensor: " + ctx.INT().getText() + " the port should be 1,2,3,4 or 5", true);
                    return (T) result;
                }

            case "soundSensor":
                String portSound = ctx.INT().getText();
                T resultValidatePortSound = validateSensorPortName(ctx, portSound);
                if ( resultValidatePortSound != null ) return resultValidatePortSound;
                mutation.setMode("SOUND");
                ExternalSensorBean externalSensorBeanSound = new ExternalSensorBean(portSound, "SOUND", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new SoundSensor(mkInlineProperty(ctx, "robSensors_sound_getSample"), externalSensorBeanSound));

            case "hiTechCompassSensor":
                String portHiTechCompass = ctx.INT().getText();
                T resultValidatePortHiTechCompass = validateSensorPortName(ctx, portHiTechCompass);
                if ( resultValidatePortHiTechCompass != null ) return resultValidatePortHiTechCompass;
                if ( ctx.children.get(2).getText().equals("getAngle") ) {
                    mutation.setMode("ANGLE");
                    ExternalSensorBean externalSensorBeanHTCompass = new ExternalSensorBean(portHiTechCompass, "ANGLE", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new CompassSensor(mkPropertyFromClass(ctx, CompassSensor.class), externalSensorBeanHTCompass));
                } else {
                    mutation.setMode("COMPASS");
                    ExternalSensorBean externalSensorBeanHTCompass = new ExternalSensorBean(portHiTechCompass, "COMPASS", "- EMPTY_SLOT -", mutation, listHide);
                    return (T) new SensorExpr(new CompassSensor(mkPropertyFromClass(ctx, CompassSensor.class), externalSensorBeanHTCompass));
                }

            case "hiTechInfraredSensor":
                String portHiTechInfrared = ctx.INT().getText();
                T resultValidatePortHiTechInfrared = validateSensorPortName(ctx, portHiTechInfrared);
                if ( resultValidatePortHiTechInfrared != null ) return resultValidatePortHiTechInfrared;
                if ( ctx.children.get(2).getText().equals("getModulated") ) {
                    mutation.setMode("MODULATED");
                    ExternalSensorBean externalSensorBeanHTInfrared = new ExternalSensorBean(portHiTechInfrared, "MODULATED", "-", mutation, listHide);
                    return (T) new SensorExpr(new IRSeekerSensor(mkPropertyFromClass(ctx, IRSeekerSensor.class), externalSensorBeanHTInfrared));
                } else {
                    mutation.setMode("UNMODULATED");
                    ExternalSensorBean externalSensorBeanHTInfrared = new ExternalSensorBean(portHiTechInfrared, "UNMODULATED", "-", mutation, listHide);
                    return (T) new SensorExpr(new IRSeekerSensor(mkPropertyFromClass(ctx, IRSeekerSensor.class), externalSensorBeanHTInfrared));
                }
            case "hiTechColorSensor":
                String portHTColor = ctx.INT().getText();
                T resultValidateHTPortColor = validateSensorPortNumber(ctx, portHTColor);
                if ( resultValidateHTPortColor != null ) return resultValidateHTPortColor;

                String modeColorHTSensor = ColorModeEv3.getMode(ctx.NAME(0).getText());
                T resulValidateModeHTColor = validateSensorModes(ctx, modeColorHTSensor);
                if ( resulValidateModeHTColor != null ) return resulValidateModeHTColor;
                mutation.setMode(modeColorHTSensor);
                ExternalSensorBean externalSensorBeanHTColor = new ExternalSensorBean(portHTColor, modeColorHTSensor, "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new HTColorSensor(mkPropertyFromClass(ctx, HTColorSensor.class), externalSensorBeanHTColor));

            // These blocks are not in the sensor Blockly category, but they use the same method and grammar rule for simplicity and consistency in the grammar and visitor
            case "connectToRobot":
                Expr exprConnect = (Expr) visit(ctx.expr());
                exprConnect.setReadOnly();
                return (T) new ActionExpr(new BluetoothConnectAction(mkPropertyFromClass(ctx, BluetoothConnectAction.class), exprConnect));

            case "receiveMessage":
                Expr exprMessage = (Expr) visit(ctx.expr());
                exprMessage.setReadOnly();
                mutation.setDatatype("String");
                return (T) new ActionExpr(new BluetoothReceiveAction(mkPropertyFromClass(ctx, BluetoothReceiveAction.class), mutation, "String", "BLUETOOTH", "-1", "ev3", exprMessage));

            case "waitForConnection":
                return (T) new ActionExpr(new BluetoothWaitForConnectionAction(mkPropertyFromClass(ctx, BluetoothWaitForConnectionAction.class)));
            case "getOutputNeuron":
                String outputNeuron = ctx.NAME(0).getText();
                return (T) new NNGetOutputNeuronVal(mkPropertyFromClass(ctx, NNGetOutputNeuronVal.class), outputNeuron);
            case "getWeight":
                String fromNeuron = ctx.NAME(0).getText();
                String toNeuron = ctx.NAME(1).getText();
                return (T) new NNGetWeight(mkPropertyFromClass(ctx, NNGetWeight.class), fromNeuron, toNeuron);
            case "getBias":
                String bias = ctx.NAME(0).getText();
                return (T) new NNGetBias(mkPropertyFromClass(ctx, NNGetBias.class), bias);
            default:
                Expr result = new EmptyExpr(BlocklyType.NOTHING);
                result.addTextlyError("Invalid Sensor definition " + sensor, true);
                return (T) result;
        }
    }

    private T validateSensorPortName(TextlyJavaParser.Ev3SensorExprContext ctx, String port) {
        if ( port == null ) {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid Port for this sensor: " + ctx.NAME(0).getText(), true);
            return (T) result;
        }
        return null;
    }

    private T validateSensorPortName(TextlyJavaParser.Ev3SensorStmtContext ctx, String port) {
        if ( port == null ) {
            StmtList statementList = new StmtList(ctx);
            statementList.setReadOnly();
            statementList.addTextlyError("Invalid Port for this sensor: " + ctx.NAME().getText(), true);
            return (T) statementList;
        }
        return null;
    }

    private T validateSensorPortNumber(TextlyJavaParser.Ev3SensorExprContext ctx, String port) {
        if ( !validatePattern(port, NameType.EV3PORTNUMB) ) {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid Port for this sensor: " + ctx.INT().getText() + " the port should be 1,2,3 or 4", true);
            return (T) result;
        }
        return null;
    }

    private T validateSensorPortNumber(TextlyJavaParser.Ev3SensorStmtContext ctx, String port) {
        if ( !validatePattern(port, NameType.EV3PORTNUMB) ) {
            StmtList statementList = new StmtList(ctx);
            statementList.setReadOnly();
            statementList.addTextlyError("Invalid Port for this sensor: " + ctx.INT().getText(), true);
            return (T) statementList;
        }
        return null;
    }

    private T validateSensorModes(TextlyJavaParser.Ev3SensorExprContext ctx, String mode) {
        if ( mode == null && (ctx.start.getText().equals("colorSensor") || ctx.start.getText().equals("hiTechColorSensor")) ) {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid mode for this sensor: " + ctx.NAME(0).getText() + " the mode should be colour, light, ambientlight or rgb", true);
            return (T) result;
        }
        return null;
    }

    private T validateActuatorPortName(TextlyJavaParser.Ev3ActuatorStmtContext ctx, String port, String textlyPortorType) {
        if ( port == null ) {
            StmtList statementList = new StmtList(ctx);
            statementList.setReadOnly();
            statementList.addTextlyError("Invalid Port/Type for this actuator: " + textlyPortorType, true);
            return (T) statementList;
        }
        return null;
    }

    @Override
    public T visitRobotEv3Expression(TextlyJavaParser.RobotEv3ExpressionContext ctx) {
        return visitEv3SensorExpr(ctx.ev3SensorExpr());
    }

    @Override
    public T visitRobotEv3SensorStatement(TextlyJavaParser.RobotEv3SensorStatementContext ctx) {
        return visitEv3SensorStmt(ctx.ev3SensorStmt());
    }

    @Override
    public T visitRobotEv3ActuatorStatement(TextlyJavaParser.RobotEv3ActuatorStatementContext ctx) {
        return visitEv3ActuatorStmt(ctx.ev3ActuatorStmt());
    }

    @Override
    public T visitRobotEv3NeuralNetworks(TextlyJavaParser.RobotEv3NeuralNetworksContext ctx) {
        return visitEv3xNN(ctx.ev3xNN());
    }

    @Override
    public T visitEv3SensorStmt(TextlyJavaParser.Ev3SensorStmtContext ctx) {
        String sensor = ctx.start.getText();
        switch ( sensor ) {
            case "timerReset":
                String portTimer = ctx.INT().getText();
                if ( portTimer.matches("[1-5]") ) {
                    TimerReset reset = new TimerReset(mkInlineProperty(ctx, "robSensors_timer_reset"), portTimer);
                    return (T) new SensorStmt(reset);
                } else {
                    StmtList statementList = new StmtList(ctx);
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid Port for this sensor: " + ctx.INT().getText() + " the port should be 1,2,3,4 or 5", true);
                    return (T) statementList;
                }

            case "encoderReset":
                String encoderPort = PortsEv3.getPort(ctx.NAME().getText());
                T resultValidateEncoderPort = validateSensorPortName(ctx, encoderPort);
                if ( resultValidateEncoderPort != null ) return resultValidateEncoderPort;
                return (T) new SensorStmt(new EncoderReset(mkExternalProperty(ctx, "robSensors_encoder_reset"), encoderPort));

            case "gyroReset":
                String gyroPort = ctx.INT().getText();
                T resultValidateGyroPort = validateSensorPortNumber(ctx, gyroPort);
                if ( resultValidateGyroPort != null ) return resultValidateGyroPort;
                return (T) new SensorStmt(new GyroReset(mkPropertyFromClass(ctx, GyroReset.class), gyroPort));

            case "hiTecCompassStartCalibration":
                String htCompassPort = ctx.INT().getText();
                T resultValidateHtCompassPort = validateSensorPortNumber(ctx, htCompassPort);
                if ( resultValidateHtCompassPort != null ) return resultValidateHtCompassPort;
                Hide hide = new Hide();
                List<Hide> listHide = new LinkedList();
                Mutation mutation = new Mutation();
                ExternalSensorBean externalSensorBeanHTCompassCalibration = new ExternalSensorBean(htCompassPort, "DEFAULT", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorStmt(new CompassCalibrate(mkPropertyFromClass(ctx, CompassCalibrate.class), externalSensorBeanHTCompassCalibration));
            default:
                StmtList statementList = new StmtList(ctx);
                statementList.setReadOnly();
                statementList.addTextlyError("Invalid Sensor textual representation", true);
                return (T) statementList;
        }
    }

    @Override
    public T visitEv3ActuatorStmt(TextlyJavaParser.Ev3ActuatorStmtContext ctx) {
        BlocklyDropdownFactory factory = new BlocklyDropdownFactory();

        String actuator = ctx.start.getText();
        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        Mutation mutation = new Mutation();

        switch ( actuator ) {
            case "turnOnRegulatedMotor":
                String portRegulatedMotor = PortsEv3.getPort(ctx.NAME(0).getText());
                T resultValidatePortEncoder = validateActuatorPortName(ctx, portRegulatedMotor, ctx.NAME(0).getText());
                if ( resultValidatePortEncoder != null ) return resultValidatePortEncoder;
                Expr exprSpeed = (Expr) visit(ctx.expr(0));
                MotionParam motionParam = new MotionParam.Builder().speed(exprSpeed).build();
                return (T) new ActionStmt(new MotorOnAction(portRegulatedMotor, motionParam, mkExternalProperty(ctx, "robActions_motor_on")));

            case "rotateRegulatedMotor":
                String portRegulatedMotor2 = PortsEv3.getPort(ctx.NAME(0).getText());
                T resultValidatePortEncoder2 = validateActuatorPortName(ctx, portRegulatedMotor2, ctx.NAME(0).getText());
                if ( resultValidatePortEncoder2 != null ) return resultValidatePortEncoder2;
                String motorType = MotorOnActionType.getAstType(ctx.NAME(1).getText());
                T resultValidateMotorType = validateActuatorPortName(ctx, motorType, ctx.NAME(1).getText());
                if ( resultValidateMotorType != null ) return resultValidateMotorType;

                Expr exprSpeed2 = (Expr) visit(ctx.expr(0));
                Expr exprDuration = (Expr) visit(ctx.expr(1));
                MotorDuration motorDurationRegulated = new MotorDuration(factory.getMotorMoveMode(motorType), exprDuration);
                MotionParam motionParam2 = new MotionParam.Builder().speed(exprSpeed2).duration(motorDurationRegulated).build();
                return (T) new ActionStmt(new MotorOnAction(portRegulatedMotor2, motionParam2, mkExternalProperty(ctx, "robActions_motor_on_for")));

            case "setRegulatedMotorSpeed":
                String portMotorSet = PortsEv3.getPort(ctx.NAME(0).getText());
                T resultValidateMotorPort = validateActuatorPortName(ctx, portMotorSet, ctx.NAME(0).getText());
                if ( resultValidateMotorPort != null ) return resultValidateMotorPort;
                Expr exprSpeed3 = (Expr) visit(ctx.expr(0));
                return (T) new ActionStmt(new MotorSetPowerAction(mkPropertyFromClass(ctx, MotorSetPowerAction.class), exprSpeed3, portMotorSet));

            case "stopRegulatedMotor":
                String portMotorStop = PortsEv3.getPort(ctx.NAME(0).getText());
                T resultValidateMotorStop = validateActuatorPortName(ctx, portMotorStop, ctx.NAME(0).getText());
                if ( resultValidateMotorStop != null ) return resultValidateMotorStop;
                String modeMotorStop = MotorStopType.getAstType(ctx.NAME(1).getText());
                T resultValidateMotorStopMode = validateActuatorPortName(ctx, modeMotorStop, ctx.NAME(1).getText());
                if ( resultValidateMotorStopMode != null ) return resultValidateMotorStopMode;

                MotorStopAction motorStopAction = new MotorStopAction(portMotorStop, factory.getMotorStopMode(modeMotorStop), mkExternalProperty(ctx, "robActions_motor_stop"));
                return (T) new ActionStmt(motorStopAction);

            case "driveDistance":
                String directionDrive = MotionDirection.getAstDirection(ctx.NAME(0).getText());
                T resultValidateDriveDirection = validateActuatorPortName(ctx, directionDrive, ctx.NAME(0).getText());
                if ( resultValidateDriveDirection != null ) return resultValidateDriveDirection;

                Expr exprSpeedDrive = (Expr) visit(ctx.expr(0));
                DriveDirection direction = (DriveDirection) factory.getDriveDirection(directionDrive);
                MotionParam motionParamDrive;
                String blocklyName;
                if ( ctx.expr().size() == 2 ) {
                    Expr exprDistanceDrive = (Expr) visit(ctx.expr(1));
                    MotorDuration motorDurationDrive = new MotorDuration(factory.getMotorMoveMode(BlocklyConstants.DISTANCE), exprDistanceDrive);
                    motionParamDrive = new MotionParam.Builder().speed(exprSpeedDrive).duration(motorDurationDrive).build();
                    blocklyName = "robActions_motorDiff_on_for";
                } else {
                    motionParamDrive = new MotionParam.Builder().speed(exprSpeedDrive).build();
                    blocklyName = "robActions_motorDiff_on";
                }

                DriveAction driveDirection = new DriveAction(direction, motionParamDrive, "- EMPTY_PORT -", null, mkExternalProperty(ctx, blocklyName));
                return (T) new ActionStmt(driveDirection);

            case "stopRegulatedDrive":
                return (T) new ActionStmt(new MotorDriveStopAction(mkPropertyFromClass(ctx, MotorDriveStopAction.class), "- EMPTY_PORT -", null));

            case "rotateDirectionAngle":
                String directionAngle = MotionTurnDirection.getAstTurnDirection(ctx.NAME(0).getText());
                T resultValidateDirectionAngle = validateActuatorPortName(ctx, directionAngle, ctx.NAME(0).getText());
                if ( resultValidateDirectionAngle != null ) return resultValidateDirectionAngle;

                Expr exprSpeedAngle = (Expr) visit(ctx.expr(0));
                Expr exprDurationAngle = (Expr) visit(ctx.expr(1));
                MotorDuration motorDurationAngle = new MotorDuration(factory.getMotorMoveMode("DEGREE"), exprDurationAngle);
                MotionParam motionParamAngle = new MotionParam.Builder().speed(exprSpeedAngle).duration(motorDurationAngle).build();

                TurnAction turnAction = new TurnAction(factory.getTurnDirection(directionAngle), motionParamAngle, "- EMPTY_PORT -", null, mkExternalProperty(ctx, "robActions_motorDiff_turn_for"));
                return (T) new ActionStmt(turnAction);

            case "rotateDirectionRegulated":
                String directionAngleRegulated = MotionTurnDirection.getAstTurnDirection(ctx.NAME(0).getText());
                T resultValidateDirectionAngleRegulated = validateActuatorPortName(ctx, directionAngleRegulated, ctx.NAME(0).getText());
                if ( resultValidateDirectionAngleRegulated != null ) return resultValidateDirectionAngleRegulated;

                Expr exprSpeedAngleRegulated = (Expr) visit(ctx.expr(0));
                MotionParam motionParamAngleRegulated = new MotionParam.Builder().speed(exprSpeedAngleRegulated).build();
                TurnAction turnActionRegulated = new TurnAction(factory.getTurnDirection(directionAngleRegulated), motionParamAngleRegulated, "- EMPTY_PORT -", null, mkExternalProperty(ctx, "robActions_motorDiff_turn"));
                return (T) new ActionStmt(turnActionRegulated);

            case "driveInCurve":
                String directionDriveInCurve = MotionDirection.getAstDirection(ctx.NAME(0).getText());
                T resultValidateDirectionInCurve = validateActuatorPortName(ctx, directionDriveInCurve, ctx.NAME(0).getText());
                if ( resultValidateDirectionInCurve != null ) return resultValidateDirectionInCurve;

                Expr exprSpeedInCurveLeft = (Expr) visit(ctx.expr(0));
                Expr exprSpeedInCurveRight = (Expr) visit(ctx.expr(1));

                MotionParam motionParamLeft;
                MotionParam motionParamRight;
                String blocklyNameDrive;
                if ( ctx.expr().size() == 3 ) {
                    Expr exprDistanceInCurve = (Expr) visit(ctx.expr(2));
                    MotorDuration motorDurationInCurve = new MotorDuration(factory.getMotorMoveMode("DISTANCE"), exprDistanceInCurve);
                    motionParamLeft = new MotionParam.Builder().speed(exprSpeedInCurveLeft).duration(motorDurationInCurve).build();
                    motionParamRight = new MotionParam.Builder().speed(exprSpeedInCurveRight).duration(motorDurationInCurve).build();
                    blocklyNameDrive = "robActions_motorDiff_curve_for";
                } else {
                    motionParamLeft = new MotionParam.Builder().speed(exprSpeedInCurveLeft).build();
                    motionParamRight = new MotionParam.Builder().speed(exprSpeedInCurveRight).build();
                    blocklyNameDrive = "robActions_motorDiff_curve";
                }

                CurveAction curveAction = new CurveAction(factory.getDriveDirection(directionDriveInCurve), motionParamLeft, motionParamRight, "- EMPTY_PORT -", null, mkExternalProperty(ctx, blocklyNameDrive));
                return (T) new ActionStmt(curveAction);

            case "drawText":
                Expr msg = (Expr) visit(ctx.expr(0));
                msg.setReadOnly();
                Expr x = (Expr) visit(ctx.expr(1));
                Expr y = (Expr) visit(ctx.expr(2));
                ShowTextAction showTextAction = new ShowTextAction(mkInlineProperty(ctx, "robActions_display_text"), msg, x, y, "- EMPTY_PORT -", null);
                return (T) new ActionStmt(showTextAction);

            case "drawPicture":
                String pictureName = Ev3PredefinedImages.getAstNameImage(ctx.NAME(0).getText());
                T resultValidatePicture = validateActuatorPortName(ctx, pictureName, ctx.NAME(0).getText());
                if ( resultValidatePicture != null ) return resultValidatePicture;

                Expr xx = new EmptyExpr(BlocklyType.NUMBER_INT);
                Expr yy = new EmptyExpr(BlocklyType.NUMBER_INT);
                return (T) new ActionStmt(new ShowPictureAction(mkExternalProperty(ctx, "robActions_display_picture_new"), pictureName, xx, yy));

            case "clearDisplay":
                ClearDisplayAction clearDisplayAction = new ClearDisplayAction(mkExternalProperty(ctx, "robActions_display_clear"), "- EMPTY_PORT -", null);
                return (T) new ActionStmt(clearDisplayAction);

            case "playTone":
                Expr freq = (Expr) visit(ctx.expr(0));
                freq.setReadOnly();
                Expr duration = (Expr) visit(ctx.expr(1));
                duration.setReadOnly();
                ToneAction toneAction = new ToneAction(mkInlineProperty(ctx, "robActions_play_tone"), freq, duration, "- EMPTY_PORT -", null);
                return (T) new ActionStmt(toneAction);

            case "playFile":
                String fileName = ctx.INT().getText();
                if ( fileName.matches("[1-5]") ) {
                    PlayFileAction playFileAction = new PlayFileAction(mkExternalProperty(ctx, "robActions_play_file"), "- EMPTY_PORT -", String.valueOf(Integer.parseInt(fileName) - 1), null);
                    return (T) new ActionStmt(playFileAction);
                } else {
                    StmtList statementList = new StmtList(ctx);
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid Number of file " + fileName, true);
                    return (T) statementList;
                }

            case "setVolume":
                Expr valueVolume = (Expr) visit(ctx.expr(0));
                SetVolumeAction setVolumeAction = new SetVolumeAction(mkExternalProperty(ctx, "robActions_play_setVolume"), "- EMPTY_PORT -", valueVolume, hide);
                return (T) new ActionStmt(setVolumeAction);

            case "setLanguage":
                Language language = Language.findByAbbr(ctx.NAME(0).getText());
                if ( language != Language.NOTSUPPORTED ) {
                    return (T) new ActionStmt(new SetLanguageAction(mkPropertyFromClass(ctx, SetLanguageAction.class), language));
                } else {
                    StmtList statementList = new StmtList(ctx);
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid language or the language is not supported " + ctx.NAME(0).getText(), true);
                    return (T) statementList;
                }

            case "sayText":
                Expr message = (Expr) visit(ctx.expr(0));
                message.setReadOnly();
                if ( ctx.expr().size() == 1 ) {
                    return (T) new ActionStmt(new SayTextAction(mkPropertyFromClass(ctx, SayTextAction.class), message));
                } else {
                    Expr voiceSpeed = (Expr) visit(ctx.expr(1));
                    voiceSpeed.setReadOnly();
                    Expr voicePitch = (Expr) visit(ctx.expr(2));
                    voiceSpeed.setReadOnly();
                    return (T) new ActionStmt(new SayTextWithSpeedAndPitchAction(mkPropertyFromClass(ctx, SayTextWithSpeedAndPitchAction.class), message, voiceSpeed, voicePitch));
                }
            case "ledOn":
                String ledMode = BrickLightModes.getAstNameImage(ctx.NAME(0).getText());
                String colour = BrickLightColour.getAstColour(ctx.NAME(1).getText());
                return (T) new ActionStmt(new BrickLightOnAction(mkInlineProperty(ctx, "actions_brickLight_on_ev3"), LightMode.valueOf(ledMode), BrickLedColor.valueOf(colour)));

            case "ledOff":
                BlocklyRegion br = new BlocklyRegion(false, false, null, null, null, true, null, null, null);
                TextRegion rg = ctx == null ? null : new TextRegion(ctx.start.getLine(), ctx.start.getStartIndex(), ctx.stop.getLine(), ctx.stop.getStopIndex());
                return (T) new ActionStmt(new BrickLightOffAction(new BlocklyProperties("actions_brickLight_off_ev3", "1", br, rg), "OFF"));
            case "resetLed":
                return (T) new ActionStmt(new BrickLightOffAction(mkPropertyFromClass(ctx, BrickLightOffAction.class), "RESET"));

            // These blocks are not in the actuator Blockly category, but they use the same method and grammar rule for simplicity and consistency in the grammar and visitor
            case "sendMessage":
                Expr sendMessage = (Expr) visit(ctx.expr(0));
                sendMessage.setReadOnly();
                Expr connection = (Expr) visit(ctx.expr(1));
                connection.setReadOnly();
                mutation.setDatatype("String");

                return (T) new ActionStmt(new BluetoothSendAction(mkInlineProperty(ctx, "robCommunication_sendBlock"), mutation, "String", "BLUETOOTH", "-1", "ev3", sendMessage, connection));
            default:
                StmtList statementList = new StmtList(ctx);
                statementList.setReadOnly();
                statementList.addTextlyError("Invalid Sensor textual representation", true);
                return (T) statementList;
        }
    }

    @Override
    public T visitEv3xNN(TextlyJavaParser.Ev3xNNContext ctx) {
        String xnnFunction = ctx.start.getText();
        switch ( xnnFunction ) {
            case "nnStep":
                return (T) new NNStepStmt(mkPropertyFromClass(ctx, NNStepStmt.class));
            case "setInputNeuron":
                Expr inputNeuron = (Expr) visit(ctx.expr());
                String nameNeuron = ctx.NAME(0).getText();
                return (T) new NNSetInputNeuronVal(mkPropertyFromClass(ctx, NNSetInputNeuronVal.class), nameNeuron, inputNeuron);
            case "setWeight":
                Expr weight = (Expr) visit(ctx.expr());
                String nameNeuron1 = ctx.NAME(0).getText();
                String nameNeuron2 = ctx.NAME(1).getText();
                return (T) new NNSetWeightStmt(mkPropertyFromClass(ctx, NNSetWeightStmt.class), nameNeuron1, nameNeuron2, weight);
            case "setBias":
                Expr neuron = (Expr) visit(ctx.expr());
                String nameBias = ctx.NAME(0).getText();
                return (T) new NNSetBiasStmt(mkPropertyFromClass(ctx, NNSetBiasStmt.class), nameBias, neuron);
            default:
                StmtList statementList = new StmtList();
                statementList.setReadOnly();
                statementList.addTextlyError("Invalid Neural network function", true);
                return (T) statementList;
        }
    }

    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }

    private static BlocklyProperties mkExternalProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", false, ctx);
    }

    private <T> BlocklyProperties mkPropertyFromClass(ParserRuleContext ctx, Class<T> clazz) {
        String[] blocklyNames = AnnotationHelper.getBlocklyNamesOfAstClass(clazz);
        if ( blocklyNames.length != 1 ) {
            throw new DbcException("rework that! Too many blockly names to generate an ast object, that can be regenerated as XML");
        }
        return mkExternalProperty(ctx, blocklyNames[0]);
    }

    private enum PortsEv3 {
        A("a"),
        B("b"),
        C("c"),
        D("d");

        private final String name;

        PortsEv3(String name) {
            this.name = name;
        }

        /**
         * Given a textly NAME Port return the port needed in the AST class
         * if the textly NAME in wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getPort(String textlyName) {
            for ( PortsEv3 port : values() ) {
                if ( port.name.equalsIgnoreCase(textlyName) ) {
                    return port.name();
                }
            }
            return null;
        }
    }

    private enum ColorModeEv3 {
        COLOUR("colour"),
        LIGHT("light"),
        AMBIENTLIGHT("ambientLight"),
        RGB("rgb");

        private final String name;

        ColorModeEv3(String name) {
            this.name = name;
        }

        /**
         * Given a textly NAME Port return the Model needed in the AST class for the color sensor
         * if the textly NAME in wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getMode(String textlyName) {
            for ( ColorModeEv3 model : values() ) {
                if ( model.name.equalsIgnoreCase(textlyName) ) {
                    return model.name();
                }
            }
            return null;
        }
    }

    private enum Ev3KeyPorts {
        UP("up"),
        DOWN("down"),
        LEFT("left"),
        RIGHT("right"),
        ESCAPE("escape"),
        ANY("any"),
        ENTER("enter");

        private final String name;

        Ev3KeyPorts(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstName(String textlyName) {
            for ( Ev3KeyPorts port : values() ) {
                if ( port.name.equalsIgnoreCase(textlyName) ) {
                    return port.name();
                }
            }
            return null;
        }
    }

    private enum MotorOnActionType {
        ROTATIONS("rotations"),
        DEGREE("degree");

        private final String name;

        MotorOnActionType(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstType(String textlyName) {
            for ( MotorOnActionType type : values() ) {
                if ( type.name.equalsIgnoreCase(textlyName) ) {
                    return type.name();
                }
            }
            return null;
        }
    }

    private enum MotorStopType {
        FLOAT("float"),
        NONFLOAT("brake");

        private final String name;

        MotorStopType(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstType(String textlyName) {
            for ( MotorStopType type : values() ) {
                if ( type.name.equalsIgnoreCase(textlyName) ) {
                    return type.name();
                }
            }
            return null;
        }
    }

    private enum MotionDirection {
        FOREWARD("forward"),
        BACKWARD("backward"),
        ;

        private final String name;

        MotionDirection(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstDirection(String textlyName) {
            for ( MotionDirection direction : values() ) {
                if ( direction.name.equalsIgnoreCase(textlyName) ) {
                    return direction.name();
                }
            }
            return null;
        }
    }

    private enum MotionTurnDirection {
        RIGHT("right"),
        LEFT("left"),
        ;

        private final String name;

        MotionTurnDirection(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstTurnDirection(String textlyName) {
            for ( MotionTurnDirection turn : values() ) {
                if ( turn.name.equalsIgnoreCase(textlyName) ) {
                    return turn.name();
                }
            }
            return null;
        }
    }

    public enum Ev3PredefinedImages {
        OLDGLASSES("oldGlasses"),
        EYESOPEN("eyesOpen"),
        EYESCLOSED("eyesClosed"),
        FLOWERS("flowers"),
        TACHO("speedo");

        public final String name;

        Ev3PredefinedImages(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstNameImage(String textlyName) {
            for ( Ev3PredefinedImages image : values() ) {
                if ( image.name.equalsIgnoreCase(textlyName) ) {
                    return image.name();
                }
            }
            return null;
        }
    }

    public enum BrickLightModes {
        ON("on"),
        OFF("off"),
        FLASH("flashing"),
        DOUBLE_FLASH("doubleFlashing");

        public final String name;

        BrickLightModes(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstNameImage(String textlyName) {
            for ( BrickLightModes modes : values() ) {
                if ( modes.name.equalsIgnoreCase(textlyName) ) {
                    return modes.name();
                }
            }
            return null;
        }
    }

    private enum BrickLightColour {
        GREEN("green"),
        ORANGE("orange"),
        RED("red");

        private final String name;

        BrickLightColour(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstColour(String textlyName) {
            for ( BrickLightColour colour : values() ) {
                if ( colour.name.equalsIgnoreCase(textlyName) ) {
                    return colour.name();
                }
            }
            return null;
        }
    }
}

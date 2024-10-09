package de.fhg.iais.roberta.textlyJava;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyJavaVisitor;
import de.fhg.iais.roberta.mode.general.Direction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.BlocklyRegion;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.ast.TextRegion;

public class Microbitv2TextlyJavaVisitor<T> extends CommonTextlyJavaVisitor<T> {

    @Override
    public T visitMicrobitv2SensorExpr(TextlyJavaParser.Microbitv2SensorExprContext ctx) {
        String sensor = ctx.start.getText();
        ExprList parameters = new ExprList();

        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        Mutation mutation = new Mutation();

        switch ( sensor ) {
            case "accelerometerSensor":
                String accelerometerSlot = AccelerometerSlot.getAstName(ctx.NAME().getText());
                if ( accelerometerSlot != null ) {
                    hide.setName("SENSORPORT");
                    hide.setValue("_A");
                    listHide.add(hide);
                    mutation.setMode("VALUE");
                    ExternalSensorBean externalSensorBeanAcce = new ExternalSensorBean("_A", "VALUE", accelerometerSlot, mutation, listHide);
                    SensorExpr sensorExprAccelerometer = new SensorExpr(new AccelerometerSensor(mkInlineProperty(ctx, "robSensors_accelerometer_getSample"), externalSensorBeanAcce));
                    return (T) sensorExprAccelerometer;
                } else {
                    Expr result = new EmptyExpr(BlocklyType.NOTHING);
                    result.addTextlyError("Invalid accelerometer slot: " + ctx.NAME().getText(), true);
                    return (T) result;
                }

            case "logoTouchSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_LO");
                listHide.add(hide);
                mutation.setMode("PRESSED");
                ExternalSensorBean externalSensorBeanLogo = new ExternalSensorBean("_LO", "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new LogoTouchSensor(makeForLogo("robsensors_logotouch_getsample", "1", ctx), externalSensorBeanLogo));

            case "compassSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_C");
                listHide.add(hide);
                mutation.setMode("ANGLE");

                ExternalSensorBean externalSensorBeanCompass = new ExternalSensorBean("_C", "ANGLE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new CompassSensor(mkInlineProperty(ctx, "robSensors_compass_getSample"), externalSensorBeanCompass));

            case "gestureSensor":
                String gesture = GestureModes.getAstName(ctx.NAME().getText());
                if ( gesture != null ) {
                    mutation.setMode(gesture);
                    ExternalSensorBean externalSensorBeanGesture = new ExternalSensorBean("- EMPTY_PORT -", gesture, "- EMPTY_SLOT -", mutation, listHide);
                    SensorExpr sensorExprGesture = new SensorExpr(new GestureSensor(mkInlineProperty(ctx, "robSensors_gesture_getSample"), externalSensorBeanGesture));
                    return (T) sensorExprGesture;
                } else {
                    Expr result = new EmptyExpr(BlocklyType.NOTHING);
                    result.addTextlyError("Invalid gesture mode: " + ctx.NAME().getText(), true);
                    return (T) result;
                }

            case "keysSensor":
                mutation.setMode("PRESSED");
                ExternalSensorBean externalSensorBeanKey = new ExternalSensorBean(ctx.NAME().getText(), "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                SensorExpr sensorExprKey = new SensorExpr(new KeysSensor(mkInlineProperty(ctx, "robSensors_key_getSample"), externalSensorBeanKey));
                return (T) checkValidationName(sensorExprKey, ctx.NAME().getText(), NameType.BUTTON);

            case "lightSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_L");
                listHide.add(hide);
                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanLight = new ExternalSensorBean("_L", "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new LightSensor(mkInlineProperty(ctx, "robSensors_light_getSample"), externalSensorBeanLight));

            case "pinGetValueSensor":
                mutation.setMode(ctx.op.getText().toUpperCase());
                validatePattern(ctx.NAME().getText(), NameType.PORT);
                ExternalSensorBean externalSensorBeanPin = new ExternalSensorBean(ctx.NAME().getText(), ctx.op.getText().toUpperCase(), "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new PinGetValueSensor(mkInlineProperty(ctx, "robSensors_pin_getSample"), externalSensorBeanPin));

            case "pinTouchSensor":

                mutation.setMode("PRESSED");
                ExternalSensorBean externalSensorBeanPinT = new ExternalSensorBean(ctx.INT().getText(), "PRESSED", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new PinTouchSensor(mkInlineProperty(ctx, "robSensors_pintouch_getSample"), externalSensorBeanPinT));

            case "soundSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_S");
                listHide.add(hide);
                mutation.setMode("SOUND");
                ExternalSensorBean externalSensorBeanSound = new ExternalSensorBean("_S", "SOUND", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new SoundSensor(mkInlineProperty(ctx, "robSensors_sound_getSample"), externalSensorBeanSound));

            case "temperatureSensor":
                hide.setName("SENSORPORT");
                hide.setValue("_T");
                listHide.add(hide);
                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanTemperature = new ExternalSensorBean("_T", "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new TemperatureSensor(mkInlineProperty(ctx, "robSensors_temperature_getSample"), externalSensorBeanTemperature));

            case "timerSensor":
                mutation.setMode("VALUE");
                ExternalSensorBean externalSensorBeanPinTimer = new ExternalSensorBean("1", "VALUE", "- EMPTY_SLOT -", mutation, listHide);
                return (T) new SensorExpr(new TimerSensor(mkInlineProperty(ctx, "robSensors_timer_getSample"), externalSensorBeanPinTimer));

            // These blocks are not in the sensor Blockly category, but they use the same method and grammar rule for simplicity and consistency in the grammar and visitor
            case "getLedBrigthness":
                Expr x = (Expr) visit(ctx.expr(0));
                Expr y = (Expr) visit(ctx.expr(1));
                DisplayGetPixelAction displayGetPixelAction = new DisplayGetPixelAction(mkExternalProperty(ctx, "mbedActions_display_getPixel"), x, y);
                ActionExpr actionDisplay = new ActionExpr(displayGetPixelAction);
                return (T) actionDisplay;

            case "receiveMessage":
                String type = ctx.PRIMITIVETYPE().getText();
                if ( (type.equals("Number") || type.equals("Boolean") || type.equals("String")) ) {
                    mutation.setDatatype(type);
                    RadioReceiveAction radioReceiveAction = new RadioReceiveAction(mkExternalProperty(ctx, "mbedCommunication_receiveBlock"), mutation, type);
                    ActionExpr actionRadio = new ActionExpr(radioReceiveAction);
                    return (T) actionRadio;
                } else {
                    Expr result = new EmptyExpr(BlocklyType.NOTHING);
                    result.addTextlyError("Invalid type for recive message: " + type, true);
                    return (T) result;
                }

            default:
                Expr result = new EmptyExpr(BlocklyType.NOTHING);
                result.addTextlyError("Invalid function name " + sensor, true);
                return (T) result;
        }
    }

    @Override
    public T visitMicrobitv2SensorStmt(TextlyJavaParser.Microbitv2SensorStmtContext ctx) {
        String sensor = ctx.start.getText();

        switch ( sensor ) {
            case "pinSetTouchMode":
                PinSetTouchMode p = new PinSetTouchMode(mkInlineProperty(ctx, "robSensors_set_pin_mode"), ctx.op.getText().toUpperCase(), ctx.INT().getText());
                SensorStmt stmt = new SensorStmt(p);
                return (T) stmt;

            case "timerReset":
                TimerReset reset = new TimerReset(mkInlineProperty(ctx, "mbedSensors_timer_reset"), "1");
                SensorStmt stmt2 = new SensorStmt(reset);
                return (T) stmt2;

            default:
                StmtList statementList = new StmtList();
                statementList.setReadOnly();
                statementList.addTextlyError("invalid sensor" + sensor, true);
                return (T) statementList;
        }
    }

    @Override
    public T visitMicrobitv2ActuatorStmt(TextlyJavaParser.Microbitv2ActuatorStmtContext ctx) {
        String actuator = ctx.start.getText();
        Hide hide = new Hide();
        List<Hide> listHide = new LinkedList();
        Mutation mutation = new Mutation();
        switch ( actuator ) {
            case "showText":
                Expr msgT = (Expr) visit(ctx.expr(0));
                msgT.setReadOnly();
                DisplayTextAction displayTextActionText = new DisplayTextAction(mkInlineProperty(ctx, "mbedActions_display_text"), "TEXT", msgT);
                ActionStmt actionStmtText = new ActionStmt(displayTextActionText);
                return (T) actionStmtText;

            case "showCharacter":
                Expr msgC = (Expr) visit(ctx.expr(0));
                msgC.setReadOnly();
                DisplayTextAction displayTextActionChar = new DisplayTextAction(mkInlineProperty(ctx, "mbedActions_display_text"), "CHARACTER", msgC);
                ActionStmt actionStmtChar = new ActionStmt(displayTextActionChar);
                return (T) actionStmtChar;

            case "pitch":
                Expr freq = (Expr) visit(ctx.expr(0));
                freq.setReadOnly();
                Expr duration = (Expr) visit(ctx.expr(1));
                duration.setReadOnly();
                hide.setName("ACTORPORT");
                hide.setValue("_B");
                ToneAction toneAction = new ToneAction(mkInlineProperty(ctx, "mbedActions_play_tone"), freq, duration, "_B", hide);
                ActionStmt actionStmtTone = new ActionStmt(toneAction);
                return (T) actionStmtTone;

            case "playFile":
                String fileName = PlayFile.getAstName(ctx.NAME(0).getText());
                if ( fileName != null ) {
                    hide.setName("ACTORPORT");
                    hide.setValue("_B");
                    PlayFileAction playFileAction = new PlayFileAction(mkInlineProperty(ctx, "actions_play_file"), "- EMPTY_PORT -", fileName, hide);
                    ActionStmt actionStmtPlayFile = new ActionStmt(playFileAction);
                    return (T) actionStmtPlayFile;
                } else {
                    StmtList statementList = new StmtList();
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid play file name: " + ctx.NAME(0).getText(), true);
                    return (T) statementList;
                }

            case "showImage":
                String displayImageMode = "IMAGE";
                mutation.setType(displayImageMode);
                Expr valuesToDisplayImage = (Expr) visit(ctx.expr(0));
                DisplayImageAction displayTextAction = new DisplayImageAction(mkInlineProperty(ctx, "mbedActions_display_image"), mutation, displayImageMode, valuesToDisplayImage);
                ActionStmt actionStmtDisplayImage = new ActionStmt(displayTextAction);
                return (T) actionStmtDisplayImage;

            case "showAnimation":
                String displayAnimationMode = "ANIMATION";
                mutation.setType(displayAnimationMode);
                Expr valuesToDisplayAnimation = (Expr) visit(ctx.expr(0));
                DisplayImageAction displayTextActionAn = new DisplayImageAction(mkInlineProperty(ctx, "mbedActions_display_image"), mutation, displayAnimationMode, valuesToDisplayAnimation);
                ActionStmt actionStmtDisplayImageAn = new ActionStmt(displayTextActionAn);
                return (T) actionStmtDisplayImageAn;

            case "clearDisplay":
                ClearDisplayAction clearDisplayAction = new ClearDisplayAction(mkExternalProperty(ctx, "mbedActions_display_clear"), "- EMPTY_PORT -", null);
                ActionStmt actionStmtClearDisplay = new ActionStmt(clearDisplayAction);
                return (T) actionStmtClearDisplay;

            case "setLed":
                Expr x = (Expr) visit(ctx.expr(0));
                Expr y = (Expr) visit(ctx.expr(1));
                Expr brightness = (Expr) visit(ctx.expr(2));
                DisplaySetPixelAction displaySetPixelAction = new DisplaySetPixelAction(mkExternalProperty(ctx, "mbedActions_display_setPixel"), x, y, brightness);
                ActionStmt actionStmtSetPixel = new ActionStmt(displaySetPixelAction);
                return (T) actionStmtSetPixel;

            case "showOnSerial":
                Expr valueShowOnSerial = (Expr) visit(ctx.expr(0));
                SerialWriteAction serialWriteAction = new SerialWriteAction(mkExternalProperty(ctx, "robActions_serial_print"), valueShowOnSerial);
                ActionStmt actionStmtShowOnSerial = new ActionStmt(serialWriteAction);
                return (T) actionStmtShowOnSerial;

            case "playSound":
                String soundPattern = SoundPattern.getAstName(ctx.NAME(0).getText());
                if ( soundPattern != null ) {
                    hide.setName("ACTORPORT");
                    hide.setValue("_B");
                    PlayFileAction playFileActionSound = new PlayFileAction(mkInlineProperty(ctx, "actions_play_expression"), "- EMPTY_PORT -", soundPattern, hide);
                    ActionStmt actionStmtPlayFileSound = new ActionStmt(playFileActionSound);
                    return (T) actionStmtPlayFileSound;
                } else {
                    StmtList statementList = new StmtList();
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid sound file: " + ctx.NAME(0).getText(), true);
                    return (T) statementList;
                }

            case "setVolume":
                Expr valueVolume = (Expr) visit(ctx.expr(0));
                hide.setName("ACTORPORT");
                hide.setValue("_B");
                SetVolumeAction setVolumeAction = new SetVolumeAction(mkExternalProperty(ctx, "robActions_play_setVolume"), "- EMPTY_PORT -", valueVolume, hide);
                ActionStmt actionStmtSetVolume = new ActionStmt(setVolumeAction);
                return (T) actionStmtSetVolume;

            case "speaker":
                String modeSpeaker = OnOff.getAstName(ctx.NAME(0).getText());
                if ( modeSpeaker != null ) {
                    hide.setName("ACTORPORT");
                    hide.setValue("_B");
                    SoundToggleAction soundToggleAction = new SoundToggleAction(mkExternalProperty(ctx, "actions_sound_toggle"), modeSpeaker, hide);
                    ActionStmt actionStmtSoundToggle = new ActionStmt(soundToggleAction);
                    return (T) actionStmtSoundToggle;
                } else {
                    StmtList statementList = new StmtList();
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid speaker mode: " + ctx.NAME(0).getText(), true);
                    return (T) statementList;
                }

            case "writeValuePin":
                String pinMode = ctx.op.getText().toUpperCase();
                String port = ctx.NAME(0).getText();
                Expr valuePin = (Expr) visit(ctx.expr(0));
                mutation.setProtocol(pinMode.toUpperCase());
                MbedPinWriteValueAction mbedPinWriteValueAction = new MbedPinWriteValueAction(mkExternalProperty(ctx, "mbedActions_write_to_pin"), mutation, pinMode, port, valuePin);
                ActionStmt actionStmtWriteValuePin = new ActionStmt(mbedPinWriteValueAction);
                return (T) checkValidationName(actionStmtWriteValuePin, pinMode, NameType.PORT);

            case "switchLed":

                String ledMode = OnOff.getAstName(ctx.NAME(0).getText());
                if ( ledMode != null ) {
                    SwitchLedMatrixAction switchLedMatrixAction = new SwitchLedMatrixAction(mkExternalProperty(ctx, "mbedActions_switch_led_matrix"), ledMode);
                    ActionStmt actionStmtSwitchLedMatrix = new ActionStmt(switchLedMatrixAction);
                    return (T) actionStmtSwitchLedMatrix;
                } else {
                    StmtList statementList = new StmtList();
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid LED mode: " + ctx.NAME(0).getText(), true);
                    return (T) statementList;
                }

            case "radioSend":
                String type = ctx.PRIMITIVETYPE().getText();
                mutation.setDatatype(type);
                String power = ctx.expr(0).getText();
                Expr msg = (Expr) visit(ctx.expr(1));

                if ( ((type.equals("Number") || type.equals("Boolean") || type.equals("String")) && (Integer.parseInt(power) >= 0 && Integer.parseInt(power) <= 7)) ) {
                    RadioSendAction radioSendAction = new RadioSendAction(mkExternalProperty(ctx, "mbedCommunication_sendBlock"), mutation, type, power, msg);
                    ActionStmt actionStmtRadioSend = new ActionStmt(radioSendAction);
                    return (T) actionStmtRadioSend;
                } else {
                    StmtList statementList = new StmtList();
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid mode or power for Radio send action", true);
                    return (T) statementList;
                }

            case "radioSet":
                Expr channel = (Expr) visit(ctx.expr(0));
                RadioSetChannelAction radioSetChannelAction = new RadioSetChannelAction(mkExternalProperty(ctx, "mbedCommunication_setChannel"), channel);
                ActionStmt actionStmtRadioSetChannel = new ActionStmt(radioSetChannelAction);
                return (T) actionStmtRadioSetChannel;
            default:
                StmtList statementList = new StmtList();
                statementList.setReadOnly();
                statementList.addTextlyError("Invalid actuator" + actuator, true);
                return (T) statementList;
        }
    }

    @Override
    public T visitUserDefinedImage(TextlyJavaParser.UserDefinedImageContext ctx) {

        String[][] imageRows = new String[5][];
        String imageArray = ctx.getText();
        int start = 0;
        for ( int i = 0; i < 5; i++ ) {

            int openBracket = imageArray.indexOf('[', start);
            int closeBracket = imageArray.indexOf(']', openBracket);
            String rowText = imageArray.substring(openBracket + 1, closeBracket);
            String[] elements = rowText.split(",");

            for ( int j = 0; j < elements.length; j++ ) {
                elements[j] = elements[j].trim();
            }
            imageRows[i] = elements;
            start = closeBracket + 1;
        }
        Image image = new Image(imageRows, mkExternalProperty(ctx, "mbedImage_image"));
        return (T) image;
    }

    @Override
    public T visitPredefinedImage(TextlyJavaParser.PredefinedImageContext ctx) {
        String imageName = PredefinedImageEnum.getAstName(ctx.NAME().getText());
        if ( imageName != null ) {
            return (T) new PredefinedImage(mkExternalProperty(ctx, "mbedImage_get_image"), imageName);
        } else {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid image name: " + ctx.NAME().getText(), true);
            return (T) result;
        }
    }

    @Override
    public T visitImageInvert(TextlyJavaParser.ImageInvertContext ctx) {
        Expr image = (Expr) visit(ctx.expr());
        FunctionExpr imageInvertFunction = new FunctionExpr(new ImageInvertFunction(mkExternalProperty(ctx, "mbedImage_invert"), image));
        return (T) imageInvertFunction;
    }

    @Override
    public T visitImageShift(TextlyJavaParser.ImageShiftContext ctx) {
        String shiftMode = ShiftModes.getAstName(ctx.NAME().getText());
        if ( shiftMode != null ) {
            Expr image = (Expr) visit(ctx.expr(0));
            Expr numShift = (Expr) visit(ctx.expr(1));
            Direction direction = Direction.valueOf(shiftMode);
            FunctionExpr imageShiftFunction = new FunctionExpr(new ImageShiftFunction(mkExternalProperty(ctx, "mbedImage_shift"), direction, image, numShift));
            return (T) imageShiftFunction;
        } else {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid Shift mode: " + ctx.NAME().getText(), true);
            return (T) result;
        }

    }

    @Override
    public T visitRobotMicrobitv2SensorStatement(TextlyJavaParser.RobotMicrobitv2SensorStatementContext ctx) {
        return visitMicrobitv2SensorStmt(ctx.microbitv2SensorStmt());
    }

    @Override
    public T visitRobotMicrobitv2ActuatorStatement(TextlyJavaParser.RobotMicrobitv2ActuatorStatementContext ctx) {
        return visitMicrobitv2ActuatorStmt(ctx.microbitv2ActuatorStmt());
    }

    @Override
    public T visitRobotMicrobitv2Expression(TextlyJavaParser.RobotMicrobitv2ExpressionContext ctx) {
        return visitMicrobitv2SensorExpr(ctx.microbitv2SensorExpr());
    }


    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }

    private static BlocklyProperties mkExternalProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", false, ctx);
    }

    private static BlocklyProperties makeForLogo(String blockType, String blocklyId, ParserRuleContext ctx) {
        TextRegion rg = ctx == null ? null : new TextRegion(ctx.start.getLine(), ctx.start.getStartIndex(), ctx.stop.getLine(), ctx.stop.getStopIndex());
        BlocklyRegion br = new BlocklyRegion(false, false, null, null, null, true, null, null, null);
        return new BlocklyProperties(blockType, blocklyId, br, rg);
    }

    public enum GestureModes {

        UP("up"),
        DOWN("down"),
        FACE_DOWN("faceDown"),
        FACE_UP("faceUp"),
        SHAKE("shake"),
        FREEFALL("freefall");
        public final String textlyName;

        GestureModes(String textlyName) {
            this.textlyName = textlyName;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstName(String textlyName) {
            for ( GestureModes gestureModes : GestureModes.values() ) {
                if ( gestureModes.textlyName.equalsIgnoreCase(textlyName) ) {
                    return gestureModes.name();
                }
            }
            return null;
        }
    }

    private enum PlayFile {
        DADADADUM("dadadadum"),
        ENTERTAINER("entertainer"),
        PRELUDE("prelude"),
        ODE("ode"),
        NYAN("nyan"),
        RINGTONE("ringtone"),
        FUNK("funk"),
        BLUES("blues"),
        BIRTHDAY("birthday"),
        WEDDING("wedding"),
        FUNERAL("funeral"),
        PUNCHLINE("punchline"),
        PYTHON("python"),
        BADDY("baddy"),
        CHASE("chase"),
        BA_DING("ba_ding"),
        WAWAWAWAA("wawawawaa"),
        JUMP_UP("jump_up"),
        JUMP_DOWN("jump_down"),
        POWER_UP("power_up"),
        POWER_DOWN("power_down");

        private final String name;

        PlayFile(String name) {
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
            for ( PlayFile playFile : values() ) {
                if ( playFile.name.equalsIgnoreCase(textlyName) ) {
                    return playFile.name();
                }
            }
            return null;
        }
    }

    public enum SoundPattern {
        GIGGLE("giggle"),
        HAPPY("happy"),
        HELLO("hello"),
        MYSTERIOUS("mysterious"),
        SAD("sad"),
        SLIDE("slide"),
        SOARING("soaring"),
        SPRING("spring"),
        TWINKLE("twinkle"),
        YAWN("yawn");

        private final String name;

        SoundPattern(String name) {
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
            for ( SoundPattern pattern : values() ) {
                if ( pattern.name.equalsIgnoreCase(textlyName) ) {
                    return pattern.name();
                }
            }
            return null;
        }
    }

    private enum AccelerometerSlot {
        X("x"),
        Y("y"),
        Z("z"),
        STRENGTH("strength");

        private final String name;

        AccelerometerSlot(String name) {
            this.name = name;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name in wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstName(String textlyName) {
            for ( AccelerometerSlot port : values() ) {
                if ( port.name.equalsIgnoreCase(textlyName) ) {
                    return port.name();
                }
            }
            return null;
        }
    }

    private enum PlayDuration {
        WHOLE("whole"),
        HALF("half"),
        QUARTER("quarter"),
        EIGHTH("eighth"),
        SIXTEENTH("sixteenth");

        private final String name;

        PlayDuration(String name) {
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
            for ( PlayDuration duration : values() ) {
                if ( duration.name.equalsIgnoreCase(textlyName) ) {
                    return duration.name();
                }
            }
            return null;
        }
    }

    private enum OnOff {
        ON("on"),
        OFF("off");

        private final String name;

        OnOff(String name) {
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
            for ( OnOff mode : values() ) {
                if ( mode.name.equalsIgnoreCase(textlyName) ) {
                    return mode.name();
                }
            }
            return null;
        }
    }

    public enum PredefinedImageEnum {
        HEART("heart"),
        HEART_SMALL("heartSmall"),
        HAPPY("happy"),
        SMILE("smile"),
        SAD("sad"),
        CONFUSED("confused"),
        ANGRY("angry"),
        ASLEEP("asleep"),
        SURPRISED("surprised"),
        SILLY("silly"),
        FABULOUS("fabulous"),
        MEH("meh"),
        YES("yes"),
        NO("no"),
        TRIANGLE("triangle"),
        TRIANGLE_LEFT("triangleLeft"),
        CHESSBOARD("chessboard"),
        DIAMOND("diamond"),
        DIAMOND_SMALL("diamondSmall"),
        SQUARE("squareBig"),
        SQUARE_SMALL("squareSmall"),
        RABBIT("rabbit"),
        COW("cow"),
        MUSIC_CROTCHET("musicCrotchet"),
        MUSIC_QUAVER("musicQuaver"),
        MUSIC_QUAVERS("musicQuavers"),
        PITCHFORK("pitchfork"),
        XMAS("xmas"),
        PACMAN("pacman"),
        TARGET("target"),
        TSHIRT("tshirt"),
        ROLLERSKATE("rollerskate"),
        DUCK("duck"),
        HOUSE("house"),
        TORTOISE("tortoise"),
        BUTTERFLY("butterfly"),
        STICKFIGURE("stickFigure"),
        GHOST("ghost"),
        SWORD("sword"),
        GIRAFFE("giraffe"),
        SKULL("skull"),
        UMBRELLA("umbrella"),
        SNAKE("snake");

        public final String imageString;

        PredefinedImageEnum(String imageString) {
            this.imageString = imageString;
        }


        public static String getAstName(String textlyName) {
            for ( PredefinedImageEnum mode : values() ) {
                if ( mode.imageString.equalsIgnoreCase(textlyName) ) {
                    return mode.name();
                }
            }
            return null;
        }
    }

    private enum ShiftModes {

        UP("up"),
        DOWN("down"),
        LEFT("left"),
        RIGHT("right");

        private final String textlyName;

        ShiftModes(String textlyName) {
            this.textlyName = textlyName;
        }

        /**
         * Given a textly name return the value needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getAstName(String textlyName) {
            for ( ShiftModes shiftModes : ShiftModes.values() ) {
                if ( shiftModes.textlyName.equalsIgnoreCase(textlyName) ) {
                    return shiftModes.name();
                }
            }
            return null;
        }
    }
}

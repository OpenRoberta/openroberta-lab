package de.fhg.iais.roberta.syntax.codegen.wedo;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedConfigurationBlock;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.wedo.LedOnAction;
import de.fhg.iais.roberta.syntax.check.hardware.wedo.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.RobotSimulationVisitor;
import de.fhg.iais.roberta.syntax.expr.wedo.LedColor;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.wedo.WeDoAstVisitor;

public class SimulationVisitor<V> extends RobotSimulationVisitor<V> implements WeDoAstVisitor<V> {
    protected Set<UsedSensor> usedSensors;
    protected Set<UsedConfigurationBlock> usedConfigurationBlocks;
    protected Set<UsedActor> usedActors;
    protected ArrayList<VarDeclaration<Void>> usedVars;
    private boolean isTimerSensorUsed;
    private Map<Integer, Boolean> loopsLabels;

    private SimulationVisitor(WeDoConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases) {
        super(brickConfiguration);
        UsedHardwareCollectorVisitor codePreprocessVisitor = new UsedHardwareCollectorVisitor(phrases, brickConfiguration);
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.usedConfigurationBlocks = codePreprocessVisitor.getUsedConfigurationBlocks();
        this.isTimerSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    public static String generate(WeDoConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        Assert.notNull(brickConfiguration);

        SimulationVisitor<Void> astVisitor = new SimulationVisitor<Void>(brickConfiguration, phrasesSet);
        astVisitor.generateCodeFromPhrases(phrasesSet);
        return astVisitor.sb.toString();
    }

    @Override
    public V visitDriveAction(DriveAction<V> driveAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitCurveAction(CurveAction<V> curveAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitTurnAction(TurnAction<V> turnAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        String end = createClosingBracket();
        this.sb.append("createTurnLight(CONST." + lightAction.getColor() + ", CONST." + lightAction.getBlinkMode());
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        final String end = createClosingBracket();
        this.sb.append("createStatusLight(CONST.OFF)");
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        boolean isDuration = motorOnAction.getParam().getDuration() != null;
        String actorName = motorOnAction.getPort().getOraName();
        UsedConfigurationBlock confMotorBlock = getConfigurationBlock(actorName);
        if ( confMotorBlock == null ) {
            throw new DbcException("no motor declared in the configuration");
        }
        String brickName = confMotorBlock.getPins().size() >= 1 ? confMotorBlock.getPins().get(0) : null;
        String port = confMotorBlock.getPins().size() >= 2 ? confMotorBlock.getPins().get(1) : null;
        if ( brickName != null && port != null ) {
            String end = createClosingBracket();
            this.sb.append("createMotorOnAction('" + brickName + "', '" + port + "', ");
            motorOnAction.getParam().getSpeed().visit(this);
            if ( isDuration ) {
                this.sb.append(", createDuration(CONST.TIME, ");
                motorOnAction.getParam().getDuration().getValue().visit(this);
                this.sb.append(")");
            }
            this.sb.append(end);
            return null;
        } else {
            this.sb.append("null");
        }
        return null;
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String end = createClosingBracket();
        this.sb.append("createStopMotorAction(");
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        String end = createClosingBracket();
        this.sb.append("createClearDisplayAction(");
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitSetLanguageAction(SetLanguageAction<V> setLanguageAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitSayTextAction(SayTextAction<V> sayTextAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitShowPictureAction(ShowPictureAction<V> showPictureAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitShowTextAction(ShowTextAction<V> showTextAction) {
        String end = createClosingBracket();
        this.sb.append("createShowTextAction(");
        showTextAction.getMsg().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitBrickSensor(BrickSensor<V> brickSensor) {
        this.sb.append("createGetSample(CONST.BUTTONS, CONST." + brickSensor.getPort() + ")");
        return null;
    }

    @Override
    public V visitColorSensor(ColorSensor<V> colorSensor) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        String sensorName = gyroSensor.getPort().getOraName();
        UsedConfigurationBlock confGyroSensor = getConfigurationBlock(sensorName);
        if ( confGyroSensor == null ) {
            throw new DbcException("no gyro sensor declared in the configuration");
        }
        String brickName = confGyroSensor.getPins().size() >= 1 ? confGyroSensor.getPins().get(0) : null;
        String port = confGyroSensor.getPins().size() >= 2 ? confGyroSensor.getPins().get(1) : null;
        String slot = gyroSensor.getSlot().toString();

        if ( brickName != null && port != null ) {
            this.sb.append("createGetSample(CONST.GYRO, '" + brickName + "', '" + port + "', '" + slot + "')");
        } else {
            throw new DbcException("operation not supported");
        }
        return null;
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        String sensorName = infraredSensor.getPort().getOraName();
        UsedConfigurationBlock confInfraredSensor = getConfigurationBlock(sensorName);
        if ( confInfraredSensor == null ) {
            throw new DbcException("no infrared sensor declared in the configuration");
        }
        String brickName = confInfraredSensor.getPins().size() >= 1 ? confInfraredSensor.getPins().get(0) : null;
        String port = confInfraredSensor.getPins().size() >= 2 ? confInfraredSensor.getPins().get(1) : null;
        if ( brickName != null && port != null ) {
            this.sb.append("createGetSample(CONST.INFRARED, '" + brickName + "', '" + port + "')");
        } else {
            this.sb.append("null");
        }
        return null;
    }

    @Override
    public V visitIRSeekerSensor(IRSeekerSensor<V> irSeekerSensor) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitTouchSensor(TouchSensor<V> touchSensor) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitLedAction(LedAction<V> ledAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        throw new DbcException("operation not supported");
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        throw new DbcException("operation not supported");
    }

    UsedConfigurationBlock getConfigurationBlock(String name) {
        for ( UsedConfigurationBlock usedConfigurationBlock : this.usedConfigurationBlocks ) {
            if ( usedConfigurationBlock.getBlockName().equals(name) ) {
                return usedConfigurationBlock;
            }
        }
        return null;
    }

    @Override
    public V visitLedColor(LedColor<V> ledColor) {
        this.sb.append(
            "createConstant(CONST."
                + ledColor.getKind().getName()
                + ", ["
                + ledColor.getRedChannel()
                + ", "
                + ledColor.getGreenChannel()
                + ", "
                + ledColor.getBlueChannel()
                + "])");
        return null;
    }

    @Override
    public V visitLedOnAction(LedOnAction<V> ledOnAction) {
        final String end = createClosingBracket();
        this.sb.append("createLedOnAction(");
        ledOnAction.getLedColor().visit(this);
        this.sb.append(end);
        return null;
    }
}

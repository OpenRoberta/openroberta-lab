package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMicrobitV2Visitor;

public abstract class MbedV2PythonVisitor extends MbedPythonVisitor implements IMicrobitV2Visitor<Void> {
    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param robotConfiguration
     * @param beans
     */
    public MbedV2PythonVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst robotConfiguration,
        String firmware,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, robotConfiguration, firmware, beans);
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
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        this.src.add(this.firmware + ".set_volume(int(2.55 * ");
        setVolumeAction.volume.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        if ( soundToggleAction.mode.equals(SC.OFF) ) {
            this.src.add(this.firmware + ".speaker.off()");
        } else {
            this.src.add(this.firmware + ".speaker.on()");
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.src.add("int((" + this.firmware + ".microphone.sound_level() / 255) * 100)");
        return null;
    }

    @Override
    public Void visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        this.src.add(this.firmware + ".pin_logo.is_touched()");
        return null;
    }

    @Override
    public Void visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode) {
        this.src.add(this.firmware + ".pin_logo.set_touch_mode(" + this.firmware + ".pin_logo.", logoSetTouchMode.mode, ")");
        return null;
    }

    @Override
    public Void visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode) {
        this.src.add(this.firmware + ".pin", pinSetTouchMode.sensorport, ".set_touch_mode(" + this.firmware + ".pin", pinSetTouchMode.sensorport, ".", pinSetTouchMode.mode, ")");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        switch ( playFileAction.fileName ) {
            case "DADADADUM":
            case "ENTERTAINER":
            case "PRELUDE":
            case "ODE":
            case "NYAN":
            case "RINGTONE":
            case "FUNK":
            case "BLUES":
            case "BIRTHDAY":
            case "WEDDING":
            case "FUNERAL":
            case "PUNCHLINE":
            case "PYTHON":
            case "BADDY":
            case "CHASE":
            case "BA_DING":
            case "WAWAWAWAA":
            case "JUMP_UP":
            case "JUMP_DOWN":
            case "POWER_UP":
            case "POWER_DOWN": {
                this.src.add("music.play(music.", playFileAction.fileName, ")");
                break;
            }
            case "GIGGLE":
            case "HAPPY":
            case "HELLO":
            case "MYSTERIOUS":
            case "SAD":
            case "SLIDE":
            case "SOARING":
            case "SPRING":
            case "TWINKLE":
            case "YAWN": {
                this.src.add(this.firmware + ".audio.play(" + this.firmware + ".Sound.", playFileAction.fileName, ")");
                break;
            }
            default:
                throw new DbcException("Invalid play file name: " + playFileAction.fileName);
        }
        return null;
    }

}

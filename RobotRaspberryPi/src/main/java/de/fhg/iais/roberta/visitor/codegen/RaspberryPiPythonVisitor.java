package de.fhg.iais.roberta.visitor.codegen;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.SCRaspberryPi;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedBlinkAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedDimAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedGetAction;
import de.fhg.iais.roberta.syntax.action.raspberrypi.LedSetAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorHexString;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.*;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensors.raspberrypi.SlotSensor;
import de.fhg.iais.roberta.util.TTSLanguageMapper;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class RaspberryPiPythonVisitor extends AbstractPythonVisitor implements IRaspberryPiVisitor<Void> {
    private final ConfigurationAst brickConfiguration;
    private final ILanguage language;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases     to generate the code from
     */
    public RaspberryPiPythonVisitor(List<List<Phrase<Void>>> programPhrases, ILanguage language, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.language = language;
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitColorHexString(ColorHexString<Void> colorHexString) {
        this.sb.append(quote(colorHexString.getValue()));
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("sleep(0.001)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("sleep(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(" / 1000)");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch (timerSensor.getMode()) {
            case SCRaspberryPi.INTENT:
            case SCRaspberryPi.DEFAULT:
            case SCRaspberryPi.VALUE:
                this.sb.append("hal.getTimerValue(1)");
                break;
            case SCRaspberryPi.RESET:
                this.sb.append("hal.resetTimer(1)");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("hal.get_sensor_status()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.accept(this);
        //nlIndent();
        nlIndent();
        if (this.getBean(UsedHardwareBean.class).isActorUsed(SC.SOUND)) {
            this.usedGlobalVarInFunctions.add("__volume");
            this.sb.append("__volume = 50");
            nlIndent();
        }
        this.sb.append("board = Board()");
        nlIndent();
        String modelLanguage = "en";
        if (TTSLanguageMapper.getLanguageString(this.language).equals("de")) {
            modelLanguage = TTSLanguageMapper.getLanguageString(this.language);
        }
        this.sb.append("rSR = RobertaSpeechRecognition(\"").append(modelLanguage).append("\", vocabulary_list)");

        generateUserDefinedMethods();
        if (!this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty()) {
            String helperMethodImpls = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
        }
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        List<Stmt<Void>> variableList = variables.get();
        if (!this.usedGlobalVarInFunctions.isEmpty()) {
            nlIndent();
            this.sb.append("global ").append(String.join(", ", this.usedGlobalVarInFunctions));
        }
        if (!variableList.isEmpty()) {
            nlIndent();
        }
        nlIndent();
        this.sb.append("signal.signal(signal.SIGTERM, _signal_handler)");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if (!withWrapping) {
            return;
        }
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.sb.append("from aiy.board import Board, Led");
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        this.sb.append("import random");
        nlIndent();
        this.sb.append("from time import sleep");
        nlIndent();
        this.sb.append("from sRR import RobertaSpeechRecognition");
        nlIndent();
        this.sb.append("import subprocess as sP");
        nlIndent();
        this.sb.append("import sys");
        nlIndent();
        this.sb.append("import string");
        nlIndent();
        this.sb.append("import signal");
        nlIndent();
        nlIndent();
        this.sb.append("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.sb.append("class ContinueLoop(Exception): pass");
        nlIndent();
        if (!this.getBean(UsedHardwareBean.class).getUsedIntents().isEmpty()) {
            nlIndent();
            List<String> vocabularyList = new ArrayList<>();
            for (ConfigurationComponent usedConfigurationBlock : this.brickConfiguration.getConfigurationComponentsValues()) {
                switch (usedConfigurationBlock.getComponentType()) {
                    case SCRaspberryPi.INTENT:
                    case SCRaspberryPi.SLOT:
                        String varName = usedConfigurationBlock.getComponentType() + "_" + usedConfigurationBlock.getUserDefinedPortName().toLowerCase();
                        this.sb.append(varName).append(" = [");
                        this.sb.append("s.translate(str.maketrans('','', string.punctuation)) for s in [\"");
                        this.sb.append(usedConfigurationBlock.getComponentProperties().values().stream().map(String::toLowerCase).collect(Collectors.joining("\", \"")));
                        this.sb.append("\"]");
                        this.sb.append("]");
                        vocabularyList.add(varName);
                        nlIndent();
                        break;
                    case SCRaspberryPi.KEY:
                        break;
                    case SCRaspberryPi.LED:
                        break;
                    default:
                        throw new DbcException("Configuration block is not supported: " + usedConfigurationBlock.getComponentType());
                }
            }
            this.sb.append("vocabulary_list = ");
            this.sb.append(String.join(" + ", vocabularyList));
            this.sb.append(" + [\"<UNK>\"]");
            nlIndent();
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if (!withWrapping) {
            return;
        }
        nlIndent();
        decrIndentation();
        decrIndentation();
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
        this.sb.append("print('Fehler im Raspi Pi')");
        nlIndent();
        this.sb.append("print(e.__class__.__name__)");
        nlIndent();
        // FIXME: we can only print about 30 chars
        this.sb.append("print(e)");
        decrIndentation();
        nlIndent();
        this.sb.append("finally:");
        incrIndentation();
        nlIndent();
        this.sb.append("board.close()");
        decrIndentation();
        nlIndent();
        decrIndentation();
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
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("board.led.state = Led.").append(lightAction.getMode().toString());
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("hal.light_off(").append(lightStatusAction.getUserDefinedPort()).append(")");
        return null;
    }

    @Override
    public Void visitLedSetAction(LedSetAction<Void> ledSetAction) {
        this.sb.append("hal.set_brightness(").append(ledSetAction.getPort()).append(", ");
        ledSetAction.getBrightness().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedBlinkAction(LedBlinkAction<Void> ledBlinkAction) {
        this.sb.append("hal.blink(").append(ledBlinkAction.getPort()).append(", ");
        ledBlinkAction.getFrequency().accept(this);
        this.sb.append(", ");
        ledBlinkAction.getDuration().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedDimAction(LedDimAction<Void> ledDimAction) {
        this.sb.append("hal.dim(").append(ledDimAction.getPort()).append(", ");
        ledDimAction.getFrom().accept(this);
        this.sb.append(", ");
        ledDimAction.getTo().accept(this);
        this.sb.append(", ");
        ledDimAction.getDuration().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedGetAction(LedGetAction<Void> ledGetAction) {
        this.sb.append("hal.get_brightness(").append(ledGetAction.getPort()).append(")");
        return null;
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

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        this.sb.append("sP.call([\"amixer\", \"-D\", \"pulse\", \"set\", \"Master\", str(__volume) + \"%\" ])");
        nlIndent();
        this.sb.append("sP.call([\"pico2wave\", \"-l=").append(TTSLanguageMapper.getLanguageCountryString(this.language)).append("\", ").append("\"-w\", \"/tmp/a.wav\", ");
        sayTextAction.getMsg().accept(this);
        this.sb.append("])");
        nlIndent();
        this.sb.append("sP.call([\"aplay\", \"/tmp/a.wav\"])");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch (volumeAction.getMode()) {
            case SET:
                this.sb.append("__volume = ");
                volumeAction.getVolume().accept(this);
                break;
            case GET:
                this.sb.append("__volume");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    @Override
    public Void visitIntentStmt(IntentStmt<Void> intentStmt) {
        String intentName = intentStmt.getIntent().toLowerCase();
        this.sb.append("_contains(myphrase.lower(), INTENT_").append(intentName.toLowerCase()).append("):");
        incrIndentation();
        if (intentStmt.get_elseIf() > 0) {
            for (int i = 0; i < intentStmt.getThenList().size(); i++) {
                if (!intentStmt.getExpr().get(i).getKind().hasName("EMPTY_EXPR")) {
                    nlIndent();
                    this.sb.append("if _contains(myphrase.lower(), ");
                    intentStmt.getExpr().get(i).accept(this);
                    this.sb.append("):");
                    incrIndentation();
                    intentStmt.getThenList().get(i).accept(this);
                    nlIndent();
                    this.sb.append("break");
                    decrIndentation();
                }
            }
            intentStmt.getElseList().accept(this);
            nlIndent();
            this.sb.append("break");
        } else {
            intentStmt.getElseList().accept(this);
            nlIndent();
            if (intentStmt.getIntent().toLowerCase().contentEquals("stop")) {
                this.sb.append("sys.exit()");
            } else {
                this.sb.append("break");
            }
        }
        decrIndentation();
        nlIndent();
        return null;
    }

    @Override
    public Void visitSlotSensor(SlotSensor<Void> slotSensor) {
        this.sb.append("SLOT_").append(slotSensor.getValue().toLowerCase());
        return null;
    }

    @Override
    public Void visitListenStepStmt(ListenStepStmt<Void> listenStepStmt) {
        //addBreakLabelToLoop(isWaitStmt)
        this.sb.append("while True:");
        incrIndentation();
        nlIndent();
        this.sb.append("myphrase = rSR.getRecognizedSpeech()");
        nlIndent();
        this.sb.append("if not myphrase:");
        incrIndentation();
        nlIndent();
        this.sb.append("continue");
        decrIndentation();
        nlIndent();
        this.sb.append("print(myphrase)");
        nlIndent();
        List<Stmt<Void>> intents = listenStepStmt.getIntents().get();
        boolean first = true;
        for (Stmt<Void> intent : intents) {
            if (first) {
                this.sb.append("if ");
                first = false;
            } else {
                this.sb.append("elif ");
            }
            intent.accept(this);
        }
        nlIndent();
        String pleaseRepeat;
        if (TTSLanguageMapper.getLanguageString(this.language).equals("de")) {
            pleaseRepeat = "Ich habe das nicht verstanden, bitte versuchen Sie es noch einmal";
        } else {
            pleaseRepeat = "I did not understand, please try again";
        }
        this.sb.append("sP.call([\"amixer\", \"-D\", \"pulse\", \"set\", \"Master\", \"50%\"])");
        nlIndent();
        this.sb.append("sP.call([\"pico2wave\", \"-l=").append(TTSLanguageMapper.getLanguageCountryString(this.language)).append("\", ").append("\"-w=/tmp/a.wav\", \"");
        this.sb.append(pleaseRepeat);
        this.sb.append("\"])");
        nlIndent();
        this.sb.append("sP.call([\"aplay\", \"/tmp/a.wav\"])");
        decrIndentation();
        nlIndent();
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        this.sb.append("_key_pressed()");
        return null;
    }
}

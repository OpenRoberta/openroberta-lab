package de.fhg.iais.roberta.visitor.codegen;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class FestobionicCppVisitor extends NepoArduinoCppVisitor implements IFestobionicVisitor<Void> {

    // mapping of the 4 "ports" of the board to the actual pin numbers
    private static final Map<String, String> PIN_MAPPING = new HashMap<>();
    static {
        PIN_MAPPING.put("1", "25");
        PIN_MAPPING.put("2", "26");
        PIN_MAPPING.put("3", "17");
        PIN_MAPPING.put("4", "16");
    }

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public FestobionicCppVisitor(List<List<Phrase>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitLedAction(LedAction ledAction) {
        String mode = transformOnOff2HighLow(ledAction.mode);
        this.src.add("digitalWrite(_led_", ledAction.port, ", ", mode, ");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        this.src.add("_servo_", motorOnAction.getUserDefinedPort(), ".write(");
        motorOnAction.param.getSpeed().accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        mainTask.variables.accept(this);
        nlIndent();
        generateConfigurationVariables();
        generateTimerVariables();
        long numberConf =
            this.programPhrases
                .stream()
                .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
                .count();
        if ( (this.configuration.getConfigurationComponents().isEmpty() || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER)) && numberConf == 0 ) {
            nlIndent();
        }
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.src.add("void setup()");
        nlIndent();
        this.src.add("{");

        incrIndentation();

        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            this.src.add("Serial.begin(9600);");
            nlIndent();
        }
        generateConfigurationSetup();

        generateUsedVars();
        StringBuilder sb = src.getStringBuilder();
        sb.delete(sb.lastIndexOf("\n"), sb.length());

        decrIndentation();

        nlIndent();
        this.src.add("}");

        nlIndent();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        } else {
            decrIndentation();
        }
        this.src.add("#define _ARDUINO_STL_NOT_NEEDED"); // TODO remove negation and thus double negation in NEPODEFS.h, maybe define when necessary
        nlIndent();
        this.src.add("#define LED_BUILTIN 13");
        nlIndent();
        nlIndent();
        this.src.add("#include <Arduino.h>\n");
        nlIndent();
        this.src.add("#include <NEPODefs.h>");
        nlIndent();
        Set<String> headerFiles = new LinkedHashSet<>();
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.SERVOMOTOR:
                    headerFiles.add("#include <ESP32Servo/src/ESP32Servo.h>");
                    break;
                case SC.LED:
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.componentType);
            }
        }
        for ( String header : headerFiles ) {
            this.src.add(header);
            nlIndent();
        }

        super.generateProgramPrefix(withWrapping);
    }

    private void generateConfigurationSetup() {
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.LED:
                    this.src.add("pinMode(_led_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    this.src.add("_servo_", usedConfigurationBlock.userDefinedPortName, ".attach(", PIN_MAPPING.get(usedConfigurationBlock.getProperty(SC.PULSE)), ");");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.componentType);
            }
        }
    }

    private void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.userDefinedPortName;
            switch ( cc.componentType ) {
                case SC.LED:
                    this.src.add("int _led_", blockName, " = ", cc.getProperty("INPUT"), ";");
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    this.src.add("Servo _servo_", blockName, ";");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.componentType);
            }
        }
    }
}

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
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class FestobionicCppVisitor extends AbstractCommonArduinoCppVisitor implements IFestobionicVisitor<Void> {

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
    public FestobionicCppVisitor(List<List<Phrase<Void>>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("digitalWrite(_led_").append(lightAction.getPort()).append(", ").append(lightAction.getMode().getValues()[0]).append(");");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append("_servo_").append(motorOnAction.getUserDefinedPort()).append(".write(");
        motorOnAction.getParam().getSpeed().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().accept(this);
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
        this.sb.append("void setup()");
        nlIndent();
        this.sb.append("{");

        incrIndentation();

        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            this.sb.append("Serial.begin(9600); ");
            nlIndent();
        }
        generateConfigurationSetup();

        generateUsedVars();
        this.sb.delete(this.sb.lastIndexOf("\n"), this.sb.length());

        decrIndentation();

        nlIndent();
        this.sb.append("}");

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
        this.sb.append("#define _ARDUINO_STL_NOT_NEEDED"); // TODO remove negation and thus double negation in NEPODEFS.h, maybe define when necessary
        nlIndent();
        this.sb.append("#define LED_BUILTIN 13");
        nlIndent();
        nlIndent();
        this.sb.append("#include <Arduino.h>\n");
        nlIndent();
        this.sb.append("#include <NEPODefs.h>");
        nlIndent();
        Set<String> headerFiles = new LinkedHashSet<>();
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.SERVOMOTOR:
                    headerFiles.add("#include <ESP32Servo/src/ESP32Servo.h>");
                    break;
                case SC.LED:
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
            }
        }
        for ( String header : headerFiles ) {
            this.sb.append(header);
            nlIndent();
        }

        super.generateProgramPrefix(withWrapping);
    }

    private void generateConfigurationSetup() {
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.LED:
                    this.sb.append("pinMode(_led_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    this.sb
                        .append("_servo_")
                        .append(usedConfigurationBlock.getUserDefinedPortName())
                        .append(".attach(")
                        .append(PIN_MAPPING.get(usedConfigurationBlock.getProperty(SC.PULSE)))
                        .append(");");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
            }
        }
    }

    private void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.getUserDefinedPortName();
            switch ( cc.getComponentType() ) {
                case SC.LED:
                    this.sb.append("int _led_").append(blockName).append(" = ").append(cc.getProperty("INPUT")).append(";");
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    this.sb.append("Servo _servo_").append(blockName).append(";");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.getComponentType());
            }
        }
    }
}

package de.fhg.iais.roberta.visitor.codegen;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class FestobionicflowerCppVisitor extends NepoArduinoCppVisitor implements IFestobionicflowerVisitor<Void> {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public FestobionicflowerCppVisitor(List<List<Phrase>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
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
        generateConfigurationInitialisationMethods();
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.src.add("void setup() {");
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
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        this.src.add("set_color(");
        rgbLedOnAction.colour.accept(this);
        this.src.add(");");

        return null;
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        this.src.add("set_color(RGB(0x00, 0x00, 0x00));");
        return null;
    }

    @Override
    public Void visitStepMotorAction(StepMotorAction stepMotorAction) {
        this.src.add("set_stepmotorpos(");
        stepMotorAction.stepMotorPos.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        String port = touchSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.configuration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("TOUCHED");
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = usedConfigurationBlock.userDefinedPortName;
            if ( usedConfigurationBlock.componentType.equals(SC.TOUCH) && touchSensor.getSensorMetaDataBean().getPort().equals(blockName) ) {
                if ( pin1.equals("PAD1") ) {
                    this.src.add("_touchsensor_", blockName, ".isRightTouched()");
                }
                if ( pin1.equals("PAD2") ) {
                    this.src.add("_touchsensor_", blockName, ".isLeftTouched()");
                }
            }
        }

        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("getLuminosity()");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        boolean i2cDefinesAdded = false;
        boolean i2cHeaderAdded = false;

        if ( !withWrapping ) {
            return;
        } else {
            decrIndentation();
        }

        //*** generate definitions ***
        this.src.add("#define _ARDUINO_STL_NOT_NEEDED");
        nlIndent();

        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.RGBLED:
                    this.src.add("#define LED_PIN 16");
                    nlIndent();
                    this.src.add("#define NUM_LEDS 5");
                    nlIndent();
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    this.src.add("#define DIR 33");
                    nlIndent();
                    this.src.add("#define STEP 25");
                    nlIndent();
                    this.src.add("#define SLEEP 13");
                    nlIndent();
                    this.src.add("#define MOTOR_STEPS 200");
                    nlIndent();
                    this.src.add("#define MICROSTEPS 1");
                    nlIndent();
                    nlIndent();
                    break;
                case SC.TOUCH:
                    if ( !i2cDefinesAdded ) {
                        this.src.add("#define I2C_SDA 4");
                        nlIndent();
                        this.src.add("#define I2C_SCL 5");
                        nlIndent();
                        i2cDefinesAdded = true;
                    }
                    break;
                case SC.LIGHT:
                    if ( !i2cDefinesAdded ) {
                        this.src.add("#define I2C_SDA 4");
                        nlIndent();
                        this.src.add("#define I2C_SCL 5");
                        nlIndent();
                        i2cDefinesAdded = true;
                    }
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.componentType);
            }
        }

        //*** generate definitions ***
        this.src.add("#include <Arduino.h>");
        nlIndent();
        this.src.add("#include <NEPODefs.h>");
        nlIndent();

        Set<String> headerFiles = new LinkedHashSet<>();
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.RGBLED:
                    headerFiles.add("#include <Adafruit_NeoPixel.h>");
                    break;
                case SC.STEPMOTOR:
                    headerFiles.add("#include <BasicStepperDriver.h>");
                    break;
                case SC.TOUCH:
                    headerFiles.add("#include <SparkFun_CAP1203_Registers.h>");
                    headerFiles.add("#include <SparkFun_CAP1203.h>");
                    if ( !i2cHeaderAdded ) {
                        headerFiles.add("#include <Wire.h>");
                        i2cHeaderAdded = true;
                    }
                    break;
                case SC.LIGHT:
                    headerFiles.add("#include \"RPR-0521RS.h\"");
                    if ( !i2cHeaderAdded ) {
                        headerFiles.add("#include <Wire.h>");
                        i2cHeaderAdded = true;
                    }
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
        boolean i2cInitialized = false;
        int touchInitialized = 0;

        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = usedConfigurationBlock.userDefinedPortName;
            switch ( usedConfigurationBlock.componentType ) {
                case SC.RGBLED:
                    this.src.add("_rgbleds_", blockName, ".begin();");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    this.src.add("_stepper_", blockName, ".begin(RPM, MICROSTEPS);");
                    nlIndent();
                    this.src.add("motor_calibration();");
                    nlIndent();
                    break;
                case SC.TOUCH:
                    if ( !i2cInitialized ) {
                        this.src.add("Wire.begin(I2C_SDA, I2C_SCL, 100000);");
                        nlIndent();
                        i2cInitialized = true;
                    }
                    if ( touchInitialized < 2 ) {
                        this.src.add("while (_touchsensor_", blockName, ".begin() == false) {");
                        incrIndentation();
                        nlIndent();
                        this.src.add("delay(1000);");
                        decrIndentation();
                        nlIndent();
                        this.src.add("}");
                        nlIndent();
                        touchInitialized++;
                    }
                    break;
                case SC.LIGHT:
                    if ( !i2cInitialized ) {
                        this.src.add("Wire.begin(I2C_SDA, I2C_SCL, 100000);");
                        nlIndent();
                        i2cInitialized = true;
                    }
                    this.src.add("rc = _lightsensor_", blockName, ".init();");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.componentType);
            }
        }
    }

    private void generateConfigurationVariables() {
        int touchSensorCreated = 0;
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.userDefinedPortName;
            switch ( cc.componentType ) {
                case SC.RGBLED:
                    this.src.add("Adafruit_NeoPixel _rgbleds_", blockName, "(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    this.src.add("BasicStepperDriver _stepper_", blockName, "(MOTOR_STEPS, DIR, STEP, SLEEP);");
                    nlIndent();
                    this.src.add("int RPM=240;");
                    nlIndent();
                    this.src.add("int FLOWER_CLOSE_TO_OPEN = 120;");
                    nlIndent();
                    this.src.add("int current_position;");
                    nlIndent();
                    break;
                case SC.TOUCH:
                    if ( touchSensorCreated < 2 ) {
                        this.src.add("CAP1203 _touchsensor_", blockName, "=CAP1203(0x28);");
                        nlIndent();
                        touchSensorCreated++;
                    }
                    break;
                case SC.LIGHT:
                    this.src.add("RPR0521RS _lightsensor_", blockName, ";");
                    nlIndent();
                    this.src.add("int rc;");
                    nlIndent();
                    break;
                default:
                    this.src.add(cc.componentType);
                    throw new DbcException("Configuration block is not supported: " + cc.componentType);
            }
        }
    }

    protected void generateConfigurationInitialisationMethods() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.userDefinedPortName;
            switch ( cc.componentType ) {
                case SC.RGBLED:
                    this.src.add("void set_color(uint32_t color) {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("for (int i = 0; i < NUM_LEDS; i++) {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("_rgbleds_", blockName, ".setPixelColor(i,color);");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    nlIndent();
                    this.src.add("_rgbleds_", blockName, ".show();");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    nlIndent();
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    this.src.add("void motor_calibration() {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("for (int i =0; i<FLOWER_CLOSE_TO_OPEN;i++) {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("_stepper_", blockName, ".rotate(-360);");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    nlIndent();
                    this.src.add("current_position=0;");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    nlIndent();
                    this.src.add("void set_stepmotorpos(int pos) {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("if(pos>current_position) {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("while(current_position < pos) {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("_stepper_", blockName, ".rotate(360);");
                    nlIndent();
                    this.src.add("current_position = current_position +1;");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    nlIndent();
                    this.src.add("else {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("while (current_position > pos) {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("_stepper_", blockName, ".rotate(-360);");
                    nlIndent();
                    this.src.add("current_position = current_position -1;");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    nlIndent();
                    nlIndent();
                    break;
                case SC.TOUCH:
                    break;
                case SC.LIGHT:
                    this.src.add("uint32_t getLuminosity() {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("uint32_t proximity;");
                    nlIndent();
                    this.src.add("float luminosity;");
                    nlIndent();
                    this.src.add("rc = _lightsensor_", blockName, ".get_psalsval(&proximity,&luminosity);");
                    nlIndent();
                    this.src.add("return (uint32_t)luminosity;");
                    decrIndentation();
                    nlIndent();
                    this.src.add("}");
                    nlIndent();
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.componentType);
            }
        }
    }
}

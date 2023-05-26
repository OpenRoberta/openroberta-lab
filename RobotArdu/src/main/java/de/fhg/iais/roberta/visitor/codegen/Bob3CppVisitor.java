package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;

/**
 * This class extends {@link NIBOCommonCppVisitor}. Methods {@link #generateProgramPrefix(boolean)} and {@link #visitPinTouchSensor(PinTouchSensor)} are overridden, and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for BOB3.</b> <br>
 */
public final class Bob3CppVisitor extends NIBOCommonCppVisitor {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     * @param beans a map of available beans
     */
    public Bob3CppVisitor(List<List<Phrase>> phrases, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, new ConfigurationAst.Builder().build(), beans);
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.src.add("#include \"bob3.h\"\n");
        this.src.add("#include <stdlib.h>\n");
        this.src.add("#include <math.h>\n");
        this.src.add("Bob3 rob;\n");

        super.generateProgramPrefix(withWrapping);
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        if ( pinTouchSensor.getSlot().equals("0") ) {
            this.src.add("( rob.getArm(", pinTouchSensor.getUserDefinedPort(), ") > ", pinTouchSensor.getSlot(), " )");
        } else {
            this.src.add("( rob.getArm(", pinTouchSensor.getUserDefinedPort(), ") == ", pinTouchSensor.getSlot(), " )");
        }
        return null;
    }
}
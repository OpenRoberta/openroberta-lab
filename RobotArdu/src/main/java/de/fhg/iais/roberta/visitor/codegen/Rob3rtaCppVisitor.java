package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;

/**
 * This class extends {@link NIBOCommonCppVisitor}. Methods {@link #generateProgramPrefix(boolean)} and {@link #visitPinTouchSensor(PinTouchSensor)} are overridden, and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for ROB3RTA.</b> <br>
 */
public final class Rob3rtaCppVisitor extends NIBOCommonCppVisitor {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     * @param beans a map of available beans
     */
    public Rob3rtaCppVisitor(List<List<Phrase>> phrases, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, new ConfigurationAst.Builder().build(), beans);
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#include \"robot.h\"\n");
        this.sb.append("#include <stdlib.h>\n");
        this.sb.append("#include <math.h>\n");
        this.sb.append("#define _ROB3RTA_\n");
        this.sb.append("Robot rob;\n");

        super.generateProgramPrefix(withWrapping);
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        this.sb.append("( rob.getTouch(" + pinTouchSensor.getUserDefinedPort() + "_" + pinTouchSensor.getSlot() + ") == true )");
        return null;
    }

}

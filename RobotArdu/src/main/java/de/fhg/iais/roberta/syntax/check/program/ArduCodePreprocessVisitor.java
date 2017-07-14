package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class ArduCodePreprocessVisitor extends PreprocessProgramVisitor {
    protected ArrayList<VarDeclaration<Void>> visitedVars = new ArrayList<VarDeclaration<Void>>();

    public ArduCodePreprocessVisitor(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        if ( !var.toString().contains("false, false") ) {
            this.visitedVars.add(var);
        }
        return null;
    }

    public ArrayList<VarDeclaration<Void>> getvisitedVars() {
        return this.visitedVars;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        // TODO Auto-generated method stub
        return null;
    }

}

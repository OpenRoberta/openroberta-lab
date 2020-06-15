package de.fhg.iais.roberta.worker;

import org.json.JSONException;
import org.json.JSONObject;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

/**
 * Uses the {@link AbstractStackMachineVisitor} to visit the current AST and generate the robot's specific stack machine code.
 */
public abstract class AbstractStackMachineGeneratorWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        UsedHardwareBean usedHardwareBean = project.getWorkerResult(UsedHardwareBean.class);
        AbstractStackMachineVisitor<Void> visitor = this.getVisitor(project, usedHardwareBean);
        visitor.generateCodeFromPhrases(project.getProgramAst().getTree());
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, visitor.getOpArray()).put(C.FUNCTION_DECLARATION, visitor.getFctDecls());
        project.setSourceCode(generatedCode.toString(2));
        project.setCompiledHex(generatedCode.toString(2));
        JSONObject simSensorConfigurationJSON = new JSONObject();
        for ( ConfigurationComponent sensor : project.getConfigurationAst().getSensors() ) {
            try {
                simSensorConfigurationJSON.put(sensor.getUserDefinedPortName(), sensor.getComponentType());
            } catch ( JSONException e ) {
                throw new DbcException("exception when generating the simulation configuration ", e);
            }
        }
        project.setSimSensorConfigurationJSON(simSensorConfigurationJSON);
        project.setResult(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
    }

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic. Could be removed in the future, when visitors are
     * specified in the properties as well, or inferred from the worker name.
     *
     * @param project the project
     * @param usedHardwareBean the used hardware bean
     * @return the appropriate visitor for the current robot
     */
    protected abstract AbstractStackMachineVisitor<Void> getVisitor(Project project, UsedHardwareBean usedHardwareBean);
}

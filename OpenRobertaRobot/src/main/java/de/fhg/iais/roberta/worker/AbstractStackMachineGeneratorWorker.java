package de.fhg.iais.roberta.worker;

import org.json.JSONObject;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public abstract class AbstractStackMachineGeneratorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        AbstractStackMachineVisitor<Void> visitor = getVisitor((UsedHardwareBean) usedHardwareBean, project);
        visitor.generateCodeFromPhrases(project.getProgramAst().getTree());
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, visitor.getOpArray()).put(C.FUNCTION_DECLARATION, visitor.getFctDecls());
        project.setSourceCode(generatedCode.toString(2));
        project.setCompiledHex(generatedCode.toString(2));
        project.setResult(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
    }

    protected abstract AbstractStackMachineVisitor<Void> getVisitor(UsedHardwareBean usedHardwareBean, Project project);
}

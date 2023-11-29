package de.fhg.iais.roberta.worker.codegen;

import org.json.JSONObject;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.visitor.codegen.WeDoStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;
import de.fhg.iais.roberta.worker.AbstractStackMachineGeneratorWorker;
import de.fhg.iais.roberta.worker.IWorker;

public final class WedoCodeGeneratorWorker implements IWorker {
    protected AbstractStackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        return new WeDoStackMachineVisitor(project.getConfigurationAst(), usedHardwareBean, nnBean);
    }

    @Override
    public void execute(Project project) {
        UsedHardwareBean usedHardwareBean = project.getWorkerResult(UsedHardwareBean.class);
        NNBean nnBean = project.getWorkerResult(NNBean.class);
        AbstractStackMachineVisitor visitor = this.getVisitor(project, usedHardwareBean, nnBean);
        visitor.generateCodeFromPhrases(project.getProgramAst().getTree());
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, visitor.getCode());

        project.setSourceCode(generatedCode.toString(2));
        project.setCompiledHex(generatedCode.toString(2));
        Util.storeGeneratedProgram(project, "/tmp/", generatedCode.toString(2), project.getToken(), project.getProgramName(), "." + project.getBinaryFileExtension());
    }

}

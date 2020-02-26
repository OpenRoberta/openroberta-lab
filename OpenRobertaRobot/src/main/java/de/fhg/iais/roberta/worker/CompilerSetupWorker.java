package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.bean.CompilerSetupBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;

/**
 * Sets up all paths and necessary information for the following compiler workers.
 * Data read from the robot's properties is stored in the {@link CompilerSetupBean}.
 */
public class CompilerSetupWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        IRobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();
        CompilerSetupBean.Builder builder = new Builder();
        builder.setCompilerBinDir(properties.getCompilerBinDir());
        builder.setCompilerResourcesDir(properties.getCompilerResourceDir());
        builder.setTempDir(properties.getTempDir());
        CompilerSetupBean compilerWorkflowBean = builder.build();
        project.addWorkerResult(compilerWorkflowBean);
    }
}

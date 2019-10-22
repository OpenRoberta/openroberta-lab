package de.fhg.iais.roberta.worker.compile;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.bean.CompilerSetupBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.worker.IWorker;

public class ArduinoCompilerSetupWorker implements IWorker {

    @Override
    public void execute(Project project) {
        IRobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();
        Builder builder = new Builder();
        builder.setCompilerBinDir(properties.getCompilerBinDir());
        builder.setCompilerResourcesDir(properties.getCompilerResourceDir());
        builder.setTempDir(properties.getTempDir());
        String robotName = project.getRobot();
        switch ( robotName ) {
            case "uno":
                builder.setFqbn("-fqbn=arduino:avr:uno");
                break;
            case "nano":
                builder.setFqbn("-fqbn=arduino:avr:nano:cpu=atmega328");
                break;
            case "mega":
                builder.setFqbn("-fqbn=arduino:avr:mega:cpu=atmega2560");
                break;
            case "botnroll":
                builder.setFqbn("-fqbn=arduino:avr:uno");
                break;
            case "mbot":
                builder.setFqbn("-fqbn=arduino:avr:uno");
                break;
            case "sensebox":
                builder.setFqbn("-fqbn=sensebox:samd:sb:power=on");
                break;
        }
        CompilerSetupBean compilerWorkflowBean = builder.build();
        project.addWorkerResult("CompilerSetup", compilerWorkflowBean);
    }

}

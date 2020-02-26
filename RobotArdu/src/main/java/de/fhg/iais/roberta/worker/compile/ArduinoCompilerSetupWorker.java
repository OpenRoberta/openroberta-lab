package de.fhg.iais.roberta.worker.compile;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.bean.CompilerSetupBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;
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
            case "unowifirev2":
                builder.setFqbn("-fqbn=arduino:megaavr:uno2018:mode=off");
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
            case "bob3":
                builder.setFqbn("-fqbn=nicai:avr:bob3");
                break;
            case "festobionic":
                builder
                    .setFqbn(
                        "-fqbn=esp32:esp32:esp32:PSRAM=disabled,PartitionScheme=default,CPUFreq=240,FlashMode=qio,FlashFreq=80,FlashSize=4M,UploadSpeed=921600,DebugLevel=none");
                break;
            default:
                throw new DbcException("This type of Arduino is not supported");
        }
        CompilerSetupBean compilerWorkflowBean = builder.build();
        project.addWorkerResult(compilerWorkflowBean);
    }

}

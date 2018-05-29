package de.fhg.iais.roberta.factory.vorwerk;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.vorwerk.VorwerkCommunicator;
import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.codegen.vorwerk.PythonVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.vorwerk.Jaxb2VorwerkConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class CompilerWorkflow extends AbstractCompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflow.class);
    public final String pathToCrosscompilerBaseDir;
    private final VorwerkCommunicator vorwerkCommunicator;

    public CompilerWorkflow(String pathToCrosscompilerBaseDir, String pathToCompilerResourcesDir) {
        this.pathToCrosscompilerBaseDir = pathToCrosscompilerBaseDir;
        this.vorwerkCommunicator = new VorwerkCommunicator(pathToCompilerResourcesDir);
    }

    @Override
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        VorwerkConfiguration configuration = (VorwerkConfiguration) data.getBrickConfiguration();
        this.vorwerkCommunicator.updateRobotInformation(configuration.getIpAddress(), configuration.getUserName(), configuration.getPassword());
        return PythonVisitor.generate((VorwerkConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true, language);
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        Key key;

        try {
            storeGeneratedProgram(token, programName, sourceCode, this.pathToCrosscompilerBaseDir, ".py");
            String programLocation = this.pathToCrosscompilerBaseDir + token + File.separator + programName + File.separator + "src";
            key = this.vorwerkCommunicator.uploadFile(programLocation, programName + ".py");
            if ( key != Key.VORWERK_PROGRAM_UPLOAD_SUCCESSFUL ) {
                return key;
            }
        } catch ( Exception e ) {
            CompilerWorkflow.LOG.error("Uloading the generated program to " + this.vorwerkCommunicator.getIp() + " failed", e);
            return Key.VORWERK_PROGRAM_UPLOAD_ERROR_CONNECTION_NOT_ESTABLISHED;
        }
        return Key.COMPILERWORKFLOW_SUCCESS;
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2VorwerkConfigurationTransformer transformer = new Jaxb2VorwerkConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }
}

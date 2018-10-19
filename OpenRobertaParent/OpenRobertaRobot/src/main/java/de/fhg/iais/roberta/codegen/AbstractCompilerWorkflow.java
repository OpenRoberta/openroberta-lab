package de.fhg.iais.roberta.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class AbstractCompilerWorkflow implements ICompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCompilerWorkflow.class);

    protected final PluginProperties pluginProperties;

    protected Key workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
    protected String generatedSourceCode = null;

    public AbstractCompilerWorkflow(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
    }

    @Override
    public void generateSourceAndCompile(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language) {
        generateSourceCode(token, programName, transformer, language);
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            compileSourceCode(token, programName, language, null);
        }
    }

    @Override
    public final Key getWorkflowResult() {
        return workflowResult;
    }

    @Override
    public void setProgramText(String programText) {
        generatedSourceCode = programText;
    }

    @Override
    public String getGeneratedProgramText() {
        return generatedSourceCode;
    }

    protected final void storeGeneratedProgram(String token, String programName, String ext) {
        try {
            String tempDir = this.pluginProperties.getTempDir();
            Assert.isTrue(token != null && programName != null && generatedSourceCode != null && workflowResult == Key.COMPILERWORKFLOW_SUCCESS);
            File sourceFile = new File(tempDir + token + "/" + programName + "/source/" + programName + ext);
            Path path = Paths.get(tempDir + token + "/" + programName + "/target/");
            try {
                Files.createDirectories(path);
                FileUtils.writeStringToFile(sourceFile, generatedSourceCode, StandardCharsets.UTF_8.displayName());
            } catch ( IOException e ) {
                String msg = "could not write source code to file system";
                LOG.error(msg, e);
                throw new DbcException(msg, e);
            }
            LOG.info("stored under: " + sourceFile.getPath());
        } catch ( Exception e ) {
            LOG.error("Storing the generated program " + programName + " into directory " + token + " failed", e);
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }
    }
}

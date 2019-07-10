package de.fhg.iais.roberta.codegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.StringJoiner;

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
    protected String crosscompilerResponse = "";
    protected String generatedSourceCode = null;

    public AbstractCompilerWorkflow(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
    }

    @Override
    public void generateSourceAndCompile(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language) {
        generateSourceCode(token, programName, transformer, language);
        if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            compileSourceCode(token, programName, language, null);
        }
    }

    @Override
    public final Key getWorkflowResult() {
        return this.workflowResult;
    }

    @Override
    public final void setSourceCode(String sourceCode) {
        this.generatedSourceCode = sourceCode;
    }

    @Override
    public final String getGeneratedSourceCode() {
        return this.generatedSourceCode;
    }

    @Override
    public final String getCrosscompilerResponse() {
        return this.crosscompilerResponse;
    }

    /**
     * run a crosscompiler in a process of its own
     *
     * @param executableWithParameters
     * @return null if the call succeeds; a String with the error message(s) from the crosscompiler otherwise
     */
    protected final boolean runCrossCompiler(String[] executableWithParameters) {
        int ecode = -1;
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(executableWithParameters);
            procBuilder.redirectErrorStream(true);
            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectOutput(Redirect.PIPE);
            Process p = procBuilder.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            crosscompilerResponse = sj.toString();
            ecode = p.waitFor();
            p.destroy();
        } catch ( Exception e ) {
            crosscompilerResponse = "exception when calling the cross compiler";
            LOG.error(crosscompilerResponse, e);
            ecode = -1;
        }
        LOG.error("DEBUG INFO: " + crosscompilerResponse);
        if ( ecode == 0 ) {
            return true;
        } else {
            LOG.error("compilation of program failed with message: \n" + crosscompilerResponse);
            return false;
        }
    }

    protected final void storeGeneratedProgram(String token, String programName, String ext) {
        try {
            String tempDir = this.pluginProperties.getTempDir();
            Assert.isTrue(token != null && programName != null && this.generatedSourceCode != null && this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS);
            File sourceFile = new File(tempDir + token + "/" + programName + "/source/" + programName + ext);
            Path path = Paths.get(tempDir + token + "/" + programName + "/target/");
            try {
                Files.createDirectories(path);
                FileUtils.writeStringToFile(sourceFile, this.generatedSourceCode, StandardCharsets.UTF_8.displayName());
            } catch ( IOException e ) {
                String msg = "could not write source code to file system";
                LOG.error(msg, e);
                throw new DbcException(msg, e);
            }
            LOG.info("stored under: " + sourceFile.getPath());
        } catch ( Exception e ) {
            LOG.error("Storing the generated program " + programName + " into directory " + token + " failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }
    }

    protected final String getBase64Encoded(String path) {
        try {
            String compiledHex = FileUtils.readFileToString(new File(path), "UTF-8");
            final Base64.Encoder urec = Base64.getEncoder();
            compiledHex = urec.encodeToString(compiledHex.getBytes());
            return compiledHex;
        } catch ( IOException e ) {
            LOG.error("Exception when reading the compiled code from " + path, e);
            return null;
        }
    }
}

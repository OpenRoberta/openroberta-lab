package de.fhg.iais.roberta.factory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class AbstractCompilerWorkflow implements ICompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCompilerWorkflow.class);

    /* (non-Javadoc)
     * @see de.fhg.iais.roberta.factory.ICompilerWorkflow#generateSourceAndCompile(java.lang.String, java.lang.String, de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer, de.fhg.iais.roberta.inter.mode.action.ILanguage)
     */
    @Override
    public abstract Key generateSourceAndCompile(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language);

    /* (non-Javadoc)
     * @see de.fhg.iais.roberta.factory.ICompilerWorkflow#generateSourceCode(de.fhg.iais.roberta.factory.IRobotFactory, java.lang.String, java.lang.String, java.lang.String, java.lang.String, de.fhg.iais.roberta.inter.mode.action.ILanguage)
     */
    @Override
    public abstract String generateSourceCode(
        IRobotFactory iRobotFactory,
        String token,
        String programName,
        BlocklyProgramAndConfigTransformer transformer,
        ILanguage language);

    /* (non-Javadoc)
     * @see de.fhg.iais.roberta.factory.ICompilerWorkflow#generateConfiguration(de.fhg.iais.roberta.factory.IRobotFactory, java.lang.String)
     */
    @Override
    public abstract Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception;

    /* (non-Javadoc)
     * @see de.fhg.iais.roberta.factory.ICompilerWorkflow#getCompiledCode()
     */
    @Override
    public abstract String getCompiledCode();

    protected final void storeGeneratedProgram(String token, String programName, String sourceCode, String pathToCrosscompilerBaseDir, String ext) {
        Assert.isTrue(token != null && programName != null && sourceCode != null);
        File sourceFile = new File(pathToCrosscompilerBaseDir + token + "/" + programName + "/src/" + programName + ext);
        Path path = Paths.get(pathToCrosscompilerBaseDir + token + "/" + programName + "/target/");
        try {
            Files.createDirectories(path);
            FileUtils.writeStringToFile(sourceFile, sourceCode, StandardCharsets.UTF_8.displayName());
        } catch ( IOException e ) {
            String msg = "could not write source code to file system";
            LOG.error(msg, e);
            throw new DbcException(msg, e);
        }
        LOG.info("stored under: " + sourceFile.getPath());
    }
}
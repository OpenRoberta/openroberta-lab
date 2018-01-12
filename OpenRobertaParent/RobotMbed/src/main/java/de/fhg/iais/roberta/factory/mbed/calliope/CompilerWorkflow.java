package de.fhg.iais.roberta.factory.mbed.calliope;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.mbed.CalliopeConfiguration;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.check.hardware.mbed.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.mbed.calliope.CppVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.mbed.Jaxb2CalliopeConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class CompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflow.class);

    public final String pathToCrosscompilerBaseDir;
    public final String robotCompilerResourcesDir;
    public final String robotCompilerDir;

    private String compiledHex = "";

    public CompilerWorkflow(String pathToCrosscompilerBaseDir, String robotCompilerResourcesDir, String robotCompilerDir) {
        this.pathToCrosscompilerBaseDir = pathToCrosscompilerBaseDir;
        this.robotCompilerResourcesDir = robotCompilerResourcesDir;

        if ( SystemUtils.IS_OS_WINDOWS && robotCompilerDir != null && robotCompilerDir.equals("") ) {
            this.robotCompilerDir = "\"\"";
        } else {
            this.robotCompilerDir = robotCompilerDir;
        }
    }

    @Override
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        return CppVisitor.generate((CalliopeConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        try {
            storeGeneratedProgram(token, programName, sourceCode, ".cpp");
        } catch ( Exception e ) {
            CompilerWorkflow.LOG.error("Storing the generated program into directory " + token + " failed", e);
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }
        boolean isRadioUsed;
        if ( flagProvider == null ) {
            isRadioUsed = false;
        } else if ( flagProvider instanceof EnumSet<?> ) {
            EnumSet<?> flags = (EnumSet<?>) flagProvider;
            isRadioUsed = flags.contains(CompilerFlag.RADIO_USED);
        } else {
            isRadioUsed = false;
        }
        Key messageKey = runBuild(token, programName, "generated.main", isRadioUsed);
        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            CompilerWorkflow.LOG.info("hex for program {} generated successfully", programName);
        } else {
            CompilerWorkflow.LOG.info(messageKey.toString());
        }
        return messageKey;
    }

    @Override
    public Key generateSourceAndCompile(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language) {
        String sourceCode = generateSourceCode(token, programName, transformer, language);
        UsedHardwareCollectorVisitor usedHardwareVisitor =
            new UsedHardwareCollectorVisitor(transformer.getProgramTransformer().getTree(), transformer.getBrickConfiguration());
        EnumSet<CompilerFlag> compilerFlags = usedHardwareVisitor.isRadioUsed() ? EnumSet.of(CompilerFlag.RADIO_USED) : EnumSet.noneOf(CompilerFlag.class);
        return compileSourceCode(token, programName, sourceCode, language, compilerFlags);
    }

    private void storeGeneratedProgram(String token, String programName, String sourceCode, String ext) throws Exception {
        Assert.isTrue(token != null && programName != null && sourceCode != null);
        File sourceFile = new File(this.pathToCrosscompilerBaseDir + token + "/" + programName + "/source/" + programName + ext);
        Path path = Paths.get(this.pathToCrosscompilerBaseDir + token + "/" + programName + "/target/");
        Files.createDirectories(path);
        CompilerWorkflow.LOG.info("stored under: " + sourceFile.getPath());
        FileUtils.writeStringToFile(sourceFile, sourceCode, StandardCharsets.UTF_8.displayName());
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2CalliopeConfigurationTransformer transformer = new Jaxb2CalliopeConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return this.compiledHex;
    }

    /**
     * 1. Make target folder (if not exists).<br>
     * 2. Clean target folder (everything inside).<br>
     * 3. Compile .java files to .class.<br>
     * 4. Make jar from class files and add META-INF entries.<br>
     *
     * @param token
     * @param mainFile
     * @param mainPackage
     */
    private Key runBuild(String token, String mainFile, String mainPackage, boolean radioUsed) {
        final StringBuilder sb = new StringBuilder();
        String scriptName = this.robotCompilerResourcesDir + "/../compile." + (SystemUtils.IS_OS_WINDOWS ? "bat" : "sh");
        String bluetooth = radioUsed ? "" : "-b";
        Path path = Paths.get(this.pathToCrosscompilerBaseDir + token + "/" + mainFile);
        Path base = Paths.get("");

        try {
            ProcessBuilder procBuilder =
                new ProcessBuilder(
                    new String[] {
                        scriptName,
                        this.robotCompilerDir,
                        mainFile,
                        base.resolve(path).toAbsolutePath().normalize().toString() + "/",
                        this.robotCompilerResourcesDir,
                        bluetooth
                    });

            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectOutput(Redirect.INHERIT);
            procBuilder.redirectError(Redirect.INHERIT);
            Process p = procBuilder.start();
            int ecode = p.waitFor();
            System.err.println("Exit code " + ecode);

            if ( ecode != 0 ) {
                return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
            this.compiledHex = FileUtils.readFileToString(new File(path + "/target/" + mainFile + ".hex"), "UTF-8");
            return Key.COMPILERWORKFLOW_SUCCESS;
        } catch ( Exception e ) {
            if ( sb.length() > 0 ) {
                CompilerWorkflow.LOG.error("build exception. Messages from the build script are:\n" + sb.toString(), e);
            } else {
                CompilerWorkflow.LOG.error("exception when preparing the build", e);
            }
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}

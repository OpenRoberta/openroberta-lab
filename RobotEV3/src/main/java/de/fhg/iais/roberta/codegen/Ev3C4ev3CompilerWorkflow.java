package de.fhg.iais.roberta.codegen;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ev3c4ev3.C4Ev3SourceCompiler;
import de.fhg.iais.roberta.components.ev3c4ev3.RbfBuilder;
import de.fhg.iais.roberta.components.ev3c4ev3.Uf2FileContainer;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.ev3.Jaxb2Ev3ConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.Ev3C4ev3Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Ev3C4ev3CompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3C4ev3CompilerWorkflow.class);

    public static final String GENERATED_SOURCE_CODE_EXTENSION = ".c";
    private static final String GENERATED_BINARY_EXTENSION = ".elf";
    private static final String GENERATED_RBF_EXTENSION = ".rbf";

    private static final String EV3_PROGRAMS_FOLDER_PATH = "../prjs/BrkProg_SAVE/";

    private final String tempDir;
    private final C4Ev3SourceCompiler compiler;
    private final RbfBuilder rbfBuilder;

    private String uf2InBase64;

    public Ev3C4ev3CompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
        String compilerResourceDir = pluginProperties.getCompilerResourceDir();
        tempDir = pluginProperties.getTempDir();
        compiler = new C4Ev3SourceCompiler(compilerResourceDir);
        rbfBuilder = new RbfBuilder(compilerResourceDir);
    }

    @Override
    public void generateSourceCode(
        String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            this.generatedSourceCode =
                Ev3C4ev3Visitor.generate(programName, data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true, language);
            LOG.info("c4ev3 code generated");
        } catch ( Exception e ) {
            LOG.error("c4ev3 code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        if ( this.workflowResult != Key.COMPILERWORKFLOW_SUCCESS ) {
            return;
        }
        storeGeneratedProgram(token, programName, GENERATED_SOURCE_CODE_EXTENSION);
        String sourceCodeFileName = getSourceCodeFileName(token, programName);
        String binaryFileName = getBinaryFileName(token, programName);
        boolean compilationSuccess = compiler.compile(sourceCodeFileName, binaryFileName);
        if ( !compilationSuccess ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            return;
        }
        boolean uf2FileCreated = createUf2File(programName, binaryFileName);
        if ( !uf2FileCreated ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            return;
        }
        LOG.info("uf2 for program {} generated successfully", programName);
        this.workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
    }

    private String getSourceCodeFileName(String token, String programName) {
        return tempDir + token + "/" + programName + "/source/" + programName + GENERATED_SOURCE_CODE_EXTENSION;
    }

    private String getBinaryFileName(String token, String programName) {
        return tempDir + token + "/" + programName + "/target/" + programName + GENERATED_BINARY_EXTENSION;
    }

    private boolean createUf2File(String programName, String binaryFileName) {
        Uf2FileContainer uf2 = new Uf2FileContainer();
        try {
            addBinaryToUf2(uf2, binaryFileName, programName);
        } catch ( IOException e ) {
            LOG.error("cannot read the binary file obtained as result of the compilation: {}", e);
            return false;
        }
        addRbfToUf2(uf2, programName);
        this.uf2InBase64 = uf2.toBase64();
        return true;
    }

    private void addBinaryToUf2(Uf2FileContainer uf2, String binaryFileName, String programName) throws IOException {
        uf2.add(new File(binaryFileName), getBinaryFileNameInEv3Storage(programName));
    }

    private void addRbfToUf2(Uf2FileContainer uf2, String programName) {
        uf2.add(rbfBuilder.build(getBinaryFileNameInEv3Storage(programName)), getRbfFileNameInEv3Storage(programName));
    }

    private String getBinaryFileNameInEv3Storage(String programName) {
        return EV3_PROGRAMS_FOLDER_PATH + programName + GENERATED_BINARY_EXTENSION;
    }

    private String getRbfFileNameInEv3Storage(String programName) {
        return EV3_PROGRAMS_FOLDER_PATH + programName + GENERATED_RBF_EXTENSION;
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2Ev3ConfigurationTransformer transformer = new Jaxb2Ev3ConfigurationTransformer(factory.getBlocklyDropdownFactory());
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return this.uf2InBase64;
    }
}

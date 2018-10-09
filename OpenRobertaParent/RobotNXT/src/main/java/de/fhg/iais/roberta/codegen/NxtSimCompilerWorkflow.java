package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.codegen.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.nxt.Jaxb2NxtConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.NxtSimVisitor;

public class NxtSimCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(NxtSimCompilerWorkflow.class);

    public NxtSimCompilerWorkflow() {
    }

    @Override
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) //
    {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        String sourceCode = NxtSimVisitor.generate(data.getBrickConfiguration(), data.getProgramTransformer().getTree());
        LOG.info("generating javascript code");
        return sourceCode;
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        return null;
    }

    @Override
    public Key generateSourceAndCompile(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language) {
        return null;
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2NxtConfigurationTransformer transformer = new Jaxb2NxtConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        // TODO Auto-generated method stub
        return null;
    }

}

package de.fhg.iais.roberta.util.test;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;

public class GenericHelperForXmlTest extends AbstractHelperForXmlTest {
    public GenericHelperForXmlTest() {
        super(new TestFactory(new PluginProperties("test", "", "", Util1.loadProperties("classpath:/pluginProperties/test.properties"))), (Configuration) null);
    }

    private static class TestFactory extends AbstractRobotFactory {

        public TestFactory(PluginProperties pluginProperties) {
            super(pluginProperties);
        }

        @Override
        public ICompilerWorkflow getRobotCompilerWorkflow() {
            return null;
        }

        @Override
        public ICompilerWorkflow getSimCompilerWorkflow() {
            return null;
        }

        @Override
        public String getFileExtension() {
            return null;
        }

        @Override
        public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
            return null;
        }

        @Override
        public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
            return null;
        }

        @Override
        public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
            return null;
        }

    }
}

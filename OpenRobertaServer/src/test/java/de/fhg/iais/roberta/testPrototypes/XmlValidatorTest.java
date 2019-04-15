package de.fhg.iais.roberta.testPrototypes;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.NaoFactory;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class XmlValidatorTest {

    @Ignore
    @Test
    public void testBlocklyXmlValidaty() throws Exception {
        String xmlAsString = Util1.readResourceContent("/crossCompilerTests/nao/FaceRecognitionTest.xml");
        Export jaxbImportExport = JaxbHelper.xml2Element(xmlAsString, Export.class);
        String programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
        String configText = JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet());
        IRobotFactory robotFactory = new NaoFactory(new PluginProperties("nao", "", "", Util1.loadProperties("classpath:/nao.properties")));
        BlocklyProgramAndConfigTransformer programAndConfigTransformer = BlocklyProgramAndConfigTransformer.transform(robotFactory, programText, configText);
        System.out.println(jaxbImportExport);
    }

}

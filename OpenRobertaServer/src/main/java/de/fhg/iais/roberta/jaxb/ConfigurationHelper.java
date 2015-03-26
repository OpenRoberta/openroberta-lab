package de.fhg.iais.roberta.jaxb;

import de.fhg.iais.roberta.ast.transformer.JaxbBrickConfigTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.brickconfiguration.BrickConfiguration;
import de.fhg.iais.roberta.conf.transformer.ConfigurationParseTree2ConfigurationVisitor;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;

/**
 * The frontend delivers a XML representation of a robot configuration. The database stores a corresponding textual representation.<br>
 * This class converts between this two representations.<br>
 * <br>
 * It is an open question, whether the jaxb- and antlr4-based processors, that do the real work, should be injected or not. Any opinion?
 *
 * @author rbudde
 */
public class ConfigurationHelper {
    private ConfigurationHelper() {
        // no objects!
    }

    public static String xmlString2textString(String name, String xmlString) throws Exception {
        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        BlockSet bs = JaxbHelper.xml2BlockSet(xmlString);
        BrickConfiguration bc = transformer.transform(bs);
        String textString = bc.generateText(name);
        return textString;
    }

    public static String textString2xmlString(String textString) throws Exception {
        JaxbBrickConfigTransformer transformer = new JaxbBrickConfigTransformer();
        EV3BrickConfiguration bc = (EV3BrickConfiguration) ConfigurationParseTree2ConfigurationVisitor.startWalkForVisiting(textString);
        BlockSet bs = transformer.transformInverse(bc);
        String xmlString = JaxbHelper.blockSet2xml(bs);
        return xmlString;
    }
}

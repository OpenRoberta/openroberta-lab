package de.fhg.iais.roberta.jaxb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.transformer.ev3.Ev3ConfigurationParseTree2Ev3ConfigurationVisitor;
import de.fhg.iais.roberta.transformer.ev3.Jaxb2Ev3ConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Option;

/**
 * The frontend delivers a XML representation of a robot configuration. The database stores a corresponding textual representation.<br>
 * This class converts between this two representations.<br>
 * <br>
 * It is an open question, whether the jaxb- and antlr4-based processors, that do the real work, should be injected or not. Any opinion?
 *
 * @deprecated as the configurations for various robot system are not stable enough yet, the idea of textual representation should be taken into account in the
 *             (far?) future
 */
@Deprecated
public class ConfigurationHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationHelper.class);

    private ConfigurationHelper() {
        // no objects!
    }

    @Deprecated
    public static String xmlString2textString(String name, String xmlString) throws Exception {
        Jaxb2Ev3ConfigurationTransformer transformer = new Jaxb2Ev3ConfigurationTransformer();
        BlockSet bs = JaxbHelper.xml2BlockSet(xmlString);
        Ev3Configuration bc = transformer.transform(bs);
        String textString = bc.generateText(name);
        return textString;
    }

    /**
     * take a textual representation of a configuration (as string) and transform it into a XML representation (as string)
     *
     * @param textString the textual representation of a configuration (as string)
     * @return a XML representation (as string) of a configuration, wrapped in an Option object. <b>Never null, no exceptions.</b>
     */
    @Deprecated
    public static Option<String> textString2xmlString(String textString) {
        try {
            Jaxb2Ev3ConfigurationTransformer transformer = new Jaxb2Ev3ConfigurationTransformer();
            Option<Ev3Configuration> msgConf = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(textString);
            if ( msgConf.isSet() ) {
                Ev3Configuration bc = msgConf.getVal();
                BlockSet bs = transformer.transformInverse(bc);
                String xmlString = JaxbHelper.blockSet2xml(bs);
                return Option.of(xmlString);
            } else {
                return Option.empty(msgConf.getMessage());
            }
        } catch ( Exception e ) {
            ConfigurationHelper.LOG.error("configuration transform failed", e);
            return Option.empty(Key.CONFIGURATION_TRANSFORM_ERROR.toString());
        }
    }
}

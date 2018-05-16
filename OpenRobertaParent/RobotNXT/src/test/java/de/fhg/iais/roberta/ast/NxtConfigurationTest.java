package de.fhg.iais.roberta.ast;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.nxt.Factory;
import de.fhg.iais.roberta.transformer.nxt.Jaxb2NxtConfigurationTransformer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class NxtConfigurationTest {
    Factory factory = new Factory(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void testRoundtrip() throws Exception {
        testRoundtrip("brick_configuration0");
    }

    @Ignore
    @Test
    public void testText2Text() throws Exception {
        testText2Text();
        testText2Text();
        testText2Text();
    }

    @Test
    public void test1() throws Exception {
        String a = //
            "robotnxttest{" //
                + "size{wheeldiameter5.0cm;trackwidth17.0cm;}"
                + "sensorport{3:infrared;}"
                + "actorport{A:middlemotor,regulated,forward;}}";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration1.xml");
        Jaxb2NxtConfigurationTransformer transformer = new Jaxb2NxtConfigurationTransformer(this.factory);
        Configuration b = transformer.transform(project);
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateText("test").replaceAll("\\s+", ""));
    }

    @Test
    public void test2() throws Exception {
        String a =
            "robotnxttest{"
                + "size{wheeldiameter5.6cm;trackwidth17.0cm;}"
                + "sensorport{1:touch;4:ultrasonic;}"
                + "actorport{B:largemotor,regulated,forward,right;C:largemotor,regulated,forward,left;}}";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration2.xml");
        Jaxb2NxtConfigurationTransformer transformer = new Jaxb2NxtConfigurationTransformer(this.factory);
        Configuration b = transformer.transform(project);
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateText("test").replaceAll("\\s+", ""));
    }

    @Test
    public void test3() throws Exception {
        String a =
            "robotnxttest{"
                + "size{wheeldiameter5.6cm;trackwidth17.0cm;}"
                + "sensorport{1:touch;4:ultrasonic;}"
                + "actorport{B:largemotor,regulated,forward,right;C:largemotor,regulated,forward,left;}}";

        BlockSet project = JaxbHelper.path2BlockSet("/ast/brickConfiguration/brick_configuration3.xml");
        Jaxb2NxtConfigurationTransformer transformer = new Jaxb2NxtConfigurationTransformer(this.factory);
        Configuration b = transformer.transform(project);
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.generateText("test").replaceAll("\\s+", ""));
    }

    /**
     * ROUND TRIP FOR CONFIGURATION TESTS. Expects three different representations of a configuration as text files.<br>
     * <br>
     * 1. make a String from a XML file, call it xmlExpected, and generate a BrickConfiguration bc1<br>
     * 2. from the BrickConfiguration bc1 generate text and check it against textExpected<br>
     * 3. from the text generate a BrickConfiguration bc2 and check it against bc1<br>
     * 4. from the BrickConfiguration bc2 generate XML xmlActual and check it against xmlExpected<br>
     * 5. from xmlActual generate a BrickConfiguration bc3 and check it against bc1 and bc2.<br>
     */
    private void testRoundtrip(String baseName) throws Exception {
        //        Jaxb2NxtConfigurationTransformer transformer = new Jaxb2NxtConfigurationTransformer(this.factory);
        //        // 1.
        //        String xmlExpected = resourceAsString(baseName + ".xml");
        //        BlockSet bs1 = JaxbHelper.xml2BlockSet(xmlExpected);
        //        Configuration bc1 = transformer.transform(bs1);
        //        // 2.
        //        String textExpected = resourceAsString(baseName + ".conf");
        //        String text = bc1.generateText("craesy");
        //        assertEq(textExpected, text);
        //        // 3.
        //        Configuration bc2 = Ev3ConfigurationParseTree2Ev3ConfigurationVisitor.startWalkForVisiting(text).getVal();
        //        Assert.assertEquals(bc1, bc2);
        //        // 4.
        //        BlockSet bs2 = transformer.transformInverse(bc2);
        //        String xmlActual = JaxbHelper.blockSet2xml(bs2);
        //        assertEq(xmlExpected, xmlActual);
        //        // 5.
        //        BlockSet bs3 = JaxbHelper.xml2BlockSet(xmlActual);
        //        Ev3Configuration bc3 = transformer.transform(bs3);
        //        Assert.assertEquals(bc1, bc3);
        //        Assert.assertEquals(bc2, bc3);
    }

    private String resourceAsString(String name) throws Exception {
        return IOUtils.toString(NxtConfigurationTest.class.getResourceAsStream("/ast/brickConfiguration/" + name), "UTF-8");
    }

    private void assertEq(String expected, String actual) {
        Assert.assertEquals(expected.replaceAll("\\s+", ""), actual.replaceAll("\\s+", ""));
    }
}

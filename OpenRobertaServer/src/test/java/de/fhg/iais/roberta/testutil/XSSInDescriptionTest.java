package de.fhg.iais.roberta.testutil;

import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.XsltTransformer;

public class XSSInDescriptionTest {
    private static XsltTransformer XSLT_TRANSFORMER;
    private static Pattern SCRIPT_MATCHER;

    @BeforeClass
    public static void setup() {
        XSLT_TRANSFORMER = new XsltTransformer();
        SCRIPT_MATCHER = Pattern.compile("\\bscript\\b");
    }

    @Test
    // this test could do more. It is only tested, that script injection is impossible.
    public void testXSSInDescription() throws Exception {
        String xssXml = IOUtils.toString(this.getClass().getResourceAsStream("/testutil/descriptionTest.xml"), "UTF-8");
        assert (SCRIPT_MATCHER.matcher(xssXml).find()); //here we have a script tag
        String cleanXml = UtilForHtmlXml.removeUnwantedDescriptionHTMLTags(xssXml);
        assert (!SCRIPT_MATCHER.matcher(cleanXml).find()); // and here we don't have it :-)
    }

    @Test
    public void testValidityOfReplacement() throws Exception {
        String xssValidXml = IOUtils.toString(this.getClass().getResourceAsStream("/testutil/xssValid.xml"), "UTF-8");
        String xssInvalidXml = IOUtils.toString(this.getClass().getResourceAsStream("/testutil/xssInvalid.xml"), "UTF-8");
        String cleanValidXml = UtilForHtmlXml.checkProgramTextForXSS(xssValidXml);
        String cleanInValidXml = UtilForHtmlXml.checkProgramTextForXSS(xssInvalidXml);
        // must be valid XML. Check that:
        XSLT_TRANSFORMER.transform(cleanValidXml);
        XSLT_TRANSFORMER.transform(cleanInValidXml);
        assert (!SCRIPT_MATCHER.matcher(cleanValidXml).find());
        assert (!SCRIPT_MATCHER.matcher(cleanInValidXml).find());
    }

}

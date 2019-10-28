package de.fhg.iais.roberta.testutil;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.fhg.iais.roberta.util.UtilForHtmlXml;

public class XSSInDescriptionTest {
    @Test
    public void testXSS() throws Exception {
        String xssXml = IOUtils.toString(this.getClass().getResourceAsStream("/testutil/xssTest.xml"), "UTF-8");
        assert (xssXml.contains("&lt;script&gt;")); //here we have script tag
        String cleanXml = UtilForHtmlXml.removeUnwantedDescriptionHTMLTags(xssXml);
        assert (!cleanXml.contains("&lt;script&gt;")); // and here we don't have it
    }

}

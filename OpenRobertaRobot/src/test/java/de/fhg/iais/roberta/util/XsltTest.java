package de.fhg.iais.roberta.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class XsltTest {

    private static XsltTransformer xsltTransformer;

    @BeforeClass
    public static void setup() {
        xsltTransformer = new XsltTransformer();
    }

    @Test
    public void transform_ShouldReturnTransformedXML_WhenGivenOldXML() throws Exception {
        String input = Resources.toString(Resources.getResource("util/Xslt_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("util/Xslt_out.xml"), Charsets.UTF_8);
        Assert.assertNull(UnitTestHelper.runXmlUnit(expected, xsltTransformer.transform(input)));
    }

    @Test
    public void transform_ShouldReturnTransformedCalliopeXML_WhenGivenOldCalliopeXML() throws Exception {
        String input = Resources.toString(Resources.getResource("util/Xslt_calliope_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("util/Xslt_calliope_out.xml"), Charsets.UTF_8);
        Assert.assertNull(UnitTestHelper.runXmlUnit(expected, xsltTransformer.transform(input)));
    }
}

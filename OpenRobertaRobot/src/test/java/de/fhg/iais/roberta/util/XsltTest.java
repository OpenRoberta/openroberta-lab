package de.fhg.iais.roberta.util;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

    @Ignore
    @Test
    public void transform_test() throws Exception {
        String input = Resources.toString(Resources.getResource("util/led_blocks_test/in/calliope.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("util/led_blocks_test/out/calliope.xml"), Charsets.UTF_8);
        Assert.assertNull(UnitTestHelper.runXmlUnit(expected, xsltTransformer.transform(input)));
    }

    @Test
    public void transform_led_blocks_test() {
        String inputDir = String.valueOf(Paths.get("src/test/resources/util/led_blocks_test/in").toAbsolutePath());
        String outputDir = String.valueOf(Paths.get("src/test/resources/util/led_blocks_test/out").toAbsolutePath());
        Util.fileStreamOfFileDirectory(inputDir).forEach(f -> {
            String input = Util.readFileContent(inputDir + '/' + f);
            String output = Util.readFileContent(outputDir + '/' + f);
            Assert.assertNull(UnitTestHelper.runXmlUnit(output, xsltTransformer.transform(input)));
        });
    }
}

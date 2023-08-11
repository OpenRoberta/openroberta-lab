package de.fhg.iais.roberta.util;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class XsltTest {

    private static XsltAndJavaTransformer xsltAndJavaTransformer;

    @BeforeClass
    public static void setup() {
        xsltAndJavaTransformer = new XsltAndJavaTransformer();
    }

    @Test
    public void transform_ShouldReturnTransformedXML_WhenGivenOldXML() throws Exception {
        String input = Resources.toString(Resources.getResource("util/Xslt_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("util/Xslt_out.xml"), Charsets.UTF_8);
        String transformed1 = xsltAndJavaTransformer.transformXslt(input);
        Assert.assertNull(UnitTestHelper.runXmlUnit(expected, transformed1));
//        String transformed2 = xsltAndJavaTransformer.transformXslt(transformed1);
//        Assert.assertNull(UnitTestHelper.runXmlUnit(expected, transformed2));
    }

    @Test
    public void transform_ShouldReturnTransformedCalliopeXML_WhenGivenOldCalliopeXML() throws Exception {
        String input = Resources.toString(Resources.getResource("util/Xslt_calliope_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("util/Xslt_calliope_out.xml"), Charsets.UTF_8);
        String transformed1 = xsltAndJavaTransformer.transformXslt(input);
        Assert.assertNull(UnitTestHelper.runXmlUnit(expected, transformed1));
//        String transformed2 = xsltAndJavaTransformer.transformXslt(transformed1);
//        Assert.assertNull(UnitTestHelper.runXmlUnit(expected, transformed2));
    }

    @Test
    public void transform_led_blocks_test() {
        String inputDir = String.valueOf(Paths.get("src/test/resources/util/led_blocks_test/in").toAbsolutePath());
        String outputDir = String.valueOf(Paths.get("src/test/resources/util/led_blocks_test/out").toAbsolutePath());
        Util.fileStreamOfFileDirectory(inputDir).forEach(f -> {
            String input = Util.readFileContent(inputDir + '/' + f);
            String expected = Util.readFileContent(outputDir + '/' + f);
            String transformed1 = xsltAndJavaTransformer.transformXslt(input);
            Assert.assertNull(UnitTestHelper.runXmlUnit(expected, transformed1));
//            String transformed2 = xsltAndJavaTransformer.transformXslt(transformed1);
//            Assert.assertNull(UnitTestHelper.runXmlUnit(expected, transformed2));
        });
    }
}

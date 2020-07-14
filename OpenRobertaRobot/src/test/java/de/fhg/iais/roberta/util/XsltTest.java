package de.fhg.iais.roberta.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class XsltTest {
    // not a valid configuration according to the xsd!
    private static final String EMPTY_CONFIG = "<block_set/>";

    @Test
    @Ignore
    public void transform_ShouldReturnTransformedXML_WhenGivenOldXML() throws Exception {
        String input = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_out.xml"), Charsets.UTF_8);

        String transformed = XsltTransformer.getInstance().transform(input, EMPTY_CONFIG).getFirst();

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(expected, transformed);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    @Test
    @Ignore
    public void transform_ShouldReturnTransformedCalliopeXML_WhenGivenOldCalliopeXML() throws Exception {
        String input = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_calliope_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_calliope_out.xml"), Charsets.UTF_8);

        String transformed = XsltTransformer.getInstance().transform(input, EMPTY_CONFIG).getFirst();

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(expected, transformed);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    @Test
    @Ignore
    public void transform_ShouldReturnTransformedCalliope2XML_WhenGivenOldCalliope2XML() throws Exception {
        String input = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_calliope2_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_calliope2_out.xml"), Charsets.UTF_8);

        String transformed = XsltTransformer.getInstance().transform(input, EMPTY_CONFIG).getFirst();

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(expected, transformed);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    /**
     * This is an optional test, used for additional validation of converted programs.
     * It requires a specifically formatted JSON file, e.g. an export from the database.
     * It should contain one root node with a list of program entries. Each entry should at least contain a PROGRAM_TEXT field with the program XML
     * Also requires blockly (with the calliconf-headless branch) being on the same level as the lab repository.
     */
    @Test
    @Ignore
    public void transform_ShouldReturnTransformedCalliopeXML_WhenGivenOldCalliopeDbEntries() throws Exception {
        JSONTokener tokener =
            new JSONTokener(Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/db/database_export_calliope.json"), StandardCharsets.UTF_8));
        JSONObject root = new JSONObject(tokener);
        JSONArray list = root.getJSONArray(root.keys().next());
        for ( int i = 0; i < list.length(); i++ ) {
            // Read block set XML
            JSONObject entry = list.getJSONObject(i);
            System.out.println(entry.getString("CREATED"));
            String input = entry.getString("PROGRAM_TEXT");

            // Windows has a problem with large console inputs/outputs, use a temp file for headless blockly as a workaround
            Path ioFile = Files.createTempFile("", ".xml");
            ioFile.toFile().deleteOnExit();
            Files.write(ioFile, input.getBytes(StandardCharsets.UTF_8));

            // Run headless blockly
            String absolutePath = ioFile.toAbsolutePath().toString().replace("\\", "\\\\");
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("node", "../../blockly/headless/transform.js", "-f", absolutePath, absolutePath); // use input file as output file as well
            Process start = builder.start();
            if ( start.waitFor() != 0 ) {
                Assert.fail(IOUtils.toString(start.getErrorStream(), Charsets.UTF_8));
            }
            String expected = IOUtils.toString(ioFile.toUri(), StandardCharsets.UTF_8);
            Assert.assertFalse(expected, expected.contains("Blockly.Xml.textToDom did not obtain a valid XML tree."));
            Assert.assertFalse(expected, expected.contains("Error [AssertionError]:"));

            // Run transformer
            String transformed = XsltTransformer.getInstance().transform(input, EMPTY_CONFIG).getFirst();

            // Compare results
            XMLUnit.setIgnoreWhitespace(true);
            Diff diff = XMLUnit.compareXML(expected, transformed);
            Assert.assertTrue(diff.toString(), diff.identical());
        }
    }
}

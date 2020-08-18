package de.fhg.iais.roberta.util;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class XsltTest {

    private static XsltTransformer xsltTransformer;

    @BeforeClass
    public static void setup() {
        xsltTransformer = new XsltTransformer();
    }

    @Test
    public void transform_ShouldReturnTransformedXML_WhenGivenOldXML() throws Exception {
        String input = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_out.xml"), Charsets.UTF_8);

        String transformed = xsltTransformer.transform(input);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(expected, transformed);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    @Test
    public void transform_ShouldReturnTransformedCalliopeXML_WhenGivenOldCalliopeXML() throws Exception {
        String input = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_calliope_in.xml"), Charsets.UTF_8);
        String expected = Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/Xslt_calliope_out.xml"), Charsets.UTF_8);

        String transformed = xsltTransformer.transform(input);

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
    public void transform_ShouldReturnTransformedXML_WhenGivenOldDbEntries() throws Exception {
        boolean SAVE_FAULTY_PROGRAMS = false;

        String name = "database_export_ev3_1";
        JSONTokener tokener =
            new JSONTokener(Resources.toString(Resources.getResource("de/fhg/iais/roberta/util/db/" + name + ".json"), StandardCharsets.UTF_8));
        JSONObject root = new JSONObject(tokener);
        JSONArray list = root.getJSONArray(root.keys().next());

        JSONObject outputRoot = new JSONObject(root);
        JSONArray outputList = new JSONArray();
        outputRoot.put(root.keys().next(), outputList);

        for ( int i = 0; i < list.length(); i++ ) {
            // Read block set XML
            JSONObject entry = list.getJSONObject(i);
            String input;
            try {
                input = entry.getString("PROGRAM_TEXT");
            } catch ( JSONException e ) {
                System.out.println(e.getMessage());
                outputList.put(entry);
                continue;
            }

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
            String result = IOUtils.toString(start.getInputStream(), Charsets.UTF_8);

            Assert.assertFalse(result, result.contains("Blockly.Xml.textToDom did not obtain a valid XML tree."));
            Assert.assertFalse(result, result.contains("Error [AssertionError]:"));
            String expected = IOUtils.toString(ioFile.toUri(), StandardCharsets.UTF_8);

            // Run transformer
            String transformed = xsltTransformer.transform(input);

            // Compare results
            XMLUnit.setIgnoreWhitespace(true);
            Diff diff = XMLUnit.compareXML(expected, transformed);
            // These are various exceptions in what ways the blockly generated output is different from the XSLT generated one
            // Once the same issue pops up multiple times it gets added here
            // However, the XSLT output is either better or the difference is negligible
            if ( diff.identical()
                || result.contains("Connection checks failed.")
                || diff.toString().contains("block id=\"") // early programs dont have autogenerated and probably overlapping ids
                || diff.toString().contains("Expected number of child nodes '2' but was '3'") // mutation
                || diff.toString().contains("Expected number of child nodes '1' but was '2'") // mutation
                || diff.toString().contains("Expected number of child nodes '3' but was '4'") // mutation
                || diff.toString().contains("Expected number of child nodes '6' but was '5'") // <data>ev3</data>
                || diff.toString().contains("Expected number of child nodes '5' but was '4'") // <data>ev3</data>
                || diff.toString().contains("Expected number of child nodes '7' but was '6'") // <data>nxt</data>
                || diff.toString().contains("Expected number of child nodes '2' but was '1'") // <data>nxt</data>
                || diff.toString().contains("Expected text value 'String2' but was 'String'") // before renaming
                || diff.toString().contains("Expected text value 'max2' but was 'max'") // before renaming
                || diff.toString().contains("Expected text value 'count2' but was 'count'") // before renaming
                || diff.toString().contains("Expected text value 'time2' but was 'time'") // before renaming
                || diff.toString().contains("Expected attribute value '' but was 'Number'") // missing datatype
                || diff.toString().contains("Expected attribute value '' but was 'String'") // missing datatype
                || diff.toString().contains("Expected attribute value '' but was 'Boolean'") // missing datatype
                || diff.toString().contains("Expected element tag name 'value' but was 'mutation'") // if/else error
                || diff.toString().contains("Expected attribute name 'else' but was 'null'") // if/else error
                || diff.toString().contains("Expected text value '#' but was '9'") // calliope image, will be remapped by blockly
                || diff.toString().contains("Expected text value 'no port' but was '") // blockly mapping error with robActions_write_pin
                || diff.toString().contains("Expected attribute value 'UP' but was '") // blockly writes UP by default, which is incorrect
                || diff.toString().contains("Expected number of element attributes '3' but was '4' - comparing <block") // error_attribute
                || diff.toString().contains("Expected number of element attributes '2' but was '3' - comparing <mutation") // missing datatype attribute
                || diff.toString().contains("Expected attribute value '' but was 'DIGITAL' - comparing <mutation mode=\"\"") // blockly writes '' by default, which is incorrect
                || diff.toString().contains("Expected attribute value '' but was 'ANALOG' - comparing <mutation mode=\"\"") // blockly writes '' by default, which is incorrect
                || diff.toString().contains("Expected attribute value '' but was 'PULSEHIGH' - comparing <mutation mode=\"\"") // blockly writes '' by default, which is incorrect
                || diff.toString().contains("Expected attribute value '' but was 'PULSELOW' - comparing <mutation mode=\"\"") // blockly writes '' by default, which is incorrect
                || diff.toString().contains("Expected number of element attributes '0' but was '3' - comparing <comment") // comments are missing width and height in blockly
            ) {
                System.out.println("Nothing seems wrong with " + i + " " + entry.getString("CREATED"));
            } else {
                if ( !SAVE_FAULTY_PROGRAMS ) {
                    Assert.fail(result + '\n' + diff.toString());
                } else {
                    System.out.println(entry.getString("CREATED"));
                    outputList.put(entry);
                }
            }
        }

        if ( SAVE_FAULTY_PROGRAMS ) {
            PrintWriter myFile = new PrintWriter(new File(name + "_out.json"), "UTF-8");
            myFile.println(outputRoot.toString(4));
        }
    }
}

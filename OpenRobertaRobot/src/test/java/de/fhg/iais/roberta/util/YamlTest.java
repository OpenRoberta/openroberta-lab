package de.fhg.iais.roberta.util;

import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class YamlTest {

    @Test
    public void testLoadYaml() {
        JSONObject rc = new JSONObject();
        Util.loadYAMLRecursive("", rc, "classpath:/yaml/y1.yml", false);
        JSONObject expected = new JSONObject(Util.readResourceContent("/yaml/expected.json"));
        JSONAssert.assertEquals(expected, rc, true);
    }

    @Test
    public void testLoadYamlOverride() {
        JSONObject rc = new JSONObject();
        Util.loadYAMLRecursive("", rc, "classpath:/yaml/y1Override.yml", true);
        JSONObject expected = new JSONObject(Util.readResourceContent("/yaml/expectedOverride.json"));
        JSONAssert.assertEquals(expected, rc, true);
    }
}
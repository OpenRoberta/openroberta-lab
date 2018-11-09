package de.fhg.iais.roberta.util;

import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class YamlTest {

    @Test
    public void testLoadYaml() {
        JSONObject rc = new JSONObject();
        Util1.loadYAMLRecursive("", rc, "classpath:yaml/y1.yml");
        JSONObject expected = new JSONObject(Util1.readResourceContent("/yaml/expected.json"));
        JSONAssert.assertEquals(expected, rc, true);
    }
}
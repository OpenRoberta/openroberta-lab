package de.fhg.iais.roberta.util;

import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class MergeJsonTest {
    private static final JSONObject j2a = new JSONObject(Util.readResourceContent("/json/json-2a.txt"));
    private static final JSONObject j2e = new JSONObject(Util.readResourceContent("/json/json-2e.txt"));
    private static final JSONObject j2override = new JSONObject(Util.readResourceContent("/json/json-2override.txt"));
    private static final JSONObject j3 = new JSONObject(Util.readResourceContent("/json/json-3.txt"));
    private static final JSONObject j3override = new JSONObject(Util.readResourceContent("/json/json-3override.txt"));

    @Test
    public void testO1() {
        JSONObject t = new JSONObject(Util.readResourceContent("/json/json-1.txt"));
        Util.mergeJsonIntoFirst("root", t, j2a, false);
        JSONAssert.assertEquals(t, j3, true);
    }

    @Test
    public void testOverride() {
        JSONObject t = new JSONObject(Util.readResourceContent("/json/json-1.txt"));
        Util.mergeJsonIntoFirst("root", t, j2override, true);
        JSONAssert.assertEquals(t, j3override, true);
    }

    @Test(expected = DbcException.class)
    public void testE1() {
        JSONObject t = new JSONObject(Util.readResourceContent("/json/json-1.txt"));
        Util.mergeJsonIntoFirst("root", t, j2e, false);

    }
}

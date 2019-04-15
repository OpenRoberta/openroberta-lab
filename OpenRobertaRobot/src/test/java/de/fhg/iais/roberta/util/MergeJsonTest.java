package de.fhg.iais.roberta.util;

import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class MergeJsonTest {
    private static final JSONObject j2a = new JSONObject(Util1.readResourceContent("/json/json-2a.txt"));
    private static final JSONObject j2e = new JSONObject(Util1.readResourceContent("/json/json-2e.txt"));
    private static final JSONObject j3 = new JSONObject(Util1.readResourceContent("/json/json-3.txt"));

    @Test
    public void testO1() {
        JSONObject t = new JSONObject(Util1.readResourceContent("/json/json-1.txt"));
        Util1.mergeJsonIntoFirst("root", t, j2a);
        JSONAssert.assertEquals(t, j3, true);
    }

    @Test(expected = DbcException.class)
    public void testE1() {
        JSONObject t = new JSONObject(Util1.readResourceContent("/json/json-1.txt"));
        Util1.mergeJsonIntoFirst("root", t, j2e);

    }
}

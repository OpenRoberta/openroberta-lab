package de.fhg.iais.roberta.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class Prop2MapTest {
    private static final Logger LOG = LoggerFactory.getLogger(Prop2MapTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Map<String, String> key2text = new HashMap<>();

    @Test
    public void testOk() {
        LOG.info("running Prop2MapTest");
        Prop2Map.fillKey2text(key2text, "/prop2map/ok.txt");
        Assert.assertTrue(key2text.size() == 3);
        Assert.assertNotNull(key2text.get("PROP_1"));
        Assert.assertNotNull(key2text.get("PROP_2"));
        Assert.assertNotNull(key2text.get("PROP_3"));
        Assert.assertEquals("ein Text für Properties 3", key2text.get("PROP_3").replaceAll("\n", " "));
    }

    @Test
    public void testStructureInvalid1() {
        thrown.expect(DbcException.class);
        thrown.expectMessage("Last entry has no terminating ; in");
        Prop2Map.fillKey2text(key2text, "/prop2map/err1.txt");
    }

    @Test
    public void testStructureInvalid2() {
        thrown.expect(DbcException.class);
        thrown.expectMessage("Invalid data in resource");
        Prop2Map.fillKey2text(key2text, "/prop2map/err2.txt");
    }

    @Test
    public void testStructureInvalid3() {
        thrown.expect(DbcException.class);
        thrown.expectMessage("Duplicate key in resource");
        Prop2Map.fillKey2text(key2text, "/prop2map/err3.txt");
    }
}

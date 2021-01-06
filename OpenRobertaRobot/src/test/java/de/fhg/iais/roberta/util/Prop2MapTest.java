package de.fhg.iais.roberta.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class Prop2MapTest {
    private static final Logger LOG = LoggerFactory.getLogger(Prop2MapTest.class);

    private final Map<String, String> key2text = new HashMap<>();

    @Test
    public void testOk() {
        LOG.info("running Prop2MapTest");
        Prop2Map.fillKey2text(this.key2text, "/prop2map/ok.txt");
        Assert.assertTrue(this.key2text.size() == 3);
        Assert.assertNotNull(this.key2text.get("PROP_1"));
        Assert.assertNotNull(this.key2text.get("PROP_2"));
        Assert.assertNotNull(this.key2text.get("PROP_3"));
        Assert.assertEquals("ein Text für Properties 3", this.key2text.get("PROP_3").replaceAll("\n", " "));
    }

    @Test
    public void testStructureInvalid1() {
        Assert.assertThrows("Last entry has no terminating ; in", DbcException.class, () -> Prop2Map.fillKey2text(this.key2text, "/prop2map/err1.txt"));
    }

    @Test
    public void testStructureInvalid2() {
        Assert.assertThrows("Invalid data in resource", DbcException.class, () -> Prop2Map.fillKey2text(this.key2text, "/prop2map/err2.txt"));
    }

    @Test
    public void testStructureInvalid3() {
        Assert.assertThrows("Duplicate key in resource", DbcException.class, () -> Prop2Map.fillKey2text(this.key2text, "/prop2map/err3.txt"));
    }
}

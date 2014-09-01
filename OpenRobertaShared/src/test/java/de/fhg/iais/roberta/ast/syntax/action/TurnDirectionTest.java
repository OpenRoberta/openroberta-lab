package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class TurnDirectionTest {

    @Test
    public void testBuilder() {
        TurnDirection mode = TurnDirection.get("RIGHT");
        Assert.assertEquals(TurnDirection.RIGHT, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        TurnDirection mode = TurnDirection.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        TurnDirection mode = TurnDirection.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        TurnDirection mode = TurnDirection.get(null);

    }
}

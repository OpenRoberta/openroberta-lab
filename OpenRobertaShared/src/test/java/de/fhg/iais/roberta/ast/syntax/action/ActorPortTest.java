package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class ActorPortTest {

    @Test
    public void testBuilder() {
        ActorPort mode = ActorPort.get("A");
        Assert.assertEquals(ActorPort.A, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        ActorPort.get("ad");

    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        ActorPort.get("");

    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        ActorPort.get(null);

    }
}

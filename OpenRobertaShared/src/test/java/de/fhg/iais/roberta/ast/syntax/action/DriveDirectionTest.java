package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class DriveDirectionTest {

    @Test
    public void testBuilder() {
        DriveDirection mode = DriveDirection.get("BACKWARD");
        Assert.assertEquals(DriveDirection.BACKWARD, mode);
    }

    @Test(expected = DbcException.class)
    public void invalideMode() {
        DriveDirection.get("ad");
    }

    @Test(expected = DbcException.class)
    public void invalideMode1() {
        DriveDirection.get("");
    }

    @Test(expected = DbcException.class)
    public void invalideMode2() {
        DriveDirection.get(null);
    }
}

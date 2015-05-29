package de.fhg.iais.roberta.ast.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.action.ev3.DriveDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

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

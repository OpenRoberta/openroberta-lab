package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.fhg.iais.roberta.dbc.DbcException;

public class PickColorTest {

    @Test
    public void testWeiss() {
        PickColor c1 = PickColor.get("WhItE");
        PickColor c2 = PickColor.get("weiß");
        PickColor c3 = PickColor.get("WEIß");
        PickColor c4 = PickColor.get("WeIsS");
        assertEquals(PickColor.WHITE, c1);
        assertEquals(PickColor.WHITE, c2);
        assertEquals(PickColor.WHITE, c3);
        assertEquals(PickColor.WHITE, c4);
    }

    @Test
    public void testRest() {
        PickColor c1 = PickColor.get("rot");
        PickColor c2 = PickColor.get("GRÜN");
        PickColor c3 = PickColor.get("grün");
        assertEquals(PickColor.RED, c1);
        assertEquals(PickColor.GREEN, c2);
        assertEquals(PickColor.GREEN, c3);
    }

    @Test(expected = DbcException.class)
    public void testExc() {
        PickColor.get("dies ist keine gültige Farbe");
    }
}

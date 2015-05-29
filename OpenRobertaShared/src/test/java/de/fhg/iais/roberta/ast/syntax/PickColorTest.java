package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.fhg.iais.roberta.shared.Pickcolor;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class PickColorTest {

    @Test
    public void testWeiss() {
        Pickcolor c1 = Pickcolor.get("WhItE");
        Pickcolor c2 = Pickcolor.get("weiß");
        Pickcolor c3 = Pickcolor.get("WEIß");
        Pickcolor c4 = Pickcolor.get("WeIsS");
        assertEquals(Pickcolor.WHITE, c1);
        assertEquals(Pickcolor.WHITE, c2);
        assertEquals(Pickcolor.WHITE, c3);
        assertEquals(Pickcolor.WHITE, c4);
    }

    @Test
    public void testRest() {
        Pickcolor c1 = Pickcolor.get("rot");
        Pickcolor c2 = Pickcolor.get("GRÜN");
        Pickcolor c3 = Pickcolor.get("grün");
        assertEquals(Pickcolor.RED, c1);
        assertEquals(Pickcolor.GREEN, c2);
        assertEquals(Pickcolor.GREEN, c3);
    }

    @Test(expected = DbcException.class)
    public void testExc() {
        Pickcolor.get("dies ist keine gültige Farbe");
    }
}

package de.fhg.iais.roberta.ast.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.fhg.iais.roberta.shared.Pickcolor;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class PickColorTest {

    @Test
    public void testWeiss() {
        Pickcolor c1 = Pickcolor.get("#FFFFFF");
        Pickcolor c2 = Pickcolor.get("#ffffff");

        assertEquals(Pickcolor.WHITE, c1);
        assertEquals(Pickcolor.WHITE, c2);

    }

    @Test
    public void testRest() {
        Pickcolor c1 = Pickcolor.get("#B30006");
        Pickcolor c2 = Pickcolor.get("#00642E");
        Pickcolor c3 = Pickcolor.get("#0057A6");
        assertEquals(Pickcolor.RED, c1);
        assertEquals(Pickcolor.GREEN, c2);
        assertEquals(Pickcolor.BLUE, c3);
    }

    @Test(expected = DbcException.class)
    public void testExc() {
        Pickcolor.get("dies ist keine gültige Farbe");
    }
}

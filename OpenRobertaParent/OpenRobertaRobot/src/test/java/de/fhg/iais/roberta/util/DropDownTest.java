package de.fhg.iais.roberta.util;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class DropDownTest {
    DropDown d = new DropDown();
    DropDowns ds = new DropDowns();

    // "not null" of parameters is not tested

    @Test(expected = DbcException.class)
    public void testSameKeyLeft() {
        d.add("l1", "r1");
        d.add("l1", "r2");
    }

    @Test(expected = DbcException.class)
    public void testSameKeyRight() {
        d.add("l1", "r1");
        d.add("l2", "r1");
    }

    @Test
    public void test() {
        d.add("l1", "r1");
        d.add("l2", "r2");
        Assert
            .assertArrayEquals(
                new String[] {
                    "l1",
                    "l2"
                },
                d.getLeft().toArray());
        Assert
            .assertArrayEquals(
                new String[] {
                    "r1",
                    "r2"
                },
                d.getRight().toArray());

        Assert.assertEquals(Pair.of("l1", "r1"), d.getLeft("l1"));
        Assert.assertEquals(Pair.of("l1", "r1"), d.getRight("r1"));
        Assert.assertEquals(Pair.of("l2", "r2"), d.getLeft("l2"));
        Assert.assertEquals(Pair.of("l2", "r2"), d.getRight("r2"));

        ds.add("ds1", "l1", "r1");
        ds.add("ds1", "l2", "r2");
        ds.add("ds2", "l1", "r1");
        ds.add("ds2", "l2", "r2");

        Assert
            .assertArrayEquals(
                new String[] {
                    "l1",
                    "l2"
                },
                ds.get("ds1").getLeft().toArray());
        Assert
            .assertArrayEquals(
                new String[] {
                    "r1",
                    "r2"
                },
                ds.get("ds1").getRight().toArray());
        Assert.assertEquals(Pair.of("l1", "r1"), ds.get("ds1").getLeft("l1"));
        Assert.assertEquals(Pair.of("l1", "r1"), ds.get("ds1").getRight("r1"));
        Assert
            .assertArrayEquals(
                new String[] {
                    "l1",
                    "l2"
                },
                ds.get("ds2").getLeft().toArray());
        Assert
            .assertArrayEquals(
                new String[] {
                    "r1",
                    "r2"
                },
                ds.get("ds2").getRight().toArray());
        Assert.assertEquals(Pair.of("l1", "r1"), ds.get("ds2").getLeft("l1"));
        Assert.assertEquals(Pair.of("l1", "r1"), ds.get("ds2").getRight("r1"));
    }

}

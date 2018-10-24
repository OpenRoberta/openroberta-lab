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
        this.d.add("l1", "r1");
        this.d.add("l1", "r2");
    }

    @Test(expected = DbcException.class)
    public void testSameKeyRight() {
        this.d.add("l1", "r1");
        this.d.add("l2", "r1");
    }

    @Test
    public void test() {
        this.d.add("l1", "r1");
        this.d.add("l2", "r2");
        Assert
            .assertArrayEquals(
                new String[] {
                    "l1",
                    "l2"
                },
                this.d.getAllFirst().toArray());
        Assert
            .assertArrayEquals(
                new String[] {
                    "r1",
                    "r2"
                },
                this.d.getAllSecond().toArray());

        Assert.assertEquals(Pair.of("l1", "r1"), this.d.getPairByFirst("l1"));
        Assert.assertEquals(Pair.of("l1", "r1"), this.d.getPairBySecond("r1"));
        Assert.assertEquals(Pair.of("l2", "r2"), this.d.getPairByFirst("l2"));
        Assert.assertEquals(Pair.of("l2", "r2"), this.d.getPairBySecond("r2"));

        this.ds.add("ds1", "l1", "r1");
        this.ds.add("ds1", "l2", "r2");
        this.ds.add("ds2", "l1", "r1");
        this.ds.add("ds2", "l2", "r2");

        Assert
            .assertArrayEquals(
                new String[] {
                    "l1",
                    "l2"
                },
                this.ds.get("ds1").getAllFirst().toArray());
        Assert
            .assertArrayEquals(
                new String[] {
                    "r1",
                    "r2"
                },
                this.ds.get("ds1").getAllSecond().toArray());
        Assert.assertEquals(Pair.of("l1", "r1"), this.ds.get("ds1").getPairByFirst("l1"));
        Assert.assertEquals(Pair.of("l1", "r1"), this.ds.get("ds1").getPairBySecond("r1"));
        Assert
            .assertArrayEquals(
                new String[] {
                    "l1",
                    "l2"
                },
                this.ds.get("ds2").getAllFirst().toArray());
        Assert
            .assertArrayEquals(
                new String[] {
                    "r1",
                    "r2"
                },
                this.ds.get("ds2").getAllSecond().toArray());
        Assert.assertEquals(Pair.of("l1", "r1"), this.ds.get("ds2").getPairByFirst("l1"));
        Assert.assertEquals(Pair.of("l1", "r1"), this.ds.get("ds2").getPairBySecond("r1"));
    }

}

package de.fhg.iais.roberta.generic.factory;

import org.junit.Test;

import junit.framework.Assert;

public class IndexLocationTest {

    @Test
    public void testGetValueSizeZero() {
        Assert.assertEquals(IndexLocation.FIRST.getValues().length, 0);

    }

    @Test
    public void testGetValueSizeLargeThenZero() {
        Assert.assertEquals(IndexLocation.FROM_START.getValues().length, 1);
    }

}

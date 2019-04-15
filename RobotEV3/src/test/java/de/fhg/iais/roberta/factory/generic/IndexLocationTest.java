package de.fhg.iais.roberta.factory.generic;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.general.IndexLocation;

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

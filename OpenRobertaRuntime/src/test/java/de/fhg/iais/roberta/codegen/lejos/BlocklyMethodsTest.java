package de.fhg.iais.roberta.codegen.lejos;

import org.junit.Assert;
import org.junit.Test;

public class BlocklyMethodsTest {

    @Test
    public void isEven() {
        Assert.assertTrue(BlocklyMethods.isEven(2));
        Assert.assertTrue(BlocklyMethods.isEven(4));
        Assert.assertTrue(BlocklyMethods.isEven(6));
        Assert.assertTrue(BlocklyMethods.isEven(100));

        Assert.assertTrue(!BlocklyMethods.isEven(1));
        Assert.assertTrue(!BlocklyMethods.isEven(19));
        Assert.assertTrue(!BlocklyMethods.isEven(999));
    }

    @Test
    public void isOdd() {
        Assert.assertTrue(!BlocklyMethods.isOdd(2));
        Assert.assertTrue(!BlocklyMethods.isOdd(4));
        Assert.assertTrue(!BlocklyMethods.isOdd(6));
        Assert.assertTrue(!BlocklyMethods.isOdd(100));

        Assert.assertTrue(BlocklyMethods.isOdd(1));
        Assert.assertTrue(BlocklyMethods.isOdd(19));
        Assert.assertTrue(BlocklyMethods.isOdd(999));
    }

    @Test
    public void isPrime() {
        Assert.assertTrue(!BlocklyMethods.isPrime(2));
        Assert.assertTrue(!BlocklyMethods.isPrime(4));
        Assert.assertTrue(!BlocklyMethods.isPrime(6));
        Assert.assertTrue(!BlocklyMethods.isPrime(100));

        Assert.assertTrue(BlocklyMethods.isPrime(1));
        Assert.assertTrue(BlocklyMethods.isPrime(19));
        Assert.assertTrue(BlocklyMethods.isPrime(999));
    }
}

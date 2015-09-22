package de.fhg.iais.roberta.runtime;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.IndexLocation;
import de.fhg.iais.roberta.shared.ListElementOperations;

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
        Assert.assertTrue(BlocklyMethods.isPrime(2));

        Assert.assertTrue(!BlocklyMethods.isPrime(4));
        Assert.assertTrue(!BlocklyMethods.isPrime(6));
        Assert.assertTrue(!BlocklyMethods.isPrime(100));
        Assert.assertTrue(!BlocklyMethods.isPrime(999));

        Assert.assertTrue(BlocklyMethods.isPrime(1));
        Assert.assertTrue(BlocklyMethods.isPrime(7));
        Assert.assertTrue(BlocklyMethods.isPrime(13));
        Assert.assertTrue(BlocklyMethods.isPrime(19));
    }

    @Test
    public void isWhole() {
        Assert.assertTrue(BlocklyMethods.isWhole(2));
        Assert.assertTrue(BlocklyMethods.isWhole((float) 2.0));
        Assert.assertTrue(!BlocklyMethods.isWhole((float) 2.1));
    }

    @Test
    public void isPositive() {
        Assert.assertTrue(BlocklyMethods.isPositive(2));
        Assert.assertTrue(!BlocklyMethods.isPositive((float) -2.1));
    }

    @Test
    public void isNegative() {
        Assert.assertTrue(!BlocklyMethods.isNegative(2));
        Assert.assertTrue(BlocklyMethods.isNegative((float) -2.1));
    }

    @Test
    public void isDivisibleBy() {
        Assert.assertTrue(BlocklyMethods.isDivisibleBy(8, 2));
        Assert.assertTrue(BlocklyMethods.isDivisibleBy(14, 2));
        Assert.assertTrue(BlocklyMethods.isDivisibleBy(40, 2));
        Assert.assertTrue(!BlocklyMethods.isDivisibleBy(9, 2));
        Assert.assertTrue(!BlocklyMethods.isDivisibleBy(7, 2));
        Assert.assertTrue(!BlocklyMethods.isDivisibleBy(3, 2));
    }

    @Test
    public void remainderOf() {
        Assert.assertTrue(BlocklyMethods.remainderOf(8, 2) == 0);
        Assert.assertTrue(BlocklyMethods.remainderOf(14, 2) == 0);
        Assert.assertTrue(BlocklyMethods.remainderOf(5, 2) == 1);
        Assert.assertTrue(BlocklyMethods.remainderOf(5, 3) == 2);
        Assert.assertTrue(BlocklyMethods.remainderOf(5, 4) == 1);
    }

    @Test
    public void clamp() {
        Assert.assertTrue(BlocklyMethods.clamp(8, 2, 10) == 8);
        Assert.assertTrue(BlocklyMethods.clamp(14, 2, 10) == 10);
        Assert.assertTrue(BlocklyMethods.clamp(1, 2, 10) == 2);
    }

    @Test
    public void randInt() {
        int a = (int) BlocklyMethods.randInt(2, 10);
        Assert.assertTrue(a >= 2 && a <= 10);

        a = (int) BlocklyMethods.randInt(10, 2);
        Assert.assertTrue(a >= 2 && a <= 10);

        a = (int) BlocklyMethods.randInt(10, 10);
        Assert.assertTrue(a == 10);
    }

    @Test
    public void sumOnList() {
        Assert.assertEquals(15.0, BlocklyMethods.sumOnList(BlocklyMethods.createListWithNumber(1.0, 1.0, 3.0, 4.0, 6.0)), 0);
        Assert.assertEquals(10, BlocklyMethods.sumOnList(BlocklyMethods.createListWithNumber(5, 3, 2)), 0);
    }

    @Test
    public void minOnList() {
        Assert.assertEquals(1.0, BlocklyMethods.minOnList(BlocklyMethods.createListWithNumber(1.0, 1.0, 3.0, 4.0, 6.0)), 0);
        Assert.assertEquals(2, BlocklyMethods.minOnList(BlocklyMethods.createListWithNumber(5, 3, 2)), 0);
        Assert.assertEquals(-1, BlocklyMethods.minOnList(BlocklyMethods.createListWithNumber(5, 3, 2, -1)), 0);
    }

    @Test
    public void maxOnList() {
        Assert.assertEquals(6, BlocklyMethods.maxOnList(BlocklyMethods.createListWithNumber(1.0, 1.0, 3.0, 4.0, 6.0)), 0);
        Assert.assertEquals(5, BlocklyMethods.maxOnList(BlocklyMethods.createListWithNumber(5, 3, 2)), 0);
        Assert.assertEquals(5, BlocklyMethods.maxOnList(BlocklyMethods.createListWithNumber(5, 3, 2, -1)), 0);
    }

    @Test
    public void averageOnList() {
        Assert.assertEquals(3, BlocklyMethods.averageOnList(BlocklyMethods.createListWithNumber(1.0, 1.0, 3.0, 4.0, 6.0)), 0);
        Assert.assertEquals(3.3333, BlocklyMethods.averageOnList(BlocklyMethods.createListWithNumber(5, 3, 2)), 5);
        Assert.assertEquals(2.25, BlocklyMethods.averageOnList(BlocklyMethods.createListWithNumber(5, 3, 2, -1)), 0);
    }

    @Test
    public void medianOnList() {
        Assert.assertEquals(3, BlocklyMethods.medianOnList(BlocklyMethods.createListWithNumber(1.0, 1.0, 3.0, 4.0, 6.0)), 0);
        Assert.assertEquals(3, BlocklyMethods.medianOnList(BlocklyMethods.createListWithNumber(5, 3, 2)), 5);
        Assert.assertEquals(2.5, BlocklyMethods.medianOnList(BlocklyMethods.createListWithNumber(5, 3, 2, -1)), 0);
    }

    @Test
    public void standardDeviationOnList() {
        Assert.assertEquals(1.8973665961010275, BlocklyMethods.standardDeviatioin(BlocklyMethods.createListWithNumber(1.0, 1.0, 3.0, 4.0, 6.0)), 5);
        Assert.assertEquals(1.247219128924647, BlocklyMethods.standardDeviatioin(BlocklyMethods.createListWithNumber(5, 3, 2)), 5);
        Assert.assertEquals(2.165063509461097, BlocklyMethods.standardDeviatioin(BlocklyMethods.createListWithNumber(5, 3, 2, -1)), 5);
    }

    @Test
    public void modeOnList() {
        Assert.assertEquals(BlocklyMethods.createListWithNumber(1.0), BlocklyMethods.modeOnList(BlocklyMethods.createListWithNumber(1.0, 1.0, 3.0, 4.0, 6.0)));
        Assert.assertEquals(BlocklyMethods.createListWithNumber(5, 3, 2), BlocklyMethods.modeOnList(BlocklyMethods.createListWithNumber(5, 3, 2)));
        Assert.assertEquals(BlocklyMethods.createListWithNumber(5, 3, 2, -1), BlocklyMethods.modeOnList(BlocklyMethods.createListWithNumber(5, 3, 2, -1)));
    }

    @Test
    public void createListWith() {
        Assert.assertTrue(BlocklyMethods.createListWithNumber(1, 1, 1, 1, 1).toString().equals("[1.0, 1.0, 1.0, 1.0, 1.0]"));
        Assert.assertTrue(BlocklyMethods.createListWithNumber(1, 1, 1, 1, 1.3).toString().equals("[1.0, 1.0, 1.0, 1.0, 1.3]"));
        Assert.assertTrue(BlocklyMethods.createListWithString("a", "a").toString().equals("[a, a]"));
    }

    @Test
    public void createListWithItem() {
        Assert.assertTrue(BlocklyMethods.createListWithNumber((float) 1, 5).toString().equals("[1.0, 5.0]"));
        Assert.assertTrue(BlocklyMethods.createListWithItem("a", 5).toString().equals("[a, a, a, a, a]"));
    }

    @Test
    public void lenght() {
        Assert.assertTrue(BlocklyMethods.lenght(BlocklyMethods.createListWithItem((float) 1, 5)) == 5);
        Assert.assertTrue(BlocklyMethods.lenght(BlocklyMethods.createListWithItem("a", 2)) == 2);
    }

    @Test
    public void isEmpty() {
        Assert.assertTrue(!BlocklyMethods.isEmpty(BlocklyMethods.createListWithItem((float) 1, 5)));
        Assert.assertTrue(BlocklyMethods.isEmpty(BlocklyMethods.createListWithItem("a", 0)));
    }

    @Test
    public void findFirst() {
        Assert.assertTrue(BlocklyMethods.findFirst(BlocklyMethods.createListWithNumber(0.0, 1.0, 2.0, 3.0, 1.0), Float.valueOf(1)) == 1);
        Assert.assertTrue(BlocklyMethods.findFirst(BlocklyMethods.createListWithNumber(0, 1, 2, 3, 1), Float.valueOf(5)) == -1);
        Assert.assertTrue(BlocklyMethods.findFirst(BlocklyMethods.createListWithString("a", "b", "c"), "c") == 2);
        Assert.assertTrue(BlocklyMethods.findFirst(BlocklyMethods.createListWithString("a", "b", "c"), "d") == -1);
    }

    @Test
    public void findLast() {
        Assert.assertTrue(BlocklyMethods.findLast(BlocklyMethods.createListWithNumber(0, 1, 2, 3, 1), Float.valueOf(1)) == 4);
        Assert.assertTrue(BlocklyMethods.findLast(BlocklyMethods.createListWithNumber(0, 1, 2, 3, 1), Float.valueOf(5)) == -1);
        Assert.assertTrue(BlocklyMethods.findLast(BlocklyMethods.createListWithString("a", "b", "c", "c"), "c") == 3);
        Assert.assertTrue(BlocklyMethods.findLast(BlocklyMethods.createListWithString("a", "b", "c"), "d") == -1);
    }

    @Test
    public void listsGetIndex() {
        Assert.assertTrue(BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0, 1, 2, 3, 1), ListElementOperations.GET, IndexLocation.FIRST) == 0);
        Assert.assertTrue(BlocklyMethods.listsIndex(BlocklyMethods.createListWithNumber(0, 1, 2, 3, 1), ListElementOperations.GET, IndexLocation.LAST) == 1);
        Assert.assertTrue(
            BlocklyMethods.listsIndex(BlocklyMethods.createListWithString("a", "b", "c", "c"), ListElementOperations.GET, IndexLocation.FROM_START, 1) == "b");
        Assert.assertTrue(
            BlocklyMethods.listsIndex(BlocklyMethods.createListWithString("a", "b", "c", "c"), ListElementOperations.GET, IndexLocation.FROM_END, 3) == "b");

        ArrayList<String> list = BlocklyMethods.createListWithString("a", "b", "c", "c");
        BlocklyMethods.listsIndex(list, ListElementOperations.REMOVE, IndexLocation.LAST);

        Assert.assertTrue(list.toString().equals("[a, b, c]"));
        Assert.assertTrue(BlocklyMethods.listsIndex(list, ListElementOperations.GET_REMOVE, IndexLocation.LAST) == "c");
        Assert.assertTrue(list.toString().equals("[a, b]"));
    }

    @Test
    public void listsSetIndex() {
        ArrayList<Float> list = BlocklyMethods.createListWithNumber(55.0, 66.0, 11.0);
        BlocklyMethods.listsIndex(list, ListElementOperations.SET, 99, IndexLocation.FIRST);

        Assert.assertTrue(list.toString().equals("[99.0, 66.0, 11.0]"));

        BlocklyMethods.listsIndex(list, ListElementOperations.INSERT, Float.valueOf(88), IndexLocation.LAST);
        Assert.assertTrue(list.toString().equals("[99.0, 66.0, 11.0, 88.0]"));

        BlocklyMethods.listsIndex(list, ListElementOperations.INSERT, Float.valueOf(88), IndexLocation.FROM_START, 1);
        Assert.assertTrue(list.toString().equals("[99.0, 88.0, 66.0, 11.0, 88.0]"));
    }

    @Test
    public void listsGetSubList() {
        ArrayList<Float> list = BlocklyMethods.createListWithNumber(55, 66, 11, 22.0, 33);
        Assert.assertTrue(BlocklyMethods.listsGetSubList(list, IndexLocation.FROM_START, 1, IndexLocation.FROM_END, 1).toString().equals("[66.0, 11.0, 22.0]"));
        Assert.assertTrue(BlocklyMethods.listsGetSubList(list, IndexLocation.FIRST, IndexLocation.FROM_END, 1).toString().equals("[55.0, 66.0, 11.0, 22.0]"));
        Assert.assertTrue(BlocklyMethods.listsGetSubList(list, IndexLocation.FROM_START, 1, IndexLocation.LAST).toString().equals("[66.0, 11.0, 22.0, 33.0]"));
        Assert.assertTrue(BlocklyMethods.listsGetSubList(list, IndexLocation.FIRST, IndexLocation.LAST).toString().equals("[55.0, 66.0, 11.0, 22.0, 33.0]"));
    }

    @Test
    public void textJoin() {
        String text = "abc123!?true0.14987720966295234";
        Assert.assertEquals(text, BlocklyMethods.textJoin("a", "b", "c", 1, 2, 3, "!", "?", true, Math.sin(19)));
    }
}

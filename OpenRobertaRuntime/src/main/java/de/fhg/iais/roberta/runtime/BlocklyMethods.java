package de.fhg.iais.roberta.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.fhg.iais.roberta.shared.IndexLocation;
import de.fhg.iais.roberta.shared.ListElementOperations;
import de.fhg.iais.roberta.shared.Pickcolor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class contain all math functions that are used in Blockly.
 * Methods from this class are called and executed on the brick
 *
 * @author kcvejoski
 */
public class BlocklyMethods {

    public static final float GOLDEN_RATIO = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);

    /**
     * Check if numbers is even.
     *
     * @param number to be check
     * @return true if number is even
     */
    public static boolean isEven(float number) {
        return (number % 2 == 0);
    }

    /**
     * Check if the number is odd.
     *
     * @param number to be checked
     * @return true if number is odd
     */
    public static boolean isOdd(float number) {
        return (number % 2 == 1);
    }

    /**
     * Check if number is prime.
     *
     * @param number to be checked
     * @return true if number is prime
     */
    public static boolean isPrime(float number) {
        for ( int i = 2; i <= Math.sqrt(number); i++ ) {
            double remainder = number % i;
            if ( remainder == 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if number is integer.
     *
     * @param number to be checked
     * @return true if is integer
     */
    public static boolean isWhole(float number) {
        return number % 1 == 0;
    }

    /**
     * Check if number is positive.
     *
     * @param number to be checked
     * @return true if number is positive
     */
    public static boolean isPositive(float number) {
        return number > 0;
    }

    /**
     * Check if number is negative.
     *
     * @param number to be checked
     * @return true if number is negative
     */
    public static boolean isNegative(float number) {
        return number < 0;
    }

    /**
     * Check if number is divisible by other number.
     *
     * @param number to be check
     * @param divisor
     * @return true if is divisible by <i>divisor</i>
     */
    public static boolean isDivisibleBy(float number, float divisor) {
        return number % divisor == 0;
    }

    /**
     * Get the remainder of division of two numbers
     *
     * @param divident
     * @param divisor
     * @return remainder of the division
     */
    public static float remainderOf(float divident, float divisor) {
        return divident % divisor;
    }

    /**
     * Checks if the number <b>x</b> is in range <b>min</b> to <b>max</b>:<br>
     * - if <b>x < max && x > min</b> return <b>x</b><br>
     * - if <b>x <= min</b> return <b>min</b><br>
     * - if <b>x >= max</b> return <b>max</b><br>
     * <br>
     *
     * @param x
     * @param min
     * @param max
     * @return
     */
    public static float clamp(float x, float min, float max) {
        return Math.min(Math.max(x, min), max);
    }

    /**
     * Generate random integer in range <i>[min, max]</i>
     *
     * @param min
     * @param max
     * @return random integer
     */
    public static float randInt(float min, float max) {
        if ( min > max ) {
            float c = min;
            min = max;
            max = c;
        }

        Random rand = new Random();

        return rand.nextInt((int) ((max - min) + 1)) + min;
    }

    /**
     * @return random double number
     */
    public static float randDouble() {
        Random rand = new Random();
        return rand.nextFloat();
    }

    /**
     * Create float ArrayList. Client must provide elements to be added to the ArrayList.
     *
     * @param elements to be added in the array list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<Float> createListWithNumber(Number... elements) {
        ArrayList<Float> result = new ArrayList<Float>();
        for ( Number number : elements ) {
            result.add(number.floatValue());

        }
        return result;
    }

    /**
     * Create boolean {@link ArrayList}. Client must provide elements to be added to the ArrayList.
     *
     * @param elements to be added in the array list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<Boolean> createListWithBoolean(Boolean... elements) {
        return new ArrayList<Boolean>(Arrays.asList(elements));
    }

    /**
     * Create string {@link ArrayList}. Client must provide elements to be added to the ArrayList.
     *
     * @param elements to be added in the array list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<String> createListWithString(String... elements) {
        return new ArrayList<String>(Arrays.asList(elements));
    }

    /**
     * Create color {@link ArrayList}. Client must provide elements to be added to the ArrayList.
     * See enumeration {@link Pickcolor} for all possible colors.
     *
     * @param elements to be added in the array list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<Pickcolor> createListWithColour(Pickcolor... elements) {
        return new ArrayList<Pickcolor>(Arrays.asList(elements));
    }

    /**
     * Create float ArrayList with given element contained <b>n</b> times.
     *
     * @param item to be added in the list
     * @param times that the item should be added to the list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<Float> createListWithItem(Float item, float times) {
        ArrayList<Float> result = new ArrayList<Float>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    /**
     * Create boolean ArrayList with given element contained <b>n</b> times.
     *
     * @param item to be added in the list
     * @param times that the item should be added to the list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<Boolean> createListWithItem(Boolean item, float times) {
        ArrayList<Boolean> result = new ArrayList<Boolean>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    /**
     * Create string ArrayList with given element contained <b>n</b> times.
     *
     * @param item to be added in the list
     * @param times that the item should be added to the list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<String> createListWithItem(String item, float times) {
        ArrayList<String> result = new ArrayList<String>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    /**
     * Create color ArrayList with given element contained <b>n</b> times.
     * See enumeration {@link Pickcolor} for all possible colors.
     *
     * @param item to be added in the list
     * @param times that the item should be added to the list
     * @return {@link ArrayList} with the given elements
     */
    public static ArrayList<Pickcolor> createListWithItem(Pickcolor item, float times) {
        ArrayList<Pickcolor> result = new ArrayList<Pickcolor>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    /**
     * Get the length of list.
     *
     * @param list
     * @return length of the list
     */
    public static <T> int lenght(List<T> list) {
        return list.size();
    }

    /**
     * Check if list is empty.
     *
     * @param list to be checked
     * @return true if the list is empty
     */
    public static <T> boolean isEmpty(ArrayList<T> list) {
        return list.size() == 0;
    }

    /**
     * Find first occurrence of an element into an array.
     *
     * @param list to search an item
     * @param item to search
     * @return index of the first occurrence of the item
     */
    public static <T> int findFirst(ArrayList<T> list, T item) {
        return list.indexOf(item);
    }

    /**
     * Find last occurrence of an element into an array.
     *
     * @param list to search an item
     * @param item to search
     * @return index of the last occurrence of the item
     */
    public static <T> int findLast(ArrayList<T> list, T item) {
        return list.lastIndexOf(item);
    }

    /**
     * Operation over an element in a list. See enum. {@link ListElementOperations} for all possible operation over element in a list. <br>
     * <br>
     * If client uses {@link ListElementOperations#GET} or {@link ListElementOperations#GET_REMOVE} then one must provide index location (see enum.
     * {@link IndexLocation}).
     *
     * @param list non-empty list
     * @param operation to be performed on element (see enum. {@link ListElementOperations})
     * @param indexLocation
     * @return element from the list
     */
    public static float listsIndex(ArrayList<Float> list, ListElementOperations operation, float element, IndexLocation indexLocation) {
        return listsIndex(list, operation, element, indexLocation, -1);
    }

    /**
     * Operation over an element in a list. See enum. {@link ListElementOperations} for all possible operation over element in a list. <br>
     * <br>
     * If client uses {@link ListElementOperations#GET} or {@link ListElementOperations#GET_REMOVE} then one must provide index location (see enum.
     * {@link IndexLocation}).
     *
     * @param list non-empty list
     * @param operation to be performed on element (see enum. {@link ListElementOperations})
     * @param indexLocation
     * @return element from the list
     */
    public static String listsIndex(ArrayList<String> list, ListElementOperations operation, String element, IndexLocation indexLocation) {
        return listsIndex(list, operation, element, indexLocation, -1);
    }

    /**
     * Operation over an element in a list. See enum. {@link ListElementOperations} for all possible operation over element in a list. <br>
     * <br>
     * If client uses {@link ListElementOperations#GET} or {@link ListElementOperations#GET_REMOVE} then one must provide index location (see enum.
     * {@link IndexLocation}).
     *
     * @param list non-empty list
     * @param operation to be performed on element (see enum. {@link ListElementOperations})
     * @param indexLocation
     * @return element from the list
     */
    public static boolean listsIndex(ArrayList<Boolean> list, ListElementOperations operation, Boolean element, IndexLocation indexLocation) {
        return listsIndex(list, operation, element, indexLocation, -1);
    }

    /**
     * Operation over an element in a list. See enum. {@link ListElementOperations} for all possible operation over element in a list. <br>
     * <br>
     * If client uses {@link ListElementOperations#REMOVE} then one must provide index location (see enum. {@link IndexLocation}).
     *
     * @param list non-empty list
     * @param operation to be performed on element (see enum. {@link ListElementOperations})
     * @param indexLocation
     * @return modified list
     */
    public static <T> T listsIndex(ArrayList<T> list, ListElementOperations operation, IndexLocation indexLocation) {
        return listsIndex(list, operation, indexLocation, -1);
    }

    /**
     * Operation over an element in a list. See enum. {@link ListElementOperations} for all possible operation over element in a list. <br>
     * <br>
     * If client uses {@link ListElementOperations#REMOVE} then one must provide index location (see enum. {@link IndexLocation}) and index.
     *
     * @param list non-empty list
     * @param operation to be performed on element (see enum. {@link ListElementOperations})
     * @param indexLocation
     * @param index
     * @return modified list
     */
    public static <T> T listsIndex(ArrayList<T> list, ListElementOperations operation, IndexLocation indexLocation, float index) {
        Assert.isTrue(operation == ListElementOperations.INSERT || list.size() != 0, "List size is 0!");
        return listsIndex(list, operation, null, indexLocation, index);
    }

    /**
     * Operation over an element in a list. See enum. {@link ListElementOperations} for all possible operation over element in a list. <br>
     * <br>
     * If client uses {@link ListElementOperations#INSERT} or {@link ListElementOperations#SET}, then one must provide element, index location (see enum.
     * {@link IndexLocation}) and index.
     *
     * @param list non-empty list
     * @param operation to be performed on element (see enum. {@link ListElementOperations})
     * @param element to be inserted or set
     * @param indexLocation
     * @param index
     * @return modified list
     */
    public static <T> T listsIndex(ArrayList<T> list, ListElementOperations operation, T element, IndexLocation indexLocation, float index) {
        Assert.isTrue(operation == ListElementOperations.INSERT || list.size() != 0, "List size iss 0!");
        int resultIndex = calculateIndex(list, indexLocation, index);
        return executeOperation(list, operation, resultIndex, element);
    }

    /**
     * Return sub-list of a given list. Client must provide non-empty {@link ArrayList},
     * start-location, end-location (see enum. {@link IndexLocation}), start-index and end-index.<br>
     * <br>
     * If client uses for <b>startLocation</b> and <b>endLocation</b> {@link IndexLocation#FROM_START} or {@link IndexLocation#FROM_END} then must provide and
     * start index.
     *
     * @param list non-empty list
     * @param startLocation
     * @param startIndex
     * @param endLocation
     * @param endIndex
     * @return sub-list of a given list
     */
    public static <T> ArrayList<T> listsGetSubList(
        ArrayList<T> list,
        IndexLocation startLocation,
        float startIndex,
        IndexLocation endLocation,
        float endIndex) {
        Assert.isTrue(list.size() != 0, "List size is 0!");
        int fromIndex = calculateIndex(list, startLocation, startIndex);
        int toIndex = calculateIndex(list, endLocation, endIndex);
        return new ArrayList<T>(list.subList(fromIndex, toIndex));
    }

    /**
     * Return sub-list of a given list. Client must provide non-empty {@link ArrayList},
     * start-location and end-location (see enum. {@link IndexLocation}) and end index. <br>
     * <br>
     * If client uses for <b>endLocation</b> {@link IndexLocation#FROM_START} or {@link IndexLocation#FROM_END} then must provide and start index.
     *
     * @param list non-empty list
     * @param startLocation
     * @param endLocation
     * @param endIndex
     * @return sub-list of a given list
     */
    public static <T> ArrayList<T> listsGetSubList(ArrayList<T> list, IndexLocation startLocation, IndexLocation endLocation, float endIndex) {
        Assert.isTrue(list.size() != 0, "List size is 0!");
        int fromIndex = calculateIndex(list, startLocation, -1);
        int toIndex = calculateIndex(list, endLocation, endIndex);
        return new ArrayList<T>(list.subList(fromIndex, toIndex));
    }

    /**
     * Return sub-list of a given list. Client must provide non-empty {@link ArrayList},
     * start-location and end-location (see enum. {@link IndexLocation}) and start index. <br>
     * <br>
     * If client uses for <b>startLocation</b> {@link IndexLocation#FROM_START} or {@link IndexLocation#FROM_END} then must provide and start index.
     *
     * @param list non-empty list
     * @param startLocation
     * @param startIndex
     * @param endLocation
     * @return sub-list of a given list
     */
    public static <T> ArrayList<T> listsGetSubList(ArrayList<T> list, IndexLocation startLocation, float startIndex, IndexLocation endLocation) {
        Assert.isTrue(list.size() != 0, "List size is 0!");
        int fromIndex = calculateIndex(list, startLocation, startIndex);
        int toIndex = calculateIndex(list, endLocation, -1);
        return new ArrayList<T>(list.subList(fromIndex, toIndex + 1));
    }

    /**
     * Return sub-list of a given list. Client must provide non-empty {@link ArrayList},
     * start-location and end-location (see enum. {@link IndexLocation}).
     *
     * @param list non-empty list
     * @param startLocation
     * @param endLocation
     * @return sub-list of a given list
     */
    public static <T> ArrayList<T> listsGetSubList(ArrayList<T> list, IndexLocation startLocation, IndexLocation endLocation) {
        Assert.isTrue(list.size() != 0, "List size is 0!");
        int fromIndex = calculateIndex(list, startLocation, -1);
        int toIndex = calculateIndex(list, endLocation, -1);
        return new ArrayList<T>(list.subList(fromIndex, toIndex + 1));
    }

    /**
     * Concatenate multiple items to string.
     *
     * @param items to concatenate
     * @return string of concatenated items
     */
    public static String textJoin(Object... items) {
        StringBuilder sb = new StringBuilder();
        for ( Object string : items ) {
            sb.append(string);
        }
        return sb.toString();
    }

    /**
     * Sums the elements of a list. Client must provide float {@link ArrayList}.
     *
     * @param arrayList that is summed
     * @return sum of the elements over array list
     */
    public static float sumOnList(ArrayList<Float> arrayList) {
        float result = 0;
        for ( float element : arrayList ) {
            result += element;
        }
        return result;
    }

    /**
     * <b>Min</b> element of float {@link ArrayList}.
     *
     * @param list
     * @return min element in the list
     */
    public static float minOnList(ArrayList<Float> list) {
        return Collections.min(list);
    }

    /**
     * <b>Max</b> element of float {@link ArrayList}.
     *
     * @param list
     * @return max element in the list
     */
    public static float maxOnList(ArrayList<Float> list) {
        return Collections.max(list);

    }

    /**
     * Calculate average of elements in {@link ArrayList}.
     *
     * @param list
     * @return average of the elements
     */
    public static float averageOnList(ArrayList<Float> list) {
        float sum = 0;
        for ( float element : list ) {
            sum += element;
        }
        return sum / list.size();
    }

    /**
     * Calculate median of elements in {@link ArrayList}.
     *
     * @param list
     * @return median of the elements
     */
    public static float medianOnList(ArrayList<Float> list) {
        Collections.sort(list);
        float median;
        if ( list.size() % 2 == 0 ) {
            median = (list.get(list.size() / 2) + list.get(list.size() / 2 - 1)) / 2;
        } else {
            median = list.get(list.size() / 2);
        }
        return median;
    }

    /**
     * Random element form list {@link ArrayList}.
     *
     * @param list
     * @return element from list chosen by random
     */
    public static float randOnList(ArrayList<Float> list) {
        return list.get((int) randInt(0, list.size() - 1));
    }

    /**
     * Standard deviation of elements in {@link ArrayList}.
     *
     * @param list
     * @return standard deviation of the elements
     */
    public static float standardDeviatioin(ArrayList<Float> list) {
        int n = list.size();
        if ( n == 0 ) {
            return 0;
        }
        float variance = 0;
        float mean = averageOnList(list);
        for ( Float number : list ) {
            variance += Math.pow(number - mean, 2);
        }
        variance /= n;
        return (float) Math.sqrt(variance);

    }

    public static ArrayList<Float> modeOnList(ArrayList<Float> list) {
        Map<Float, Integer> seen = new HashMap<Float, Integer>();
        int max = 0;
        ArrayList<Float> maxElems = new ArrayList<Float>();
        for ( Float value : list ) {
            if ( seen.containsKey(value) ) {
                seen.put(value, seen.get(value) + 1);
            } else {
                seen.put(value, 1);
            }
            if ( seen.get(value) > max ) {
                max = seen.get(value);
                maxElems.clear();
                maxElems.add(value);
            } else if ( seen.get(value) == max ) {
                maxElems.add(value);
            }
        }
        return maxElems;
    }

    private static <T> T executeOperation(ArrayList<T> list, ListElementOperations operation, float resultIndex, T element) {
        T result = null;
        if ( resultIndex < list.size() ) {
            result = list.get((int) resultIndex);
        }
        switch ( operation ) {
            case SET:
                list.set((int) resultIndex, element);
                break;
            case INSERT:
                resultIndex = resultIndex == list.size() - 1 ? resultIndex + 1 : resultIndex;
                list.add((int) resultIndex, element);
                result = list.get((int) resultIndex);
            case GET:
                break;
            case GET_REMOVE:
            case REMOVE:
                list.remove((int) resultIndex);
                break;
            default:
                throw new DbcException("Invalid operation!");
        }
        return result;

    }

    private static <T> int calculateIndex(ArrayList<T> list, IndexLocation indexLocation, float index) {
        //Assert.isTrue(index < list.size(), "Index location is larger then the size of array!");
        switch ( indexLocation ) {
            case FROM_START:
                return (int) index;
            case FROM_END:
                return (int) (list.size() - index);
            case FIRST:
                return 0;
            case LAST:
                return list.size() - 1;
            case RANDOM:
                return (int) randInt(0, list.size() - 1);
            default:
                throw new DbcException("Unknown index location!");
        }
    }
}

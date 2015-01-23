package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.fhg.iais.roberta.ast.syntax.IndexLocation;
import de.fhg.iais.roberta.ast.syntax.ListElementOperations;
import de.fhg.iais.roberta.ast.syntax.Pickcolor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class BlocklyMethods {

    public static double GOLDEN_RATIO = (1.0 + Math.sqrt(5.0)) / 2.0;

    public static boolean isEven(double number) {
        return (number % 2 == 0);
    }

    public static boolean isOdd(double number) {
        return (number % 2 == 1);
    }

    public static boolean isPrime(double number) {
        for ( int i = 2; i <= Math.sqrt(number); i++ ) {
            double remainder = number % i;
            if ( remainder == 0 ) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhole(double number) {
        return number % 1 == 0;
    }

    public static boolean isPositive(double number) {
        return number > 0;
    }

    public static boolean isNegative(double number) {
        return number < 0;
    }

    public static boolean isDivisibleBy(double number, double divisor) {
        return number % divisor == 0;
    }

    public static double remainderOf(double divident, double divisor) {
        return divident % divisor;
    }

    public static double clamp(double x, double min, double max) {
        return Math.min(Math.max(x, min), max);
    }

    public static int randInt(int min, int max) {
        if ( min > max ) {
            // Swap min and max to ensure a is smaller.
            int c = min;
            min = max;
            max = c;
        }
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    public static double randDouble() {
        Random rand = new Random();
        return rand.nextDouble();
    }

    public static ArrayList<Float> createListWith(Number... a) {
        ArrayList<Float> result = new ArrayList<Float>();
        for ( Number number : a ) {
            result.add(number.floatValue());

        }
        return result;
    }

    public static ArrayList<Boolean> createListWith(Boolean... a) {
        return new ArrayList<Boolean>(Arrays.asList(a));
    }

    public static ArrayList<String> createListWith(String... a) {
        return new ArrayList<String>(Arrays.asList(a));
    }

    public static ArrayList<Pickcolor> createListWith(Pickcolor... a) {
        return new ArrayList<Pickcolor>(Arrays.asList(a));
    }

    public static ArrayList<Float> createListWithItem(Float item, int times) {
        ArrayList<Float> result = new ArrayList<Float>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    public static ArrayList<Boolean> createListWithItem(Boolean item, int times) {
        ArrayList<Boolean> result = new ArrayList<Boolean>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    public static ArrayList<String> createListWithItem(String item, int times) {
        ArrayList<String> result = new ArrayList<String>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    public static ArrayList<Pickcolor> createListWithItem(Pickcolor item, int times) {
        ArrayList<Pickcolor> result = new ArrayList<Pickcolor>();
        for ( int i = 0; i < times; i++ ) {
            result.add(item);
        }
        return result;
    }

    public static <T> int lenght(List<T> list) {
        return list.size();
    }

    public static <T> boolean isEmpty(ArrayList<T> list) {
        return list.size() == 0;
    }

    public static <T> int findFirst(ArrayList<T> list, T item) {
        return list.indexOf(item);
    }

    public static <T> int findLast(ArrayList<T> list, T item) {
        return list.lastIndexOf(item);
    }

    public static float listsIndex(ArrayList<Float> list, ListElementOperations operation, float element, IndexLocation indexLocation) {
        return listsIndex(list, operation, element, indexLocation, -1);
    }

    public static String listsIndex(ArrayList<String> list, ListElementOperations operation, String element, IndexLocation indexLocation) {
        return listsIndex(list, operation, element, indexLocation, -1);
    }

    public static boolean listsIndex(ArrayList<Boolean> list, ListElementOperations operation, Boolean element, IndexLocation indexLocation) {
        return listsIndex(list, operation, element, indexLocation, -1);
    }

    public static <T> T listsIndex(ArrayList<T> list, ListElementOperations operation, IndexLocation indexLocation) {
        return listsIndex(list, operation, indexLocation, -1);
    }

    public static <T> T listsIndex(ArrayList<T> list, ListElementOperations operation, IndexLocation indexLocation, float index) {
        return listsIndex(list, operation, null, indexLocation, index);
    }

    public static <T> T listsIndex(ArrayList<T> list, ListElementOperations operation, T element, IndexLocation indexLocation, float index) {
        int resultIndex = calculateIndex(list, indexLocation, index);
        T result = executeOperation(list, operation, resultIndex, element);
        return result;
    }

    public static <T> ArrayList<T> listsGetSubList(ArrayList<T> list, IndexLocation startLocation, int startIndex, IndexLocation endLocation, float endIndex) {
        int fromIndex = calculateIndex(list, startLocation, startIndex);
        int toIndex = calculateIndex(list, endLocation, endIndex);
        return new ArrayList<T>(list.subList(fromIndex, toIndex));
    }

    public static <T> ArrayList<T> listsGetSubList(ArrayList<T> list, IndexLocation startLocation, IndexLocation endLocation, float endIndex) {
        int fromIndex = calculateIndex(list, startLocation, -1);
        int toIndex = calculateIndex(list, endLocation, endIndex);
        return new ArrayList<T>(list.subList(fromIndex, toIndex));
    }

    public static <T> ArrayList<T> listsGetSubList(ArrayList<T> list, IndexLocation startLocation, float startIndex, IndexLocation endLocation) {
        int fromIndex = calculateIndex(list, startLocation, startIndex);
        int toIndex = calculateIndex(list, endLocation, -1);
        return new ArrayList<T>(list.subList(fromIndex, toIndex + 1));
    }

    public static <T> ArrayList<T> listsGetSubList(ArrayList<T> list, IndexLocation startLocation, IndexLocation endLocation) {
        int fromIndex = calculateIndex(list, startLocation, -1);
        int toIndex = calculateIndex(list, endLocation, -1);
        return new ArrayList<T>(list.subList(fromIndex, toIndex + 1));
    }

    public static String textJoin(Object... list) {
        StringBuilder sb = new StringBuilder();
        for ( Object string : list ) {
            sb.append(string);
        }
        return sb.toString();
    }

    public static float sumOnList(ArrayList<Float> arrayList) {
        float result = 0;
        for ( float element : arrayList ) {
            result += element;
        }
        return result;
    }

    public static float minOnList(ArrayList<Float> list) {
        return Collections.min(list);
    }

    public static float maxOnList(ArrayList<Float> list) {
        return Collections.max(list);

    }

    public static float averageOnList(ArrayList<Float> list) {
        float sum = 0;
        for ( float element : list ) {
            sum += element;
        }
        return sum / list.size();
    }

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

    private static <T> T executeOperation(ArrayList<T> list, ListElementOperations operation, int resultIndex, T element) {
        T result = list.get(resultIndex);
        switch ( operation ) {
            case SET:
                list.set(resultIndex, element);
                return result;
            case INSERT:
                //check if it is last index
                resultIndex = resultIndex == list.size() - 1 ? resultIndex + 1 : resultIndex;
                list.add(resultIndex, element);
                return result;
            case GET:
                break;
            case GET_REMOVE:
                list.remove(resultIndex);
                break;
            case REMOVE:
                list.remove(resultIndex);
                return result;
            default:
                throw new DbcException("Invalid operation!");
        }
        return result;
    }

    private static <T> int calculateIndex(ArrayList<T> list, IndexLocation indexLocation, float index) {
        Assert.isTrue(index < list.size(), "Index location is larger then the size of array!");
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
                return randInt(0, list.size() - 1);
            default:
                throw new DbcException("Unknown index location!");
        }
    }
}

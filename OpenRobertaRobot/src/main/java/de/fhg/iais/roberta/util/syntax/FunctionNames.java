package de.fhg.iais.roberta.util.syntax;

import java.util.Locale;

import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import static de.fhg.iais.roberta.typecheck.Sig.VOID;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum FunctionNames {
    TIME(VOID),
    MODE(VOID),
    // MathConst:
    PI(Sig.of(BlocklyType.NUMBER)),
    GOLDEN_RATIO(Sig.of(BlocklyType.NUMBER)),
    SQRT2(Sig.of(BlocklyType.NUMBER)),
    SQRT1_2(Sig.of(BlocklyType.NUMBER)),
    INFINITY(Sig.of(BlocklyType.NUMBER)),
    E(Sig.of(BlocklyType.NUMBER)),

    //MathSingleFunct Trigonometric:
    SIN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "sin"),
    COS(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "cos"),
    TAN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "tan"),
    ASIN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "asin"),
    ACOS(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "acos"),
    ATAN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "atan"),

    //MathSingleFunct Singles:
    EXP(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "exp"),
    ROOT(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "sqrt"),
    ABS(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "abs"),
    LN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "log"),
    LOG10(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "log10"),
    SQUARE(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "pow"),
    POW10(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "pow10"),

    //MathSingleFunct Round:
    ROUND(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "round"),
    ROUNDUP(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "ceil"),
    ROUNDDOWN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "floor"),

    // MathRandomIntFunct and MathRandomFloatFunct:
    RANDOMINT(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "randomInt"),
    RANDOM_DOUBLE(Sig.of(BlocklyType.NUMBER), "randomFloat"),

    //MathNumPropFunct:
    EVEN(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER), "isEven"),
    ODD(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER), "isOdd"),
    PRIME(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER), "isPrime"),
    WHOLE(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER), "isWhole"),
    POSITIVE(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER), "isPositive"),
    NEGATIVE(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER), "isNegative"),
    DIVISIBLE_BY(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER), "isDivisibleBy"),

    //MathOnListFunct:
    AVERAGE(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER), "average"),
    STD_DEV(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER), "stddev"),
    RANDOM(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE), "randomItem"),
    MIN(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER), "min"),
    MAX(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER), "max"),
    SUM(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER), "sum"),
    MEDIAN(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER), "median"),

    //LengthOfListFunct:
    LIST_LENGTH(Sig.of(BlocklyType.NUMBER, BlocklyType.CAPTURED_TYPE), "size"),

    //IndexOfFunct:
    INDEXOF(Sig.of(BlocklyType.NUMBER, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM), "indexOfFirst", "indexOfLast"),

    // ListSetIndex:
    SETINDEX(Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.NUMBER), "set", "setFromEnd", "insert", "insertFromEnd"),
    SETINDEXFIRSTORLAST(Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM), "setFirst", "setLast", "insertFirst", "insertLast"),

    // ListGetIndex:
    GETLISTELEMENT(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER), "get", "getFromEnd", "getAndRemove", "getAndRemoveFromEnd"),
    GETFIRST(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE), "getFirst", "getAndRemoveFirst"),
    GETLAST(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE), "getLast", "getAndRemoveLast"),
    REMOVE(Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER), "remove", "removeFromEnd"),
    REMOVEFIRSTORLAST(Sig.of(BlocklyType.VOID, BlocklyType.CAPTURED_TYPE), "removeFirst", "removeLast"),
    //ListRepeat:
    LISTS_REPEAT(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.NUMBER), "createListWith"),

    //GetSubFunct:
    GET_SUBLIST(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER, BlocklyType.NUMBER), "subList", "subListFromIndexToEnd", "subListFromEndToIndex", "subListFromEndToEnd"),
    SUBFIRSTORLAST(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER), "subListFromIndexToLast", "subListFromFirstToIndex", "subListFromFirstToEnd", "subListFromEndToLast"),
    SUBFIRSTANDLAST(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE), "subListFromFirstToLast"),

    // TextJoinFunct:
    TEXTJOIN(Sig.of(BlocklyType.STRING, BlocklyType.VARARGS, BlocklyType.ANY), "createTextWith"),

    //MathConstrainFunct:
    CONSTRAIN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "constrain"),
    //  IsListEmptyFunct:
    LIST_IS_EMPTY(Sig.of(BlocklyType.BOOLEAN, BlocklyType.CAPTURED_TYPE), "isEmpty"),
    //RgbColor:
    GETRGB(Sig.of(BlocklyType.COLOR, BlocklyType.VARARGS, BlocklyType.NUMBER), "getRGB"),
    //MathPowerFunct:
    POWER(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "^"),

    //MathChangeStmt
    CHANGEBY(Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER), "changeBy"),
    //TextAppendStmt
    TEXTAPPEND(Sig.of(BlocklyType.VOID, BlocklyType.STRING, BlocklyType.STRING), "appendText"),
    //MathCastStringFunc and MathCastCharFunc
    CASTSTRINGCHAR(Sig.of(BlocklyType.STRING, BlocklyType.NUMBER), "castToString", "castToChar"),
    //TextStringCastNumFunc
    CASTTONUMBER(Sig.of(BlocklyType.NUMBER, BlocklyType.STRING), "castToNumber"),
    //TextCharCastNumb
    CASTSTRINGTONUMBER(Sig.of(BlocklyType.NUMBER, BlocklyType.STRING, BlocklyType.NUMBER), "castStringToNumber"),
    //CreateEmptyList
    LISTCREATE(Sig.of(BlocklyType.CAPTURED_TYPE), "createEmptyList"),
    LEFT(VOID),
    RIGHT(VOID),
    TEXT(VOID, "text"),
    NUMBER(VOID, "number"),
    CAST(VOID);
    private final String[] values;
    public final Sig signature;

    private FunctionNames(Sig signature, String... values) {
        this.signature = signature;
        this.values = values;
    }


    /**
     * @return symbol of the function if it exists otherwise the name of the function.
     */
    public String getOpSymbol() {
        if ( this.values.length == 0 ) {
            return this.toString();
        } else {
            return this.values[0];
        }
    }

    /**
     * get function from {@link FunctionNames} from string parameter. It is possible for one function to have multiple string mappings. Throws exception if the
     * operator does not exists.
     *
     * @param s name of the function
     * @return function from the enum {@link FunctionNames}
     */

    public static FunctionNames get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid function name: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( FunctionNames funct : FunctionNames.values() ) {
            if ( funct.toString().equals(sUpper) ) {
                return funct;
            }
            for ( String value : funct.values ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return funct;
                }
            }
        }
        throw new DbcException("Invalid function name: " + s);
    }
}




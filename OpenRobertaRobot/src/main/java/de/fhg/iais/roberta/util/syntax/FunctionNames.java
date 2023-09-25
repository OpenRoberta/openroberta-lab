package de.fhg.iais.roberta.util.syntax;

import java.util.Locale;

import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import static de.fhg.iais.roberta.typecheck.Sig.VOID;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum FunctionNames {
    TIME(VOID),
    DIVISIBLE_BY(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER, BlocklyType.NUMBER)),
    MAX(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER)),
    MIN(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER)),
    LISTS_REPEAT(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER)),
    RANDOM(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE)),
    RANDOM_DOUBLE(VOID),
    EVEN(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER)),
    ODD(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER)),
    PRIME(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER)),
    WHOLE(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER)),
    POSITIVE(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER)),
    NEGATIVE(Sig.of(BlocklyType.BOOLEAN, BlocklyType.NUMBER)),
    SUM(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER)),
    AVERAGE(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER)),
    MEDIAN(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER)),
    MODE(VOID),
    STD_DEV(Sig.of(BlocklyType.NUMBER, BlocklyType.ARRAY_NUMBER)),
    SQUARE(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    ROOT(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "SQRT"),
    ABS(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    LN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    LOG10(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    EXP(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    POW10(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    SIN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    COS(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    TAN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    ASIN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    ACOS(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    ATAN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    POWER(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER), "^"),
    ROUND(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER)),
    ROUNDUP(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "CEIL"),
    ROUNDDOWN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER), "FLOOR"),
    LIST_IS_EMPTY(Sig.of(BlocklyType.BOOLEAN, BlocklyType.CAPTURED_TYPE)),
    LEFT(VOID),
    RIGHT(VOID),
    TEXT(VOID, "TEXT"),
    NUMBER(VOID, "NUMBER"),
    LIST_LENGTH(Sig.of(BlocklyType.NUMBER, BlocklyType.CAPTURED_TYPE)),
    GET_SUBLIST(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER, BlocklyType.NUMBER)),
    CAST(VOID),
    INDEXOF(Sig.of(BlocklyType.NUMBER, BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE_ARRAY_ITEM)),
    GETLISTELEMENT(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER)),
    GETFIRST(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE)),
    GETLAST(Sig.of(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM, BlocklyType.CAPTURED_TYPE)),
    SUBFIRSTORLAST(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE, BlocklyType.NUMBER)),
    SUBFIRSTANDLAST(Sig.of(BlocklyType.CAPTURED_TYPE, BlocklyType.CAPTURED_TYPE)),
    TEXTJOIN(Sig.of(BlocklyType.STRING, BlocklyType.CAPTURED_TYPE)),
    CONSTRAIN(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER)),
    GETRGB(Sig.of(BlocklyType.ARRAY_NUMBER)),
    RANDOMINT(Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER)),
    PI(Sig.of(BlocklyType.NUMBER)),
    GOLDEN_RATIO(Sig.of(BlocklyType.NUMBER)),
    SQRT2(Sig.of(BlocklyType.NUMBER)),
    SQRT1_2(Sig.of(BlocklyType.NUMBER)),
    INFINITY(Sig.of(BlocklyType.NUMBER)),
    E(Sig.of(BlocklyType.NUMBER));
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
     * @param functName of the function
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

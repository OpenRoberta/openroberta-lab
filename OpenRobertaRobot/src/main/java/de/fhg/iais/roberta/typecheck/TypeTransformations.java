package de.fhg.iais.roberta.typecheck;

import java.util.HashMap;
import java.util.Map;

public class TypeTransformations {

    private static final Map<String, Sig> FUNCTIONS = new HashMap<>();
    private static final Map<String, BlocklyType> CONSTANTS = new HashMap<>();

    static {

        FUNCTIONS.put("SIN", Sig.of(BlocklyType.NUMBER, BlocklyType.NUMBER));

        CONSTANTS.put("PI", BlocklyType.NUMBER);
    }

    /**
     * get the signature. The caller has to check for <code>null</code>!
     * 
     * @param functionName whose signature is requested
     * @return the signature; if not found, return <code>null</code>
     */
    public static Sig getFunctionSignature(String functionName) {
        return FUNCTIONS.get(functionName);
    }

    /**
     * get the type. The caller has to check for <code>null</code>!
     * 
     * @param constantName whose type is requested
     * @return the type; if not found, return <code>null</code>
     */
    public static BlocklyType getConstantSignature(String constantName) {
        return CONSTANTS.get(constantName);
    }

}

package de.fhg.iais.roberta.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Class to generate helper method definitions and respective method names for usage in code generation.
 */
public class HelperMethodGenerator {

    /**
     * Programming languages we support.
     */
    public enum Language {
        PYTHON, C, JAVA, JSON
    }

    private final Map<FunctionNames, String> helperMethods = new EnumMap<>(FunctionNames.class);
    private final Language language;

    /**
     * Constructs a generator from the loaded JSON of the method definition YAML file.
     *
     * @param jsonHelperMethods the loaded JSON of the YAML file
     * @param baseProgLanguage the base programming language
     */
    public HelperMethodGenerator(JSONObject jsonHelperMethods, Language baseProgLanguage) {
        for ( String methodName : jsonHelperMethods.keySet() ) {
            JSONObject jsonHelperMethod = jsonHelperMethods.getJSONObject(methodName);
            String implementation = jsonHelperMethod.optString(baseProgLanguage.toString());
            if ( implementation != null ) {
                this.helperMethods.put(FunctionNames.get(methodName), implementation);
            }
        }
        this.language = baseProgLanguage;
    }

    /**
     * Returns the name of the helper function.
     *
     * @param method the wanted helper implementation
     * @return the function name to use in code generation
     */
    public String getHelperMethodName(FunctionNames method) {
        String implementation = this.helperMethods.get(method);
        return extractFunctionName(implementation);
    }

    /**
     * Returns the helper method definitions for the set of used methods.
     *
     * @param usedMethods a set of used methods that may need to be generated as a helper method
     * @return the helper method definitions
     */
    public String getHelperMethodDefinitions(Set<FunctionNames> usedMethods) {
        StringBuilder sb = new StringBuilder();

        // guarantee order of methods
        List<FunctionNames> sortedUsedMethods = new ArrayList<>(usedMethods);
        Collections.sort(sortedUsedMethods);

        // append all method implementations
        for ( FunctionNames usedFunction : sortedUsedMethods ) {
            String implementation = this.helperMethods.get(usedFunction);
            if ( implementation != null ) { // TODO avoid this?
                sb.append('\n');
                sb.append(implementation);
            }
        }

        return sb.toString();
    }

    /**
     * Extracts the function names from the implementation. Uses the current language.
     *
     * @param implementation the implementation of the helper method
     * @return the extracted function name
     */
    private String extractFunctionName(String implementation) {
        String declaration = implementation.split("\n")[0];
        Matcher nameMatcher;
        switch ( this.language ) {
            case PYTHON:
                nameMatcher = Pattern.compile("def (.*)\\(.*\\):").matcher(declaration);
                break;
            case C: // Java and C matchers are identical
            case JAVA:
                nameMatcher = Pattern.compile(".* &?\\*?(.*)\\(.*\\) \\{").matcher(declaration);
                break;
            case JSON: // json/stackmachine should not need helper functions
            default:
                throw new DbcException("Programming language is not supported");
        }
        if ( nameMatcher.find() ) {
            return nameMatcher.group(1);
        } else {
            throw new DbcException("Method definition in helper methods is incorrect!");
        }
    }
}

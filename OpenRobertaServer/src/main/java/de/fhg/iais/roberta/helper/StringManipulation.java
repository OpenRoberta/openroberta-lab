package de.fhg.iais.roberta.helper;

/**
 * This class is used to store helper methods which are used for the code generation.
 * 
 * @author kcvejoski
 */
public class StringManipulation {
    /**
     * Appends custom string to string builder.
     * 
     * @param sb on which the generated string will be appended,
     * @param indentation,
     * @param newLineBefore the generated string,
     * @param newLineAfter the generated sting,
     * @param text which we want to append.
     */
    public static void appendCustomString(StringBuilder sb, int indentation, boolean newLineBefore, boolean newLineAfter, String text) {
        if ( newLineBefore ) {
            sb.append("\n");
        }
        for ( int i = 0; i < indentation; i++ ) {
            sb.append(" ");
        }
        if ( text != null ) {
            sb.append(text);
        }
        if ( newLineAfter ) {
            sb.append("\n");
        }
    }

    /**
     * Generates custom string to string builder.
     * 
     * @param sb on which the generated string will be appended,
     * @param indentation,
     * @param newLineBefore the generated string,
     * @param newLineAfter the generated sting,
     * @param text which we want to generate.
     */
    public static String generateString(int indentation, boolean newLineBefore, boolean newLineAfter, String text) {
        StringBuilder sb = new StringBuilder();
        appendCustomString(sb, indentation, newLineBefore, newLineAfter, text);
        return sb.toString();
    }
}

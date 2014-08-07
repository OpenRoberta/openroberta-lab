package de.fhg.iais.roberta.helper;

public class StringManipulation {
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

    public static String generateString(int indentation, boolean newLineBefore, boolean newLineAfter, String text) {
        StringBuilder sb = new StringBuilder();
        appendCustomString(sb, indentation, newLineBefore, newLineAfter, text);
        return sb.toString();
    }
}

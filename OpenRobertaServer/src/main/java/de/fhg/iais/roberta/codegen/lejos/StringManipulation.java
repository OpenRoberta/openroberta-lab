package de.fhg.iais.roberta.codegen.lejos;

public class StringManipulation {
    public static void appendCustomString(StringBuilder sb, int indentation, boolean newLine, String text) {
        if ( newLine ) {
            sb.append("\n");
        }
        for ( int i = 0; i < indentation; i++ ) {
            sb.append(" ");
        }
        if ( text != null ) {
            sb.append(text);
        }
    }

    public static String generateString(int indentation, boolean newLine, String text) {
        StringBuilder sb = new StringBuilder();
        appendCustomString(sb, indentation, newLine, text);
        return sb.toString();
    }
}

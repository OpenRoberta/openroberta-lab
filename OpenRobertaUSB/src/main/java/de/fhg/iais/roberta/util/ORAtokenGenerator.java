package de.fhg.iais.roberta.util;

import java.util.Random;

/**
 * Class for generating tokens.
 *
 * @author dpyka
 */
public class ORAtokenGenerator {

    private static final String alphabet = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
    private static final int n = alphabet.length();

    /**
     * Create a new token as String of 8 characters length.
     *
     * @return The token on which the brick is being linked to a client.
     */
    public static String generateToken() {
        String token = "";
        Random random = new Random();
        for ( int i = 0; i < 8; i++ ) {
            token = token + alphabet.charAt(random.nextInt(n));
        }
        return token;
    }
}

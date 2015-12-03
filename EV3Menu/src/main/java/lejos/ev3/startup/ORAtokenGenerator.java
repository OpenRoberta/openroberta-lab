package lejos.ev3.startup;

import java.util.Random;

/**
 * Class for generating tokens.
 *
 * @author dpyka
 */
public class ORAtokenGenerator {

    private final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final int n = this.alphabet.length();

    public ORAtokenGenerator() {
        //
    }

    /**
     * Create a new token as String of 8 characters length.
     *
     * @return The token on which the brick is being linked to a client.
     */
    public String generateToken() {
        String token = "";
        Random random = new Random();
        for ( int i = 0; i < 8; i++ ) {
            token = token + this.alphabet.charAt(random.nextInt(this.n));
        }
        return token;
    }
}

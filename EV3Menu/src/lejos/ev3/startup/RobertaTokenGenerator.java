package lejos.ev3.startup;

import java.util.Random;

/**
 * Class for generating tokens<br>
 * TODO not used at the moment @ server<br>
 * 
 * @author dpyka
 */
public class RobertaTokenGenerator {

    private final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int n = this.alphabet.length();

    public RobertaTokenGenerator() {
        //
    }

    public String generateToken(int tokenlength) {
        String token = "";
        Random random = new Random();
        for ( int i = 0; i < tokenlength; i++ ) {
            token = token + this.alphabet.charAt(random.nextInt(this.n));
        }
        return token;
    }
}

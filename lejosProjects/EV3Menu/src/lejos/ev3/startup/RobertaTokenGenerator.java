package lejos.ev3.startup;

import java.util.Random;

/**
 * Class for generating tokens<br>
 * TODO not used at the moment<br>
 * TODO how many characters should token have??
 * 
 * @author dpyka
 */
public class RobertaTokenGenerator {

    private final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int n = this.alphabet.length();

    public static void main(String[] args) {
        RobertaTokenGenerator tokenGenerator = new RobertaTokenGenerator();
        System.out.println(tokenGenerator.generateToken());
    }

    public RobertaTokenGenerator() {
        //
    }

    public String generateToken() {
        String token = "";
        Random random = new Random();
        // how many characters?!
        for ( int i = 0; i < 8; i++ ) {
            token = token + this.alphabet.charAt(random.nextInt(this.n));
        }
        return token;
    }
}

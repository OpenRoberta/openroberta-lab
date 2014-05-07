package de.fhg.iais.roberta.javaServer.util;

import java.util.Random;

public class TokenGenerator {

    private final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int N = this.alphabet.length();

    public static void main(String[] args) {
        TokenGenerator tokenGenerator = new TokenGenerator();
        System.out.println(tokenGenerator.generateToken());
    }

    public TokenGenerator() {
        //
    }

    public String generateToken() {
        String token = "";
        Random random = new Random();
        for ( int i = 0; i < 5; i++ ) {
            token = token + this.alphabet.charAt(random.nextInt(this.N));
        }
        return token;
    }
}

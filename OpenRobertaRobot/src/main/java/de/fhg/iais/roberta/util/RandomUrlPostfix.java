package de.fhg.iais.roberta.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUrlPostfix {
    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM = "0123456789";
    private static final String SPL_CHARS = "-_";

    private static final Random RANDOM = new SecureRandom();

    public static String generate(int minLen, int maxLen, int noOfCAPSAlpha, int noOfDigits, int noOfSplChars) {
        if ( minLen > maxLen ) {
            throw new IllegalArgumentException("Min. Length > Max. Length!");
        }
        if ( (noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen ) {
            throw new IllegalArgumentException("Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
        }
        int len = RANDOM.nextInt(maxLen - minLen + 1) + minLen;
        char[] pswd = new char[len];
        int index = 0;
        for ( int i = 0; i < noOfCAPSAlpha; i++ ) {
            index = getNextIndex(len, pswd);
            pswd[index] = ALPHA_CAPS.charAt(RANDOM.nextInt(ALPHA_CAPS.length()));
        }
        for ( int i = 0; i < noOfDigits; i++ ) {
            index = getNextIndex(len, pswd);
            pswd[index] = NUM.charAt(RANDOM.nextInt(NUM.length()));
        }
        for ( int i = 0; i < noOfSplChars; i++ ) {
            index = getNextIndex(len, pswd);
            pswd[index] = SPL_CHARS.charAt(RANDOM.nextInt(SPL_CHARS.length()));
        }
        for ( int i = 0; i < len; i++ ) {
            if ( pswd[i] == 0 ) {
                pswd[i] = ALPHA.charAt(RANDOM.nextInt(ALPHA.length()));
            }
        }
        return String.valueOf(pswd);
    }

    private static int getNextIndex(int len, char[] pswd) {
        int index = RANDOM.nextInt(len);
        while ( pswd[index] != 0 ) {
            index = RANDOM.nextInt(len);
        }
        return index;
    }
}
package de.fhg.iais.roberta.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encryption {
    private static final Logger LOG = LoggerFactory.getLogger(Encryption.class);
    private static final SecureRandom secureRandom;

    static {
        SecureRandom secureTemp;
        try {
            secureTemp = SecureRandom.getInstance("SHA1PRNG");
        } catch ( NoSuchAlgorithmException e ) {
            LOG.error("creating a secure random failed. It will be IMPOSSIBLE to create new users!");
            secureTemp = null;
        }
        secureRandom = secureTemp;
    }

    /**
     * create a secure hash from a String.
     *
     * @return a string consisting of 8 bytes (16 chars) of salt, and 8 bytes (16 chars) of the generated hash
     * @throws Exception
     */
    public static String createHash(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        byte[] salt = createNextSalt();
        digest.update(salt);
        String saltHex = Hex.encodeHexString(salt);
        String passwordHex = Hex.encodeHexString(digest.digest(password.getBytes("UTF-8")));
        return saltHex + ":" + passwordHex;
    }

    /**
     * check a password against a secure hash.
     *
     * @return true, if the password matches the hash; false, otherwise
     * @throws Exception
     */
    public static boolean isPasswordCorrect(String saltedHashString, String password) throws Exception {
        String[] splitted = saltedHashString.split(":");
        byte[] salt = Hex.decodeHex(splitted[0].toCharArray());
        byte[] hash = Hex.decodeHex(splitted[1].toCharArray());
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] passwordHash = digest.digest(password.getBytes("UTF-8"));
        return Arrays.equals(hash, passwordHash);
    }

    /**
     * create a secure salt (for encryption of passwords, e.g.) and return it as a 16 byte base64 encoded string
     *
     * @return
     */
    public static synchronized byte[] createNextSalt() {
        byte[] nextSalt = new byte[8];
        secureRandom.nextBytes(nextSalt);
        return nextSalt;
    }
}

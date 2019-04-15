package de.fhg.iais.roberta.persistence.bo;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationDataTest {

    @Test
    public void testHash() {
        String input =
            ""
                + "from: https://crypto.stackexchange.com/questions/2994/what-is-the-recommended-replacement-for-md5:\n"
                + "Since MD5 is broken for purposes of security, what hash should I be using now for secure applications?\n"
                + "That depends on what you want to use the hash function for.\n"
                + "For signing documents, sha2 (e. g. sha512) is considered secure.\n"
                + "For storing passwords, you should use one of the algorithms dedicated for this purpose: e. g. bcrypt, sha512crypt or scrypt. "
                + "In order to slow down an attacker, these algorithms apply the hash functions many times with an "
                + "input that is based on the number of the current round.\n"
                + "Scrypt takes this concept one step further and uses a huge amount of memory. Typical hardware for password cracking has access "
                + "to about a couple of KB of memory, the default configuration of scrypt requires 16 MB.";
        String expected = "a0e6361ba7cc4f2081369eb818b8bc49aa2daa027e346b183684f39f7ccd28c8";
        Assert.assertEquals(expected, ConfigurationData.createHash(input));
    }

}

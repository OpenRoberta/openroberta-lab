package de.fhg.iais.roberta.components.ev3c4ev3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtils {
    public static void setBytes(byte[] block, int offset, byte[] bytes) {
        System.arraycopy(bytes, 0, block, offset, bytes.length);
    }

    /**
     * set 4 bytes in the block byte array
     *
     * @param block block to which set the 4 bytes
     * @param offset offset to where put the bytes in the block
     * @param word the 4 bytes to write represented as an integer
     */
    public static void setWord(byte[] block, int offset, int word) {
        byte[] wordInBytes = intToWord(word);
        setBytes(block, offset, wordInBytes);
    }

    public static byte[] intToWord(int value) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
    }

}

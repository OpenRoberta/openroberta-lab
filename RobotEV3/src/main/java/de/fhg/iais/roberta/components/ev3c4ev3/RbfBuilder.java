package de.fhg.iais.roberta.components.ev3c4ev3;

import java.nio.charset.StandardCharsets;

import static de.fhg.iais.roberta.components.ev3c4ev3.ByteUtils.setWord;

/**
 * Builder to create rbf files, used by the EV3 to show a program in the menu
 */
public class RbfBuilder {

    private static final int RBF_MAGIC_CONSTANT = 0x4f47454c; // LEGO
    private static final byte[] RBF_CONSTANT_BEFORE_COMMAND = { (byte) 0x68,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x1C,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x08,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x60,(byte) 0x80 };
    private static final byte[] RBF_CONSTANT_AFTER_COMMAND = { (byte) 0x44,(byte) 0x85,(byte) 0x82,(byte) 0xE8,(byte) 0x03,(byte) 0x40,(byte) 0x86,(byte) 0x40,(byte) 0x0A };

    private static final int LENGTH_MAGIC_CONSTANT = 4;
    private static final int LENGTH_RBF_FILE_SIZE = 4;

    private static final int OFFSET_MAGIC_CONSTANT = 0;
    private static final int OFFSET_RBF_FILE_SIZE = 4;
    private static final int OFFSET_COMMAND = LENGTH_MAGIC_CONSTANT + LENGTH_RBF_FILE_SIZE + RBF_CONSTANT_BEFORE_COMMAND.length;

    /**
     * Build the rbf file to execute the specified command.
     * The command may be just the name of the executable to execute.
     * @param command
     * @return bytes of the rbf file
     */
    public byte[] build(String command) {
        byte[] commandBytes = command.getBytes(StandardCharsets.UTF_8);
        int rbfSize = getRbfSize(commandBytes);
        byte[] rbf = new byte[rbfSize];

        setWord(rbf, OFFSET_MAGIC_CONSTANT, RBF_MAGIC_CONSTANT);
        setWord(rbf, OFFSET_RBF_FILE_SIZE, rbfSize);
        System.arraycopy(RBF_CONSTANT_BEFORE_COMMAND, 0, rbf, 8, RBF_CONSTANT_BEFORE_COMMAND.length);
        System.arraycopy(commandBytes, 0, rbf, OFFSET_COMMAND, commandBytes.length);
        System.arraycopy(RBF_CONSTANT_AFTER_COMMAND, 0, rbf, getOffsetConstantAfterCommand(commandBytes), RBF_CONSTANT_AFTER_COMMAND.length);

        return rbf;
    }

    private static int getRbfSize(byte[] commandBytes) {
        return LENGTH_MAGIC_CONSTANT
            + LENGTH_RBF_FILE_SIZE
            + RBF_CONSTANT_BEFORE_COMMAND.length
            + commandBytes.length
            + 1 // null byte to terminate the command string
            + RBF_CONSTANT_AFTER_COMMAND.length;
    }

    private static int getOffsetConstantAfterCommand(byte[] commandBytes) {
        return OFFSET_COMMAND
            + commandBytes.length
            + 1; // null byte to terminate the command string
    }

}

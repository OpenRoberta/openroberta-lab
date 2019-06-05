package de.fhg.iais.roberta.components.ev3c4ev3;

import de.fhg.iais.roberta.util.dbc.DbcException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static de.fhg.iais.roberta.components.ev3c4ev3.ByteUtils.setWord;

/**
 * Builder to create rbf files, used by the EV3 to show a program in the menu.
 * An rbf file is an executable file that contains EV3 VM instructions. This builder
 * patches an existing rbf file (contained in the cross compiler resources folder)
 * that starts a program given the executable file name.
 * The rbf file to patch contains a placeholder that this builder replaces with the
 * name of the executable to run.
 */
public class RbfBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RbfBuilder.class);

    private static final String EXECUTABLE_PLACEHOLDER = "--executableName--";
    private static final String EXECUTABLE_PLACEHOLDER_HEX_STRING = Hex.encodeHexString(EXECUTABLE_PLACEHOLDER.getBytes(StandardCharsets.UTF_8));

    private static final int OFFSET_RBF_FILE_SIZE = 4;

    private final String compilerResourceDir;

    public RbfBuilder(String compilerResourceDir) {
        this.compilerResourceDir = compilerResourceDir;
    }

    /**
     * Build the rbf file to execute the specified file.
     *
     * @param executableFileName file name of the executable on the EV3 file system
     * @return content of the rbf file
     */
    public byte[] build(String executableFileName) {
        byte[] executableFileNameBytes = executableFileName.getBytes(StandardCharsets.UTF_8);
        byte[] rbfToPatch = loadRbfToPatch();
        return patch(rbfToPatch, executableFileNameBytes);
    }

    private byte[] loadRbfToPatch() {
        try {
            return FileUtils.readFileToByteArray(new File(compilerResourceDir + "c4ev3/launcher/launcher.rbf"));
        } catch ( IOException e ) {
            LOG.error("cannot load rbf file to patch: {}", e);
            throw new DbcException("cannot load rbf file to patch", e);
        }
    }

    /**
     * Return a new array that contains the same bytes of the rbfToPatch parameter except the 4 bytes of the size and the placeholder
     * @param rbfToPatch
     * @param executableFileName
     * @return
     */
    byte[] patch(byte[] rbfToPatch, byte[] executableFileName) {
        int rbfSize = getRbfSize(rbfToPatch, executableFileName);
        byte[] patchedRbf = new byte[rbfSize];
        byte[][] rbfPrefixAndPostfix = splitRbfFileAtPlaceholder(rbfToPatch);

        byte[] prefix = rbfPrefixAndPostfix[0];
        byte[] postfix = rbfPrefixAndPostfix[1];

        addPrefixToRbf(patchedRbf, prefix);
        addExecutableNameToRbf(patchedRbf, executableFileName, prefix);
        addPostfixToRbf(patchedRbf, postfix, prefix, executableFileName);
        setWord(patchedRbf, OFFSET_RBF_FILE_SIZE, rbfSize);

        return patchedRbf;
    }

    private int getRbfSize(byte[] rbfToPatch, byte[] executableFileName) {
        return rbfToPatch.length - EXECUTABLE_PLACEHOLDER.length() + executableFileName.length;
    }

    private byte[][] splitRbfFileAtPlaceholder(byte[] rbf) {
        String rbfToPatchAsHexString = Hex.encodeHexString(rbf);
        String[] rbfPrefixAndPostfixAsHexStrings = rbfToPatchAsHexString.split(EXECUTABLE_PLACEHOLDER_HEX_STRING);
        try {
            return new byte[][] {
                Hex.decodeHex(rbfPrefixAndPostfixAsHexStrings[0]),
                Hex.decodeHex(rbfPrefixAndPostfixAsHexStrings[1])
            };
        } catch ( DecoderException e ) {
            LOG.error("unexpected hex string cannot be decoded: {}", e);
            throw new DbcException("unexpected hex string cannot be decoded", e);
        }
    }

    private void addPrefixToRbf(byte[] rbf, byte[] prefix) {
        System.arraycopy(prefix, 0, rbf, 0, prefix.length);
    }

    /**
     * @param rbf
     * @param executableFileName
     * @param prefix used to compute the offset
     */
    private void addExecutableNameToRbf(byte[] rbf, byte[] executableFileName, byte[] prefix) {
        int offset = prefix.length;
        System.arraycopy(executableFileName, 0, rbf, offset, executableFileName.length);
    }

    /**
     *
     * @param rbf
     * @param postfix
     * @param prefix used to compute the offset
     * @param executableFileName used to compute the offset
     */
    private void addPostfixToRbf(byte[] rbf, byte[] postfix, byte[] prefix, byte[] executableFileName) {
        int offset = prefix.length + executableFileName.length;
        System.arraycopy(postfix, 0, rbf, offset, postfix.length);
    }

}

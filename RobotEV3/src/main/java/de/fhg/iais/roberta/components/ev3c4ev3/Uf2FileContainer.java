package de.fhg.iais.roberta.components.ev3c4ev3;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static de.fhg.iais.roberta.components.ev3c4ev3.ByteUtils.setBytes;
import static de.fhg.iais.roberta.components.ev3c4ev3.ByteUtils.setWord;

/**
 * Represent a UF2 file in 'file container' mode.
 * Specifications of this file format can be found here: https://github.com/Microsoft/uf2
 */
public class Uf2FileContainer {

    private static final int UF2_BLOCK_SIZE = 512;
    /**
     * TODO: Since we are now using c++ with static linking of libstdcc+, the generate binaries are bigger.
     * check if the EV3 supports bigger payload sizes, to reduce the transfer time (which now takes around 8 seconds)
     */
    private static final int UF2_BLOCK_PAYLOAD_SIZE = 256;
    //private static final int UF2_BLOCK_PAYLOAD_SIZE = 400;

    private static final int UF2_START_BLOCK_MAGIC_CONSTANT_1 = 0x0A324655;
    private static final int UF2_START_BLOCK_MAGIC_CONSTANT_2 = 0x9E5D5157;
    private static final int UF2_FILE_CONTAINER_FLAG = 0x00001000;
    private static final int UF2_END_BLOCK_MAGIC_CONSTANT = 0x0AB16F30;

    private static final int OFFSET_MAGIC_CONSTANT_1 = 0;
    private static final int OFFSET_MAGIC_CONSTANT_2 = 4;
    private static final int OFFSET_FLAGS = 8;
    private static final int OFFSET_OFFSET_CURRENT_FILE = 12;
    private static final int OFFSET_BLOCK_PAYLOAD_SIZE = 16;
    private static final int OFFSET_CURRENT_FILE_SEQUENTIAL_BLOCK_COUNT = 20;
    private static final int OFFSET_CURRENT_FILE_BLOCKS_COUNT = 24;
    private static final int OFFSET_FILE_SIZE = 28;

    private final List<byte[]> fileContentBlocks = new ArrayList<>();

    /**
     * add a file to the uf2 file container.
     *
     * @param sourceFile file to add to the container
     * @param fileName name that the file is going to have inside the container
     * @throws IOException
     */
    public void add(File sourceFile, String fileName) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(sourceFile);
        this.add(fileContent, fileName);
    }

    /**
     * add a file to uf2 file container given the file content
     *
     * @param fileContent content of the file to add to the container
     * @param fileName name that the file is going to have inside the container
     */
    public void add(byte[] fileContent, String fileName) {
        List<byte[]> uf2BlockPayloads = splitByteArrayIntoUf2BlockPayloads(fileContent);
        List<byte[]> uf2Blocks = getUf2Blocks(uf2BlockPayloads, fileName, fileContent.length);
        this.fileContentBlocks.addAll(uf2Blocks);
    }

    private static List<byte[]> splitByteArrayIntoUf2BlockPayloads(byte[] content) {
        int blocksCount = (int) Math.ceil((double) (content.length / UF2_BLOCK_PAYLOAD_SIZE));
        List<byte[]> payloads = new ArrayList<>(blocksCount);
        for ( int from = 0; from < content.length; from += UF2_BLOCK_PAYLOAD_SIZE ) {
            byte[] payload = new byte[UF2_BLOCK_PAYLOAD_SIZE];
            int bytesToCopy = Math.min(UF2_BLOCK_PAYLOAD_SIZE, content.length - from);
            System.arraycopy(content, from, payload, 0, bytesToCopy);
            payloads.add(payload);
        }
        return payloads;
    }

    private static List<byte[]> getUf2Blocks(List<byte[]> payloads, String fileName, int fileSize) {
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        int blocksCount = payloads.size();
        List<byte[]> uf2Blocks = new ArrayList<>(blocksCount);

        for ( int i = 0; i < payloads.size(); i++ ) {
            byte[] payload = payloads.get(i);
            byte[] uf2Block = getUf2Block(payload, fileNameBytes, blocksCount, i, fileSize);
            uf2Blocks.add(uf2Block);
        }

        return uf2Blocks;
    }

    private static byte[] getUf2Block(byte[] payload, byte[] fileNameBytes, int blocksCount, int blockIndex, int fileSize) {
        byte[] uf2Block = new byte[UF2_BLOCK_SIZE];

        // Header
        setWord(uf2Block, OFFSET_MAGIC_CONSTANT_1, UF2_START_BLOCK_MAGIC_CONSTANT_1);
        setWord(uf2Block, OFFSET_MAGIC_CONSTANT_2, UF2_START_BLOCK_MAGIC_CONSTANT_2);
        setWord(uf2Block, OFFSET_FLAGS, UF2_FILE_CONTAINER_FLAG);
        setWord(uf2Block, OFFSET_OFFSET_CURRENT_FILE, blockIndex * UF2_BLOCK_PAYLOAD_SIZE);
        setWord(uf2Block, OFFSET_BLOCK_PAYLOAD_SIZE, UF2_BLOCK_PAYLOAD_SIZE); // length
        setWord(uf2Block, OFFSET_CURRENT_FILE_SEQUENTIAL_BLOCK_COUNT, blockIndex);
        setWord(uf2Block, OFFSET_CURRENT_FILE_BLOCKS_COUNT, blocksCount);
        setWord(uf2Block, OFFSET_FILE_SIZE, fileSize);

        // File content
        setBytes(uf2Block, 32, payload);

        // File name
        setBytes(uf2Block, 32 + UF2_BLOCK_PAYLOAD_SIZE, fileNameBytes);

        // Footer
        setWord(uf2Block, 512 - 4, UF2_END_BLOCK_MAGIC_CONSTANT);

        return uf2Block;
    }

    public String toBase64() {
        byte[] content = this.toByteArray();
        return Base64.getEncoder().encodeToString(content);
    }

    public byte[] toByteArray() {
        int size = this.fileContentBlocks.size() * UF2_BLOCK_SIZE;
        byte[] content = new byte[size];
        for ( int i = 0; i < this.fileContentBlocks.size(); i++ ) {
            byte[] block = this.fileContentBlocks.get(i);
            System.arraycopy(block, 0, content, i * UF2_BLOCK_SIZE, UF2_BLOCK_SIZE);
        }
        return content;
    }

}
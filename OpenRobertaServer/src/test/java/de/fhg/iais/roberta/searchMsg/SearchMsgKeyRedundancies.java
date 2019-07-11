package de.fhg.iais.roberta.searchMsg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

public class SearchMsgKeyRedundancies {

    private Map<String, Set<String>> robMsgPairs, blocklyMsgPairs;
    private static int expectedKeyCount = 1800; // Should be 50% higher than the last known key count

    /**
     * @param robKeyFile A file that contains a set of message keys and the corresponding messages
     * @param blocklyKeyFile Another file with messages and keys, that shall be used as fallback for unregistered keys, in case the key is used in another
     *        project
     * @throws IOException In case the files can not be opened
     */
    public SearchMsgKeyRedundancies(File robKeyFile, File blocklyKeyFile) throws IOException {
        this.robMsgPairs = getMsgPairs(robKeyFile);
        this.blocklyMsgPairs = getMsgPairs(blocklyKeyFile);

        println("Found " + this.robMsgPairs.size() + " unique messages in: " + robKeyFile.getName());
        println("Found " + this.blocklyMsgPairs.size() + " unique messages in: " + blocklyKeyFile.getName());
        println("");
    }

    /**
     * @param msgKeyFile File with message keys
     * @return
     * @throws IOException In case the file can not be opened
     */
    private Map<String, Set<String>> getMsgPairs(File msgKeyFile) throws IOException {
        Assert.assertNotNull(msgKeyFile);

        int expectedKeyCountPerRedundancy = (int) Math.ceil(Math.sqrt(SearchMsgKeyRedundancies.expectedKeyCount));
        Pattern regex =
            Pattern
                .compile(
                    "^Blockly\\.Msg\\.([A-Z0-9]+(?:_[A-Z0-9]+)*[a-z]?)(?!\\\\w|\\\\d)[ \\t]*\\=[ \\t]*(?:['\"](.*?)['\"]|Blockly\\.Msg\\.([A-Z0-9]+(?:_[A-Z0-9]+)*[a-z]?)(?!\\\\w|\\\\d));*$");
        String line;
        Map<String, String> keys = new HashMap<>(SearchMsgKeyRedundancies.expectedKeyCount),
            directRedundants = new HashMap<>(SearchMsgKeyRedundancies.expectedKeyCount / 2);
        Map<String, Set<String>> result;
        LineNumberReader lineReader = new LineNumberReader(new FileReader(msgKeyFile));

        while ( (line = lineReader.readLine()) != null ) {
            Matcher matcher = regex.matcher(line);
            if ( matcher.find() ) {
                if ( matcher.group(3) == null ) {
                    keys.put(matcher.group(1), matcher.group(2));
                } else {
                    directRedundants.put(matcher.group(1), matcher.group(3));
                }
            }
        }

        lineReader.close();

        result = new HashMap<>(keys.size(), 1f);

        directRedundants.forEach(new BiConsumer<String, String>() {

            @Override
            public void accept(String key, String refKey) {
                String finalReference = refKey;
                while ( directRedundants.get(finalReference) != null ) {
                    finalReference = directRedundants.get(finalReference);
                }
                if ( keys.get(finalReference) != null ) {
                    keys.putIfAbsent(key, keys.get(finalReference));
                }
            }
        });

        keys.forEach(new BiConsumer<String, String>() {

            @Override
            public void accept(String key, String message) {
                if ( message.isEmpty() ) {
                    return;
                }

                Set<String> keyList = result.get(message);
                if ( keyList != null ) {
                    keyList.add(key);
                    return;
                }

                keyList = new HashSet<>(expectedKeyCountPerRedundancy, .9f);
                keyList.add(key);
                result.put(message, keyList);
            }
        });

        return result;
    }

    public void stastics() {

        this.robMsgPairs.forEach(new BiConsumer<String, Set<String>>() {

            @Override
            public void accept(String message, Set<String> keys) {
                if ( keys.size() > 1 ) {
                    printf("Redundant message definitions for the message: %s\n", message);
                    print("Keys: ");
                    keys.forEach(key -> System.out.printf("%s, ", key));
                    println();
                    println();
                }
            }
        });

        this.blocklyMsgPairs.forEach(new BiConsumer<String, Set<String>>() {

            @Override
            public void accept(String message, Set<String> keys) {
                if ( robMsgPairs.get(message) != null ) {
                    printf("The message \"%s\" is redinfed in both files:\n", message);
                    print("Roberta keys: ");
                    robMsgPairs.get(message).forEach(key -> System.out.printf("%s, ", key));
                    println();
                    print("Blockly keys: ");
                    keys.forEach(key -> System.out.printf("%s, ", key));
                    println();
                    println();
                }
            }
        });
    }

    private static void println(String msg) {
        System.out.println(msg);
    }

    private static void println() {
        System.out.println();
    }

    private static void print(String msg) {
        System.out.print(msg);
    }

    private static void printf(String format, Object... params) {
        System.out.printf(format, params);
    }
}

package de.fhg.iais.roberta.searchMsg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

/**
 * This class<br>
 * - gets in its constructor two files with keys (e.g. a property file, a JSON file with key-value pairs, ...)<br>
 * - and using a pattern and further extraction logic it extracts all the keys from the file<br>
 * - then it gets many pairs of directories and patterns<br>
 * - for each pair it retrieves all files from the directory (recursively) matching the pattern<br>
 * - for each file it iterates over all lines and checks if a key can be found in that line<br>
 * - and checks if that key is found in the first key file then<br>
 * - furthermore it remembers that the key has been found at all<br>
 * - it prints a summary about the results for the whole directory<br>
 * <br>
 * - if all directories have been processed, it tells which keys are <b>not</b> used or not registered in the key file.<br>
 *
 * @author lbudde + rbudde + pmaurer
 */
class SearchMsgKeyOccurrences {
    private final List<String> robKeyList, blocklyKeyList, unusedKeys, unknownKeys;
    private final String keyPattern = "([A-Z0-9]+(?:_[A-Z0-9]+)*[a-z]?)(?!\\w|\\d)";
    private final Pattern keyRegexp = Pattern.compile("[^\"]Key\\." + keyPattern + "(?:[^\\w\"]|$)"),
        nepoInfoRegExp = Pattern.compile("NepoInfo\\.\\w+\\((?:Serverity\\.[A-Z]+\\s*,)?\\s*\"" + keyPattern + "\"\\)"),
        checkMotorRegExp = Pattern.compile("checkIfMotorRegulated\\(.+?,\\s*\"" + keyPattern + "\"\\)"),
        directRegExp = Pattern.compile("Blockly\\.Msg(?:\\.|\\[[\"'])" + keyPattern + "(?:[^\\w]|$)"),
        helperRegExp =
            Pattern
                .compile(
                    "\\.(?:showMsgOnTop\\(|display(?:(?:Popup)?Message\\(|Information\\(\\s*(?:\\{\\s*[\"']?\\w+?[\"']?\\s*:\\s*[\"']\\w+[\"']\\s*\\}|\\w+)\\s*,))\\s*[\"']"
                        + keyPattern
                        + "[\"']"),
        createButtonRegExp = Pattern.compile("\\.createButton\\_\\(.+?,\\-?\\d+,\\-?\\d+,[\"']" + keyPattern + "[\"']\\)"),
        categoryNameRegExp = Pattern.compile("\\<category(?:\\s+\\w+\\=(?:[\"][^\"]+[\"]|['][^']+[']))*\\s+name\\=[\"']" + keyPattern + "[\"']");

    /**
     * @param robKeyFile A file that contains a set of message keys and the corresponding messages
     * @param blocklyKeyFile Another file with messages and keys, that shall be used as fallback for unregistered keys, in case the key is used in another
     *        project
     * @throws IOException In case the files can not be opened
     */
    public SearchMsgKeyOccurrences(File robKeyFile, File blocklyKeyFile) throws IOException {
        this.robKeyList = getKeyList(robKeyFile);
        this.blocklyKeyList = getKeyList(blocklyKeyFile);
        this.unusedKeys = new ArrayList<>(this.robKeyList);
        this.unknownKeys = new ArrayList<>(100);
        println("Found " + this.robKeyList.size() + " message keys in: " + robKeyFile.getName());
        println("Found " + this.blocklyKeyList.size() + " message keys in: " + blocklyKeyFile.getName());
    }

    /**
     * Searches all files in the given directory, or the given file, for keys and matches them with the keys from the key file Afterwards it prints a short
     * report in the following format:<br/>
     * <br/>
     * The directory {path} with {numFiles} files contained: {numFoundKeys} Roberta keys, {numFallbackKeys} Blockly keys and {numUnknownKeys} unregistered keys.
     *
     * @param dir A directory that contains files, or a single file
     * @param fileNamePattern A pattern, that the files need to match to be searched
     * @throws Exception In case a file cannot be opened
     */
    public void search(File dir, Pattern fileNamePattern) throws Exception {
        List<File> files = filterDir(dir, fileNamePattern);
        String dirPath = dir.getCanonicalPath();
        KeyCounter keyCounts = this.searchForKeys(files);
        System.out
            .printf(
                "The directory %s with %d files contained: %d Roberta keys, %d Blockly keys and %d unregistered keys.\n",
                dirPath,
                files.size(),
                keyCounts.getRobertaKeyCount(),
                keyCounts.getBlocklyKeyCount(),
                keyCounts.getUnknownKeyCount());
    }

    /**
     * Prints a detailed report of the executed searches, that contains the unused and unregistered keys of the key file.
     */
    public void statistics() {
        Collections.sort(this.unusedKeys);
        Collections.sort(this.unknownKeys);
        System.out.println("The following message keys seem to be unused:");
        printListLn(this.unusedKeys);
        println("The following message keys seem to be unknown:");
        printListLn(this.unknownKeys);
        println(this.robKeyList.size() + " message keys");
        println(this.unusedKeys.size() + " unused message keys");
        println(this.unknownKeys.size() + " unknown message keys");
    }

    /**
     * Searches a message key file for all Strings which represents message keys. Then creates a List with all these keys.<br>
     * <br>
     * <u>Each use of this method has to be changed to reflect different patterns and extraction of substrings.</u>
     *
     * @param msgKeyFile File with message keys
     * @return A List of the message keys
     */
    private List<String> getKeyList(File msgKeyFile) throws IOException {
        Assert.assertNotNull(msgKeyFile);

        Pattern regex = Pattern.compile("^Blockly.Msg.([^ =]+)");
        String line;
        List<String> keys = new ArrayList<>(1500);

        try (LineNumberReader lineReader = new LineNumberReader(new FileReader(msgKeyFile))) {
            while ( (line = lineReader.readLine()) != null ) {
                Matcher matcher = regex.matcher(line);
                if ( matcher.find() ) {
                    keys.add(matcher.group(1));
                }
            }
            lineReader.close();
        }
        return keys;
    }

    /**
     * Seeks for all the non-Directory-Files in a given File(root). Then checks which of these file names match the given Pattern. The matching Files are then
     * added to the resultDataList.
     *
     * @param root the File to work with
     * @param pattern the pattern to look up in file names
     * @return a List of Files, whose file names match the given Pattern
     * @throws IOException
     */
    private List<File> filterDir(File root, Pattern pattern) throws IOException {
        List<File> resultDataList = new ArrayList<>(250);

        if ( root.isDirectory() ) {
            File[] directoryFiles = root.listFiles();
            if ( directoryFiles == null ) {
                System.out.println("Directory is not readable and thus ignored: " + root.getCanonicalPath());
                return resultDataList;
            }
            for ( File file : directoryFiles ) {
                List<File> resultFiles = filterDir(file, pattern);
                resultDataList.addAll(resultFiles);
            }
        } else {
            Matcher matcher = pattern.matcher(root.getAbsolutePath());
            if ( matcher.matches() ) {
                resultDataList.add(root);
            }
        }
        return resultDataList;
    }

    /**
     * Searches in all Files for message keys.
     *
     * @param fileList to search within
     * @param regexList the List of patterns for searching
     * @return An object that contains the result counts for roberta keys, blockly keys and unregistered keys.
     * @throws IOException
     */
    private KeyCounter searchForKeys(List<File> fileList) throws Exception {
        KeyCounter keyCounter = new KeyCounter();
        String key, line, filePath, fileEnding;
        Matcher matcher = null;
        ArrayList<Pattern> usedPatterns;

        for ( File file : fileList ) {
            filePath = file.getAbsolutePath();

            if ( filePath.contains("target") || filePath.contains("demos") ) {
                continue;
            }

            fileEnding = filePath.substring(filePath.lastIndexOf('.') + 1);

            try (LineNumberReader lineReader = new LineNumberReader(new FileReader(file))) {

                while ( (line = lineReader.readLine()) != null ) {

                    usedPatterns = new ArrayList<>(4);
                    while ( matcher != null && matcher.find() || (matcher = this.matchLine(line, fileEnding, usedPatterns)) != null ) {
                        key = matcher.pattern().equals(keyRegexp) ? "ORA_" + matcher.group(1) : matcher.group(1);

                        if ( robKeyList.contains(key) ) {
                            this.unusedKeys.remove(key);
                            keyCounter.addRobertaKey();
                        } else if ( this.blocklyKeyList.contains(key) && filePath.contains("blockly") ) {
                            // Only accept the messages.js keys if they belong to the blockly core.
                            // If we reuse keys in our code we should define them.
                            keyCounter.addBlocklyKey();
                        } else {
                            if ( !this.unknownKeys.contains(key) ) {
                                this.unknownKeys.add(key);
                            }
                            keyCounter.addUnknownKey();
                        }
                    }
                }
                lineReader.close();
            }
        }

        return keyCounter;
    }

    /**
     * Matches a line against multiple patterns, depending on the file ending. If a pattern matches, the matcher is returned. In addition to that a list of
     * patterns is returned, that already matched to the given
     *
     * @param line The line that shall be matched against
     * @param fileEnding The file ending of the file, that contains the line
     * @param usedPatterns Patterns, that were already used for that line
     * @return The matcher, that matched to one of the key patterns. If no pattern matches null is returned.
     */
    private Matcher matchLine(String line, String fileEnding, List<Pattern> usedPatterns) {
        Matcher matcher = null;

        switch ( fileEnding ) {
            case "java":
                if ( !usedPatterns.contains(this.keyRegexp) ) {
                    matcher = this.keyRegexp.matcher(line);
                    usedPatterns.add(this.keyRegexp);
                }
                if ( matcher == null || !matcher.find() ) {
                    if ( !usedPatterns.contains(this.nepoInfoRegExp) ) {
                        matcher = this.nepoInfoRegExp.matcher(line);
                        usedPatterns.add(this.nepoInfoRegExp);
                    }
                    if ( matcher == null || !matcher.find() ) {
                        if ( !usedPatterns.contains(this.checkMotorRegExp) ) {
                            matcher = this.checkMotorRegExp.matcher(line);
                            usedPatterns.add(this.checkMotorRegExp);
                        }
                        if ( matcher == null || !matcher.find() ) {
                            return null;
                        }
                    }
                }
                break;
            default:
                if ( !usedPatterns.contains(this.directRegExp) ) {
                    matcher = this.directRegExp.matcher(line);
                    usedPatterns.add(this.directRegExp);
                }
                if ( matcher == null || !matcher.find() ) {
                    if ( !usedPatterns.contains(this.helperRegExp) ) {
                        matcher = this.helperRegExp.matcher(line);
                        usedPatterns.add(this.helperRegExp);
                    }
                    if ( matcher == null || !matcher.find() ) {
                        if ( !usedPatterns.contains(this.createButtonRegExp) ) {
                            matcher = this.createButtonRegExp.matcher(line);
                            usedPatterns.add(this.createButtonRegExp);
                        }
                        if ( matcher == null || !matcher.find() ) {
                            if ( !usedPatterns.contains(this.categoryNameRegExp) ) {
                                matcher = this.categoryNameRegExp.matcher(line);
                                usedPatterns.add(this.categoryNameRegExp);
                            }
                            if ( matcher == null || !matcher.find() ) {
                                return null;
                            }
                        }
                    }
                }
        }
        return matcher;
    }

    private void printListLn(List<String> msgList) {
        for ( String msg : msgList ) {
            println("  " + msg);
        }
    }

    private static void println(String msg) {
        System.out.println(msg);
    }

    private class KeyCounter {
        private int robertaKeys, blocklyKeys, unknownKeys;

        public void addRobertaKey() {
            this.robertaKeys += 1;
        }

        public void addBlocklyKey() {
            this.blocklyKeys += 1;
        }

        public void addUnknownKey() {
            this.unknownKeys += 1;
        }

        public int getRobertaKeyCount() {
            return this.robertaKeys;
        }

        public int getBlocklyKeyCount() {
            return this.blocklyKeys;
        }

        public int getUnknownKeyCount() {
            return this.unknownKeys;
        }
    }
}

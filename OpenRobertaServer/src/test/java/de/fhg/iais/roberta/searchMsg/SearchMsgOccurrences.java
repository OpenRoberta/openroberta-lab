package de.fhg.iais.roberta.searchMsg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import de.fhg.iais.roberta.util.Pair;

/**
 * This class<br>
 * - gets in its constructor a file with keys (e.g. a property file, a JSON file with key-value pairs, ...)<br>
 * - and using a pattern and further extraction logic it extracts all the keys from the file<br>
 * - then it gets many pairs of directories and patterns<br>
 * - for each pair it retrieves all files from the directory (recursively) matching the pattern<br>
 * - for each file it iterates over all lines and checks whether one of the keys is used in the file<br>
 * - it remembers the key found, the matching line and its line number in a multi map<br>
 * - furthermore it remembers that the key has been found at all<br>
 * - it prints a summary about the results for the whole directory<br>
 * <br>
 * - if all directories have been processed, it tells which keys are <b>not</b> used.<br>
 * - the matching lines can be printed, too (has to be activated, because the output is huge)<br>
 * <br>
 * For different use,<br>
 * - adapt the extraction logic in {@link SearchMsgOccurrences#getMsgList(File)}<br>
 * - adapt the print logic<br>
 * - and adapt the list of directories to be searched (see class {@link SearchMsgOccurrencesTest}
 *
 * @author lbudde + rbudde
 */
public class SearchMsgOccurrences {
    private static final boolean DEBUG = true; // true: debugging into to syso; false: only infos and errors
    private final List<String> msgList;
    private final ListMultimap<String, MsgHits> msgKeyOccurrences = ArrayListMultimap.create();
    private final List<String> msgNotYetMatched;

    public SearchMsgOccurrences(File msgKeyFile) throws IOException {
        this.msgList = getMsgList(msgKeyFile);
        Collections.sort(this.msgList);
        this.msgNotYetMatched = new ArrayList<>(this.msgList);
        String propfilePath = msgKeyFile.getCanonicalPath();
        System.out.println("Found " + this.msgList.size() + " message keys in file " + propfilePath);
        if ( false && DEBUG ) {
            sysoList(this.msgList);
        }
    }

    public void search(File file, Pattern fileNamePattern) throws Exception {
        List<File> files = filterDir(file, fileNamePattern);
        String filePath = file.getCanonicalPath();
        Pair<Integer, Integer> mC = findTheMsg(files);
        System.out.println("In " + filePath + " with " + files.size() + " files: " + mC.getFirst() + " keys and " + mC.getSecond() + " keys first time");
    }

    public void statistics() {
        System.out.println("The following message keys seem to be unused:");
        sysoList(msgNotYetMatched);
        System.out.println(this.msgList.size() + " message keys");
        System.out.println(this.msgNotYetMatched.size() + " unused message keys");
        // prettyPrint();
    }

    /**
     * searches a message key file for all Strings which represents message keys. Then creates a List with all these message keys.<br>
     * <br>
     * <b>For each use this method has to be changed to reflect different patterns and extraction of substrings.</b>
     *
     * @param msgKeyFile File with message keys
     * @return A List of the message keys
     */
    private List<String> getMsgList(File msgKeyFile) throws IOException {
        Pattern regex = Pattern.compile("^Blockly.Msg.[^ ]+");
        Assert.assertNotNull(msgKeyFile);
        String line;
        List<String> msgs = new ArrayList<String>();
        try (LineNumberReader lineReader = new LineNumberReader(new FileReader(msgKeyFile))) {
            while ( (line = lineReader.readLine()) != null ) {
                Matcher matcher = regex.matcher(line);
                if ( matcher.find() ) {
                    String group = matcher.group();
                    group = group.substring(12);
                    if ( group.startsWith("ORA_") ) {
                        group = group.substring(4);
                    }
                    msgs.add(group);
                }
            }
        }
        return msgs;
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
        List<File> resultDataList = new ArrayList<>();

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
     * Searches in all Files for Strings which contain at least one of the messsage keys. Store matches in a MultiMap, which maps the keys to its occurrences.
     *
     * @param fileList to search within
     * @param regexList the List of patterns for searching
     * @return A MultiMap which maps the patterns to its occurrences.
     * @throws IOException
     */
    private Pair<Integer, Integer> findTheMsg(List<File> fileList) throws Exception {
        int msgKeysMatched = 0;
        int msgKeysMatchedForTheFirstTime = 0;
        for ( File javaFile : fileList ) {
            int number;
            String line;
            if ( javaFile.getAbsolutePath().contains("target") ) {
                continue;
            }
            try (LineNumberReader lineReader = new LineNumberReader(new FileReader(javaFile))) {

                while ( (line = lineReader.readLine()) != null ) {
                    for ( String msg : this.msgList ) {
                        if ( line.contains(msg) ) {
                            msgKeysMatched++;
                            if ( this.msgNotYetMatched.remove(msg) ) {
                                msgKeysMatchedForTheFirstTime++;
                            }
                            number = lineReader.getLineNumber();
                            MsgHits msgChar = new MsgHits(javaFile.getCanonicalPath(), number, line);
                            msgKeyOccurrences.put(msg, msgChar);
                        }
                    }

                }
            }
        }
        return Pair.of(msgKeysMatched, msgKeysMatchedForTheFirstTime);
    }

    private void prettyPrint() {
        Map<String, Collection<MsgHits>> asMap = this.msgKeyOccurrences.asMap();
        for ( String msg : asMap.keySet() ) {
            Collection<MsgHits> hits = asMap.get(msg);
            System.out.println(msg);
            for ( MsgHits hit : hits ) {
                System.out.println("  " + hit.getFileName() + ";" + hit.getLineNumber() + ";" + hit.getContent());
            }
        }
    }

    private void sysoList(List<String> msgList) {
        for ( String msg : msgList ) {
            System.out.println("  " + msg);
        }
    }
}
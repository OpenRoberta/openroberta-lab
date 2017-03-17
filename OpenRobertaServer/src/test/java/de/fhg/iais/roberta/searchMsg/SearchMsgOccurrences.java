package de.fhg.iais.roberta.searchMsg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class SearchMsgOccurrences {

    /**
     * Searches in a Property File for all the Strings which match the given Pattern.
     * Then creates a List with all these Strings.
     *
     * @param propfile Property File to work with
     * @param pattern
     * @return A List of the Strings which match the given Pattern.
     * @throws IOException
     */
    public List<String> getMsgList(File propfile, String pattern) throws IOException {
        Pattern regex = Pattern.compile(pattern);
        Assert.assertNotNull(propfile);
        String line;
        List<String> msgList = new ArrayList<String>();
        try (LineNumberReader lineReader = new LineNumberReader(new FileReader(propfile))) {
            while ( (line = lineReader.readLine()) != null ) {
                Matcher matcher = regex.matcher(line);
                if ( matcher.find() ) {
                    String group = matcher.group();
                    group = group.substring(12);
                    if ( group.startsWith("ORA_") ) {
                        group = group.substring(4);
                    }
                    msgList.add(group);
                }
            }
        }
        return msgList;

    }

    /**
     * Searches in all Files for Strings which match the given patterns.
     * Then creates a MultiMap, which maps the patterns to its occurrences.
     *
     * @param fileList to search within
     * @param regexList the List of patterns for searching
     * @return A MultiMap which maps the patterns to its occurrences.
     * @throws IOException
     */
    public ListMultimap<String, MsgHits> findTheMsg(List<File> fileList, List<String> regexList) throws IOException, SecurityException {
        ListMultimap<String, MsgHits> msgMultimap = ArrayListMultimap.create();
        Set<String> unusedMsg = new HashSet<>(regexList);
        for ( File javaFile : fileList ) {
            int number;
            String line;
            if ( javaFile.getAbsolutePath().contains("target") ) {
                continue;
            }
            try (LineNumberReader lineReader = new LineNumberReader(new FileReader(javaFile))) {

                while ( (line = lineReader.readLine()) != null ) {
                    for ( String msg : regexList ) {
                        if ( line.contains(msg) ) {
                            unusedMsg.remove(msg);
                            number = lineReader.getLineNumber();
                            MsgHits msgChar = new MsgHits(javaFile.getCanonicalPath(), number, line);
                            msgMultimap.put(msg, msgChar);
                        }
                    }

                }
            }
        }
        System.out.println(unusedMsg);
        return msgMultimap;
    }

    /**
     * Seeks for all the non-Directory-Files in a given File(root).
     * Then checks which of these Filenames match the given Pattern.
     * The matching Files are then added to a List(resultDataList).
     *
     * @param root the File to work with
     * @param pattern the pattern to look up in Filenames
     * @return a List of Files, which Filenames match the given Pattern
     */
    public List<File> filterDir(File root, Pattern pattern) {

        List<File> resultDataList = new ArrayList<>();

        if ( root.isDirectory() ) {
            File[] directoryFiles = root.listFiles();
            if ( directoryFiles == null ) {
                System.out.println("Directory is not readable: " + root.getPath());
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

    private void prettyPrint(ListMultimap<String, MsgHits> result) {
        for ( String msg : result.asMap().keySet() ) {
            Collection<MsgHits> hits = result.asMap().get(msg);
            for ( MsgHits hit : hits ) {
                System.out.println(msg + ";" + hit.getFileName() + ";" + hit.getLineNumber() + ";" + hit.getContent());
            }
        }

    }

    @Ignore
    @Test
    public void test1() throws IOException {
        Pattern pattern = Pattern.compile(".*");
        File propfile = new File("../../blockly/robMsg/robMessages.js");
        File origin = new File("./src/main/java");

        List<File> fileList = filterDir(origin, pattern);
        List<String> msgList = getMsgList(propfile, "^Blockly.Msg.[^ ]+");
        ListMultimap<String, MsgHits> result = findTheMsg(fileList, msgList);
        //prettyPrint(result);
    }

}
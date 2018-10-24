package de.fhg.iais.roberta.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fhg.iais.roberta.util.dbc.Assert;

public class DropDown {
    private final List<Pair<String, String>> sequence = new ArrayList<>();
    private final Map<String, Pair<String, String>> leftMap = new HashMap<>();
    private final Map<String, Pair<String, String>> rightMap = new HashMap<>();

    public DropDown() {
        // create maps and list
    }

    public void add(String left, String right) {
        Assert.notNull(left);
        Assert.notNull(right);
        Pair<String, String> entry = Pair.of(left, right);
        sequence.add(entry);
        Assert.isNull(leftMap.put(left, entry), "this key was already mapped in left: {}", left);
        Assert.isNull(rightMap.put(right, entry), "this key was already mapped in right: {}", right);
    }

    public Pair<String, String> getLeft(String left) {
        Assert.notNull(left);
        return leftMap.get(left);
    }

    public Pair<String, String> getRight(String right) {
        Assert.notNull(right);
        return rightMap.get(right);
    }

    public List<String> getLeft() {
        return sequence.stream().map(e -> e.getFirst()).collect(Collectors.toList());
    }

    public List<String> getRight() {
        return sequence.stream().map(e -> e.getSecond()).collect(Collectors.toList());
    }
}

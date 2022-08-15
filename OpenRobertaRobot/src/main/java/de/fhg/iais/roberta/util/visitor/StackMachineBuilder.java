package de.fhg.iais.roberta.util.visitor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.util.dbc.Assert;

public class StackMachineBuilder {
    private boolean codeGeenrationFinished = false;
    private List<JSONObject> opArray = new ArrayList<>();

    public StackMachineBuilder() {
    }

    public StackMachineBuilder add(JSONObject op) {
        Assert.isFalse(codeGeenrationFinished);
        this.opArray.add(op);
        return this;
    }

    public List<JSONObject> getCode() {
        this.codeGeenrationFinished = true;
        return opArray;
    }

    public boolean isEmpty() {
        return opArray.isEmpty();
    }

    public JSONObject getLast() {
        return opArray.get(opArray.size() - 1);
    }

    public int size() {
        return opArray.size();
    }
}

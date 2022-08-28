package de.fhg.iais.roberta.util.visitor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class StackMachineBuilder {
    private boolean codeGenerationFinished = false;
    private final List<JSONObject> opArray = new ArrayList<>();

    private int uniqueCompoundNumberGenerator = 1;
    private final List<Pair<Compound, Integer>> compoundStack = new ArrayList<>();

    public StackMachineBuilder() {
    }

    public StackMachineBuilder add(JSONObject op) {
        Assert.isFalse(codeGenerationFinished);
        this.opArray.add(op);
        return this;
    }

    public List<JSONObject> getCode() {
        this.codeGenerationFinished = true;
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

    public int pushCompound(Compound compound) {
        int uniqueCompoundNumber = this.uniqueCompoundNumberGenerator++;
        compoundStack.add(Pair.of(compound, uniqueCompoundNumber));
        return uniqueCompoundNumber;
    }

    public void popCompound(Compound compound) {
        Pair<Compound, Integer> stackItem = compoundStack.remove(compoundStack.size() - 1);
        Assert.isTrue(compound == stackItem.getFirst());
    }

    public int getUniqueCompoundNumber(Compound compundExpected) {
        Assert.isTrue(compoundStack.size() > 0, "compound stack is empty");
        for ( int i = compoundStack.size() - 1; i >= 0; i-- ) {
            Pair<Compound, Integer> stackItem = compoundStack.get(i);
            if ( stackItem.getFirst() == compundExpected ) {
                return stackItem.getSecond();
            }
        }
        throw new DbcException("compound " + compundExpected + " not found in compound stack");
    }

    public int getUniqueCompoundNumberForBreakOrContinue() {
        Assert.isTrue(compoundStack.size() > 0, "compound stack is empty");
        for ( int i = compoundStack.size() - 1; i >= 0; i-- ) {
            Pair<Compound, Integer> stackItem = compoundStack.get(i);
            if ( stackItem.getFirst().isTargetOfContinueOrBreak() ) {
                return stackItem.getSecond();
            }
        }
        throw new DbcException("no compound for break or continue found");
    }

    public enum Compound {
        BINARY(false),
        TERNARY(false),
        IF(false),
        REPEAT(true),
        WAIT(false),
        METHOD(false),
        RETURN(false),
        CALL(false);

        private final boolean targetOfContinueOrBreak;

        Compound(boolean targetOfContinueOrBreak) {
            this.targetOfContinueOrBreak = targetOfContinueOrBreak;
        }

        public boolean isTargetOfContinueOrBreak() {
            return this.targetOfContinueOrBreak;
        }
    }

}

package de.fhg.iais.roberta.ast.syntax;

public class NepoBlocklyId implements INepoId {
    private final int blocklyId;

    private NepoBlocklyId(int blocklyId) {
        super();
        this.blocklyId = blocklyId;
    }

    public static INepoId of(int blocklyId) {
        return new NepoBlocklyId(blocklyId);
    }

    @Override
    public String toString() {
        return "NepoBlocklyId [" + this.blocklyId + "]";
    }
}

package de.fhg.iais.roberta.ast.typecheck;

import de.fhg.iais.roberta.ast.syntax.INepoId;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;

public class NepoProblem {
    private final INepoId id;
    private final int severity;
    private final Kind kind;
    private final String message;

    private NepoProblem(INepoId id, int severity, Kind kind, String message) {
        this.id = id;
        this.severity = severity;
        this.kind = kind;
        this.message = message;
    }

    public static NepoProblem of(INepoId id, int severity, Kind kind, String message) {
        return new NepoProblem(id, severity, kind, message);
    }

    public INepoId getId() {
        return this.id;
    }

    public int getSeverity() {
        return this.severity;
    }

    public String getMessage() {
        return this.message;
    }

    public Kind getKind() {
        return this.kind;
    }

    @Override
    public String toString() {
        return "NepoProblem [id=" + this.id + ", severity=" + this.severity + ", kind=" + this.kind + ", message=" + this.message + "]";
    }
}

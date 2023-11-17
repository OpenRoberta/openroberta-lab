package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.List;

/**
 * the summary of all errors or problems of a phrase of the AST. Is stored in the mutable part of the phrase (within a phrase the tree structure is immutable,
 * but attaching infos must be possible at any time, of course.
 *
 * @author rbudde
 */
public class NepoInfos {
    private final List<NepoInfo> infos = new ArrayList<>();
    private int errorCount = 0;

    public List<NepoInfo> getInfos() {
        return this.infos;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    /**
     * add an info to the list of all info.
     *
     * @param info to be added
     */
    public void addInfo(NepoInfo info) {
        if ( !infos.contains(info) ) {
            if ( info.getSeverity() == NepoInfo.Severity.ERROR ) {
                this.errorCount++;
            }
            this.infos.add(info);
        }
    }
}

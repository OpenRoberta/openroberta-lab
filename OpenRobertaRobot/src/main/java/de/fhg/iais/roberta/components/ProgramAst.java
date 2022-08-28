package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;

public final class ProgramAst {
    private final String robotType;
    private final String xmlVersion;
    private final String description;
    private final String tags;
    private final List<List<Phrase>> forests;

    private ProgramAst(String robotType, String xmlVersion, String description, String tags, List<List<Phrase>> tree) {
        this.robotType = robotType;
        this.xmlVersion = xmlVersion;
        this.description = description;
        this.tags = tags;
        this.forests = tree;
    }

    public String getRobotType() {
        return this.robotType;
    }

    public String getXmlVersion() {
        return this.xmlVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTags() {
        return this.tags;
    }

    public List<List<Phrase>> getTree() {
        return Collections.unmodifiableList(this.forests);
    }

    public static class Builder {
        private String robotType = "";
        private String xmlVersion = "";
        private String description = "";
        private String tags = "";
        private final List<List<Phrase>> tree = new ArrayList<>();

        public Builder setRobotType(String robotType) {
            this.robotType = robotType;
            return this;
        }

        public Builder setXmlVersion(String xmlVersion) {
            this.xmlVersion = xmlVersion;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTags(String tags) {
            this.tags = tags;
            return this;
        }

        public Builder addTree(List<List<Phrase>> tree) {
            this.tree.addAll(tree);
            return this;
        }

        public Builder addToTree(List<Phrase> phrases) {
            this.tree.add(phrases);
            return this;
        }

        public ProgramAst build() {
            return new ProgramAst(this.robotType, this.xmlVersion, this.description, this.tags, this.tree);
        }
    }
}
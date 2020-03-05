package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;

public class ProgramAst<V> {
    private final String robotType;
    private final String xmlVersion;
    private final String description;
    private final String tags;
    private final List<List<Phrase<V>>> forests = new ArrayList<>();

    private ProgramAst(String robotType, String xmlVersion, String description, String tags) {
        this.robotType = robotType;
        this.xmlVersion = xmlVersion;
        this.description = description;
        this.tags = tags;
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

    public List<List<Phrase<V>>> getTree() {
        return this.forests;
    }

    public static class Builder {
        private String robotType = "";
        private String xmlVersion = "";
        private String description = "";
        private String tags = "";

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

        public <V> ProgramAst<V> build() {
            return new ProgramAst<>(robotType, xmlVersion, description, tags);
        }
    }
}
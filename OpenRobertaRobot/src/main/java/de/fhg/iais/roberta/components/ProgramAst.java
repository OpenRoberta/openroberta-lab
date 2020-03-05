package de.fhg.iais.roberta.components;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;

public class ProgramAst<V> {
    private String robotType = "";
    private String xmlVersion = "";
    private String description = "";
    private String tags = "";
    private List<List<Phrase<V>>> forests = new ArrayList<>();

    public String getRobotType() {
        return this.robotType;
    }

    public void setRobotType(String robotType) {
        this.robotType = robotType;
    }

    public String getXmlVersion() {
        return this.xmlVersion;
    }

    public void setXmlVersion(String xmlVersion) {
        this.xmlVersion = xmlVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<List<Phrase<V>>> getTree() {
        return this.forests;
    }

    public void setTree(List<List<Phrase<V>>> tree) {
        this.forests = tree;
    }
}
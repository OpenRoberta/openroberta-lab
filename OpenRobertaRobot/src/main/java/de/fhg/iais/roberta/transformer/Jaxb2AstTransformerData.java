package de.fhg.iais.roberta.transformer;

import java.util.ArrayList;

import de.fhg.iais.roberta.syntax.Phrase;

public class Jaxb2AstTransformerData<V> {
    private String robotType = "";
    private String xmlVersion = "";
    private String description = "";
    private String tags = "";
    private ArrayList<ArrayList<Phrase<V>>> tree = new ArrayList<>();

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
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ArrayList<ArrayList<Phrase<V>>> getTree() {
        return this.tree;
    }

    public void setTree(ArrayList<ArrayList<Phrase<V>>> tree) {
        this.tree = tree;
    }
}
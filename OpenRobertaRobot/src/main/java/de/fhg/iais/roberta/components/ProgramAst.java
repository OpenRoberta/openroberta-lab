package de.fhg.iais.roberta.components;

import java.util.ArrayList;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.IVisitor;

public class ProgramAst<V> {
    private String robotType = "";
    private String xmlVersion = "";
    private String description = "";
    private String tags = "";
    private ArrayList<ArrayList<Phrase<V>>> forests = new ArrayList<>();

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

    public ArrayList<ArrayList<Phrase<V>>> getTree() {
        return this.forests;
    }

    public void setTree(ArrayList<ArrayList<Phrase<V>>> tree) {
        this.forests = tree;
    }

    public void accept(IVisitor<V> visitor) {
        for ( ArrayList<Phrase<V>> forest : this.forests ) {
            for ( Phrase<V> tree : forest ) {
                tree.accept(visitor);
            }
        }
    }
}
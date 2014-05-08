package de.fhg.iais.roberta.persistence.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.fhg.iais.roberta.javaServer.util.State;

@Entity
@Table(name = "PROGRAM")
public class Program implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "PROGRAM_TEXT")
    private String programText;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private State state;

    protected Program() {
        // Hibernate
    }

    /**
     * create a new ingest object
     * 
     * @param name the name, not null
     * @param project the provider of this ingest, not null
     * @param metadataFormat the format of the metadata (e.g. DC, LIDO, EDM, MARC, ...)
     */
    public Program(String name, Project project, String text, String programText) {
        this.name = name;
        this.project = project;
        this.text = text;
        this.programText = programText;
        this.state = State.ReadyForTransform;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getProgramText() {
        return this.programText;
    }

    public void setProgramText(String programText) {
        this.programText = programText;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

}

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

    @Column(name = "ACCESS")
    @Enumerated(EnumType.STRING)
    private Access access;

    protected Program() {
        // Hibernate
    }

    /**
     * create a new program
     * 
     * @param name the name of the program, not null
     * @param project the project this program belongs to, not null
     * @param text describing the program literally
     * @param programText the xml representation of the program
     */
    public Program(String name, Project project, String text, String programText) {
        this.name = name;
        this.project = project;
        this.text = text;
        this.programText = programText;
        this.access = Access.Private;
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

    public Access getAccess() {
        return this.access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

}

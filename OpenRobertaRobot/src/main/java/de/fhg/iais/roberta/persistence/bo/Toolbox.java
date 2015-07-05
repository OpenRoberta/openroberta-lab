package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.fhg.iais.roberta.util.Util;

@Entity
@Table(name = "TOOLBOX")
public class Toolbox implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "TOOLBOX_TEXT")
    private String ToolboxText;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "LAST_CHANGED")
    private Timestamp lastChanged;

    @Column(name = "LAST_CHECKED")
    private Timestamp lastChecked;

    @Column(name = "LAST_ERRORFREE")
    private Timestamp lastErrorFree;

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "ICON_NUMBER")
    private int iconNumber;

    protected Toolbox() {
        // Hibernate
    }

    /**
     * create a new Toolbox
     *
     * @param name the name of the Toolbox, not null
     * @param owner the user who created and thus owns the program
     */
    public Toolbox(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.created = Util.getNow();
        this.lastChanged = Util.getNow();
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getToolboxText() {
        return this.ToolboxText;
    }

    public void setToolboxText(String ToolboxText) {
        this.ToolboxText = ToolboxText;
        this.lastChanged = Util.getNow();
    }

    public Timestamp getLastChecked() {
        return this.lastChecked;
    }

    public void setLastChecked(Timestamp lastChecked) {
        this.lastChecked = lastChecked;
    }

    public Timestamp getLastErrorFree() {
        return this.lastErrorFree;
    }

    public void setLastErrorFree(Timestamp lastErrorFree) {
        this.lastErrorFree = lastErrorFree;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
        this.lastChanged = Util.getNow();
    }

    /**
     * get the user, who is the owner
     *
     * @return the owner, never <code>null</code>
     */
    public User getOwner() {
        return this.owner;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    public Timestamp getLastChanged() {
        return this.lastChanged;
    }

    @Override
    public String toString() {
        return "Toolbox [id="
            + this.id
            + ", name="
            + this.name
            + ", ownerId="
            + (this.owner == null ? "???" : this.owner.getId())
            + ", created="
            + this.created
            + ", lastChanged="
            + this.lastChanged
            + "]";
    }

}

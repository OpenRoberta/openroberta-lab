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
import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.dbc.Assert;

@Entity
@Table(name = "PROGRAM")
public class Program implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "ROBOT_ID")
    private Robot robot;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROGRAM_TEXT")
    private String programText;

    /**
     * the name of the attached configuration. If null, configHash must be not null. If not null, configHash must be null.
     */
    @Column(name = "CONFIG_NAME")
    private String configName;

    /**
     * the hash of the attached configuration. If null, configName must be not null. If not null, configName must be null.
     */
    @Column(name = "CONFIG_HASH")
    private String configHash;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "LAST_CHANGED")
    private Timestamp lastChanged;

    @Column(name = "LAST_CHECKED")
    private Timestamp lastChecked;

    @Column(name = "LAST_ERRORFREE")
    private Timestamp lastErrorFree;

    @Column(name = "VIEWED")
    private int viewed;

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "ICON_NUMBER")
    private int iconNumber;

    protected Program() {
        // Hibernate
    }

    /**
     * create a new program
     *
     * @param name the name of the program, not null
     * @param owner the user who created and thus owns the program
     */
    public Program(String name, User owner, Robot robot, User author) {
        Assert.notNull(name);
        Assert.notNull(owner);
        Assert.notNull(author);
        this.name = name;
        this.owner = owner;
        this.robot = robot;
        this.author = author;
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

    /*
     * This method is used _solely_ for checking all the programs in the database for XSS
     * if you need to obtain the text of the program, please use getProgramText instead
     */
    public String getUncheckedProgramText() {
        return this.programText;
    }

    public String getProgramText() {
        return UtilForHtmlXml.checkProgramTextForXSS(this.programText);
    }

    public void setProgramText(String programText) {
        this.programText = UtilForHtmlXml.checkProgramTextForXSS(programText);
        this.lastChanged = Util.getNow();
    }

    /**
     * get the configuration name. At least one of the configuration name and hash must be null.
     *
     * @return the configuration name
     */
    public String getConfigName() {
        return this.configName;
    }

    /**
     * get the configuration hash. At least one of the configuration name and hash must be null.
     *
     * @return the configuration hash
     */
    public String getConfigHash() {
        return this.configHash;
    }

    /**
     * set configuration name and configuration hash.<br>
     * At least one of the both values must be null. If both are null, the default configuration is associated with this program.
     *
     * @param configName the name of the configuration
     * @param configHash the hash of the configuration
     */
    public void setConfigData(String configName, String configHash) {
        Assert.isTrue(configName == null || configHash == null);
        this.configName = configName;
        this.configHash = configHash;
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

    public int getNumberOfViews() {
        return this.viewed;
    }

    public void incrViewed() {
        this.viewed++;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
        this.lastChanged = Util.getNow();
    }

    public void setIconNumber(int iconNumber) {
        this.iconNumber = iconNumber;
    }

    /**
     * get the user, who is the owner
     *
     * @return the owner, never <code>null</code>
     */
    public User getOwner() {
        return this.owner;
    }

    public Robot getRobot() {
        return this.robot;
    }

    /**
     * get the user, who shared this program with someone, currently either the owner or the Gallery user
     *
     * @return the author, never <code>null</code>
     */
    public User getAuthor() {
        return this.author;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    public Timestamp getLastChanged() {
        return this.lastChanged;
    }

    @Override
    public String toString() {
        return "Program [id="
            + this.id
            + ", name="
            + this.name
            + ", ownerId="
            + (this.owner == null ? "???" : this.owner.getId())
            + ", robotId="
            + (this.robot == null ? "???" : this.robot.getId())
            + ", authorId="
            + (this.author == null ? "???" : this.author.getId())
            + ", configName="
            + this.configName
            + ", configHash="
            + this.configHash
            + ", created="
            + this.created
            + ", lastChanged="
            + this.lastChanged
            + "]";
    }
}

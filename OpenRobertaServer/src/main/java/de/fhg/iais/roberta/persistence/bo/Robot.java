package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.fhg.iais.roberta.util.Util;

@Entity
@Table(name = "ROBOT")
public class Robot implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "ICON_NUMBER")
    private int iconNumber;

    protected Robot() {
        // Hibernate
    }

    /**
     * create a new Robot
     *
     * @param name the name of the Robot, not null
     */
    public Robot(String name) {
        this.name = name;
        this.created = Util.getNow();
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the robot in the database <br>
     * <br>
     * <font color=#ff0000><b>Dangerous operation</b></font>, should be used only from the Administration class
     * Was used for renaming ardu to botnroll
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    @Override
    public String toString() {
        return "Robot [id=" + this.id + ", name=" + this.name + ", created=" + this.created + "]";
    }

}

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

@Entity
@Table(name = "PROGRAM_PROFILE")
public class UserProgram implements WithSurrogateId {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NAME")
    private String programName;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "PROGRAM_TEXT")
    private String programText;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    //@Column(name = "USER_ID")

    @Column(name = "GROUP_ID")
    private int groupId;

    protected UserProgram() {
        //Hibernate
    }

    public UserProgram(String programName, String text, String programText, Timestamp created, Timestamp lastUpdated, User user, int groupId) {

        this.programName = programName;
        this.text = text;
        this.programText = programText;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.user = user;
        this.groupId = groupId;

    }

    @Override
    public int getId() {
        // TODO Auto-generated method stub
        return this.id;
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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

    public Timestamp getCreated() {
        return this.created;
    }

    public Timestamp getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    //	public User getUserId(){
    //		return User.userId;
    //	}
    //	public void setUserId(int userId){
    //		this.userId = userId;
    //	}

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
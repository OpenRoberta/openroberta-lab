package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

@Entity
@Table(name = "USERGROUP")
public class UserGroup implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCESS_RIGHT")
    private AccessRight accessRight;

    @Column(name = "CREATED")
    private Timestamp created;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup")
    private Set<User> members;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup")
    private Set<UserGroupProgramShare> sharedPrograms;

    protected UserGroup() {
        // Hibernate
    }

    /**
     * create a new group
     *
     * @param name the name of the group, not null
     * @param owner the user who created and thus owns the group
     */
    public UserGroup(String name, User owner) {
        Assert.notNull(name);
        Assert.notNull(owner);

        this.name = name;
        this.owner = owner;
        this.accessRight = AccessRight.ADMIN_READ;
        this.created = Util.getNow();
    }

    @Override
    public int getId() {
        return this.id;
    }

    /**
     * get the name of the group
     *
     * @return the name, never <code>null</code>
     */

    public String getName() {
        return this.name;
    }

    /**
     * get the access right of the programs that the groups members create
     *
     * @return the access right, never <code>null</code>
     */

    public AccessRight getAccessRight() {
        return this.accessRight;
    }

    /**
     * set the access right of the programs that the groups members create<br/>
     * Asserts, that the given access right is not null
     *
     * @param accessRight The new access right.
     * @throws DbcException in case null is provided as accessRight
     */
    public void setAccessRight(AccessRight accessRight) throws DbcException {
        Assert.notNull(accessRight);
        this.accessRight = accessRight;
    }

    /**
     * get the owner of the group
     *
     * @return the owner, never <code>null</code>
     */
    public User getOwner() {
        return this.owner;
    }

    /**
     * get the time stamp of the creation time of the user group
     *
     * @return The time stamp of the group's creation, never <code>null</code>
     */
    public Timestamp getCreated() {
        return this.created;
    }

    /**
     * Adds a user as a member of the user group.<br/>
     * Asserts, that the given user is not null<br/>
     * Asserts, that the given user is not a member of another group
     *
     * @param member The user to add as a member to this user group
     */
    public void addMember(User member) {
        Assert.notNull(member);
        Assert.isTrue(member.getUserGroup() == null || member.getUserGroup().equals(this));
        if ( this.members == null ) {
            this.members = new HashSet<>();
        }
        this.members.add(member);
    }

    /**
     * Removes a member from the user group.
     *
     * @param member The member that shall be removed.
     */
    public void removeMember(User member) {
        if ( this.members == null ) {
            this.members = new HashSet<>();
        } else {
            this.members.remove(member);
        }
    }

    /**
     * get a list of members of this user group
     *
     * @return All members of this user group
     */
    public Set<User> getMembers() {
        if ( this.members == null ) {
            this.members = new HashSet<>();
        }
        return new HashSet<>(this.members);
    }

    /**
     * add a program to this user group, so it is shared to all its members.<br/>
     * Asserts, that the given program is not null<br/>
     * Asserts, that the given relation is not null<br/>
     * Has no effect, if the program is already shared with the user group
     *
     * @param program The program that shall be shared to all its members
     * @param rights The relation the members shall have. (read / write / x_write)
     */
    public void addSharedProgram(Program program, Relation rights) {
        Assert.notNull(program);
        Assert.notNull(rights);
        if ( this.sharedPrograms == null ) {
            this.sharedPrograms = new HashSet<>();
        }
        UserGroupProgramShare userGroupProgramShare = new UserGroupProgramShare(this, program, rights);
        this.sharedPrograms.add(userGroupProgramShare);
    }

    /**
     * Removes a program from the set of programs that are shared with this user group.
     *
     * @param program The program that shall no longer be shared with the user group.
     */
    public void removeSharedProgram(Program program) {
        if ( this.sharedPrograms == null ) {
            this.sharedPrograms = new HashSet<>();
        } else {
            if ( program == null ) {
                return;
            }
            for ( UserGroupProgramShare userGroupProgramShare : this.sharedPrograms ) {
                if ( userGroupProgramShare.getProgram().equals(program) ) {
                    this.sharedPrograms.remove(userGroupProgramShare);
                    return;
                }
            }
        }
    }

    /**
     * Get all share objects of programs, that are shared with this user group
     *
     * @return A set of user group program share objects
     */
    public Set<UserGroupProgramShare> getSharedPrograms() {
        if ( this.sharedPrograms == null ) {
            this.sharedPrograms = new HashSet<>();
        }
        return new HashSet<>(this.sharedPrograms);
    }

    @Override
    public String toString() {
        return "UserGroup [id=" + this.id + ", name=" + this.name + ", ownerId=" + this.owner + ", created=" + this.created + "]";
    }

    /**
     * Sets a new name for the user group.<br/>
     * Does not check, whether or not the name is already taken, or whether or not it is a valid name.<br/>
     * Asserts, that the given string is not null
     *
     * @param newGroupName The new name for the user group
     */
    public void rename(String newGroupName) {
        Assert.notNull(newGroupName);
        this.name = newGroupName;
    }

    /**
     * Converts the UserGroup Object into a JSON representation, which's structure is optimized for showing it in a front end list
     *
     * @return JSObject A JSON representation of the UserGroup object
     */
    public JSONObject toListJSON() {
        JSONObject jsonObject = new JSONObject(), tmp;
        JSONArray members = new JSONArray(), programs = new JSONArray();
        Program tmpProg;

        for ( User user : this.getMembers() ) {
            try {
                tmp = new JSONObject();
                //TODO: Call method in User class, do not re implement it here. Or create a new method in the JSON UTIL? Discuss with team
                tmp.put("id", user.getId());
                tmp.put("account", user.getAccount());
                tmp.put("hasDefaultPassword", user.isPasswordCorrect(user.getAccount()));
                members.put(tmp);
            } catch ( Exception e ) {
                //Either a JSONException, or a general Exception in case of the Password check
                return null;
            }
        }

        for ( UserGroupProgramShare sharedProgram : this.getSharedPrograms() ) {
            tmpProg = sharedProgram.getProgram();
            try {
                tmp = new JSONObject();
                tmp.put("id", tmpProg.getId());
                tmp.put("name", tmpProg.getName());
                tmp.put("owner", tmpProg.getOwner().getAccount());
                tmp.put("author", tmpProg.getAuthor().getAccount());
                tmp.put("robot", tmpProg.getRobot().getName());
                tmp.put("right", sharedProgram.getRelation().toString());
                programs.put(tmp);
            } catch ( JSONException e ) {
                //In case of JSONException do not output the whole user group
                return null;
            }
        }

        try {
            jsonObject.put("name", this.getName());
            jsonObject.put("owner", this.getOwner().getAccount());
            jsonObject.put("created", this.getCreated());
            jsonObject.put("members", members);
            jsonObject.put("programs", programs);
        } catch ( JSONException e ) {
            return null;
        }
        return jsonObject;
    }
}

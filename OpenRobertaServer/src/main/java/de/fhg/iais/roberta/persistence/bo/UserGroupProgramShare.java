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

import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * the share object of a user-group for a program. The relation may be a READ, WRITE or X_WRITE permission.
 */
@Entity
@Table(name = "USERGROUP_PROGRAM")
public class UserGroupProgramShare implements WithSurrogateId, ProgramShare {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Override
    public int getId() {
        return this.id;
    }

    @ManyToOne
    @JoinColumn(name = "USERGROUP_ID")
    private UserGroup userGroup;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;

    @Enumerated(EnumType.STRING)
    @Column(name = "RELATION")
    private Relation relation;

    protected UserGroupProgramShare() {
        // Hibernate
    }

    /**
     * create a new share object of a program for a user group.
     *
     * @param userGroup user group that will get a read/write permission, not null
     * @param program the program affected, not null
     * @param relation read or write permission, not null
     */
    public UserGroupProgramShare(UserGroup userGroup, Program program, Relation relation) {
        Assert.notNull(userGroup);
        Assert.notNull(program);
        Assert.notNull(relation);

        this.userGroup = userGroup;
        this.program = program;
        this.relation = relation;
    }

    /**
     * get the associated user group
     *
     * @return The associated user group. Never <code>null</code>
     */
    public UserGroup getUserGroup() {
        return this.userGroup;
    }

    @Override
    public Program getProgram() {
        return this.program;
    }

    @Override
    public Relation getRelation() {
        return this.relation;
    }

    /**
     * Sets a new relation for the share relation between the set user group and program.<br/>
     * Asserts, that the provided relation is not null.
     *
     * @param relation The new relation.
     */
    public void setRelation(Relation relation) {
        Assert.notNull(relation);
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "UserGroupProgramShare [id=" + this.id + ", usergroup=" + this.userGroup + ", relation=" + this.relation + "]";
    }

    @Override
    public String getEntityLabel() {
        return this.userGroup == null ? "" : this.userGroup.getName();
    }

    @Override
    public String getEntityType() {
        return UserGroup.class.getSimpleName();
    }
}

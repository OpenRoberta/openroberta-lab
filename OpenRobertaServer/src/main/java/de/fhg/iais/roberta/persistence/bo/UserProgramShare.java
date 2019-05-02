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
 * the access right of a user for a program. Used for sharing. The relation may be a READ or a WRITE permission.
 */
@Entity
@Table(name = "USER_PROGRAM")
public class UserProgramShare implements WithSurrogateId, ProgramShare {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Override
    public int getId() {
        return this.id;
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;

    @Enumerated(EnumType.STRING)
    @Column(name = "RELATION")
    private Relation relation;

    protected UserProgramShare() {
        // Hibernate
    }

    /**
     * create a new access right of a user for a program.
     *
     * @param user that will get a read/write permission, not null
     * @param program the program affected, not null
     * @param relation read or write permission
     */
    public UserProgramShare(User user, Program program, Relation relation) {
        Assert.notNull(user);
        Assert.notNull(program);
        Assert.notNull(relation);
        this.user = user;
        this.program = program;
        this.relation = relation;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public Program getProgram() {
        return this.program;
    }

    @Override
    public Relation getRelation() {
        return this.relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "UserProgramShare [id=" + this.id + ", user=" + this.user + ", relation=" + this.relation + "]";
    }

    @Override
    public String getEntityLabel() {
        return this.user == null ? "" : this.user.getAccount();
    }

    @Override
    public String getEntityType() {
        return User.class.getSimpleName();
    }
}
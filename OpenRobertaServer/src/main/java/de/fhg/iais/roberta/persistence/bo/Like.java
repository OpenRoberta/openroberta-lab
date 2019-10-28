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
import de.fhg.iais.roberta.util.dbc.Assert;

@Entity
@Table(name = "USER_PROGRAM_LIKE")
public class Like implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "MARK")
    private String mark;

    @Column(name = "COMMENT")
    private String comment;

    protected Like() {
        // Hibernate
    }

    /**
     * create a like
     *
     * @param userId of the user that forgot his password
     * @throws Exception
     */
    public Like(User user, Program program) throws Exception {
        Assert.notNull(user);
        Assert.notNull(program);
        this.user = user;
        this.program = program;
        this.created = Util.getNow();
    }

    /**
     * @return the user likes the program
     */
    public User getUser() {
        return this.user;
    }

    /**
     * @return the program that is liked by the user
     */
    public Program getProgram() {
        return this.program;
    }

    /**
     * @return the created
     */
    public Timestamp getCreated() {
        return this.created;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Like [id=" + this.id + ", user=" + this.user + ", program=" + this.program + ", created=" + this.created + "]";
    }

}

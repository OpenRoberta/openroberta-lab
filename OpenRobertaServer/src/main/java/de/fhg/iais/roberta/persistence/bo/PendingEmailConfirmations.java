package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.Util;

@Entity
@Table(name = "PENDING_EMAIL_CONFIRMATIONS")
public class PendingEmailConfirmations implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "USER_ID")
    private int userID;

    @Column(name = "URL_POSTFIX")
    private String urlPostfix;

    @Column(name = "CREATED")
    private Timestamp created;

    protected PendingEmailConfirmations() {
        // Hibernate
    }

    /**
     * create link to be sent to user email address
     *
     * @param userId of the user that forgot his password
     * @throws Exception
     */
    public PendingEmailConfirmations(int userId) throws Exception {
        this.userID = userId;
        this.urlPostfix = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
        this.created = Util.getNow();
    }

    public boolean isUrlCorrect(String urlToCheck) throws Exception {
        return this.urlPostfix.equals(urlToCheck);
    }

    /**
     * @return the userID
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * @return the url postfix
     */
    public String getUrlPostfix() {
        return this.urlPostfix;
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
        return "PendingEmailConfirmations [id=" + this.id + ", userID=" + this.userID + ", urlPostfix=" + this.urlPostfix + ", created=" + this.created + "]";
    }

}

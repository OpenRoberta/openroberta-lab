package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.fhg.iais.roberta.util.Encryption;
import de.fhg.iais.roberta.util.Util;

@Entity
@Table(name = "TMP_PASSWORDS")
public class TmpPassword implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "USER_ID")
    private int userID;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CREATED")
    private Timestamp created;

    protected TmpPassword() {
        // Hibernate
    }

    /**
     * create new temporary password for given user
     *
     * @param userId of the user that asks for temporary password
     * @throws Exception
     */
    public TmpPassword(int userId) throws Exception {
        this.userID = userId;
        this.password = Encryption.createHash("1"); //TODO Implement random password generator
        this.created = Util.getNow();
    }

    public boolean isPasswordCorrect(String passwordToCheck) throws Exception {
        return Encryption.isPasswordCorrect(this.password, passwordToCheck);
    }

    /**
     * @return the userID
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
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
        return "TmpPassword [id=" + this.id + ", userID=" + this.userID + ", password=" + this.password + ", created=" + this.created + "]";
    }

}

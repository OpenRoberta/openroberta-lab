package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.fhg.iais.roberta.util.Encryption;
import de.fhg.iais.roberta.util.Util;

@Entity
@Table(name = "USER")
public class User implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ACCOUNT")
    private String account;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    // temporary solution until authent/
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @Column(name = "CREATED")
    private Timestamp created;

    @Column(name = "LAST_LOGIN")
    private Timestamp lastLogin;

    @Column(name = "TAGS")
    private String tags;

    protected User() {
        // Hibernate
    }

    /**
     * create a new program
     *
     * @param account the system wide unique account of a new user
     */
    public User(String account) {
        this.account = account;
        this.created = Util.getNow();
        this.lastLogin = Util.getNow();
    }

    public boolean isPasswordCorrect(String passwordToCheck) throws Exception {
        return Encryption.isPasswordCorrect(this.password, passwordToCheck);
    }

    public void setPassword(String password) throws Exception {
        this.password = Encryption.createHash(password);
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getAccount() {
        return this.account;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    public Timestamp getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin() {
        this.lastLogin = Util.getNow();
    }

    @Override
    public String toString() {
        return "User [id=" + this.id + ", account=" + this.account + ", role=" + this.role + ", lastLogin=" + this.lastLogin + "]";
    }

}

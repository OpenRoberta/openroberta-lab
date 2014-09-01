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

import java.sql.Timestamp;

@Entity
@Table(name = "USER_PROFILE")
public class User implements WithSurrogateId{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "NAME")
    private String userName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "ROLE")
    private String role;
    
    @Column(name = "CREATED")
    private Timestamp created;
    
    @Column(name = "LAST_SIGNED_ON")
    private Timestamp lastSignedOn;
    
    protected User() {
        // Hibernate
    }
    /**
     * create a new program
     * 
     * @param name the name of the program, not null
     * @param project the project this program belongs to, not null
     * @param text describing the program literally
     * @param programText the xml representation of the program
     */
    public User(String accountName, String userName, String email, String password, String role, Timestamp created, Timestamp lastSignedOn){
    	
    	this.accountName = accountName;
    	this.userName = userName;
    	this.email = email;
    	this.password = password;
    	this.role = role;
    	this.created = created;
    	this.lastSignedOn = lastSignedOn;

    }
    
	@Override
	public int getId() {
		return this.id;
	}

	public String getAccountName(){
		return this.accountName;
	}
	
	public void setAccountName(String accountName){
		this.accountName = accountName;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getRole(){
		return this.role;
	}
	
	public void setRole(String role){
		this.role = role;
	}
	
	public Timestamp getCreated(){
		return this.created;
	}
	
	public Timestamp getLastSignedOn(){
		return this.lastSignedOn;
	}
	
	public void setLastSignedOn(Timestamp lastSignedOn){
		this.lastSignedOn = lastSignedOn;
	}
	
}

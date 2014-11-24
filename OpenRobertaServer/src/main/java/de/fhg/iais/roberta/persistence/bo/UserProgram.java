package de.fhg.iais.roberta.persistence.bo;

import java.sql.Timestamp;

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

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.util.Util;

@Entity
@Table(name = "USER_PROGRAM")
public class UserProgram implements WithSurrogateId{
    
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    

    @Override
    public int getId(){
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
    
    
    protected UserProgram() {
        // Hibernate
    }

	/**
     * create a new program
     *
     * @param name the name of the program, not null
     * @param owner the user who created and thus owns the program
     */
    public UserProgram(User user, Program program, Relation relation) {
    	
        Assert.notNull(user);
        Assert.notNull(program);
        Assert.notNull(relation);
        this.user = user;
        this.program = program;
        this.relation = relation;
        
    }
    
    public User getUser(){
		return user;
	}

	public void setUser(User user){
		this.user = user;
	}

	public Program getProgram(){
		return program;
	}

	public void setProgram(Program program){
		this.program = program;
	}

	public Relation getRelation(){
		return this.relation;
	}

	public void setRelation(Relation relation){
		this.relation = relation;
	}

    
}

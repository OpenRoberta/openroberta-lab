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
@Table(name = "GROUP_PROFILE")
public class Group implements WithSurrogateId{
	
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(name = "GROUP_NAME")
    private String groupName;

    @Column(name = "GROUP_ADMIN_ID")
    private int adminId;

    @Column(name = "DASHBOARD")
    private String dashboard;

    
    protected Group() {
        // Hibernate
    }
    
    public Group( String groupName, int adminID, String dashboard){
    	this.groupName = groupName;
    	this.adminId = adminID;
    	this.dashboard = dashboard;
    }
    
	@Override
	public int getId() {
		return this.id;
	}
	
	
	public String getGroupName(){
		return this.groupName;
	}
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public int getAdminID(){
		return this.adminId;
	}
	public void setAdminID(int adminId){
		this.adminId = adminId; 
	}
	public String getDashboard(){
		return this.dashboard;
	}
	public void setDashboard(String dashboard){
		this.dashboard = dashboard;
	}

}
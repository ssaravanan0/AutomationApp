package com.waitrose.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * 
 * @author Saravanan
 *
 */
@Entity
@Table(name = "User_Role")
public class UserRole {
 
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="role_entry_seq")
	@SequenceGenerator(
		name="role_entry_seq",
		sequenceName="role_entry_seq",
		allocationSize=1
	)
    @Column(name = "Id", nullable = false)
    private Long id;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "User_Id", nullable = false)
    private AppUser appUser;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Role_Id", nullable = false)
    private AppRole appRole;
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public AppUser getAppUser() {
        return appUser;
    }
 
    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
 
    public AppRole getAppRole() {
        return appRole;
    }
 
    public void setAppRole(AppRole appRole) {
        this.appRole = appRole;
    }
     
}
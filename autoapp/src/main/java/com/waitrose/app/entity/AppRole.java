package com.waitrose.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 *  
 * @author Saravanan
 *
 */
@Entity
@Table(name = "App_Role")
public class AppRole {
     
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="app_role_entry_seq")
	@SequenceGenerator(
		name="app_role_entry_seq",
		sequenceName="app_role_entry_seq",
		allocationSize=1
	)
    @Column(name = "Role_Id", nullable = false)
    private Long roleId;
 
    @Column(name = "Role_Name", length = 30, nullable = false)
    private String roleName;
    
    @Column(name = "Role_Prefix", length = 3, nullable = false)
    private String rolePrefix;
 
    public Long getRoleId() {
        return roleId;
    }
 
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
 
    public String getRoleName() {
        return roleName;
    }
 
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

	public String getRolePrefix() {
		return rolePrefix;
	}

	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
	}
    
     
}

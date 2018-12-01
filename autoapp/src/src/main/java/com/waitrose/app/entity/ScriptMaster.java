package com.waitrose.app.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
/**
 * 
 * @author Saravanan
 *
 */
@Entity
@Table(name = "Script_Master")
public class ScriptMaster {
 
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "SCRIPT_ID", nullable = false)
    private Long scriptId;
 
    @Column(name = "SCRIPT_NAME", length = 60, nullable = false)
    private String scriptName;
 
    @Column(name = "SCRIPT_DESC", length = 60, nullable = false)
    private String scriptDesc;
 
    @Column(name = "LOCATION", length = 250, nullable = false)
    private String location;
    
    @Column(name = "PREFIX", length = 250, nullable = false)
    private String prefix;
    
    @Column(name = "ACCESS", length = 2, nullable = false)
    private String access;
    
    @Column(name = "CREATED_DATE", nullable = true)
    private Date createdDate;
    
    @Column(name = "LAST_MODIFIED_DATE", nullable = true)
    private Date lastModifiedDate;
    
    @Column(name = "CREATED_BY", length = 50, nullable = true)
    private String createdBy;
    
    @Column(name = "MODIFIED_BY", length = 50, nullable = true)
    private String modifiedBy;

	public Long getScriptId() {
		return scriptId;
	}

	public void setScriptId(Long scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScriptDesc() {
		return scriptDesc;
	}

	public void setScriptDesc(String scriptDesc) {
		this.scriptDesc = scriptDesc;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
 
}
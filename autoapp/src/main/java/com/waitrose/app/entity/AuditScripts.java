package com.waitrose.app.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
/**
 *  
 * @author Saravanan
 *
 */
@Entity
@Table(name = "AUDIT_SCRIPTS")
public class AuditScripts {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="audit_entry_seq")
	@SequenceGenerator(
		name="audit_entry_seq",
		sequenceName="audit_entry_seq",
		allocationSize=20
	)
    private Long auditId;
     
    @Column(name = "SCRIPT_ID", nullable = false)
    private Long scriptId;
 
    @Column(name = "SCRIPT_NAME", length = 60, nullable = false)
    private String scriptName;
    
/*    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)*/
    
	@Column(name = "EXECUTION_DATE", nullable = false)
    private String executedOn;
    
    @Column(name = "EXECUTION_IN_SECS", length = 50, nullable = false)
    private long executionTime;
    
    @Column(name = "GROUP_ID", length = 10, nullable = false)
    private String groupId;
    
    @Column(name = "EXECUTED_BY", length = 60, nullable = false)
    private String executedBy;
    
    @Column(name = "STATUS", length = 10, nullable = false)
    private String status;
    
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

	public String getExecutedOn() {
		return executedOn;
	}

	public void setExecutedOn(String executedOn) {
		this.executedOn = executedOn;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

package com.waitrose.app.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
/**
 *  
 * @author Saravanan
 *
 */
@Entity
@Table(name = "App_Role")
public class AuditScripts {
     
	@Id
    @GeneratedValue
    @Column(name = "SCRIPT_ID", nullable = false)
    private Long scriptId;
 
    @Column(name = "SCRIPT_NAME", length = 60, nullable = false)
    private String scriptName;
 
    @Column(name = "LAST_EXECUTION_DATE", length = 60, nullable = false)
    private String scriptDesc;
 
    @Column(name = "EXECUTION_IN_SECS", length = 250, nullable = false)
    private String location;
    
    @Column(name = "GROUP_ID", length = 250, nullable = false)
    private String prefix;
    
    @Column(name = "EXECUTED_BY", length = 2, nullable = false)
    private String access;
    
    @Column(name = "CREATED_DATE", nullable = true)
    private Date createdDate;
    
    @Column(name = "LAST_MODIFIED_DATE", nullable = true)
    private Date lastModifiedDate;
    
    @Column(name = "CREATED_BY", length = 50, nullable = true)
    private String createdBy;
    
    @Column(name = "MODIFIED_BY", length = 50, nullable = true)
    private String modifiedBy;
}

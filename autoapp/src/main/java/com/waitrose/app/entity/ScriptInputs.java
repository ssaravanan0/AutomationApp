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
@Table(name = "SCRIPT_INPUTS")
public class ScriptInputs {
 
	@Column(name = "SCRIPT_ID", nullable = false)
    private Long scriptId;
 
    @Column(name = "SCRIPT_NAME", length = 60, nullable = true)
    private String scriptName;
    
    @Id
    @Column(name = "INPUT_NAME", length = 60, nullable = true)
    private String inputName;
 
    @Column(name = "INPUT_TYPE", length = 250, nullable = true)
    private String inputType;
    
    @Column(name = "REQUIRED", length = 10, nullable = true)
    private String required;
    
    @Column(name = "CREATED_DATE", nullable = true)
    private Date createdDate;
    
    @Column(name = "LAST_MODIFIED_DATE", nullable = true)
    private Date lastModifiedDate;
    
    @Column(name = "CREATED_BY", length = 50, nullable = true)
    private String createdBy;
    
    @Column(name = "MODIFIED_BY", length = 50, nullable = true)
    private String modifiedBy;

	/**
	 * @return the scriptId
	 */
	public Long getScriptId() {
		return scriptId;
	}

	/**
	 * @param scriptId the scriptId to set
	 */
	public void setScriptId(Long scriptId) {
		this.scriptId = scriptId;
	}

	/**
	 * @return the scriptName
	 */
	public String getScriptName() {
		return scriptName;
	}

	/**
	 * @param scriptName the scriptName to set
	 */
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	/**
	 * @return the inputName
	 */
	public String getInputName() {
		return inputName;
	}

	/**
	 * @param inputName the inputName to set
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	/**
	 * @return the inputType
	 */
	public String getInputType() {
		return inputType;
	}

	/**
	 * @param inputType the inputType to set
	 */
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	/**
	 * @return the required
	 */
	public String getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(String required) {
		this.required = required;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
 
}
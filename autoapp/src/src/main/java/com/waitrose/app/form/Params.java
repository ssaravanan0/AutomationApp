package com.waitrose.app.form;

import javax.validation.constraints.NotEmpty;


public class Params {
    
	@NotEmpty(message = "*Please provide ...")
    private String name;
    @NotEmpty(message = "*Please provide ...")
    private String lastName;
	
    public Params() {
		super();
	}
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", lastName=" + lastName + "]";
	}
	
}

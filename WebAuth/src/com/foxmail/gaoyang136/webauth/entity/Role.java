package com.foxmail.gaoyang136.webauth.entity;

import java.sql.Timestamp;

public class Role {
	private String roleName;
	private String description;
	private Timestamp grantTime;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getGrantTime() {
		return grantTime;
	}

	public void setGrantTime(Timestamp grantTime) {
		this.grantTime = grantTime;
	}
}

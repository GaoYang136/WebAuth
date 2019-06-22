package com.foxmail.gaoyang136.webauth.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.foxmail.gaoyang136.webauth.entity.Role;

public interface RoleDao {
	public static final String TABLE_NAME = "roles";

	public static final String ROLE_NAME_LABLE = TABLE_NAME + ".role_name";

	public static final String DESCRIPTION_LABLE = TABLE_NAME + ".description";


	public Role getRolebyName(Connection conn, String roleName) throws SQLException;

	public void addRole(Connection conn, Role role) throws SQLException;

	public void deleteRole(Connection conn, String roleName) throws SQLException;

	public void updateRole(Connection conn, String oldRoleName, Role newRole) throws SQLException;
}

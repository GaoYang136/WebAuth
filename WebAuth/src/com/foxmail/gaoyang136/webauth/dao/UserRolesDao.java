package com.foxmail.gaoyang136.webauth.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.foxmail.gaoyang136.webauth.entity.Role;

public interface UserRolesDao {

	public static final String TABLE_NAME = "user_roles";

	public static final String USER_ID_LABLE = TABLE_NAME + ".user_id";

	public static final String ROLE_NAME_LABLE = TABLE_NAME + ".role_name";
	
	public static final String USER_NAME_LABLE = TABLE_NAME + ".user_name";

	public static final String GRANT_TIME_LABLE = TABLE_NAME + ".grant_time";


	public List<String> getRoleNamesbyUserID(Connection conn, int userID) throws SQLException;

	public List<Role> getUserRolesbyUserID(Connection conn, int userID) throws SQLException;

	//public void assemUserWithRoles(Connection conn, LinkedList<User> userList) throws SQLException;

	public List<Integer> getUserIDbyRoleName(Connection conn, String roleName, int offset, int rows) throws SQLException;

	//public List<User> getUsersbyRoleName(Connection conn, String roleName, int offset, int rows) throws SQLException;

	public void addUserRole(Connection conn, int userID, String userName, String roleName) throws SQLException;

	public int[] addUserRoles(Connection conn, int userID, String userName, List<Role> roleList) throws SQLException;

	public void deleteUserRole(Connection conn, int userID, String roleName) throws SQLException;
}

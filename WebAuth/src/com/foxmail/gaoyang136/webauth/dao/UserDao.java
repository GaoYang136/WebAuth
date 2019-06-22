package com.foxmail.gaoyang136.webauth.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.foxmail.gaoyang136.webauth.entity.User;

public interface UserDao {
	public static final String TABLE_NAME = "users";

	public static final String ID_LABLE = TABLE_NAME + ".id";

	public static final String USER_NAME_LABLE = TABLE_NAME + ".user_name";

	public static final String CREDENTIAL_LABLE = TABLE_NAME + ".credential";

	public static final String REGISTER_TIME_LABLE = TABLE_NAME + ".register_time";


	public void addUser(Connection conn, User user) throws SQLException;

	int[] addUsers(Connection conn, LinkedList<User> userList) throws SQLException;

	public void updateUser(Connection conn, User user) throws SQLException;

	public void deleteUser(Connection conn, int userID) throws SQLException;

	public User getUserbyID(Connection conn, int userID) throws SQLException;

	public User getUserbyName(Connection conn, String userName) throws SQLException;

	public List<User> getUsersbyRoleName(Connection conn, String roleName, int offset, int rows) throws SQLException;
}

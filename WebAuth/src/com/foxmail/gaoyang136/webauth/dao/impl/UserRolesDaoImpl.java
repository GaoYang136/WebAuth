package com.foxmail.gaoyang136.webauth.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.foxmail.gaoyang136.webauth.dao.RoleDao;
import com.foxmail.gaoyang136.webauth.entity.Role;

public class UserRolesDaoImpl implements com.foxmail.gaoyang136.webauth.dao.UserRolesDao {

	@Override
	public List<String> getRoleNamesbyUserID(Connection conn, int userID) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select " + ROLE_NAME_LABLE + " from " + TABLE_NAME
					+ " where "+ USER_ID_LABLE + " = ?;");

			ps.setInt(1, userID);
			resultSet = ps.executeQuery();

			List<String> roleList = new LinkedList<String>();
			while (resultSet.next()) {
				roleList.add(resultSet.getString(ROLE_NAME_LABLE));
			}

			return roleList;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public List<Role> getUserRolesbyUserID(Connection conn, int userID) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select * from " + TABLE_NAME + " join "
					+ RoleDao.TABLE_NAME + " on " + ROLE_NAME_LABLE + " = "
					+ RoleDao.ROLE_NAME_LABLE + " where " + USER_ID_LABLE + " = ?;");

			ps.setInt(1, userID);
			resultSet = ps.executeQuery();

			Role role = null;
			List<Role> roleList = new LinkedList<Role>();

			while (resultSet.next()) {
				role = new Role();
				role.setRoleName(resultSet.getString(RoleDao.ROLE_NAME_LABLE));
				role.setDescription(resultSet.getString(RoleDao.DESCRIPTION_LABLE));
				role.setGrantTime(resultSet.getTimestamp(GRANT_TIME_LABLE));

				roleList.add(role);
			}

			return roleList;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	/*@Override
	public List<User> getUsersbyRoleName(Connection conn, String roleName, int offset, int rows) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select * from " + TABLE_NAME + " join " + UserDao.TABLE_NAME
					+ " on " + USER_ID_LABLE + " = " + UserDao.ID_LABLE + " where " + ROLE_NAME_LABLE
					+ " = ? order by " + UserDao.ID_LABLE + " asc limit ?, ?;");

			ps.setString(1, roleName);
			ps.setInt(2, offset);
			ps.setInt(3, rows);
			resultSet = ps.executeQuery();

			User user = null;
			List<User> userList = new LinkedList<User>();
			while (resultSet.next()) {
				user = new User();

				user.setUserID(resultSet.getInt(UserDao.ID_LABLE));
				user.setUserName(resultSet.getString(UserDao.USER_NAME_LABLE));
				user.setCredential(resultSet.getString(UserDao.CREDENTIAL_LABLE));
				user.setRegisterTime(resultSet.getTimestamp(UserDao.REGISTER_TIME_LABLE));

				userList.add(user);
			}

			return userList;
		} finally {
			if (ps != null)
				ps.close();
		}
	}*/

	@Override
	public List<Integer> getUserIDbyRoleName(Connection conn, String roleName, int offset, int rows) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select " + USER_ID_LABLE + " from " + TABLE_NAME + " where "
					+ ROLE_NAME_LABLE + " = ? order by " + USER_ID_LABLE + " asc limit ?, ?;");

			ps.setString(1, roleName);
			ps.setInt(2, offset);
			ps.setInt(3, rows);
			resultSet = ps.executeQuery();

			List<Integer> userIDList = new LinkedList<Integer>();
			while (resultSet.next()) {
				userIDList.add(resultSet.getInt(USER_ID_LABLE));
			}

			return userIDList;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public void addUserRole(Connection conn, int userID, String userName, String roleName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into " + TABLE_NAME + " (" + USER_ID_LABLE
					+ ", "+ ROLE_NAME_LABLE + ", "+ USER_NAME_LABLE +") values (?, ?, ?);");

			ps.setInt(1, userID);
			ps.setString(2, roleName);
			ps.setString(3, userName);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public int[] addUserRoles(Connection conn, int userID, String userName, List<Role> roleList) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into " + TABLE_NAME + " (" + USER_ID_LABLE
					+ ", "+ ROLE_NAME_LABLE + ", "+ USER_NAME_LABLE +") values (?, ?, ?);");

			Role role = null;
			Iterator<Role> iterator = roleList.iterator();

			while (iterator.hasNext()) {
				role = iterator.next();
				ps.setInt(1, userID);
				ps.setString(2, role.getRoleName());
				ps.setString(3, userName);
				ps.addBatch();
			}

			return ps.executeBatch();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public void deleteUserRole(Connection conn, int userID, String roleName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from " + TABLE_NAME + " where " + USER_ID_LABLE
					+ " = ? and " + ROLE_NAME_LABLE + " = ?;");

			ps.setInt(1, userID);
			ps.setString(2, roleName);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	/*@Override
	public void assemUserWithRoles(Connection conn, LinkedList<User> userList) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("select * from " + TABLE_NAME + " join "
					+ RoleDao.TABLE_NAME + " on " + ROLE_NAME_LABLE + " = "
					+ RoleDao.ROLE_NAME_LABLE + " where " + USER_ID_LABLE + " = ?;");

			User user = null;
			Iterator<User> userIterator = userList.iterator();

			while (userIterator.hasNext()) {
				user = userIterator.next();
				ps.setInt(1, user.getUserID());
				ps.addBatch();
			}

			int[] result = ps.executeBatch();

			int i = 0;
			Role role = null;
			List<Role> roleList = null;
			ResultSet resultSet = null;

			userIterator = userList.iterator();
			{
				resultSet = ps.getResultSet();
				roleList = new LinkedList<Role>();
				while (resultSet.next()) {
					role = new Role();

					role.setRoleName(resultSet.getString(RoleDao.ROLE_NAME_LABLE));
					role.setDescription(resultSet.getString(RoleDao.DESCRIPTION_LABLE));
					role.setGrantTime(resultSet.getTimestamp(GRANT_TIME_LABLE));
					
					roleList.add(role);
				}
				userIterator.next().setRoles(roleList);

				i++;
			} while (i < result.length && ps.getMoreResults());

		} finally {
			if (ps != null)
				ps.close();
		}
	}*/
}

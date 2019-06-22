package com.foxmail.gaoyang136.webauth.dao.impl;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.foxmail.gaoyang136.webauth.dao.UserDao;
import com.foxmail.gaoyang136.webauth.dao.UserRolesDao;
import com.foxmail.gaoyang136.webauth.entity.User;

public class UserDaoImpl implements UserDao {

	@Override
	public void addUser(Connection conn, User user) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into " + TABLE_NAME + " ("
					+ USER_NAME_LABLE + ", "+ CREDENTIAL_LABLE +") values (?, ?);",
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getCredential());
			ps.executeUpdate();

		    ResultSet rs = ps.getGeneratedKeys();

		    if (rs.next()) {
		    	user.setUserID(rs.getInt(1));
		    } else {
		    	throw new SQLException("Insert user failed or cann't get the auto generated user ID.");
		    }
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public int[] addUsers(Connection conn, LinkedList<User> userList) throws BatchUpdateException, SQLException {
		int[] result = null;
		User user = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Iterator<User> iterator = null;

		try {
			ps = conn.prepareStatement("insert into " + TABLE_NAME + " ("
					+ USER_NAME_LABLE + ", "+ CREDENTIAL_LABLE +") values (?, ?);",
					Statement.RETURN_GENERATED_KEYS);

			iterator = userList.iterator();

			while (iterator.hasNext()) {
				user = iterator.next();
				ps.setString(1, user.getUserName());
				ps.setString(2, user.getCredential());
				ps.addBatch();
			}

			result = ps.executeBatch();

		    rs = ps.getGeneratedKeys();
			iterator = userList.iterator();

		    while (rs.next() && iterator.hasNext()) {
		    	iterator.next().setUserID(rs.getInt(1));
		    }

			return result;
		} catch (BatchUpdateException e) {
			/*result = e.getUpdateCounts();
			rs = ps.getGeneratedKeys();
			iterator = userList.iterator();

			//result.length <= userList.size, rs only contain auto keys generated by statement success excuted!!!
			int autoGenKey = -1;
			for (int i = 0; i < result.length && iterator.hasNext(); i++) {
				user = iterator.next();
				if (result[i] != Statement.EXECUTE_FAILED) {
					rs.next();
					autoGenKey = rs.getInt(1);

					user.setUserID(autoGenKey);
				} else {
					user.setUserID(-1);
				}
			}*/

			throw e;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public void updateUser(Connection conn, User user) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update " + TABLE_NAME + " set "+ USER_NAME_LABLE
					+ " = ?, " + CREDENTIAL_LABLE + " = ? where " + ID_LABLE + " = ?;");

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getCredential());
			ps.setInt(3, user.getUserID());
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public void deleteUser(Connection conn, int userID) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from " + TABLE_NAME + " where " + ID_LABLE + " = ?;");

			ps.setInt(1, userID);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public User getUserbyID(Connection conn, int userID) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select * from " + TABLE_NAME + " where " + ID_LABLE + " = ?;");

			ps.setInt(1, userID);
			resultSet = ps.executeQuery();

			User user = null;
			if (resultSet.next()) {
				user = new User();

				user.setUserID(resultSet.getInt(ID_LABLE));
				user.setUserName(resultSet.getString(USER_NAME_LABLE));
				user.setCredential(resultSet.getString(CREDENTIAL_LABLE));
				user.setRegisterTime(resultSet.getTimestamp(REGISTER_TIME_LABLE));
			}

			return user;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public User getUserbyName(Connection conn, String userName) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select * from " + TABLE_NAME + " where " + USER_NAME_LABLE + " = ?;");

			ps.setString(1, userName);
			resultSet = ps.executeQuery();

			User user = null;
			if (resultSet.next()) {
				user = new User();

				user.setUserID(resultSet.getInt(ID_LABLE));
				user.setUserName(resultSet.getString(USER_NAME_LABLE));
				user.setCredential(resultSet.getString(CREDENTIAL_LABLE));
				user.setRegisterTime(resultSet.getTimestamp(REGISTER_TIME_LABLE));
			}

			return user;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public List<User> getUsersbyRoleName(Connection conn, String roleName, int offset, int rows) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select * from " + TABLE_NAME + " join " + UserRolesDao.TABLE_NAME + " on "
					+ ID_LABLE + " = " + UserRolesDao.USER_ID_LABLE + " where "+ UserRolesDao.ROLE_NAME_LABLE
					+ " = ? order by " + ID_LABLE + " asc limit ?, ?;");

			ps.setString(1, roleName);
			ps.setInt(2, offset);
			ps.setInt(3, rows);
			resultSet = ps.executeQuery();

			User user = null;
			List<User> userList = new LinkedList<User>();
			while (resultSet.next()) {
				user = new User();

				user.setUserID(resultSet.getInt(ID_LABLE));
				user.setUserName(resultSet.getString(USER_NAME_LABLE));
				user.setCredential(resultSet.getString(CREDENTIAL_LABLE));
				user.setRegisterTime(resultSet.getTimestamp(REGISTER_TIME_LABLE));

				userList.add(user);
			}

			return userList;
		} finally {
			if (ps != null)
				ps.close();
		}
	}
}
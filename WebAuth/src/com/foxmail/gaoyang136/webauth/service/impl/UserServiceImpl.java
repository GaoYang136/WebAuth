package com.foxmail.gaoyang136.webauth.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.foxmail.gaoyang136.webauth.dao.UserDao;
import com.foxmail.gaoyang136.webauth.dao.UserRolesDao;
import com.foxmail.gaoyang136.webauth.dao.impl.UserDaoImpl;
import com.foxmail.gaoyang136.webauth.dao.impl.UserRolesDaoImpl;
import com.foxmail.gaoyang136.webauth.entity.User;
import com.foxmail.gaoyang136.webauth.service.UserService;
import com.foxmail.gaoyang136.webauth.ulit.DataSourceUlit;

public class UserServiceImpl implements UserService {

	@Override
	public User getUserbyName(String userName) throws SQLException {
		Connection conn = null;
		try {
			conn = DataSourceUlit.dataSource.getConnection();

			UserDao u = new UserDaoImpl();
			User user = u.getUserbyName(conn, userName);

			if (user != null) {
				UserRolesDao r = new UserRolesDaoImpl();
				user.setRoles(r.getUserRolesbyUserID(conn, user.getUserID()));
			}

			return user;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public boolean registerUser(User user) throws SQLException {
		boolean autoCommit = true;
		Connection conn = null;
		try {
			conn = DataSourceUlit.dataSource.getConnection();

			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			UserDao u = new UserDaoImpl();
			u.addUser(conn, user);

			UserRolesDao r = new UserRolesDaoImpl();
			r.addUserRoles(conn, user.getUserID(), user.getUserName(), user.getRoles());

			/*for (int i = 0; i < roleResult.length; i++) {
				if (roleResult[i] == Statement.EXECUTE_FAILED)
					throw new SQLException();
			}*/

			conn.commit();
			return true;
		} catch (SQLException e) {
			conn.rollback();
			return false;
		} finally {
			conn.setAutoCommit(autoCommit);

			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public List<User> getUsersbyRoleName(String roleName, int offset, int rows) throws SQLException {
		Connection conn = null;
		try {
			conn = DataSourceUlit.dataSource.getConnection();

			UserDao u = new UserDaoImpl();
			List<User> userList = u.getUsersbyRoleName(conn, roleName, offset, rows);

			UserRolesDao r = new UserRolesDaoImpl();

			User user = null;
			Iterator<User> iterator = userList.iterator();
			while (iterator.hasNext()) {
				user = iterator.next();
				user.setRoles(r.getUserRolesbyUserID(conn, user.getUserID()));
			}

			return userList;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

}

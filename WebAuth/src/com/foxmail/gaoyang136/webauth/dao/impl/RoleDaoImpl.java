package com.foxmail.gaoyang136.webauth.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.foxmail.gaoyang136.webauth.dao.RoleDao;
import com.foxmail.gaoyang136.webauth.entity.Role;

public class RoleDaoImpl implements RoleDao {

	@Override
	public Role getRolebyName(Connection conn, String roleName) throws SQLException {
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = conn.prepareStatement("select * from " + TABLE_NAME + " where "
					+ ROLE_NAME_LABLE + " = ?;");

			ps.setString(1, roleName);
			resultSet = ps.executeQuery();

			Role role = null;
			if (resultSet.next()) {
				role = new Role();

				role.setRoleName(resultSet.getString(ROLE_NAME_LABLE));
				role.setDescription(resultSet.getString(DESCRIPTION_LABLE));
			}

			return role;
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public void addRole(Connection conn, Role role) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into " + TABLE_NAME + " ("
					+ ROLE_NAME_LABLE + ", "+ DESCRIPTION_LABLE +") values (?, ?);");

			ps.setString(1, role.getRoleName());
			ps.setString(2, role.getDescription());
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public void deleteRole(Connection conn, String roleName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from " + TABLE_NAME + " where " + ROLE_NAME_LABLE + " = ?;");

			ps.setString(1, roleName);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public void updateRole(Connection conn, String oldRoleName, Role newRole) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update " + TABLE_NAME + " set "+ ROLE_NAME_LABLE
					+ " = ?, " + DESCRIPTION_LABLE + " = ? where " + ROLE_NAME_LABLE + " = ?;");

			ps.setString(1, newRole.getRoleName());
			ps.setString(2, newRole.getDescription());
			ps.setString(3, oldRoleName);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

}

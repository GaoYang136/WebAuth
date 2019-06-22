package com.foxmail.gaoyang136.webauth.test;

import java.sql.Connection;
import java.sql.SQLException;

import com.foxmail.gaoyang136.webauth.dao.RoleDao;
import com.foxmail.gaoyang136.webauth.dao.impl.RoleDaoImpl;
import com.foxmail.gaoyang136.webauth.entity.Role;

public class RoleDaoTest {

	public static void main(String[] args) {

		Role role = new Role();
		role.setRoleName("xxzx");
		role.setDescription("description");

		Role newRole = new Role();
		newRole.setRoleName("fgfg");
		newRole.setDescription("fasfs");

		RoleDao r = new RoleDaoImpl();
		try {
			Connection conn = DaoTestUlit.getConnection();
			r.addRole(conn, role);
			r.updateRole(conn, "xxzx", newRole);
			Role role1 = r.getRolebyName(conn, "fgfg");
			System.out.printf("%s:%s\n",role1.getRoleName(),role1.getDescription());
			r.deleteRole(conn, "fgfg");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

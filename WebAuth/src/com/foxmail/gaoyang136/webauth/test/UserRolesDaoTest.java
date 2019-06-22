package com.foxmail.gaoyang136.webauth.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.foxmail.gaoyang136.webauth.dao.UserRolesDao;
import com.foxmail.gaoyang136.webauth.dao.impl.UserRolesDaoImpl;
import com.foxmail.gaoyang136.webauth.entity.Role;

public class UserRolesDaoTest {

	public static void main(String[] args) {

		try {
			Connection conn = DaoTestUlit.getConnection();
			UserRolesDao u = new UserRolesDaoImpl();


			u.addUserRole(conn, 20, "fdf", "ff");


			Iterator<String> iterator1 = u.getRoleNamesbyUserID(conn, 11).iterator();
			while (iterator1.hasNext()) {
				System.out.println(iterator1.next());
			}
			System.out.println("--------------");


			Role role = null;
			Iterator<Role> iterator2 = u.getUserRolesbyUserID(conn, 11).iterator();
			while (iterator2.hasNext()) {
				role = iterator2.next();
				System.out.printf("%s---%s\n", role.getRoleName(), role.getDescription());
			}
			System.out.println("--------------");

			Iterator<Integer> iterator3 = u.getUserIDbyRoleName(conn, "web_user", 0, 100).iterator();
			while (iterator3.hasNext()) {
				System.out.printf("%d\n", iterator3.next());
			}
			System.out.println("--------------");

			u.deleteUserRole(conn, 11, "ff");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

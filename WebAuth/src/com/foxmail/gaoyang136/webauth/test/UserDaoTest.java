package com.foxmail.gaoyang136.webauth.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.foxmail.gaoyang136.webauth.dao.UserDao;
import com.foxmail.gaoyang136.webauth.dao.impl.UserDaoImpl;
import com.foxmail.gaoyang136.webauth.entity.User;

public class UserDaoTest {

	public static void main(String[] args) throws SQLException {
		UserDao u = new UserDaoImpl();
		Connection conn = null;

			User user1 = new User();
			user1.setUserID(522);
			user1.setUserName("user1");
			user1.setCredential("DF5FAE65$3$GF35DY1SW6BDH5USBD8JU");
			user1.setRegisterTime(new Timestamp(System.currentTimeMillis()));

			User user2 = new User();
			user2.setUserID(54);
			user2.setUserName(null);
			user2.setCredential("45657665$4$GF3564565USBgD8JU");
			user2.setRegisterTime(new Timestamp(System.currentTimeMillis()));

			User user3 = new User();
			user3.setUserID(12);
			user3.setUserName("user3");
			user3.setCredential("45657665$5$GF3564565USBgD8JU");
			user3.setRegisterTime(new Timestamp(System.currentTimeMillis()));
			
			LinkedList<User> userList = new LinkedList<User>();
			userList.add(user1);
			userList.add(user2);
			userList.add(user3);

		try {
			conn = DaoTestUlit.getConnection();
			conn.setAutoCommit(false);

			
			
			

			/*u.addUsers(conn, user1);

			List<User> userList = new LinkedList<User>();
			userList.add(user2);
			userList.add(user3);
			int[] result = u.addUsers(conn, userList);
			for (int i = 0; i < result.length; i++) {
				System.out.printf("%d\n", result[i]);
			}


			User userx = u.getUserbyID(conn, 11);
			System.out.printf("%s-%s-%s-%s\n", userx.getUserID(), userx.getUserName(), userx.getCredential(),
					userx.getRegisterTime().toString());

			User userx1 = u.getUserbyName(conn, "user3");
			System.out.printf("%s-%s-%s-%s\n", userx1.getUserID(), userx1.getUserName(), userx1.getCredential(),
					userx1.getRegisterTime().toString());*/

			
			
			u.addUsers(conn, userList);
			
			printUserList(userList);
			
			/*User userm = null;
			
			Iterator<User> iterator = u.getUsersbyRoleName(conn, "web_user", 1, 2).iterator();
			while (iterator.hasNext()) {
				userm = iterator.next();
				System.out.printf("%s-%s-%s-%s\n", userm.getUserID(), userm.getUserName(),
						userm.getCredential(), userm.getRegisterTime().toString());
			}
			System.out.println("------------\n");
			
			iterator = u.getUsersbyRoleName(conn, "audit", 0, 1).iterator();
			while (iterator.hasNext()) {
				userm = iterator.next();
				System.out.printf("%s-%s-%s-%s\n", userm.getUserID(), userm.getUserName(),
						userm.getCredential(), userm.getRegisterTime().toString());
			}
			System.out.println("------------\n");
			
			iterator = u.getUsersbyRoleName(conn, "sys_admin", 1, 100).iterator();
			while (iterator.hasNext()) {
				userm = iterator.next();
				System.out.printf("%s-%s-%s-%s\n", userm.getUserID(), userm.getUserName(),
						userm.getCredential(), userm.getRegisterTime().toString());
			}
			System.out.println("------------\n");
			
			iterator = u.getUsersbyRoleName(conn, "user_admin", 2, 100).iterator();
			while (iterator.hasNext()) {
				userm = iterator.next();
				System.out.printf("%s-%s-%s-%s\n", userm.getUserID(), userm.getUserName(),
						userm.getCredential(), userm.getRegisterTime().toString());
			}
			System.out.println("------------\n");*/

			/*user3.setUserName("ujjj3");
			user3.setCredential("45657665$6$GF3564565USBgD8JU");

			u.updateUser(conn, user3);
			u.deleteUser(conn, 12);*/
			

			
			
			conn.commit();
		/*} catch (BatchUpdateException e) {
			printUserList(userList);
			
			conn.commit();*/
		} catch (SQLException e) {
			printUserList(userList);

			conn.rollback();
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	public static void printUserList(List<User> userList) {
		User user = null;
		Iterator<User> iterator = userList.iterator();

		while (iterator.hasNext()) {
			user = iterator.next();
			System.out.printf("%s-%s-%s-%s\n", user.getUserID(), user.getUserName(),
					user.getCredential(), user.getRegisterTime().toString());
		}

		System.out.println("------------\n");
	}

}

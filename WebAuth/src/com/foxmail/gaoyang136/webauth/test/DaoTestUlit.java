package com.foxmail.gaoyang136.webauth.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoTestUlit {

	private static final String IP="127.0.0.1";
	private static final String username="root";
	private static final String password="xxzx-2016";
	private static final String database="authdb";

	/*static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public static Connection getConnection() throws SQLException {
		String content="jdbc:mysql://"+IP+":3306/"+database+"?user="+username
				+"&password="+password+"&userUnicode=true&CharterEnoding=UTF-8&serverTimezone=Asia/Shanghai&sslMode=DISABLED";

		return DriverManager.getConnection(content);
	}
}

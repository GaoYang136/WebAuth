package com.foxmail.gaoyang136.webauth.ulit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class DataSourceUlit implements ServletContextListener {
	public static DataSource dataSource = null;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			dataSource = (DataSource) envCtx.lookup("jdbc/AuthDB");
			if (dataSource == null) {
				context.log("Get DataSource failed, can't start this webapp!");
				System.exit(1);
			}
		} catch (NamingException e) {
			context.log("Maybe the container not support javaEE!", e);
			e.printStackTrace();
			System.exit(2);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (dataSource != null)
			dataSource = null;
	}
}

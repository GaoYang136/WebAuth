package com.foxmail.gaoyang136.webauth.test;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*Connection conn = null;
		try {
			conn = DataSourceUlit.dataSource.getConnection();
			
			UserDao u = new UserDaoImpl();
			
			User userm= null;
			Iterator<User> iterator = u.getUsersbyRoleName(conn, "user_admin", 0, 100).iterator();
			while (iterator.hasNext()) {
				userm = iterator.next();
				System.out.printf("%s-%s-%s-%s\n", userm.getUserID(), userm.getUserName(),
						userm.getCredential(), userm.getRegisterTime().toString());
			}
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new IOException(e);
				}
			}
		}*/
		
		//resp.sendError(501, "fwesazrfgws501test");
		Writer writer = resp.getWriter();
		writer.write("*:" + req.isUserInRole("*") + "<br/>");
		writer.write("**:" + req.isUserInRole("**") + "<br/>");
		
	}

}

package com.foxmail.gaoyang136.webauth.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getAuthType() != null || req.getUserPrincipal() != null || req.getRemoteUser() != null) {
			req.logout();

			/*The method request.logout() don't unbinds any objects bound to it, special the CSRF token.
			 *So we must call the session.invalidate() method to remove all objects bound to the session.
			 */
			req.getSession().invalidate();

			req.getRequestDispatcher("/WEB-INF/jsp/user/logout.jsp").forward(req, resp);
		} else {
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/"));
		}
	}
}

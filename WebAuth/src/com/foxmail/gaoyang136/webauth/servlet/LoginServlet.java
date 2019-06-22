package com.foxmail.gaoyang136.webauth.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String USER_NAME = "user_name";

	public static final String PASSWORD = "password";

	public static final String CAPTCHA = "captcha";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		String userName = req.getParameter(USER_NAME);
		String password = req.getParameter(PASSWORD);
		String captcha = req.getParameter(CAPTCHA);

		boolean ok = true;
		String userNameError = null;
		String passwordError = null;
		String captchaError = null;
		String savedCaptcha = (String) session.getAttribute(CaptchaServlet.CAPTCHA_ATT_NAME);
		//important!!!!!! captcha is disposable!!
		session.removeAttribute(CaptchaServlet.CAPTCHA_ATT_NAME);

		/*
		 * Check register information submitted by user.
		 */
		if (userName == null || userName.trim().isEmpty()) {
			ok = false;
			userNameError = "Please input user name!";
		}

		if (password == null || password.trim().isEmpty()) {
			ok = false;
			passwordError = "Please input password!";
		}

		if (captcha == null || captcha.trim().isEmpty()) {
			ok = false;
			captchaError = "Please input Verification Code!";
		} else if (!captcha.equalsIgnoreCase(savedCaptcha)) {
			ok = false;
			captchaError = "Captcha is incorrect!";
		}

		if (!ok) {
			req.setAttribute("userNameError", userNameError);
			req.setAttribute("passwordError", passwordError);
			req.setAttribute("captchaError", captchaError);

			req.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(req, resp);

			return;
		}

		if (req.getAuthType() != null || req.getUserPrincipal() != null || req.getRemoteUser() != null) {
			resp.sendRedirect(resp.encodeRedirectURL("logout"));

			return;
		}

		try {
			req.login(userName, password);
		} catch (ServletException e) {
			req.setAttribute("loginFailed", "Login failed! user name or passwor is incorrect!");

			req.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(req, resp);

			return;
		}

		String fromURL = (String) session.getAttribute("fromURL");
		if (fromURL != null) {
			session.removeAttribute("fromURL");
		}

		System.out.printf("User Principal:%s\n"
				+ "Auth Type:%s\n"
				+ "Remote User:%s\n"
				+ "Is sys_admin:%b\n"
				+ "Is audit:%b\n"
				+ "Is user_admin:%b\n"
				+ "Is web_user:%b\n"
				+ "Is ff:%b\n"
				+ "From URL:%s\n"
				+ "-------------------\n",
				req.getUserPrincipal().getName(), req.getAuthType(), req.getRemoteUser(), req.isUserInRole("sys_admin"),
				req.isUserInRole("audit"), req.isUserInRole("user_admin"), req.isUserInRole("web_user"), req.isUserInRole("ff"), fromURL);

		if (fromURL == null || fromURL.trim().isEmpty()) {
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/"));
		} else {
			resp.sendRedirect(resp.encodeRedirectURL(fromURL));
		}
	}
}

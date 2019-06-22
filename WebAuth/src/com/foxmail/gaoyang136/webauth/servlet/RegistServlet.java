package com.foxmail.gaoyang136.webauth.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.realm.DigestCredentialHandlerBase;

import com.foxmail.gaoyang136.webauth.entity.Role;
import com.foxmail.gaoyang136.webauth.entity.User;
import com.foxmail.gaoyang136.webauth.service.UserService;
import com.foxmail.gaoyang136.webauth.service.impl.UserServiceImpl;
import com.foxmail.gaoyang136.webauth.ulit.StringUlit;

public class RegistServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String USER_NAME = "user_name";

	public static final String PASSWORD = "password";

	public static final String REPEAT_PW = "repeat_pw";

	public static final String CAPTCHA = "captcha";

	private final int MAX_USER_NAME_LENGTH = 20;

	private int iterations = 3;

	private int saltLength = 8;

	private String algorithm = "SHA-256";

	private String handlerClassName = "org.apache.catalina.realm.MessageDigestCredentialHandler";

	@Override
	public void init() throws ServletException {
		algorithm = getInitParameter("algorithm");
		handlerClassName = getInitParameter("handlerClassName");
		iterations = Integer.parseInt(getInitParameter("iterations"));
		saltLength = Integer.parseInt(getInitParameter("saltLength"));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		String userName = req.getParameter(USER_NAME);
		String password = req.getParameter(PASSWORD);
		String repeatPW = req.getParameter(REPEAT_PW);
		String captcha = req.getParameter(CAPTCHA);

		boolean ok = true;
		String userNameError = null;
		String passwordError = null;
		String confirmPWError = null;
		String captchaError = null;
		String savedCaptcha = (String) session.getAttribute(CaptchaServlet.CAPTCHA_ATT_NAME);
		//important!!!!!! captcha is disposable!!
		session.removeAttribute(CaptchaServlet.CAPTCHA_ATT_NAME);

		UserService us = new UserServiceImpl();

		/*
		 * Check register information submitted by user.
		 */
		if (userName == null || userName.trim().isEmpty()) {
			ok = false;
			userNameError = "Please input user name!";
		} else if (userName.length() > MAX_USER_NAME_LENGTH) {
			ok = false;
			userNameError = "User name is too long than " + MAX_USER_NAME_LENGTH + " characters!";
		} else if (!StringUlit.checkUserName(userName)) {
			ok = false;
			userNameError = "User name is invalid!";
		} else {
			try {
				if (us.getUserbyName(userName) != null) {
					ok = false;
					userNameError = "User name has been registered!";
				}
			} catch (SQLException e) {
				throw new ServletException(e);
			}
		}

		if (password == null || password.trim().isEmpty()) {
			ok = false;
			passwordError = "Please input password!";
		} else if (!StringUlit.checkPassword(password)) {
			ok = false;
			passwordError = "Password is invalid!";
		}

		if (repeatPW == null || repeatPW.trim().isEmpty()) {
			ok = false;
			confirmPWError = "Please input confirm password!";
		} else if (!repeatPW.equals(password)) {
			ok = false;
			confirmPWError = "Confirm password is not same as password!";
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
			req.setAttribute("confirmPWError", confirmPWError);
			req.setAttribute("captchaError", captchaError);

			req.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(req, resp);

			return;
		}

		Class<? extends DigestCredentialHandlerBase> handlerClass = null;
		try {
			handlerClass = (Class<? extends DigestCredentialHandlerBase>) Class.forName(handlerClassName);
		} catch (ClassNotFoundException e) {
			throw new ServletException("couldn't find the class designated by init-param of the servlet element!", e);
		}

		DigestCredentialHandlerBase handler = null;
		try {
			handler = (DigestCredentialHandlerBase) handlerClass.getDeclaredConstructor().newInstance();
			handler.setAlgorithm(algorithm);
			handler.setIterations(iterations);
			handler.setSaltLength(saltLength);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException
				| InvocationTargetException | NoSuchMethodException | NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}

		User user = new User();
		user.setUserName(userName);
		user.setCredential(handler.mutate(password));

		Role role = new Role();
		role.setRoleName("web_user");
		user.setRoles(Arrays.asList(role));

		boolean success = false;
		try {
			success = us.registerUser(user);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

		req.setAttribute("registerResult", success);
		req.getRequestDispatcher("/WEB-INF/jsp/user/regresult.jsp").forward(req, resp);
	}

}

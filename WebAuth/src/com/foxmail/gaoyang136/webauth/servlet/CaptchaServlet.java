package com.foxmail.gaoyang136.webauth.servlet;

import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.foxmail.gaoyang136.webauth.ulit.CaptchaUlit;

public class CaptchaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CAPTCHA_ATT_NAME = "savedCaptcha";

	private int pictureWidth = 80;

	private int pictureHeight = 30;

	private int captchaLength = 4;

	@Override
	public void init() throws ServletException {
		pictureWidth = Integer.parseInt(getInitParameter("pictureWidth"));
		pictureHeight = Integer.parseInt(getInitParameter("pictureHeight"));
		captchaLength = Integer.parseInt(getInitParameter("captchaLength"));
	}

	@Override
	protected long getLastModified(HttpServletRequest req) {
		return System.currentTimeMillis();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		String captchaString = CaptchaUlit.generateCaptchaString(captchaLength);
		RenderedImage image = CaptchaUlit.generateCaptchaImage(captchaString, pictureWidth, pictureHeight);

		session.setAttribute(CAPTCHA_ATT_NAME, captchaString);

		response.setContentType("image/bmp");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");

		ImageIO.write(image, "BMP", response.getOutputStream());
	}
}
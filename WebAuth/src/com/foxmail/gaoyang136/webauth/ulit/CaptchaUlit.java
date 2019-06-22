package com.foxmail.gaoyang136.webauth.ulit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Random;

public class CaptchaUlit {

	private static final Color[] COLOR = {new Color(0, 255, 255), new Color(255, 0, 255), new Color(138, 43, 226),
			new Color(255, 0, 0), new Color(0, 128, 0), new Color(0, 128, 192), new Color(165, 42, 42)};

	private static final char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'G', 'K', 'M', 'N', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm',
			'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	private static final Font[] FONTS = {new Font("Calibri", Font.BOLD, 28), new Font("Bodoni", Font.BOLD, 28),
			new Font("隶书", Font.BOLD, 28), new Font("新宋体", Font.BOLD, 28)};

	private static final Random random = new Random();

	public static String generateCaptchaString(int length) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < length; i++) {
			builder.append(CHARS[random.nextInt(CHARS.length)]);
		}

		return builder.toString();
	}

	public static RenderedImage generateCaptchaImage(String captchaString, int width, int height) {
		int length = captchaString.length();
		char[] data = captchaString.toCharArray();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		AffineTransform at = new AffineTransform();
		int dis = (image.getWidth() - 20) / length;
		int mid = image.getHeight() / 2 + 7;
		int acount = image.getWidth() * image.getHeight() / 400;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//Rendering image's background with white.
		g2d.setColor(new Color(240, 240, 240));
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		//g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

		//Drawing interference lines.
		for (int i = 0; i < acount; i++) {
			g2d.setColor(COLOR[random.nextInt(COLOR.length)]);
	        int x = random.nextInt(image.getWidth());
	        int y = random.nextInt(image.getHeight());
	        int xl = random.nextInt(12);
	        int yl = random.nextInt(12);
	        g2d.drawLine(x, y, x + xl, y + yl);
	    }

		for (int i = 0; i < length; i++) {
			at.setToRotation((random.nextDouble() - 0.5) * Math.PI / 2, dis * i + 10, mid);
			g2d.setTransform(at);
			g2d.setColor(COLOR[random.nextInt(COLOR.length)]);
			g2d.setFont(FONTS[random.nextInt(FONTS.length)]);
			g2d.drawChars(data, i, 1, dis * i + 10, mid);
		}

		return image;
	}
}

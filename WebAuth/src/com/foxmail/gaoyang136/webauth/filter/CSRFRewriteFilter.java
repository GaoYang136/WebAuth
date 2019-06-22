package com.foxmail.gaoyang136.webauth.filter;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

public class CSRFRewriteFilter extends HttpFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SAVED_TOKEN_ATT_NAME = "savedToken";

	private String tokenName = "token";

	@Override
	public void init() throws ServletException {
		tokenName = getInitParameter("tokenName");
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		String savedToken = null;
		String requestToken = req.getParameter(tokenName);
		HttpSession session = req.getSession(false);

		/*if (session != null) {
			savedToken = (String) session.getAttribute(SAVED_TOKEN_ATT_NAME);

			if (savedToken != null && savedToken.equals(requestToken)) {
				chain.doFilter(req, new CSRFRewriteResponseWrraper(req, resp));

				return;
			} else if (req.getAuthType() == null && req.getUserPrincipal() == null && req.getRemoteUser() == null) {
				chain.doFilter(req, new CSRFRewriteResponseWrraper(req, resp));

				return;
			} else if (req.getDispatcherType() == DispatcherType.ERROR) {
				chain.doFilter(req, resp);

				return;
			} else {
				resp.sendError(HttpServletResponse.SC_FORBIDDEN);

				return;
			}
		}

		chain.doFilter(req, resp);*/

		if (req.getAuthType() != null || req.getUserPrincipal() != null || req.getRemoteUser() != null) {
			if (session == null) {
				throw new ServletException("Server configuration not allow to use the filter to prevent CSRF!");
			}

			savedToken = (String) session.getAttribute(SAVED_TOKEN_ATT_NAME);

			if (savedToken == null || !savedToken.equals(requestToken)) {
				if (req.getDispatcherType() == DispatcherType.ERROR) {
					chain.doFilter(req, resp);

					return;
				}

				resp.sendError(HttpServletResponse.SC_FORBIDDEN, "No token, illegal request!");

				return;
			}
		}

		chain.doFilter(req, new CSRFRewriteResponseWrraper(req, resp));
	}

	class CSRFRewriteResponseWrraper extends HttpServletResponseWrapper {

		private HttpServletRequest req;

		public CSRFRewriteResponseWrraper(HttpServletRequest req, HttpServletResponse response) {
			super(response);

			this.req = req;
		}

		@Override
		public String encodeURL(String url) {
			if (req.getAuthType() == null && req.getUserPrincipal() == null && req.getRemoteUser() == null) {
				return super.encodeURL(url);
			}

			HttpSession session = req.getSession();
			String nonce = (String) session.getAttribute(SAVED_TOKEN_ATT_NAME);

			if (nonce == null) {
				nonce = generateNonce();
				session.setAttribute(SAVED_TOKEN_ATT_NAME, nonce);
			}

			return addNonce(super.encodeURL(url), nonce);
		}

		@Override
		public String encodeRedirectURL(String url) {
			if (req.getAuthType() == null && req.getUserPrincipal() == null && req.getRemoteUser() == null) {
				return super.encodeRedirectURL(url);
			}

			HttpSession session = req.getSession();
			String nonce = (String) session.getAttribute(SAVED_TOKEN_ATT_NAME);

			if (nonce == null) {
				nonce = generateNonce();
				session.setAttribute(SAVED_TOKEN_ATT_NAME, nonce);
			}

			return addNonce(super.encodeRedirectURL(url), nonce);
		}

		@Override
		@Deprecated
		public String encodeUrl(String url) {
			return encodeURL(url);
		}

		@Override
		@Deprecated
		public String encodeRedirectUrl(String url) {
			return encodeRedirectURL(url);
		}

		private String generateNonce() {
			byte random[] = new byte[16];

			// Render the result as a String of hexadecimal digits
			StringBuilder buffer = new StringBuilder();

			SecureRandom randomSource = new SecureRandom();
			randomSource.nextBytes(random);

			for (int j = 0; j < random.length; j++) {
				byte b1 = (byte) ((random[j] & 0xf0) >> 4);
				byte b2 = (byte) (random[j] & 0x0f);
				if (b1 < 10) {
					buffer.append((char) ('0' + b1));
				} else {
					buffer.append((char) ('A' + (b1 - 10)));
				}
				if (b2 < 10) {
					buffer.append((char) ('0' + b2));
				} else {
					buffer.append((char) ('A' + (b2 - 10)));
				}
			}

			return buffer.toString();
		}

		private String addNonce(String url, String nonce) {

	        if ((url == null) || (nonce == null)) {
	            return url;
	        }

	        String path = url;
	        String query = "";
	        String anchor = "";
	        int pound = path.indexOf('#');
	        if (pound >= 0) {
	            anchor = path.substring(pound);
	            path = path.substring(0, pound);
	        }
	        int question = path.indexOf('?');
	        if (question >= 0) {
	            query = path.substring(question);
	            path = path.substring(0, question);
	        }
	        StringBuilder sb = new StringBuilder(path);
	        if (query.length() >0) {
	            sb.append(query);
	            sb.append('&');
	        } else {
	            sb.append('?');
	        }
	        sb.append(tokenName);
	        sb.append('=');
	        sb.append(nonce);
	        sb.append(anchor);
	        return sb.toString();
	    }
	}

}

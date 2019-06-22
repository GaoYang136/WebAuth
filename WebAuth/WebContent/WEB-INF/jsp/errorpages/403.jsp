<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ResourceBundle" %>
<%-- You can do this by using Filter with dispatcher element on value "ERROR" and url-pattner element on value "*". --%>
<%! private static final String ERROR_MESSAGE = "Access to the requested resource has been denied"; %>
<%
	ResourceBundle resource = ResourceBundle.getBundle("org/apache/catalina/realm/LocalStrings");
	String errorMessage = resource.getString("realmBase.forbidden");

	String message = (String) request.getAttribute("javax.servlet.error.message");

	if (errorMessage == null) {
		errorMessage = ERROR_MESSAGE;
	}

	if (errorMessage.equals(message)) {
		if (request.getUserPrincipal() == null && request.getAuthType() == null && request.getRemoteUser() == null) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			request.setAttribute("javax.servlet.error.message", "Unauthenticated, Please login first!");
			request.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_UNAUTHORIZED);

			/*
			 *If you want to return extra http body, not only http status code, you shouldn't use
			 *response.sendError() method. Please forward request or directly return content at here.
			 */
			request.getRequestDispatcher("/WEB-INF/jsp/errorpages/401.jsp").forward(request, response);

			return;
		}
	}
%>
<%-- Real 403 error page. Maybe use jsp:include to include content below. --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>403</title>
</head>
<body>
403<br/>
message:<%= request.getAttribute("javax.servlet.error.message") %><br/>
status code:<%= request.getAttribute("javax.servlet.error.status_code") %><br/>
exception type:<%= request.getAttribute("javax.servlet.error.exception_type") %><br/>
exception:<%= request.getAttribute("javax.servlet.error.exception") %><br/>
request uri:<%= request.getAttribute("javax.servlet.error.request_uri") %><br/>
servlet name:<%= request.getAttribute("javax.servlet.error.servlet_name") %><br/><br/>

javax.servlet.forward.request_uri:<%= request.getAttribute("javax.servlet.forward.request_uri") %><br/>
javax.servlet.forward.context_path:<%= request.getAttribute("javax.servlet.forward.context_path") %><br/>
javax.servlet.forward.servlet_path:<%= request.getAttribute("javax.servlet.forward.servlet_path") %><br/>
javax.servlet.forward.path_info:<%= request.getAttribute("javax.servlet.forward.path_info") %><br/>
javax.servlet.forward.query_string:<%= request.getAttribute("javax.servlet.forward.query_string") %><br/>

你的当前登录用户没有访问权限，是否注销当前用户
</body>
</html>
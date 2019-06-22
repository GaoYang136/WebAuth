<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= request.getAttribute("javax.servlet.error.status_code") %></title>
</head>
<body>
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

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注册结果</title>
</head>
<body>
注册${requestScope.registerResult ? "成功" : "失败"}，
<a href="${requestScope.registerResult ? 'login' : 'register'}">
${requestScope.registerResult ? "去登陆" : "重新注册"}?</a>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登录</title>
</head>
<body>
	<form action="${pageContext.response['encodeURL']('login')}" method="post">
		<span>${requestScope.loginFailed}</span><br/>
		<span>${requestScope.userNameError}</span><br/>
		<label>用&nbsp;&nbsp;户&nbsp;&nbsp;名：</label>
		<input type="text" name="user_name" value="${param.user_name}" /><br/><br/>
		<span>${requestScope.passwordError}</span><br/>
		<label>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
		<input type="password" name="password" /><br/><br/>
		<span>${requestScope.captchaError}</span><br/>
		<label>验&nbsp;&nbsp;证&nbsp;&nbsp;码：</label>
		<input type="text" name="captcha" />
		<img id="captcha" alt="验证码" src="${pageContext.response['encodeURL']('captcha')}"/>
		<input type="button" value="刷新验证码" onclick="document.getElementById('captcha').src='${pageContext.response['encodeURL']('captcha?time=')}'+(new Date()).getTime();"/><br/>
		<br/><br/>
		<input type="submit" value="登录" />
		<input type="reset" value="重置" />
	</form>
</body>
</html>
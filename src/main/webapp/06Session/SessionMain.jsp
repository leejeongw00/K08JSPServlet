<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

long creationTime = session.getCreationTime();
String creationTimeStr = dateFormat.format(new Date(creationTime));

long lastTime = session.getCreationTime();
String lastTimeStr = dateFormat.format(new Date(creationTime));
%>
<html>
<head>
<meta charset="UTF-8">
<title>Session 설정 확인</title>
</head>
<body>
	<h2>Session 설정 확인</h2>
	<!-- 
	세션 유지 시간은 별도의 설정이 없으면 30분(1800초)으로 지정된다.
	 -->
	<ul>
		<li>세션 유지 시간 : <%= session.getMaxInactiveInterval() %></li>
		<li>세션 아이디 : <%= session.getId() %></li>
		<li>최초 요청 시각 : <%= creationTimeStr %></li>
		<li>마지막 요청 시각 : <%= lastTimeStr %></li>
	</ul>
</body>
</html>
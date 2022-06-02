<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<h4>OtherPage.jsp</h4>
<ul>
	<!-- request영역에 저장된 변수값 -->
	<li>저장된 값 : ${requestVar }</li>
	<!-- param태그 -->
	<li>매개변수 1 : ${param.user_param1 }</li>
	<li>매개변수 2 : ${param.user_param2 }</li>
</ul>
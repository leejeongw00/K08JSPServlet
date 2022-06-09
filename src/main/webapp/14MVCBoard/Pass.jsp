<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 첨부형 게시판</title>

<script type="text/javascript">
	//글쓰기 폼에서 누락된 내용이 있는지 확인하는 함수
	function validateForm(form){
		if(form.pass.value == ""){
			alert("비밀번호를 입력하세요.");
			form.pass.focus();
			return false;
		}
	}
</script>
</head>
<body>
<h2>파일 첨부형 게시판 - 패스워드 검증(Pass)</h2>
<!-- 
해당 요청명으로 넘어온 파라미터는 컨트롤러에서 받은 후 request영역에 저장하여
View에서 확인할 수 있지만, EL을 이용하면 해당 과정없이 param내장객체로 즉시
값을 받아올 수 있다.

※hidden 박스를 사용하는 경우 웹브라우저에서는 숨김처리 되기 때문에 값이 제대로
입력되었는지 화면으로 확인할 수 없다. 따라서 개발자모드를 사용하거나 type을 디버깅용으로
잠깐 수정한 후 값이 제대로 입력되었는지 반드시 확인해야 한다.
 -->
<form name="writeFrm" method="post" action="../mvcboard/pass.do" 
	onsubmit="return validateForm(this);">
<input type="hid den" name="idx" value="${param.idx }" />
<input type="hid den" name="mode" value="${mode }"/>
<table border="1" width="90%">
	<tr>
		<td>비밀번호</td>
		<td>
			<input type="password" name="pass" style="width:100px" />
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<button type="submit">검증하기</button>
			<button type="reset">RESET</button>
			<button type="button" onclick="location.href='../mvcboard/list.do'">
				목록 바로가기
			</button>
		</td>
	</tr>
</table>	
</form>
</body>
</html>
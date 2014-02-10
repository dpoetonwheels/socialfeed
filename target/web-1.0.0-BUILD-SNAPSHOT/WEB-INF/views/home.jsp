<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<p>Twitter template = ${screenName} </p>

<c:forEach items="${tweets}" var="tweet">
	<tr>
		<td>${tweet.getText()}</td>
	</tr>
</c:forEach>

</body>
</html>

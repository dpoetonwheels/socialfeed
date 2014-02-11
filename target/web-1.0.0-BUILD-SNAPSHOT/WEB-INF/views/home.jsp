<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Social Feeds
</h1>

<P>  The time on the server is ${serverTime}. </P>

<p> Twitter Feeds from a user with hashtag search
<c:forEach items="${tweets}" var="tweet">
	<tr>
		<td>${tweet.getText()}</td>
	</tr>
</c:forEach>
</p>

<p> Facebook posts from a user with hashtag search
<c:forEach items="${fbposts}" var="post">
	<tr>
		<td>${post.get("message")}</td>
	</tr>
</c:forEach>
</p>

<p> Instagram posts from specific user.
<tr>
	<td>Instagram Picture - ${instauser.getFullName()}</td>
</tr>
</p>

</body>
</html>

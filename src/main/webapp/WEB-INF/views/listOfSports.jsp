<%--
  Created by IntelliJ IDEA.
  User: chand
  Date: 8/8/2017
  Time: 6:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sports</title>
</head>
<body>
<h1> ${sportlist}</h1>
<a href="/listevents">Soccer</a>
<form action="/addevent">
    <input type="submit" value="Add New Event">
</form>
<form action="/">
    <input type="submit" value="Back to Home">
</form>
</body>
</html>

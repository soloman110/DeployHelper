<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
		<tiles:insertAttribute name="header" />
		<%-- <tiles:insertAttribute name="content"/> --%>
		<tiles:insertAttribute name="footer" />
		<tiles:insertAttribute name="body" />
</body>
</html>
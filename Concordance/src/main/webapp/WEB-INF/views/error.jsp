<%@taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@page session="false"%>
<html>
<head>
<title>Error uploading file</title>
</head>
<body>

<s:url value="/" var="base_url" htmlEscape="true" />

<p>An error occurred while upload file <b>${filename}</b>.
<p>Try uploading <a href="${base_url}">again</a>

</body>
</html>
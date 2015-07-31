<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="false"%>
<html>
<head>
<title>File upload</title>
</head>
<body>

<p>Upload a text document written in English and this application will generate a concordance of the document.

<form:form method="POST" action="upload" enctype="multipart/form-data">
<input type="file" name="file">
<input type="submit" value="Upload">
</form:form>

</body>
</html>
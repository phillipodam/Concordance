<%@taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@page session="false"%>
<s:url value="/" var="base_url" htmlEscape="true" />
<html>
<head>
<title>File uploaded</title>
<script type="text/javascript" src="${base_url}resources/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
$(scheduleConcordanceCheck);

function scheduleConcordanceCheck() {
	window.setTimeout(getConcordance, 10000);
}

function getConcordance() {
	$.getJSON("${base_url}concordance/${id}", function (data) {
		$("#wait").hide();
		if (data.hasOwnProperty('concordance')) {
			$("#concordance").append("<table><tbody></tbody></table>");
			$.each(data.concordance, function (i, val) {
				$("#concordance table tbody").append("<tr><td>")
				.append(i + 1 + ".")
				.append("</td><td>")
				.append(val.word)
				.append("</td><td>")
				.append("{" + val.occurrence + ":" + val.appearances.join() + "}")
				.append("</td></tr>");
			});
		} else {
			$("#concordance").html("<span style=\"font-weight: bold; color: red\">" + data.error + "</span>");
		}
		$("#concordance").show();
	}).fail(scheduleConcordanceCheck);
}
</script>
</head>
<body>

<p>Successfully uploaded <b>${filename}</b>.
<div id="wait">Please wait...</div>
<div id="concordance" style="display:none"></div>
<p>Finished reviewing concordance or tired of waiting? <a href="${base_url}">upload another file</a>.

</body>
</html>
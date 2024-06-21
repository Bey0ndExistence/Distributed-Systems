<%@ page import="beans.StudentBean" %>
<%@ page import="java.util.List" %>
<html xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
	<title>Informatii student</title>
	<meta charset="UTF-8" />
</head>
<body>
<h3>Informatii student</h3>

<!-- populare bean cu informatii din cererea HTTP -->

<!-- folosirea bean-ului pentru afisarea informatiilor -->
<p>Urmatoarele informatii au fost introduse:</p>
<form action="json-student" method="get">
	<button type="submit" name="submit">GET JSON</button>
</form>
<form action="./search-student" method="post">
	<select name="selected">
		<option value="nume">Nume</option>
		<option value="prenume">Prenume</option>
		<option value="varsta">Varsta</option>
	</select>
	<input name="valoare" type="text">
	<button type="submit" name="submit" >Cauta</button>
</form>
<% List<StudentBean> studentBeanList = (List<StudentBean>)request.getAttribute("studentBeanList");
	for (StudentBean student : studentBeanList) {
		if (student == null) {
			continue;
		}
		int studentId = student.getId();
%>
<li>
	ID: <%=studentId%>
	<ul>
		<li>Nume: <%=student.getNume()%></li>
		<li>Preume: <%=student.getPrenume()%></li>
		<li>Varsta: <%=student.getVarsta()%></li>
	</ul>
</li>
<form action="./update-student" method="get">
	<input type="hidden" name="studentId" value="<%=studentId%>">
	<button type="submit" name="submit">Actualizeaza</button>
</form>
<form action="./delete-student" method="post">
	<input type="hidden" name="studentId" value="<%=studentId%>">
	<button type="submit" name="submit" >Sterge</button>
</form>

<% } %>
</body>
</html>
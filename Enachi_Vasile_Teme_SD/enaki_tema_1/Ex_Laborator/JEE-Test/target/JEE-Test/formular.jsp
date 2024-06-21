<%@ page import="java.io.File" %>
<%@ page import="com.fasterxml.jackson.dataformat.xml.XmlMapper" %>
<%@ page import="beans.StudentBean" %>
<%@ page import="java.io.FileWriter" %>
<%@ page import="javax.validation.constraints.Null" %>
<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
		<title>Formular student</title>
		<meta charset="UTF-8" />
	</head>
	<body>
		<h3>Formular student</h3>
		Introduceti datele despre student:
		<form action="./process-student" method="post">
			<%File file = new File("/home/enaki/Documents/TUIasi/SD/Laboratoare/Lab_1/Ex_Laborator/JEE-Test/student.xml");
				if (!file.exists() || request.getParameter("submit")==null) { %>
					Nume: <input type="text" name="nume"/>
					<br />
					Prenume: <input type="text" name="prenume" />
					<br />
					Varsta: <input type="number" name="varsta" />
					<br />
				<%} else {
						XmlMapper xmlMapper = new XmlMapper();
						StudentBean bean = xmlMapper.readValue(file, StudentBean.class);
				%>
					Nume: <input type="text" name="nume" value=<%=bean.getNume()%> />
					<br />
					Prenume: <input type="text" name="prenume" value=<%=bean.getPrenume()%> />
					<br />
					Varsta: <input type="number" name="varsta" value=<%=Integer.toString(bean.getVarsta())%> />
					<br />
				<%}%>

			<br />
			<button type="submit" name="submit" >Trimite</button>
		</form>
	</body>
</html>
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
    <p>Urmatoarele informatii au fost introduse:</p>
    <ul>
        <% List<StudentBean> studentBeanList = (List<StudentBean>)request.getAttribute("studentList");
            for (StudentBean student : studentBeanList) {
                if (student == null) {
                    continue;
                }
        %>
        <li>

            <ul>
                <li>Nume: <%=student.getNume()%></li>
                <li>Preume: <%=student.getPrenume()%></li>
                <li>Varsta: <%=student.getVarsta()%></li>
                <li>An Nastere: <%=student.getAnNastere()%></li>
            </ul>
        </li>
        <% } %>
    </ul>
    </body>
	<form action="./read-student" method="post">
            <label for="searchType">Select search type:</label>
            <select name="searchType" id="searchType">
                <option value="nume">Nume</option>
                <option value="prenume">Prenume</option>
                <option value="varsta">Varsta</option>
            </select>
            <input type="text" name="searchQuery" placeholder="Enter search query">
            <button type="submit">Search</button>
        </form>

      <form action="./delete" method="post">
                  <label for="deleteType">Select delete type:</label>
                  <select name="deleteType" id="deleteType">
                      <option value="nume">Nume</option>
                      <option value="prenume">Prenume</option>
                      <option value="varsta">Varsta</option>
                  </select>
                  <input type="text" name="deleteQuery" placeholder="Enter delete query">
                  <button type="submit">Delete</button>
     </form>

	 <form action="formular.jsp">
                    <button type="submit">Enter a new student</button>
      </form>

      <form action="update-student.jsp" method="post">
          <label for="studentNameToUpdate">Enter student name to update:</label>
          <input type="text" name="studentNameToUpdate" id="studentNameToUpdate" required>
          <button type="submit">Submit</button>
      </form>

</html>
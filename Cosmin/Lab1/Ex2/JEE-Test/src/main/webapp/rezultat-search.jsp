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

<% } %>
</body>
</html>
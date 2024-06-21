<%@ page import="beans.StudentBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <title>Title</title>
</head>
<body>
<jsp:useBean id="studentBean" class="beans.StudentBean" scope="request" />
<jsp:setProperty name="studentBean" property="id" value='<%= request.getAttribute("studentId") %>'/>
<jsp:setProperty name="studentBean" property="nume" value='<%= request.getAttribute("nume") %>'/>
<jsp:setProperty name="studentBean" property="prenume" value='<%= request.getAttribute("prenume") %>'/>
<jsp:setProperty name="studentBean" property="varsta" value='<%= request.getAttribute("varsta") %>'/>
<form action="./update-student" method="post">
    <input type="hidden" name="studentId" value="<%=studentBean.getId()%>">
    Nume: <input type="text" name="nume" value='<%= studentBean.getNume() %>'/>
    <br />
    Prenume: <input type="text" name="prenume" value='<%= studentBean.getPrenume() %>'/>
    <br />
    Varsta: <input type="number" name="varsta" value='<%= studentBean.getVarsta() %>'/>
    <br />
    <br />
    <button type="submit" name="submit">Trimite</button>
</form>
</body>
</html>

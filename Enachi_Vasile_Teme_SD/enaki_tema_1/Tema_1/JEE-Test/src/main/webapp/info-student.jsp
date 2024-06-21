<%@ page import="java.io.File" %>
<%@ page import="com.fasterxml.jackson.dataformat.xml.XmlMapper" %>
<%@ page import="beans.StudentList" %>
<%@ page import="Utilities.StudentHtmlGenerator" %>
<%@ page import="beans.StudentBean" %>
<%@ page import="java.io.PrintWriter" %>
<html xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <title>Informatii student</title>
</head>
<body>
<h3>Informatii student</h3>
<!-- populare bean cu informatii din cererea HTTP -->

<p>Urmatoarele informatii au fost introduse:</p>
    <%
        File file = new File("/home/enaki/Documents/TUIasi/SD/Laboratoare/Lab_1/Tema_1/JEE-Test/student_list.xml");
        // se returneaza un raspuns HTTP de tip 404 in cazul in care nu se gaseste fisierul cu date
        if (!file.exists()) {
            //response.sendError(404, "Nu a fost gasit niciun student serializat pe disc!");
            response.getWriter().println("<html><body>" +
                    "Nu exista fisierul student_list.xml<br />" +
                    "<a href=\".\">Inapoi</a>" +
                    "</body></html>");
            return;
        }
        XmlMapper xmlMapper = new XmlMapper();
        StudentList student_list = xmlMapper.readValue(file, StudentList.class);
        StringBuilder syntax = new StringBuilder();
        StudentHtmlGenerator htmlGenerator = new StudentHtmlGenerator();
        for (StudentBean student : student_list.getStudent_list()){
            syntax.append(htmlGenerator.serialize(student));
        }
        syntax.append("<a href=\".\">Inapoi</a>");
        out.println(syntax.toString());
    %>
</body>
</html>
<!-- update-student.jsp -->

<%@ page import="beans.StudentBean" %>

<html xmlns:jsp="http://java.sun.com/JSP/Page">
    <head>
        <title>Update Student</title>
        <meta charset="UTF-8" />
    </head>
    <body>
        <h3>Update Student</h3>

        <% String studentNameToUpdate = request.getParameter("studentNameToUpdate"); %>

        <!-- Form to update the student information -->
        <form action="./update-student" method="post">

            <input type="hidden" name="studentNameToUpdate" value="<%= studentNameToUpdate %>">

            <label for="nume">Nume:</label>
            <input type="text" name="nume" value="">

            <label for="prenume">Prenume:</label>
            <input type="text" name="prenume" value="">

            <label for="varsta">Varsta:</label>
            <input type="text" name="varsta" value="">

            <button type="submit">Submit</button>
        </form>
    </body>
</html>

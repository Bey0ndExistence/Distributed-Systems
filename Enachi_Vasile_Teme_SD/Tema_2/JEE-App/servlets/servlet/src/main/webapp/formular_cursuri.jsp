<%@ page import="javax.persistence.EntityManagerFactory" %>
<%@ page import="javax.persistence.Persistence" %>
<%@ page import="javax.persistence.EntityManager" %>
<%@ page import="ejb.StudentEntity" %>
<%@ page import="javax.persistence.TypedQuery" %>
<%@ page import="java.util.List" %>
<%@ page import="ejb.CourseEntity" %>
<html xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <title>Formular cursuri</title>
    <meta charset="UTF-8" />
</head>
<body>
<h3>Formular cursuri</h3>
Introduceti datele despre cursuri:
<form action="./process-curs" method="post">
    <fieldset label="operatiuni">
        <legend>Alegeti operatiunea dorita:</legend>
        <select name="operation">
            <option value="add">Adaugare</option>
            <option value="modify">Modificare</option>
            <option value="delete">Stergere</option>
        </select>
        <br />
        <br />
        ID: <input type="number" name="id" />
        <br />
        Denumire: <input type="text" name="denumire" />
        <br />
        Numar Credite: <input type="number" name="nr_credite" />
        <br />
        <br />
        <button type="submit" name="submit">Trimite</button>
    </fieldset>
    <br />
    <br />
    <a href='./'>Inapoi la meniul principal</a>
    <br />
    <br />
    <h2>Lista Cursuri</h2>
    <table border='1'>
        <thead>
        <tr>
            <th>ID</th>
            <th>Denumire</th>
            <th>Nr Credite</th>
        </tr>
        </thead>
        <tbody>
        <%
            EntityManagerFactory factory =   Persistence.createEntityManagerFactory("CursuriSQL");
            EntityManager em = factory.createEntityManager();

            StringBuilder responseText = new StringBuilder();

            // preluare date studenti din baza de date
            TypedQuery<CourseEntity> query = em.createQuery("select course from CourseEntity course", CourseEntity.class);
            List<CourseEntity> results = query.getResultList();
            for (CourseEntity course : results) {
                // se creeaza cate un rand de tabel HTML pentru fiecare student gasit
                out.print("<tr><td>" + course.getId() + "</td><td>" +
                        course.getDenumire() + "</td><td>" + course.getNr_credite() +
                        "</td></tr>");
            }
            // inchidere EntityManager
            em.close();
            factory.close();
            // trimitere raspuns la client
        %>
        </tbody></table><br /><br />
</form>
</body>
</html>
<html xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <title>Formular student</title>
    <meta charset="UTF-8" />
</head>
<body>
<h3>Formular student</h3>
Introduceti datele despre student:
<form action="./process-student" method="post">
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
        Nume: <input type="text" name="nume" />
        <br />
        Prenume: <input type="text" name="prenume" />
        <br />
        Varsta: <input type="number" name="varsta" />
        <br />
        <br />
        <button type="submit" name="submit">Trimite</button>
    </fieldset>
    <br />
    <br />
    <a href='./'>Inapoi la meniul principal</a>
</form>
</body>
</html>
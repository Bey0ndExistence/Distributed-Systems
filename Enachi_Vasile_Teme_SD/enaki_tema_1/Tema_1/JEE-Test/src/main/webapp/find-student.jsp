<html xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <title>Formular student</title>
    <meta charset="UTF-8">
</head>
<body>
<h3>Formular student</h3>
Cautare dupa nume:
<form action="./find-student" method="post">
    <input type="hidden" name="find" value="nume">
    Nume: <input type="text" name="nume" />
    <br />
    <br />
    <button type="submit" name="submit">Cauta</button>
</form>

<br />
Cautare dupa prenume:
<form action="./find-student" method="post">
    <input type="hidden" name="find" value="prenume">
    Prenume: <input type="text" name="prenume" />
    <br />
    <br />
    <button type="submit" name="submit">Cauta</button>
</form>

Cautare dupa varsta:
<form action="./find-student" method="post">
    <input type="hidden" name="find" value="varsta">
    Varsta: <input type="text" name="varsta" />
    <br />
    <br />
    <button type="submit" name="submit">Cauta</button>
</form>

</body>
</html>
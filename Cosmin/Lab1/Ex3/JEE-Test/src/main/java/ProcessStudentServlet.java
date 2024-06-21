import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class ProcessStudentServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // se citesc parametrii din cererea de tip POST
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/home/student/Documents/sd-1311a-homeworks-CosminPanciuc/Lab1/Ex3/JEE-Test/src/main/resources/database.db");
            //connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS \"Storage\" (\n" +
                            "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "\t\"nume\"\tTEXT,\n" +
                            "\t\"prenume\"\tTEXT,\n" +
                            "\t\"varsta\"\tINTEGER\n" +
                            ");"
                    );
            stmt.executeUpdate("INSERT INTO Storage (id, nume, prenume, varsta) VALUES (NULL, '" + nume + "', '" + prenume + "', " + varsta + ");");
            stmt.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Sql error");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.getRequestDispatcher("./index.jsp").forward(request, response);
    }
}
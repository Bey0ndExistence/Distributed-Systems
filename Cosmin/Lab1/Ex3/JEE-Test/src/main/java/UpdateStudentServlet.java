import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<StudentBean> studentBeanList = new ArrayList<>();
        String nume = null;
        String prenume = null;
        int varsta = 0;
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        Connection connection;
        try {

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/home/student/Documents/sd-1311a-homeworks-CosminPanciuc/Lab1/Ex3/JEE-Test/src/main/resources/database.db");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Storage WHERE id = " + studentId + ";");
            while(rs.next()){
                nume = rs.getString("nume");
                prenume = rs.getString("prenume");
                varsta = rs.getInt("varsta");
            }
            stmt.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Sql error");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("studentId",studentId);
        request.setAttribute("nume", nume);
        request.setAttribute("prenume", prenume);
        request.setAttribute("varsta", varsta);

        request.getRequestDispatcher("./update-student.jsp").forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // se citesc parametrii din cererea de tip POST
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/home/student/Documents/sd-1311a-homeworks-CosminPanciuc/Lab1/Ex3/JEE-Test/src/main/resources/database.db");
            //connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("UPDATE Storage SET nume = '" + nume + "', prenume = '" + prenume + "', varsta = " + varsta + " WHERE id = " + studentId
            );
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

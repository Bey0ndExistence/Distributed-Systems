import beans.StudentBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // deserializare student din fisierul XML de pe disc
        String selected = request.getParameter("selected");
        String valoare = request.getParameter("valoare");

        List<StudentBean> studentBeanList = new ArrayList<>();
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/home/student/Documents/sd-1311a-homeworks-CosminPanciuc/Lab1/Ex3/JEE-Test/src/main/resources/database.db");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Storage WHERE " + selected + " = '" + valoare + "' ;");
            while(rs.next()){
                StudentBean bean = new StudentBean();
                bean.setId(rs.getInt("id"));
                bean.setNume(rs.getString("nume"));
                bean.setPrenume(rs.getString("prenume"));
                bean.setVarsta(rs.getInt("varsta"));
                studentBeanList.add(bean);

            }
            stmt.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Sql error");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("studentBeanList", studentBeanList);


        // redirectionare date catre pagina de afisare a informatiilor studentului
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }
}
import beans.StudentBean;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JSONDatabaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/home/student/Documents/sd-1311a-homeworks-CosminPanciuc/Lab1/Ex3/JEE-Test/src/main/resources/database.db");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Storage;");
            List<StudentBean> studentBeanList = new ArrayList<>();
            while (rs.next()) {
                StudentBean bean = new StudentBean();
                bean.setId(rs.getInt("id"));
                bean.setNume(rs.getString("nume"));
                bean.setPrenume(rs.getString("prenume"));
                bean.setVarsta(rs.getInt("varsta"));
                studentBeanList.add(bean);
            }

            Gson gson = new Gson();
            String json = gson.toJson(studentBeanList);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            out.print(json);
            out.flush();

        } catch (ClassNotFoundException e) {
            System.out.println("Sql error");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

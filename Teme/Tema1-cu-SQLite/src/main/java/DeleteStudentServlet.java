import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.sql.*;

public class DeleteStudentServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:sqlite:/home/student/1306B/Molo/JEE-Test-Tema-SQLite/src/main/database.db";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String deleteType = request.getParameter("deleteType");
        String deleteQuery = request.getParameter("deleteQuery");

        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {

            deleteStudentsFromDatabase(connection, deleteType, deleteQuery);

            List<StudentBean> studentList = readStudentListFromDatabase(connection);

            request.setAttribute("studentList", studentList);

            request.getRequestDispatcher("./info-student.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteStudentsFromDatabase(Connection connection, String deleteType, String deleteQuery) throws SQLException {
        String deleteSql;
        switch (deleteType) {
            case "nume":
                deleteSql = "DELETE FROM students WHERE nume=?";
                break;
            case "prenume":
                deleteSql = "DELETE FROM students WHERE prenume=?";
                break;
            case "varsta":
                deleteSql = "DELETE FROM students WHERE varsta=?";
                break;
            default:
                return;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setString(1, deleteQuery);
            pstmt.executeUpdate();
        }
    }
    private List<StudentBean> readStudentListFromDatabase(Connection connection) throws SQLException {
        List<StudentBean> studentList = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                StudentBean student = new StudentBean();
                student.setNume(resultSet.getString("nume"));
                student.setPrenume(resultSet.getString("prenume"));
                student.setVarsta(resultSet.getInt("varsta"));
                student.setAnNastere(resultSet.getInt("an_nastere"));
                studentList.add(student);
            }
        }

        return studentList;
    }
}

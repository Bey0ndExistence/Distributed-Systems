import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class UpdateStudentServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:sqlite:/home/student/1306B/Molo/JEE-Test-Tema-SQLite/src/main/database.db";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentNameToUpdate = request.getParameter("studentNameToUpdate");
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));

        int anCurent = Year.now().getValue();
        int anNastere = anCurent - varsta;

        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            // Update the student information in the database
            String updateSql = "UPDATE students SET nume=?, prenume=?, varsta=?, an_nastere=? WHERE nume=?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
                pstmt.setString(1, nume);
                pstmt.setString(2, prenume);
                pstmt.setInt(3, varsta);
                pstmt.setInt(4, anNastere);
                pstmt.setString(5, studentNameToUpdate);
                pstmt.executeUpdate();
            }
            List<StudentBean> studentList = readStudentListFromDatabase(connection);

            request.setAttribute("studentList", studentList);

            request.getRequestDispatcher("/info-student.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
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

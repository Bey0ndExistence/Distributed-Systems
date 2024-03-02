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
import java.sql.*;

public class ReadStudentServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:sqlite:/home/student/1306B/Molo/JEE-Test-Tema-SQLite/src/main/database.db";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Initialize the SQLite database connection
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            // Create a statement to retrieve all students from the database
            String sql = "SELECT * FROM students";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                ResultSet resultSet = pstmt.executeQuery();

                // Process the result set and create a list of StudentBeans
                List<StudentBean> studentList = new ArrayList<>();
                while (resultSet.next()) {
                    StudentBean student = new StudentBean();
                    student.setNume(resultSet.getString("nume"));
                    student.setPrenume(resultSet.getString("prenume"));
                    student.setVarsta(resultSet.getInt("varsta"));
                    student.setAnNastere(resultSet.getInt("an_nastere"));
                    studentList.add(student);
                }

                // Set the studentList attribute in the request
                request.setAttribute("studentList", studentList);

                // Forward to the info-student.jsp
                request.getRequestDispatcher("/info-student.jsp").forward(request, response);
            }

        } catch (SQLException e) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQueryType = request.getParameter("searchType");
        String searchQuery = request.getParameter("searchQuery");

        List<StudentBean> searchResults = new ArrayList<>();
        int ok = 0;
        if (searchQueryType != null && !searchQueryType.isEmpty() && searchQuery != null && !searchQuery.isEmpty()) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
                searchResults = searchStudents(connection, searchQueryType, searchQuery);
                ok = 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ok == 0) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
                searchResults = readStudentListFromDatabase(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        request.setAttribute("studentList", searchResults);

        request.getRequestDispatcher("./info-student.jsp").forward(request, response);

    }
    private List<StudentBean> searchStudents(Connection connection, String searchQueryType, String searchQuery) throws SQLException {
        List<StudentBean> searchResults = new ArrayList<>();
        String sql = "";

        switch (searchQueryType) {
            case "nume":
                sql = "SELECT * FROM students WHERE nume = ?";
                break;

            case "prenume":
                sql = "SELECT * FROM students WHERE prenume = ?";
                break;

            case "varsta":
                sql = "SELECT * FROM students WHERE varsta = ?";
                break;

            default:
                // invalid input somehow
                break;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, searchQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                StudentBean student = new StudentBean();
                student.setNume(resultSet.getString("nume"));
                student.setPrenume(resultSet.getString("prenume"));
                student.setVarsta(resultSet.getInt("varsta"));
                student.setAnNastere(resultSet.getInt("an_nastere"));
                searchResults.add(student);
            }
        }

        return searchResults;
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

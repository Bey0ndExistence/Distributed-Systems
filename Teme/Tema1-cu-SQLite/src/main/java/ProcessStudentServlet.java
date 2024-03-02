import beans.StudentBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

public class ProcessStudentServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:sqlite:/home/student/1306B/Molo/JEE-Test-Tema-SQLite/src/main/database.db";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // se citesc parametrii din cererea de tip POST
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));

        /*
        procesarea datelor - calcularea anului nasterii
         */
        int anCurent = Year.now().getValue();
        int anNastere = anCurent - varsta;

        // Initialize the SQLite database connection
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            connection.setAutoCommit(false);
            createStudentsTable(connection);
            System.out.println("merge frate baza de date");
            // Insert the new student into the database
            String insertSql = "INSERT INTO students (nume, prenume, varsta, an_nastere) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                pstmt.setString(1, nume);
                pstmt.setString(2, prenume);
                pstmt.setInt(3, varsta);
                pstmt.setInt(4, anNastere);
                pstmt.executeUpdate();
            }
            connection.commit();  // Commit the transaction
            // Retrieve the updated list of students from the database
            List<StudentBean> studentList = readStudentListFromDatabase(connection);

            // Set the studentList attribute in the request
            request.setAttribute("studentList", studentList);

            request.getRequestDispatcher("./info-student.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createStudentsTable(Connection connection) throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nume TEXT NOT NULL, "
                + "prenume TEXT NOT NULL, "
                + "varsta INTEGER NOT NULL, "
                + "an_nastere INTEGER NOT NULL)";

        try (PreparedStatement pstmt = connection.prepareStatement(createTableSql)) {
            pstmt.executeUpdate();
            System.out.println("s-a creat tabela student");
        }
    }
    private List<StudentBean> readStudentListFromDatabase(Connection connection) throws SQLException {
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
            return studentList;
        }
    }
}

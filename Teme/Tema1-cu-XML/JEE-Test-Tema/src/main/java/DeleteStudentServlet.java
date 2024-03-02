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

public class DeleteStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String deleteType = request.getParameter("deleteType");
        String deleteQuery = request.getParameter("deleteQuery");

        List<StudentBean> studentList = readStudentListFromXML();

        // iterate through the list of students to find and remove the matching student
        Iterator<StudentBean> iterator = studentList.iterator();
        while (iterator.hasNext()) {
            StudentBean student = iterator.next();
            if (matchesDeleteCriteria(student, deleteType, deleteQuery)) {
                iterator.remove(); // remove the student if it matches the criteria
            }
        }

        // write the updated list of students back to XML
        writeStudentListToXML(studentList);

        request.setAttribute("studentList", studentList);

        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }

    private boolean matchesDeleteCriteria(StudentBean student, String deleteType, String deleteQuery) {
        switch (deleteType) {
            case "nume":
                return student.getNume().equalsIgnoreCase(deleteQuery);
            case "prenume":
                return student.getPrenume().equalsIgnoreCase(deleteQuery);
            case "varsta":
                return String.valueOf(student.getVarsta()).equalsIgnoreCase(deleteQuery);
            default:
                return false;
        }
    }

    private List<StudentBean> readStudentListFromXML() throws IOException {
        File file = new File("/home/student/1306B/Molo/student.xml");
        List<StudentBean> studentList;
        if (file.exists()) {
            XmlMapper xmlMapper = new XmlMapper();
            studentList = xmlMapper.readValue(file, xmlMapper.getTypeFactory().constructCollectionType(List.class, StudentBean.class));
        } else {
            studentList = new ArrayList<>();
        }
        return studentList;
    }

    private void writeStudentListToXML(List<StudentBean> studentList) throws IOException {
        File file = new File("/home/student/1306B/Molo/student.xml");
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writeValue(file, studentList);
    }
}

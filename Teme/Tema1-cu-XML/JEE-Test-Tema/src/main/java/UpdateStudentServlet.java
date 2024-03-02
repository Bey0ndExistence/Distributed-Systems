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

public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentNameToUpdate = request.getParameter("studentNameToUpdate");

        List<StudentBean> studentList = readStudentListFromXML();

        StudentBean studentToUpdate = findStudentByName(studentList, studentNameToUpdate);

        if (studentToUpdate != null) {
            if(request.getParameter("nume")!=null)
                studentToUpdate.setNume(request.getParameter("nume"));
            if(request.getParameter("prenume")!=null)
                studentToUpdate.setPrenume(request.getParameter("prenume"));
            if(request.getParameter("varsta")!=null) {
                int varsta = Integer.parseInt(request.getParameter("varsta"));
                int anCurent = Year.now().getValue();
                int anNastere = anCurent - varsta;
                studentToUpdate.setAnNastere(anNastere);
                studentToUpdate.setVarsta(varsta);
            }
            // replace the old student information
            int index = studentList.indexOf(studentToUpdate);
            if (index != -1) { //if that student wasn't somehow found
                studentList.set(index, studentToUpdate);
            }

            XmlMapper mapper = new XmlMapper();
            mapper.writeValue(new File("/home/student/1306B/Molo/student.xml"), studentList);
            request.setAttribute("studentList", studentList);
            request.getRequestDispatcher("/info-student.jsp").forward(request, response);
        }
    }

    public static StudentBean findStudentByName(List<StudentBean> studentList, String nameToFind) {
        for (StudentBean student : studentList) {
            if (student != null && student.getNume() != null && student.getNume().equals(nameToFind)) {
                return student;
            }
        }
        return null;
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
}

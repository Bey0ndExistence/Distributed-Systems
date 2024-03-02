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

public class ProcessStudentServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
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

        // initializare serializator Jackson
        XmlMapper mapper = new XmlMapper();

        // deserializare lista de studenti din fisierul XML de pe disc
        List<StudentBean> studentList = readStudentListFromXML();

        // creare bean pentru noul student
        StudentBean newStudent = new StudentBean();
        newStudent.setNume(nume);
        newStudent.setPrenume(prenume);
        newStudent.setVarsta(varsta);
        newStudent.setAnNastere(anNastere);

        // adaugare noul student la lista
        studentList.add(newStudent);

        // serializare lista actualizata de studenti sub forma de string XML
        mapper.writeValue(new File("/home/student/1306B/Molo/student.xml"), studentList);
        
        request.setAttribute("studentList", studentList);
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
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
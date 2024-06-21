import beans.StudentBean;
import com.fasterxml.jackson.core.type.TypeReference;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // deserializare student din fisierul XML de pe disc
        File file = new File("/home/student/Lab1/student.xml");
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        if (!file.exists()) {
            response.sendError(404, "Nu a fost gasit niciun student serializat pe disc!");
            return;
        }

        XmlMapper mapper = new XmlMapper();
        List<StudentBean> studentBeanList = new ArrayList<>();
        if(file.exists()){
            studentBeanList = mapper.readValue(file, new TypeReference<List<StudentBean>>() {});
        }
        int newIndex = -1;
        for (int i = 0; i < studentBeanList.size(); i++) {
            if (studentBeanList.get(i).getId() == studentId) {
                newIndex = i;
                break;
            }
        }
        String nume = studentBeanList.get(newIndex).getNume();
        String prenume = studentBeanList.get(newIndex).getPrenume();
        int varsta = studentBeanList.get(newIndex).getVarsta();

        request.setAttribute("studentId",studentId);
        request.setAttribute("nume", nume);
        request.setAttribute("prenume", prenume);
        request.setAttribute("varsta", varsta);

        // redirectionare date catre pagina de afisare a informatiilor studentului
        request.getRequestDispatcher("./update-student.jsp").forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // se citesc parametrii din cererea de tip POST
        int studentId = Integer.parseInt(request.getParameter("studentId"));
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

        // creare bean si populare cu date
        File file = new File("/home/student/Lab1/student.xml");
        List<StudentBean> studentBeanList = new ArrayList<>();
        if(file.exists()){
            studentBeanList = mapper.readValue(file, new TypeReference<List<StudentBean>>() {});
        }
        int newIndex = -1;
        for (int i = 0; i < studentBeanList.size(); i++) {
            if (studentBeanList.get(i).getId() == studentId) {
                newIndex = i;
                break;
            }
        }

        studentBeanList.get(newIndex).setNume(nume);
        studentBeanList.get(newIndex).setPrenume(prenume);
        studentBeanList.get(newIndex).setVarsta(varsta);

        // serializare bean sub forma de string XML
        mapper.writeValue(file, studentBeanList);

        // se trimit datele primite si anul nasterii catre o alta pagina JSP pentru afisare
        request.setAttribute("studentBeanList",studentBeanList);
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }
}

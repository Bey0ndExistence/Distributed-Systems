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

        // creare bean si populare cu date
        File file = new File("/home/student/Lab1/student.xml");
        StudentBean bean = new StudentBean();
        List<StudentBean> studentBeanList = new ArrayList<>();
        if(file.exists()){
            studentBeanList = mapper.readValue(file, new TypeReference<List<StudentBean>>() {});
        }

        int tempid;
        if(studentBeanList.isEmpty()){
            tempid = 0;
        }else {
         tempid = studentBeanList.get(studentBeanList.size() - 1).getId() + 1;
        }
        bean.setId(tempid);
        bean.setNume(nume);
        bean.setPrenume(prenume);
        bean.setVarsta(varsta);
        studentBeanList.add(bean);

        // serializare bean sub forma de string XML
        mapper.writeValue(file, studentBeanList);

        // se trimit datele primite si anul nasterii catre o alta pagina JSP pentru afisare
        request.setAttribute("studentBeanList",studentBeanList);
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }
}
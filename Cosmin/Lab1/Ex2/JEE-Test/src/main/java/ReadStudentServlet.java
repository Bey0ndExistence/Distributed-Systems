import beans.StudentBean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // deserializare student din fisierul XML de pe disc
        File file = new File("/home/student/Lab1/student.xml");

        if (!file.exists()) {
            response.sendError(404, "Nu a fost gasit niciun student serializat pe disc!");
            return;
        }

        XmlMapper mapper = new XmlMapper();

        List<StudentBean> studentBeanList = new ArrayList<>();
        if(file.exists()){
            studentBeanList = mapper.readValue(file, new TypeReference<List<StudentBean>>() {});
        }
        request.setAttribute("studentBeanList", studentBeanList);

        // redirectionare date catre pagina de afisare a informatiilor studentului
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }
}

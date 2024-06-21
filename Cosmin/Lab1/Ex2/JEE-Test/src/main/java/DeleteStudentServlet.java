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
import java.util.Iterator;
import java.util.List;


public class DeleteStudentServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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

        Iterator<StudentBean> iterator = studentBeanList.iterator();
        while (iterator.hasNext()) {
            StudentBean student = iterator.next();
            if (student.getId() == studentId) {
                iterator.remove();
                break;
            }
        }

        mapper.writeValue(file, studentBeanList);

        request.getRequestDispatcher("./index.jsp").forward(request, response);
    }
}
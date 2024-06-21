import beans.StudentBean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SearchStudentServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String selected = request.getParameter("selected");
        String valoare = request.getParameter("valoare");
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
        Iterator<StudentBean> iterator = studentBeanList.iterator();
        List<StudentBean> rezList = new ArrayList<>();

        switch (selected){
            case "nume":{
                    while (iterator.hasNext()) {
                    StudentBean student = iterator.next();
                    if (Objects.equals(student.getNume(), valoare)) {
                        rezList.add(student);
                    }
                }
                break;
            }
            case "prenume":{
                while (iterator.hasNext()) {
                    StudentBean student = iterator.next();
                    if (Objects.equals(student.getPrenume(), valoare)) {
                        rezList.add(student);
                    }
                }
                break;
            }
            case "varsta":{
                while (iterator.hasNext()) {
                    StudentBean student = iterator.next();
                    if (student.getVarsta() == Integer.parseInt(valoare)) {
                        rezList.add(student);
                    }
                }
                break;
            }

        }
        request.setAttribute("studentBeanList", rezList);
        request.getRequestDispatcher("./rezultat-search.jsp").forward(request, response);
    }
}
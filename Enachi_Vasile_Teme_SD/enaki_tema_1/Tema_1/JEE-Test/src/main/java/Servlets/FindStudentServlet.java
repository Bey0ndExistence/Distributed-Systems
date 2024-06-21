package Servlets;

import Utilities.StudentHtmlGenerator;
import beans.StudentBean;
import beans.StudentList;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FindStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        // deserializare student din fisierul XML de pe disc
        File file = new File("/home/enaki/Documents/TUIasi/SD/Laboratoare/Lab_1/Tema_1/JEE-Test/student_list.xml");

        // se returneaza un raspuns HTTP de tip 404 in cazul in care nu se gaseste fisierul cu date
        if (!file.exists()) {
            //response.sendError(404, "Nu a fost gasit niciun student serializat pe disc!");
            response.getWriter().println("<html><body>" +
                    "Nu exista fisierul student_list.xml<br />" +
                    "<a href=\".\">Inapoi</a>" +
                    "</body></html>");
            return;
        }
        XmlMapper xmlMapper = new XmlMapper();
        StudentList student_list = xmlMapper.readValue(file, StudentList.class);
        String syntax = "<head>\n" +
                "<title>Informatii student</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h3>Informatii student</h3>";
        StudentHtmlGenerator htmlGenerator = new StudentHtmlGenerator();
        //MyLogger m = new MyLogger();
        //m.log(request.getParameter("find"));
        //m.log(request.getParameter("nume"));
        if (request.getParameter("find").equals("nume")){

            for (StudentBean student : student_list.getStudent_list()){
                if (student.getNume().equals(request.getParameter("nume"))){
                    syntax += htmlGenerator.serialize(student);
                }
            }
        } else if (request.getParameter("find").equals("prenume")){
            for (StudentBean student : student_list.getStudent_list()){
                if (student.getPrenume().equals(request.getParameter("prenume"))){
                    syntax += htmlGenerator.serialize(student);
                }
            }
        }
        else if (request.getParameter("find").equals("varsta")){
            Integer varsta;
            try {
                varsta = Integer.parseInt(request.getParameter("varsta"));
            }catch (NumberFormatException e){
                varsta = 0;
            }
            for (StudentBean student : student_list.getStudent_list()){
                if (student.getVarsta() == varsta){
                    syntax += htmlGenerator.serialize(student);
                }
            }
        }
        //for (StudentBean student : student_list.getStudent_list()){
        //    syntax += htmlGenerator.serialize(student);
        //}
        syntax += "<a href=\".\">Inapoi</a>";
        syntax += "</body>\n" +
                "</html>";
        PrintWriter writer = response.getWriter();
        writer.println(syntax);
    }
}
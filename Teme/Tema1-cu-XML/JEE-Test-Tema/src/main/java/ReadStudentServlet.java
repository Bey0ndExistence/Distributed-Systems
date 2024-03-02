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

public class ReadStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // deserializare student din fisierul XML de pe disc
        File file = new File("/home/student/1306B/Molo/student.xml");

        if (!file.exists()) {
            response.sendError(404, "Nu a fost gasit niciun student serializat pe disc!");
            return;
        }

        List<StudentBean> studentList = readStudentListFromXML();

        request.setAttribute("studentList", studentList);

        // redirectionare date catre pagina de afisare a informatiilor studentilor
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQueryType = request.getParameter("searchType");
        String searchQuery = request.getParameter("searchQuery");

        List<StudentBean> searchResults = new ArrayList<>();
        int ok=0;
        if (searchQueryType != null && !searchQueryType.isEmpty() && searchQuery != null && !searchQuery.isEmpty()) {

            List<StudentBean> studentList = readStudentListFromXML();

            switch (searchQueryType) {
                case "nume":
                    for (StudentBean student : studentList) {
                        if (student.getNume().equalsIgnoreCase(searchQuery)) {
                            searchResults.add(student);
                            ok=1;
                        }
                    }
                    break;

                case "prenume":
                    for (StudentBean student : studentList) {
                        if (student.getPrenume().equalsIgnoreCase(searchQuery)) {
                            searchResults.add(student);
                            ok=1;
                        }
                    }
                    break;

                case "varsta":
                        int targetVarsta = Integer.parseInt(searchQuery);
                        for (StudentBean student : studentList) {
                            if (student.getVarsta() == targetVarsta) {
                                searchResults.add(student);
                                ok=1;
                            }
                        }

                    break;

                default:
                    // invalid input somehow
                    break;
            }
        } else {
            // if no valid search query is provided
            searchResults = readStudentListFromXML();
        }
        if(ok==0){
            searchResults = readStudentListFromXML();
        }


        request.setAttribute("studentList", searchResults);

        // Forward to the JSP for displaying the results
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

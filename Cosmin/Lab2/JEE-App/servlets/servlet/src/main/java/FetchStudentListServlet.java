import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FetchStudentListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // pregatire EntityManager
        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        StringBuilder responseText = new StringBuilder();
        responseText.append("<h2>Lista studenti</h2>");
        responseText.append("<table border='1'><thead><tr><th>ID</th><th>Nume</th><th>Prenume</th><th>Varsta</th><th>Stare</th><th style=\"border: none;\"></th> <th style=\"border: none;\"></th></thead>");
        responseText.append("<tbody>");

        // preluare date studenti din baza de date
        TypedQuery<StudentEntity> query = em.createQuery("select student from StudentEntity student", StudentEntity.class);
        List<StudentEntity> results = query.getResultList();
        for (StudentEntity student : results) {
            // se creeaza cate un rand de tabel HTML pentru fiecare student gasit
            responseText.append("<tr><td>" + student.getId() + "</td><td>" +
                    student.getNume() + "</td><td>" + student.getPrenume() +
                    "</td><td>" + student.getVarsta() + "</td>");
            String placeholder;
            String color;
            switch (student.getValidat()){
                case 3: placeholder = "RolledBack";
                        color = "blue";
                        break;
                case 2: placeholder = "Pending";
                        color = "gray";
                        break;
                case 1: placeholder = "Valid";
                        color = "green";
                        break;
                case 0: placeholder = "Invalid";
                        color = "red";
                        break;
                default: placeholder = "Erroare de sistem";
                        color = "black";
                        break;
            }

            responseText.append("<td style=\"color:" + color + ";\">" + placeholder + "</td>");
            responseText.append("<td style=\"border: none;\"><form action=\"./update-student\" method=\"get\"><button type=\"submit\" name=\"id\" value=\"" + student.getId() + "\">Actualizare</button></form></td>");
            responseText.append("<td style=\"border: none;\"><form action=\"./delete-student\" method=\"post\"><button type=\"submit\" name=\"id\" value=\"" + student.getId() + "\">Stergere</button></form></td></tr>");
        }

        responseText.append("</tbody></table><br /><br /><a href='./'>Inapoi la meniul principal</a>");

        // inchidere EntityManager
        em.close();
        factory.close();

        // trimitere raspuns la client
        response.setContentType("text/html");
        response.getWriter().print(responseText.toString());
    }
}

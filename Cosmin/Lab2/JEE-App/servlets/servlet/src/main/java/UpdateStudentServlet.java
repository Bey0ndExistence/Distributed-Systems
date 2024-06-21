import ejb.StudentEntity;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;


public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        // pregatire EntityManager
        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        // preluare date studenti din baza de date
        TypedQuery<StudentEntity> query = em.createQuery("select student from StudentEntity student where student.id = :id", StudentEntity.class);
        query.setParameter("id", id);
        List<StudentEntity> results = query.getResultList();
        StringBuilder responseText = new StringBuilder();

        responseText.append("<h2>Update Student</h2>");
        for (StudentEntity student : results) {
            responseText.append("<form action=\"./update-student\" method=\"post\">");
            responseText.append(" <input type=\"hidden\" name=\"id\" value=\"" + student.getId() + "\"/>");
            responseText.append("Nume: <input type=\"text\" name=\"nume\" value=\""+ student.getNume()  +"\"/><br/>");
            responseText.append("Prenume: <input type=\"text\" name=\"prenume\" value=\""+ student.getPrenume()  +"\"/><br/>");
            responseText.append("Varsta: <input type=\"number\" name=\"varsta\" value=\""+ student.getVarsta()  +"\"/><br/>");
            responseText.append("<button type=\"submit\" name=\"submit\">Trimite</button></form>");
        }
        // inchidere EntityManager
        em.close();
        factory.close();

        // trimitere raspuns la client
        response.setContentType("text/html");
        response.getWriter().print(responseText.toString());
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nume = StringEscapeUtils.escapeHtml4(request.getParameter("nume"));
        String prenume = StringEscapeUtils.escapeHtml4(request.getParameter("prenume"));
        int varsta = Integer.parseInt(request.getParameter("varsta"));
        int id = Integer.parseInt(request.getParameter("id"));

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();


        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        StudentEntity student = em.find(StudentEntity.class, id);

        student.setNume(nume);
        student.setPrenume(prenume);
        student.setVarsta(varsta);
        student.setValidat(2);

        transaction.commit();

        em.close();
        factory.close();

        response.setContentType("text/html");
        response.getWriter().println("Datele au fost actualizate in baza de date." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a>");

    }
}

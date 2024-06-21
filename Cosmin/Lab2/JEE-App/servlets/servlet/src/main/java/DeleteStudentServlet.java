import ejb.DBHistory;
import ejb.StudentEntity;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DeleteStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        // pregatire EntityManager
        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        TypedQuery<StudentEntity> query = em.createQuery("select student from StudentEntity student where student.id = :id", StudentEntity.class);
        query.setParameter("id", id);
        List<StudentEntity> results = query.getResultList();
        em.remove(results.get(0));
        TypedQuery<DBHistory> queryHistory = em.createQuery("select history from DBHistory history where history.id = :id", DBHistory.class);
        queryHistory.setParameter("id", id);
        List<DBHistory> resultHistory = queryHistory.getResultList();
        em.remove(resultHistory.get(0));
        transaction.commit();

        // inchidere EntityManager
        em.close();
        factory.close();

        // trimitere raspuns la client
        response.setContentType("text/html");
        response.getWriter().println("Datele au fost actualizate in baza de date." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a>");
    }
}

import ejb.DBChecker;
import ejb.DBHistory;
import ejb.StudentEntity;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ThreadControlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder responseText = new StringBuilder();
        String checked = "";
        if(DBChecker.getRollback()){
            checked = "checked";
        }
        responseText.append("<h2>Optiuni thread</h2>");
        responseText.append("<form action=\"./thread-control\" method=\"post\">");
        responseText.append("Min: <input type=\"number\" name=\"min\" value=\""+ DBChecker.getMin() +"\"/><br/>");
        responseText.append("Max: <input type=\"number\" name=\"max\" value=\""+ DBChecker.getMax()  +"\"/><br/>");
        responseText.append("<input type=\"checkbox\" name=\"rollback\" value=\"1\" " + checked + "/>Rollback<br/>");
        responseText.append("<button type=\"submit\" name=\"submit\">Schimba</button></form>");

        response.setContentType("text/html");
        response.getWriter().print(responseText.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int min = Integer.parseInt(request.getParameter("min"));
        int max = Integer.parseInt(request.getParameter("max"));
        String checkboxValue = request.getParameter("rollback");
        if(checkboxValue != null){
            DBChecker.setRollback(true);
        }else{
            DBChecker.setRollback(false);
        }


        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        if(DBChecker.getMax() != max || DBChecker.getMin() != min){
            transaction.begin();

            TypedQuery<StudentEntity> query = em.createQuery("select student from StudentEntity student", StudentEntity.class);
            List<StudentEntity> results = query.getResultList();
            for (StudentEntity student : results){
                student.setValidat(2);
            }

            TypedQuery<DBHistory> queryHistory = em.createQuery("select history from DBHistory history", DBHistory.class);
            List<DBHistory> resultHistory = queryHistory.getResultList();
            for (DBHistory history : resultHistory) {
                em.remove(history);
            }
            transaction.commit();

            DBChecker.setMax(max);
            DBChecker.setMin(min);
        }




        em.close();
        factory.close();

        response.setContentType("text/html");
        response.getWriter().println("Valorile minime si maxime au fost actualizate." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a>");

    }
}

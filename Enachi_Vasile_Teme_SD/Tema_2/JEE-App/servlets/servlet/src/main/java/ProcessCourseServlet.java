import Utilities.MyLogger;
import ejb.CourseEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProcessCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // se citesc parametrii din cererea de tip POST
        String operation = request.getParameter("operation");
        String denumire = request.getParameter("denumire");
        String nr_credite = request.getParameter("nr_credite");
        String id_str = request.getParameter("id");
        (new MyLogger()).log("Denumire "+denumire+ "\nNr Credite: " + nr_credite + "\nID : " + id_str + "\nOption : " + operation);


        if (operation.equals("add")){
            if (denumire.equals("") || nr_credite.equals("") ){
                response.sendError(404, "Date introduse incorecte");
                return;
            }
            add_course(denumire, nr_credite);

        } else if (operation.equals("modify")){
            if (id_str.equals("")) {
                response.sendError(404, "Date introduse incorecte");
                return;
            }
            if (! update(denumire, nr_credite, id_str)){
                response.setContentType("text/html");
                response.getWriter().println("Studentul cu id-ul " + id_str + " nu exista in baza de date");
                return;
            };

        } else if (operation.equals("delete")){
            if (id_str.equals("")) {
                response.sendError(404, "Date introduse incorecte");
                return;
            }
            if (!delete(id_str)){
                response.setContentType("text/html");
                response.getWriter().println("Cursul cu id-ul " + id_str + " nu exista in baza de date");
                return;
            };
        }

        // trimitere raspuns inapoi la client
        response.setContentType("text/html");
        response.getWriter().println("Operatiunea s-a terminat cu succes." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a><br /><br />" +
                "<a href='./formular_cursuri.jsp'>Inapoi la formular</a>");
    }

    private boolean delete(String id_str) {
        int id = Integer.parseInt(id_str);

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("CursuriSQL");
        EntityManager em = factory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        CourseEntity course = em.find(CourseEntity.class, id);

        if (course == null){
            return false;
        }

        em.remove(course);
        transaction.commit();
        em.close();
        factory.close();
        return true;
    }

    private boolean update(String denumire, String nr_credite, String id_str) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("CursuriSQL");
        EntityManager em = factory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        int id = Integer.parseInt(id_str);
        CourseEntity course = em.find(CourseEntity.class, id);
        if (course == null){
            return false;
        }
        if (!denumire.equals("")){
            course.setDenumire(denumire);
        }
        if (!nr_credite.equals("")){
            course.setNr_credite(Integer.parseInt(nr_credite));
        }
        transaction.commit();
        em.close();
        factory.close();
        return true;
    }

    private boolean add_course(String denumire, String nr_credite) {
        // pregatire EntityManager
        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("CursuriSQL");
        EntityManager em = factory.createEntityManager();

        // creare entitate JPA si populare cu datele primite din formular
        int nr_credite_int = (nr_credite.equals("")) ? 0 : Integer.parseInt(nr_credite);

        CourseEntity course = new CourseEntity();
        course.setDenumire(denumire);
        course.setNr_credite(nr_credite_int);

        // adaugare entitate in baza de date (operatiune de persistenta)
        // se face intr-o tranzactie
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(course);
        transaction.commit();

        // inchidere EntityManager
        em.close();
        factory.close();
        return true;
    }
}

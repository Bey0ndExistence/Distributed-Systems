import Utilities.MyLogger;
import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProcessStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // se citesc parametrii din cererea de tip POST
        String operation = request.getParameter("operation");
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        String age = request.getParameter("varsta");
        String id_str = request.getParameter("id");
        (new MyLogger()).log("Nume "+nume+ "\nPrenume: " + prenume + "\nVarsta: " + age + "ID : " + id_str);


        if (operation.equals("add")){
            if (age.equals("") || nume.equals("") || prenume.equals("")){
                response.sendError(404, "Date introduse incorecte");
                return;
            }
            add_student(nume, prenume, age);

        } else if (operation.equals("modify")){
            if (id_str.equals("")) {
                response.sendError(404, "Date introduse incorecte");
                return;
            }
            if (! update(nume, prenume, age, id_str)){
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
                response.getWriter().println("Studentul cu id-ul " + id_str + " nu exista in baza de date");
                return;
            };
        }

        // trimitere raspuns inapoi la client
        response.setContentType("text/html");
        response.getWriter().println("Operatiunea s-a terminat cu succes." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a>");
    }

    private boolean delete(String id_str) {
        int id = Integer.parseInt(id_str);

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        StudentEntity student = em.find(StudentEntity.class, id);

        if (student == null){
            return false;
        }

        em.remove(student);
        transaction.commit();
        em.close();
        factory.close();
        return true;
    }

    private boolean update(String nume, String prenume, String age, String id_str) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        int id = Integer.parseInt(id_str);
        StudentEntity student = em.find(StudentEntity.class, id);
        if (student == null){
            return false;
        }
        if (!nume.equals("")){
            student.setNume(nume);
        }
        if (!prenume.equals("")){
            student.setPrenume(prenume);
        }
        if (!age.equals("")){
            int varsta = Integer.parseInt(age);
            student.setVarsta(varsta);
        }
        transaction.commit();
        em.close();
        factory.close();
        return true;
    }

    private boolean add_student(String nume, String prenume, String age) {
        // pregatire EntityManager
        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        // creare entitate JPA si populare cu datele primite din formular
        int varsta = (age.equals("")) ? 0 : Integer.parseInt(age);

        StudentEntity student = new StudentEntity();
        student.setNume(nume);
        student.setPrenume(prenume);
        student.setVarsta(varsta);

        // adaugare entitate in baza de date (operatiune de persistenta)
        // se face intr-o tranzactie
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(student);
        transaction.commit();

        // inchidere EntityManager
        em.close();
        factory.close();
        return true;
    }
}

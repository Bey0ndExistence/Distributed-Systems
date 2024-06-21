package ejb;

import sun.awt.X11.XSystemTrayPeer;

import javax.persistence.*;
import java.util.List;

public class DBChecker implements Runnable{
    private static int min = 10;
    private static int max = 20;
    private static boolean rollback;


    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()) {
            EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
            EntityManager em = factory.createEntityManager();

            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            TypedQuery<StudentEntity> query = em.createQuery("select student from StudentEntity student", StudentEntity.class);
            List<StudentEntity> results = query.getResultList();
            TypedQuery<DBHistory> queryHistory = em.createQuery("select history from DBHistory history", DBHistory.class);
            List<DBHistory> resultHistory = queryHistory.getResultList();

            for(StudentEntity student:results){
                if(student.getValidat() == 2){
                    if(student.getVarsta() >= min && student.getVarsta() <= max){
                        student.setValidat(1);
                        DBHistory dbHistory = new DBHistory();
                        dbHistory.setId(student.getId());
                        dbHistory.setNume(student.getNume());
                        dbHistory.setPrenume(student.getPrenume());
                        dbHistory.setVarsta(student.getVarsta());
                        em.persist(dbHistory);
                    }else {
                        student.setValidat(0);
                        if(rollback){
                            boolean found = false;
                            for (DBHistory history : resultHistory) {
                                if (history.getId() == student.getId()) {
                                    student.setNume(history.getNume());
                                    student.setPrenume(history.getPrenume());
                                    student.setVarsta(history.getVarsta());
                                    student.setValidat(3);
                                    found = true;
                                    break;
                                }
                            }
                            if(!found){
                                em.remove(student);
                            }
                        }
                    }
                }else if(student.getValidat() == 0 && rollback){
                    em.remove(student);
                }
            }
            transaction.commit();

            em.close();
            factory.close();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }

    public static int getMin() {
        return min;
    }

    public static void setMin(int min) {
        DBChecker.min = min;
    }

    public static int getMax() {
        return max;
    }

    public static void setMax(int max) {
        DBChecker.max = max;
    }
    public static boolean getRollback() {
        return rollback;
    }

    public static void setRollback(boolean rollback) {
        DBChecker.rollback = rollback;
    }
}

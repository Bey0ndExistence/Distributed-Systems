import ejb.DBChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

public class ThreadServletListener implements ServletContextListener {
    private Thread checkerThread;
    @Override
    public void contextInitialized(ServletContextEvent sce)  {
        DBChecker checker = new DBChecker();
        checkerThread = new Thread(checker);
        checkerThread.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (checkerThread != null && checkerThread.isAlive()) {
            checkerThread.interrupt();
            try {
                checkerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

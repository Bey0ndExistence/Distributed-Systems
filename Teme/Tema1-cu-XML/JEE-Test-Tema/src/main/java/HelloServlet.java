import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class HelloServlet extends HttpServlet
{   @Override
    protected void doGet(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse)
        throws ServletException, IOException {
            httpServletResponse.getWriter().print("Hellofromservlet");
    }
}
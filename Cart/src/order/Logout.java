package order;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logout extends HttpServlet
{
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		
		//Invalidating the user session and redirecting him/her to the login page
		
		arg0.getSession().invalidate();
		arg1.sendRedirect("http://localhost:8888/Cart/login.html");
	}
}

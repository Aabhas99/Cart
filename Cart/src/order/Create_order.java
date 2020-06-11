package order;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Create_order extends HttpServlet
{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try
		{
			//Calling the getWriter() method to print html content on the webpage
			PrintWriter pw=res.getWriter();
			
			//Making the database connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection co=DriverManager.getConnection("jdbc:mysql://localhost:3306/cart",
					"root", "abcd");
			Statement st=co.createStatement();
			
			//Fetching email and password from the request
			String email=req.getParameter("email");
			String password=req.getParameter("password");

			//Validating the user details
			ResultSet r=st.executeQuery("select email from account where email='"+email+"' and password='"+password+"'");
			
			//In case of wrong Credentials
			if(!r.next())
			{
				pw.print("Invalid details");
			}
			
			//In case of correct credentials
			else
			{
				//Creating session for the user
				HttpSession session=req.getSession();
				session.setAttribute("email",email);
				
				//Fetching inventory items
				r=st.executeQuery("select * from inventory");
				pw.println("<!DOCTYPE html>\r\n" + 
						"<html>\r\n" + 
						"<body>");
				
				//Displaying the inventory items
				while(r.next())
				{
					int id=r.getInt(1);
					int quantity=r.getInt(3);
					String name=r.getString(2);
					
					//If the quantity of any item is zero it will not be displayed
					if(quantity==0)continue;
					
					
					pw.println("<p>"+name+"</p>");
					
					//User can enter the quantity of the product he wants to buy
					//Html validation takes care that the quantity entered is within the range
					//After buying the items Order_done page opens
					pw.println("<br><form action='http://localhost:8888/Cart/order_done' method='post'"+
					  ">Enter Quantity:  <input type='number' id='quantity' name='quantity' min='1' max='"+quantity+"'>"
					  + "<input type='hidden' name='id' value='"+id+"'>"
					  		+ "<input type='hidden' name='email' value='"+email+"'><br>"
					  + "<input type='submit' value='Buy'>"+
					"</form><br>");
				}
				
				//Logout button
				pw.println("<br><br><form action='http://localhost:8888/Cart/logout' method='post'"+
						   "><input type='submit' value='Logout'>"+
						"</form>");
				pw.println("</body>\r\n" + 
						"</html>");
			}
		}
		catch(Exception e)
		{
			//In case of any error
			res.getWriter().print("Error");
		}
	}
}

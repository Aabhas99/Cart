package order;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Order_done extends HttpServlet
{
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		
		//Fetching the quantity and id of the item selected by the user and his/her email id
		int quantity =Integer.parseInt(arg0.getParameter("quantity"));
		int id =Integer.parseInt(arg0.getParameter("id"));
		String email =arg0.getParameter("email");

		
		PrintWriter pw=arg1.getWriter();
		pw.println("<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<body>");
		
		//getting status of the update
		int status=updateQuantity(id,quantity,email);
		
		//if the update takes place correctly
		if(status==1)
		{
			arg1.getWriter().print("Order recieved");
		}
		//if number of items in inventory are less
		else if(status==3)
		{
			arg1.getWriter().print("Required number of items are not there int the inventory");
		}
		//for any other error
		else
		{
			arg1.getWriter().print("Error Occured");
		}
		
		//logout button
		pw.println("<br><br><form action='http://localhost:8888/Cart/logout' method='post'"+
				   "><input type='submit' value='Logout'>"+
				"</form>");
	}
	public int updateQuantity(int id,int quantity,String email)
	{
		try
		{
			//Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection co=DriverManager.getConnection("jdbc:mysql://localhost:3306/cart",
					"root", "abcd");
			Statement st=co.createStatement();
			
			//Fetching the current quantity of the item from the database
			ResultSet r=st.executeQuery("select quantity from inventory where id="+id);
			r.next();
			int initial_quantity=r.getInt(1);
			
			//if number of items in inventory are less
			if(quantity>initial_quantity)
			{
				return 3;
			}
			//Updating the quantity of the item in the database
			st.executeUpdate("update inventory set quantity='"+(initial_quantity-quantity)+"'"
					+ "where id='"+id+"'");
			
			//getting the number of entries in orders table
			ResultSet rr=st.executeQuery("select count(*) from orders");
			rr.next();
			int size=1+rr.getInt(1);
			
			//list represents item id and the quantity of item ordered
			String list=id+" "+quantity;
			
			//Update Query
			st.executeUpdate("insert into orders values("+size+",'"+email+"','"+list+"')");
			co.close();
			
			//in case of successful update
			return 1;
		}
		catch(Exception e)
		{
			//In case of error
			System.out.println(e);
			return 2;
		}
	}
}

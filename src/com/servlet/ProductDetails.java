package com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecommerce.DBConnection;

/**
 * Servlet implementation class ProductDetails
 */
@WebServlet("/ProductDetails")
public class ProductDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			out.print("<htm><body>");
				
			InputStream in = getServletContext().getResourceAsStream("config.properties");
				
			//load DB information
			Properties props = new Properties();
			props.load(in);
				
			// set DB connection variables
			String url = props.getProperty("url");
			String user = props.getProperty("userid");
			String password = props.getProperty("password");
				
			// initialize DB connection
			DBConnection conn = new DBConnection(url, user, password);

			// create select statement
			Statement stmt = 
					conn.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			//make result set up on query
			String pid = request.getParameter("pid");
			String sql = "select * from eproduct where id = "+pid; 
			ResultSet rs = stmt.executeQuery(sql);
			
			int size = rs.getFetchSize();
			
			if (size==0)
				out.println("Given Product Id not found!");
			
			// show specific record
			while (rs.next()) {
				out.println("Product Id : "+ rs.getInt("ID")+"<br>");
				out.println("Product Name : "+ rs.getString("name")+"<br>");
				out.println("Product Price : "+ rs.getDouble("price")+"<br>");
				out.println("Product Price : "+ rs.getDate("date_added")+"<br>");
			}
			
			out.print("</body></html>");
			stmt.close();
			conn.closeConnection();
		} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

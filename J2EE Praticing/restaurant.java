package com.FoodApp.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/restaurant")
public class restaurant extends HttpServlet {
    
    String SQL = "select * from restaurant";
    Connection con;
    Statement st;
    ResultSet res;
    PrintWriter pw;
    
    @Override
    public void init() throws ServletException {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/app","root","root@dk");
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set the response content type to HTML
        resp.setContentType("text/html");
        pw = resp.getWriter();
        
        try {
            st = con.createStatement();
            res = st.executeQuery(SQL);
            
            // Output general heading
            pw.println("<h1>Welcome to Last Try</h1>");
            
            // Loop through results
            while(res.next()) {
                pw.println("<h2>Details about restaurant information</h2>");
                pw.println("<p><strong>Restaurant Name:</strong> " + res.getString("restaurantName") + "</p>");
                pw.println("<p><strong>Rating:</strong> " + res.getString("rating") + "</p>");
                pw.println("<p><strong>ETA:</strong> " + res.getString("eta") + "</p>");
                
                // Retrieve image as a byte array (BLOB)
                byte[] imageBytes = res.getBytes("imagePath");

                // If the image exists, convert it to Base64 and display it in an img tag
                if (imageBytes != null && imageBytes.length > 0) {
                    String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
                    pw.println("<img src='data:image/jpg;base64," + base64Image + "' alt='Restaurant Image' style='width:300px;height:200px;'/>");
                } else {
                    pw.println("<p>No image available</p>");
                }
                
                pw.println("<hr>");  // Add a horizontal line between records for better readability
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void destroy() {
        try {
            if (res != null) res.close();
            if (st != null) st.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

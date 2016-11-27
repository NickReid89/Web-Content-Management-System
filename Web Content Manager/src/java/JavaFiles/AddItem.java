/*
Author: Nickolas Reid
Date last modified: October 25th 2016
Purpose: The purpose of this servlet is to take in the parameters from index.jsp 
         specifically from the form named addBox and use those parameters to add
         a new item to the database.
            
*/
package JavaFiles;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//Tag for the JSP to know where to send the form to.
@WebServlet("/add")
// Limits the photo uploaded to 16 megabytes. This is useful to prevent slow down of 
// users waiting on a high quality photo to download.
@MultipartConfig(maxFileSize = 32354430) 

public class AddItem extends HttpServlet {

    // database connection settings
    private final Connection conn;
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb";
    private final String dbUser = "root";
    private final String dbPass = "";

    
    // Database connectivity done in the constructor to clean things up a bit.
    public AddItem() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // gets values of text fields
        String itemTitle = request.getParameter("itemTitle");
        String Price = request.getParameter("Price");
        String foodType = request.getParameter("menuItems");
        String description = request.getParameter("Description");

        InputStream inputStream = null;

        // obtain the upload file part in this multipart request
        Part filePart = request.getPart("Image");
        if (filePart != null) {
 
            // obtain the input stream of the photo
            inputStream = filePart.getInputStream();
        }

        //String message = null; 

        try {
            // construct the SQL statement
            String sql = "INSERT INTO menuitems (itemTitle, Price, Image, Description, foodType) values (?, ?, ?,?,?)";

            //Set up the prepared statement.
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, itemTitle);
            statement.setString(2, Price);

            //the photo exists.
            if (inputStream != null) {
                /*
                The photo must be stored as a blob as there is no "photo" option in the database
                */
                statement.setBlob(3, inputStream);
            }
            statement.setString(4, description);
            statement.setString(5, foodType);
            
            //Fire off the insert statement
            statement.executeUpdate();

            /*
            Insert ignore is special in that it tries to insert a value into the database. If the value that it 
            tries to insert exists it simply ignores it. If it is not there, then the query adds it. In this case
            this is the statement that creates a menu section
            */
            String updatePageList = " INSERT IGNORE INTO coresitedata(page) VALUES  (?)";
            statement = conn.prepareStatement(updatePageList);
            statement.setString(1, foodType);
            statement.executeUpdate();

        } catch (SQLException ex) {
            
         //   message = "ERROR: " + ex.getMessage();
        }
        // sets the message in request scope
       // request.setAttribute("Message", message);

        // Fires the page off to the home page
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}

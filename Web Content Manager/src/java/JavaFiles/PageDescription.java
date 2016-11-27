/*
author: Nickolas Reid
date last modified: Oct 25 2016

Purpose: The purpose of this file is to either create a new page to stick the 
         item description into, or create a new menu section and stick that item 
         in.

*/
package JavaFiles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//servlet identified
@WebServlet("/PageDescription")
public class PageDescription extends HttpServlet {
    // database connection settings
    private final Connection conn;
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb";
    private final String dbUser = "root";
    private final String dbPass = "";
    
    //Set up connection to database.
    public PageDescription() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Read in input from form.
        String description = request.getParameter("DescriptionInput");
        boolean results = false;
        try {
            //Set up SQL statement.
            String sql = "SELECT * FROM coresitedata WHERE page = '" + request.getParameter("menuItems") + "'";
            //create prepared statement.
            PreparedStatement statement = conn.prepareStatement(sql);
            //execute query.
            ResultSet rs = statement.executeQuery(sql);
            //Determine there are results 
            while (rs.next()) {                
                results = true;
            }
        } catch (SQLException sql) {
        }
        //If there are no results,
        if (!results) {
            try {

                String sql = "INSERT INTO coresitedata (description, Page) values (?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, description);
                statement.setString(2, request.getParameter("menuItems"));
                statement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(PageImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {

                String sql = "UPDATE coresitedata set description = ? WHERE page = '" + request.getParameter("menuItems") + "'";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, description);
                int row = statement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(PageImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // forwards to the message page
        response.sendRedirect(request.getContextPath());

    }
}

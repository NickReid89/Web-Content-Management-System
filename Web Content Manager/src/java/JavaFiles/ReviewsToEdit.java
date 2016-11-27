/*
Author: Nickolas Reid
Date last Modified: Oct 26 2016

Purpose: The purpose of this servlet is part of a 2 part process. The first part is that
         the client makes an ajax call here then this servlet grabs all of the reviews
         for that section and then proceeds to send the reviews in an HTML form back
         to the AJAX call for the admin to view.
 */
package JavaFiles;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/ReviewsToEdit")

public class ReviewsToEdit extends HttpServlet {

    //connection info
    private Connection conn;
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb";
    private final String dbUser = "root";
    private final String dbPass = "";

    private ResultSet rs;

    public String returnReviews(String menuItem) throws SQLException {

        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
        String returnResponse;
        boolean exists = false;
        try {
            String sql = "SELECT ID,RATING,NAME,REVIEW,DATE,TITLE FROM COMMENT WHERE itemID = ? ";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, menuItem);
            rs = stmt.executeQuery();

            returnResponse = "<table>";
            while (rs.next()) {
                exists = true;
                returnResponse += "<tr><td>Name: " + rs.getString("name") + "</td><td>Title: " + rs.getString("title") + "</td></tr>";
                returnResponse += "<tr><td>Date" + rs.getDate("date") + "</td><td>Rating: " + rs.getString("rating") + "</td></tr>";
                returnResponse += "<tr><td colspan='2'> Review: " + rs.getString("review") + "</td></tr>";
                returnResponse += "<tr border='0'><td colspan='2' border='0'><input type='button' name='" + menuItem + "' id='" + rs.getString("ID") + "' value='Delete!' onclick='RemoveReview(this.id, this.name)'></td></tr>";
                returnResponse += "<tr border='0'><td colspan='2' border='0'>&nbsp;</td></tr>";

            }
            if (exists == false) {
                returnResponse += "<tr><td><h3> There are no review for this item.</h3></tr></td>";
            }
            returnResponse += "</table>";
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReviewsToEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
            return returnResponse;
        } catch (SQLException ex) {
            Logger.getLogger(ReviewsToEdit.class.getName()).log(Level.SEVERE, null, ex);
            return "<h1> There was an error, please reload the page  </h1>";

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        if (request.getParameter("menuItem") != null) {
            try {
                String reviewItem = request.getParameter("menuItem");
                out.println(returnReviews(reviewItem));
            } catch (SQLException ex) {
                Logger.getLogger(ReviewsToEdit.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}

/*
Author: Nickolas Reid
Date last modified: Oct 26th 2016

Purpose:  The purpose of this servlet is to take in a parameter from the deletion 
            form in index.jsp. The form however first makes an AJAX call which then 
            calls this servlet. The servlet then checks to ensure there is no null
            value that could be disasterous. IF there is a value the servlet then 
            deletes that review and then makes a call to ReviewToEdit.java which 
            grabs all the reviews for a given section. This servlet then sends the
            review to the ajax call and finally to the user.
 */
package JavaFiles;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RemoveReview")
public class RemoveReview extends HttpServlet {

    //connection info
    private final Connection conn;
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb";
    private final String dbUser = "root";
    private final String dbPass = "";

    //connect to db in constructor.
    public RemoveReview() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        //Check to see if the input was empty to save the database. 
        if (request.getParameter("id") != null) {
            String[] params = request.getParameter("id").split(",");

            try {
                //SQL statement.
                String sql = "DELETE FROM COMMENT WHERE ID = ? ";

                PreparedStatement stmt = conn.prepareStatement(sql);
                //Set the value to delete
                stmt.setString(1, params[0]);
                //Delete it
                stmt.executeUpdate();
                //Delete the reviews associated with the value.
                out.println(new ReviewsToEdit().returnReviews(params[1]));
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReviewsToEdit.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RemoveReview.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

}

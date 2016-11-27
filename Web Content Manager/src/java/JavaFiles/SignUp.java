/*
Author: Nickolas Reid
Date last modified: Oct 26 2016

Purpose: The purpose of this servlet is to allow an administrator to sign up for an
         admin account. The jsp form sends the two parameters via post and then
         this servlet enters them in the database, after which the servlet redirects
         the admin to the login page to log in. This is so the admin can make sure 
         their username and password works. Otherwise something is worng.
*/

package JavaFiles;

import java.io.IOException;
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
import org.mindrot.jbcrypt.BCrypt;

//Web identifier
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Must test for nulls
        if (request.getParameter("username") != null && request.getParameter("password") != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn;
                conn = DriverManager.getConnection("jdbc:mysql://localhost/adminDB", "root", "");
                // constructs SQL statement
                String sql = "INSERT INTO admin (userName,password) values (?, ?)";

                String userPassword = request.getParameter("password");
                //Encrypt the password
                String generatedSecuredPasswordHash = BCrypt.hashpw(userPassword, BCrypt.gensalt(12));

                String userName = request.getParameter("username");
                //Encrypt the username
                String generatedSecuredUsernameHash = BCrypt.hashpw(userName, BCrypt.gensalt(12));

                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, generatedSecuredUsernameHash);
                statement.setString(2, generatedSecuredPasswordHash);
                //Execute query
                statement.executeUpdate();

                //Redirect admin to sign in
                request.getRequestDispatcher("/LoginAdmin").forward(request, response);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

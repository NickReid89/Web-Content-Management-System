/*
Author: Nickolas Reid
Date last modified: Oct 25th 2016

Purpose: This servlet serves a multitude of purposes. Namely it exploits the doGet 
         and doPost methods. To get to this page the admin first tries to access
         index.jsp. If the user is not logged in index.jsp sends the user to login.java
         namely to doGet. At doGet the meth does a quick query to see if an admin
         even exists. If the admin does not exist then the method spits out a generated
         html page for the user to sign up with. If the user does exist the doGet method
         generates a login page. The user then puts in their credentials. The doGet
         method then calls the doPost method. In the doPost method the method evaluates 
         the users credentials with the database. If it is a success a session key(cookie)
         is generated and the user is allowed into index.jsp. If the credentials are 
         incorrect the doPost method sends a generated login page that now informs 
         the user the need to write the proper username and password.
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

// Identifier tag for web pages.
@WebServlet(name = "LoginAdmin", urlPatterns = {"/LoginAdmin"})
public class Login extends HttpServlet {

    private Connection conn;
    private ResultSet rs;

    //Create the connector info in the constructor.
    public Login() throws SQLException, ClassNotFoundException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/adminDB", "root", "");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //If there are parameters.
        if (request.getParameter("loginAttempt") != null) {
            PrintWriter out = response.getWriter();
            //Check to see there is a username and password in case of corruption.
            if (request.getParameter("username") != null && request.getParameter("password") != null) {
                //Grab each
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                try {
                    //Always assume the worst.
                    boolean exists = false;
                    //Check to see if the user is valid
                    String sql = "SELECT * FROM admin";
                    String userName = username;
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    rs = stmt.executeQuery();
                    //Execute the query.
                    while (rs.next()) {
                        //Compare the username and password input to the database version.
                        if (BCrypt.checkpw(password, rs.getString("password")) && BCrypt.checkpw(userName, rs.getString("username"))) {
                           //There is a user!
                            exists = true;
                            
                        }
                    }
                    //user is validated here
                    if (exists == true) {
                        //Create a key for the user to gain permission to index.jsp
                        Cookie validateAdmin = new Cookie("validated", "true");
                        //Logs out after one hour.
                        //This is just an autolog out feature. The user only needs to shut the window and it'll log itself out
                        validateAdmin.setMaxAge(60 * 60 * 1);
                        //Add the cookie
                        response.addCookie(validateAdmin);
                        //Send the user to index.jsp
                        /*
                        NOTE: If the server does not use virtual hosts or SSL you only need request.getContextPath()
                              for some odd reason if you are using VH's and SSL you need request.getContextPath() + "/"
                              + request.getContextPath()
                        */
                        response.sendRedirect(request.getContextPath());

                        //If the user is not valid, send them an error page.
                    } else {
                        String logonTable = "<html>\n"
                                + "    <head>\n"
                                + "        <title>Log into Admin Portal</title>\n"
                                + "        <meta charset=\"UTF-8\">\n"
                                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                                + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/loginStyle.css\" />\n"
                                + "    </head>\n"
                                + "    <body>\n"
                                + "        <h1> Administrator Log In</h1>\n"
                                + "        <h1> Bad password!</h1>\n"
                                + "        <div class='left'></div>\n"
                                + "        <div class='right'></div>\n"
                                + "        <div class=\"SignIn fadeInRight\">\n"
                                + "\n"
                                + "            <form action='LoginAdmin'  method='POST'>\n"
                                + "                <h2 style='padding-left: 175px;'> name</h2> \n"
                                + "                <label>\n"
                                + "                    <input type='text' name='username'/>\n"
                                + "                </label>\n"
                                + "                <br>\n"
                                + "                <h2 style='padding-left: 160px;'>password</h2> \n"
                                + "                <label>\n"
                                + "                    <input type='password' name='password'/>                        \n"
                                + "                </label>\n"
                                + "                <label>\n"
                                + "                    <input type='hidden' name='loginAttempt' value ='loginAttempt'/>\n"
                                + "                </label>\n"
                                + "                <br><br>\n"
                                + "                <div class='submit'>\n"
                                + "                    <input type='submit' />\n"
                                + "                </div>\n"
                                + "\n"
                                + "            </form>\n"
                                + "        </div>\n"
                                + "\n"
                                + "\n"
                                + "    </body>\n"
                                + "</html>";
                        out.println(logonTable);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //If there no parameters something weird happened, send them to doGet to resolve it.
        } else {
            doGet(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Assumes there is no admin in the system.
        boolean adminExists = false;
        PrintWriter out = response.getWriter();
        try {
            String adminExistsSQL = "SELECT * FROM admin";
            PreparedStatement stmt = conn.prepareStatement(adminExistsSQL);
            rs = stmt.executeQuery();
            //If there is a result there must be an admin
            if (rs.next()) {
                adminExists = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Send the admin a login page
        if (adminExists) {
            String logonTable = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Log into Admin Portal</title>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/loginStyle.css\" />\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <h1> Administrator Log In</h1>\n"
                    + "        <div class='left'></div>\n"
                    + "        <div class='right'></div>\n"
                    + "        <div class=\"SignIn fadeInRight\">\n"
                    + "\n"
                    + "            <form action='LoginAdmin'  method='POST'>\n"
                    + "                <h2 style='padding-left: 175px;'> name</h2> \n"
                    + "                <label>\n"
                    + "                    <input type='text' name='username'/>\n"
                    + "                </label>\n"
                    + "                <br>\n"
                    + "                <h2 style='padding-left: 160px;'>password</h2> \n"
                    + "                <label>\n"
                    + "                    <input type='password' name='password'/>                        \n"
                    + "                </label>\n"
                    + "                <label>\n"
                    + "                    <input type='hidden' name='loginAttempt' value ='loginAttempt'/>\n"
                    + "                </label>\n"
                    + "                <br><br>\n"
                    + "                <div class='submit'>\n"
                    + "                    <input type='submit' />\n"
                    + "                </div>\n"
                    + "\n"
                    + "            </form>\n"
                    + "        </div>\n"
                    + "\n"
                    + "\n"
                    + "    </body>\n"
                    + "</html>\n"
                    + "";

            out.println(logonTable);
            //If there is no admin, send a sign up page
        } else {
            String signUpAdmin = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Add an Admin</title>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/loginStyle.css\" />\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <h1> User this form to create a username and password for admin </h1>\n"
                    + "        <div class='left'></div>\n"
                    + "        <div class='right'></div>\n"
                    + "        <div class=\"SignIn fadeInRight\">\n"
                    + "\n"
                    + "            <form action='SignUp'  method='POST'>\n"
                    + "                <h2 style='padding-left: 175px;'> name</h2> \n"
                    + "                <label>\n"
                    + "                    <input type='text' name='username'/>\n"
                    + "                </label>\n"
                    + "                <br>\n"
                    + "                <h2 style='padding-left: 160px;'>password</h2> \n"
                    + "                <label>\n"
                    + "                    <input type='password' name='password'/>                        \n"
                    + "                </label>\n"
                    + "                <br><br>\n"
                    + "                <div class='submit'>\n"
                    + "                    <input type='submit' />\n"
                    + "                </div>\n"
                    + "\n"
                    + "            </form>\n"
                    + "        </div>\n"
                    + "\n"
                    + "\n"
                    + "    </body>\n"
                    + "</html>\n"
                    + "";

            out.println(signUpAdmin);
        }
    }

}

/*
Author: Nickolas Reid
Date last modified: Oct 25 2016

Purpose: The purpose of this servlet is to take in parameters from index.jsp used
         to modify a specific item in a certain section.
 */
package JavaFiles;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//Tag for index.jsp to use
@WebServlet("/modify")
@MultipartConfig(maxFileSize = 32354430) // Max image size
public class ModifyItem extends HttpServlet {

    // database connection settings
    public Connection conn;
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb";
    private final String dbUser = "root";
    private final String dbPass = "";

    //Create database connection
    public ModifyItem() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //Grab all the parameters from the modify form.
            String[] x = request.getParameterValues("modify");
            //Set the first part of the sql statement.
            String table = "UPDATE menuitems SET ";
            //Set the where clause of the sql statement.
            String where = "WHERE ID = '" + (request.getParameter("menuItems")) + "'";
            InputStream inputStream = null;

            //This is the guts of the sql statement.
            String values = "";
            for (int i = 0; i < x.length; i++) {
                switch (x[i]) {
                    case "itemTitle":
                        values += "itemTitle = " + ((x.length > 1) ? "? ," : "? ");
                        break;
                    case "Price":
                        values += "Price = " + ((i < x.length - 1) ? "? ," : "? ");
                        break;
                    case "Image":
                        values += "Image = " + ((i < x.length - 1) ? "? ," : "? ");
                        break;
                    case "foodType":
                        values += "foodType = " + ((i < x.length - 1) ? "? ," : "? ");
                        break;
                    case "Description":
                        values += "Description = ? ";
                        break;
                    default:
                        break;
                }
            }

            //build the sql statement. 
            String SQLStatement = table + values + where;
            //Create a prepared statement for the update
            PreparedStatement statement = conn.prepareStatement(SQLStatement);
            //The counter is used to determine where the statements are put in the sql statement.
            int counter = 1;
            for (String x1 : x) {
                switch (x1) {
                    case "itemTitle":
                        statement.setString(counter, request.getParameter("menu"));
                        counter++;
                        break;
                    case "Price":
                        statement.setString(counter, request.getParameter(x1 + "Input"));
                        counter++;
                        break;
                    case "Image":
                        Part filePart = request.getPart("Image");
                        if (filePart != null) {
                            inputStream = filePart.getInputStream();
                        }
                        if (inputStream != null) {
                            statement.setBlob(counter, inputStream);
                        }
                        counter++;
                        break;
                    case "foodType":
                        statement.setString(counter, request.getParameter("menu"));
                        counter++;
                        break;
                    case "Description":
                        statement.setString(counter, request.getParameter(x1 + "Input"));
                        counter++;
                        break;
                    default:
                        break;
                }
            }

            // sends the statement to the database server
            statement.executeUpdate();
            //Refreshed home page to show that it's done
            response.sendRedirect(request.getContextPath());
        } catch (SQLException ex) {
            Logger.getLogger(ModifyItem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

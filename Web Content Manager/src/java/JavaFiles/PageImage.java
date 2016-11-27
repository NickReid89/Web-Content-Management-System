/*
Author: Nickolas Reid
Date last modified: Oct 25 2016

Purpose: To upload a section image to the database.
 */
package JavaFiles;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

//Page identified
@WebServlet("/PageImage")
@MultipartConfig(maxFileSize = 32354430) // upload file up to 16MB
public class PageImage extends HttpServlet {
    // database connection settings
    private final Connection conn;
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb";
    private final String dbUser = "root";
    private final String dbPass = "";
    private boolean imageLoaded = false;

    //Set up connection to DB
    public PageImage() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        InputStream inputStream = null;

        // obtains the image
        Part filePart = request.getPart("ImageInput");
        if (filePart != null) {

            // put the image in the input stream
            inputStream = filePart.getInputStream();
            imageLoaded = true;
        }
        //Assume worst case for results
        boolean results = false;
        try {
            //create sql statement
            String sql = "SELECT * FROM coresitedata WHERE page = '" + request.getParameter("menuItems") + "'";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                results = true;
            }
        } catch (SQLException sql) {
        }

        if (!results && imageLoaded) {
            try {

                String sql = "INSERT INTO coresitedata (image, Page) values (?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                if (inputStream != null) {
                    // fetches input stream of the upload file for the blob column
                    statement.setBlob(1, inputStream);
                }
                statement.setString(2, request.getParameter("menuItems"));
                int row = statement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(PageImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {

                String sql = "UPDATE coresitedata set image = ? WHERE page = '" + request.getParameter("menuItems") + "'";
                PreparedStatement statement = conn.prepareStatement(sql);
                if (inputStream != null) {
                    // fetches input stream of the upload file for the blob column
                    statement.setBlob(1, inputStream);
                }
                int row = statement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(PageImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // forwards to the message page
        response.sendRedirect(request.getContextPath());

    }
}

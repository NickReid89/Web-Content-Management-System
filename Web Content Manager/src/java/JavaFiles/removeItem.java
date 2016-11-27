/*
Author: Nickolas Reid
Last date modified: Oct 26th 2016

Purpose: The purpose of this servlet is to remove an item from the database. This
         is a little complicated and requires a few queries. First the item to be 
         removed needs to be used in a select query. We need to grab the section
         it belongs to. Next we need to see if it's the last item left. If it is
         we need to destroy the entire section. Next we need to remove all the 
         reviews associated with it.
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

@WebServlet("/remove")
public class removeItem extends HttpServlet {

    public static final String deleteMenuItem = "DELETE FROM menuitems WHERE ID = ?";
    public static final String deleteMenuComments = "DELETE FROM comment WHERE itemID = ?";
    public static final String ifLastItem = "SELECT COUNT(*) FROM menuitems where foodType = ? ";
    public static final String getPageData = "SELECT foodType FROM menuitems where ID = ? ";
    public static final String deleteMenuSectionData = "DELETE FROM CORESITEDATA WHERE PAGE = ? ";
    public Connection conn;
    // database connection settings
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb";
    private final String dbUser = "root";
    private final String dbPass = "";

    public removeItem() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Grab parameter
        String menuItemID = request.getParameter("menuItems");
        PreparedStatement preparedStmt;
        String page = "";
        //Used to determine how many items are left.
        int count = Integer.MAX_VALUE;
        try {
            preparedStmt = conn.prepareStatement(getPageData);
            preparedStmt.setInt(1, Integer.parseInt(menuItemID));
          //Find which section it belongs to
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                page = rs.getString("foodType");
            }
            preparedStmt = conn.prepareStatement(ifLastItem);
            preparedStmt.setString(1, page);
            //Check if last item
            rs = preparedStmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            //If it is delete the section
            if (count == 1) {
                preparedStmt = conn.prepareStatement(deleteMenuSectionData);
                preparedStmt.setString(1, page);
                preparedStmt.execute();
            }
            //Delete the item
            preparedStmt = conn.prepareStatement(deleteMenuItem);
            preparedStmt.setInt(1, Integer.parseInt(menuItemID));
            preparedStmt.execute();
            //Delete the reviews
            preparedStmt = conn.prepareStatement(deleteMenuComments);
            preparedStmt.setInt(1, Integer.parseInt(menuItemID));
            preparedStmt.execute();
            //redirect to home page.
            response.sendRedirect(request.getContextPath());
        } catch (SQLException ex) {
            Logger.getLogger(removeItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

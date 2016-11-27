/*
Author: Nickolas Reid
Date last modified: Oct 26 2016

Purpose: The purpose of this servlet is to be called via AJAX to request a certain
         amount of data to be returned.The data is used to fill fields where a user
         may wish to modify an item. 
*/



package JavaFiles;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//Web identified
@WebServlet(name = "GetMenuItems", urlPatterns = {"/GetMenuItems"})
public class GetMenuItems extends HttpServlet {

    //Connect to DB
    public GetMenuItems() throws SQLException, ClassNotFoundException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Class.forName("com.mysql.jdbc.Driver");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //Use this to send response to AJAX
        PrintWriter out = response.getWriter();
        try {
            String items = "";
            //Find out what the admin needs
            String foodType = request.getParameter("id");

            //Sql for sendsing query
            String sql;
            
            //Use this to build query
            switch (foodType) {
                case "all":
                    sql = "SELECT Itemtitle,Price FROM menuitems";
                    break;
                case "foodType":
                    sql = "SELECT DISTINCT foodType FROM menuitems";
                    break;
                case "foodTypeRemove":
                    sql = "SELECT DISTINCT foodType FROM menuitems";
                    break;

                case "pages":
                    sql = "SELECT DISTINCT PAGE FROM coresitedata";
                    break;
                default:
                    sql = "SELECT Itemtitle, id FROM menuitems WHERE foodType ='" + foodType + "'";
                    break;
            }
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/clientsDB", "root", "")) {
                Statement stmt;

                
                //Set up the query
                stmt = conn.createStatement();
                //set up how the data will be structured
                switch (foodType) {
                    case "all":
                        items += "<table>";
                        break;
                    case "foodType":
                        items += "<select name='menu' >";
                        break;
                    case "foodTypeRemove":
                        items += "<select name='menuRemove' onchange='getMenuItems(this.value)' onclick='getMenuItems(this.value)'>";
                        break;
                    case "pages":
                        items += "<select name='menuItems'>";

                        break;
                    default:
                        items += "<select name='menuItems' onchange='getMenuItemReviews(this.value)' onclick='getMenuItemReviews(this.value)'>";
                        break;
                }

                try (ResultSet rs = stmt.executeQuery(sql)) {
                 
                    while (rs.next()) {
                        //Retrieve data by column name
                        switch (foodType) {
                            case "all":
                                items += "<tr><td><input type='checkbox' name='sales' value='" + rs.getString("itemTitle") + "' id='itemTitle'/><label for='itemTitle'>" + rs.getString("ItemTitle") + "</label></td><td><input placeholder='" + rs.getString("Price") + "' type='number' name='Price' min='1' max='1000' step='any' ></td></tr>";
                                break;
                            case "foodType":
                                items += "<option name='foodType' value='" + rs.getString("foodType") + "'>" + rs.getString("foodType") + "</option>";
                                break;
                            case "foodTypeRemove":
                                items += "<option name='foodTypeRemove' value='" + rs.getString("foodType") + "'>" + rs.getString("foodType") + "</option>";
                                break;
                            case "pages":
                                items += "<option value='" + rs.getString("PAGE") + "'>" + rs.getString("PAGE") + "</option>";
                                break;
                            default:
                                items += "<option value='" + rs.getString("id") + "'>" + rs.getString("Itemtitle") + "</option>";
                                break;
                        }
                    }
                }
                if (foodType.equals("all")) {
                    items += "</table>";
                } else {
                    items += "</select>";
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetMenuItems.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Send the data to admin
            out.write(items);
        } catch (ClassNotFoundException ex) {
            out.write(ex.getMessage());
        }
    }
}

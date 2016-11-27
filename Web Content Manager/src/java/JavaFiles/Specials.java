/*
Author: Nickolas Reid
Date last modified: Oct 26th 2016

Purpose: The purpose of this servlet is to turn on and off specials and set prices
         for the item. It's pretty straight forward except for setting the special.
         the special is a boolean value in the database. to exploit this I set the
         special to special - 1. The reasoning behind this is the boolean is a bit 
         value, a 0 or a 1. Since I know this and I know a 1 is on sale, then 1 - 1
         = 0 so therefore it turns the sale off. Additionally 0 - 1 is not -1 since
         bits roll over. Therefore 0 - 1 is actually +1 and therefore sets it on sale.
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

//Web identifier
@WebServlet("/specials")
public class Specials extends HttpServlet {

    // database connection settings
    private final String dbURL = "jdbc:mysql://localhost:3306/clientsdb?autoReconnect=true";
    private final String dbUser = "root";
    private final String dbPass = "";



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

            // The values that set a sale on or off
            
            String[] items = request.getParameterValues("sales");
            
            for(String s : items){
                System.out.println("SPECIAL NUMBER: " + s );    
            }
            // The values that set a new price.
            String[] price = request.getParameterValues("Price");
            for (int i = 0; i < items.length; i++) {

                PreparedStatement preparedStmt;
                /*
            For this loop it might seem really strange that I'm not including a price only statement. However, this is not the 
            place to set the price, that would be in the ModifyItem.java file and it will be assumed that it was a typo, so 
            therefore ignored.
                 */
                try {
                    //If there is no price to set.
                    
                    if (price[i].equals("")) {
                        preparedStmt = conn.prepareStatement("UPDATE menuitems set OnSale = !OnSale WHERE itemTitle ='" + items[i].replaceAll("'", "''") + "'");
                        //If there is a price to set
                    } else {
                        preparedStmt = conn.prepareStatement("UPDATE menuitems set Price = " + price[i] + ", OnSale = !OnSale WHERE itemTitle ='" + items[i].replaceAll("'", "''") + "'");

                    }
                    //  execute the preparedstatement
             
                    preparedStmt.execute();

                } catch (SQLException ex) {
                    try {
                        conn.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(Specials.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    Logger.getLogger(ModifyItem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            try {
                //Gotta close the connection
                conn.close();
            } catch (SQLException ex) {
                System.out.println("Connection was already closed");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Specials.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Redirect the user to the home page
        response.sendRedirect(request.getContextPath());
    }
}

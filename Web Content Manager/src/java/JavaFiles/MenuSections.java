/*
Author: Nickolas Reid
Date last modifed: Oct 26 2016

Purpose: This servlet is what generates the header bar on page load. While,
         home logs and Piwik are static links the menu sections are not and must
         be queried against the DB. In the case that there are no menu items a 
         predefined link is used to create a "new item" section.

 */
package JavaFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static javax.servlet.SessionTrackingMode.URL;

public class MenuSections {

    public String returnMenuSections() throws IOException {
        String items = "<li><a id=\"home\" href=\"#\" class=\"tablinks\" onclick=\"openOption(event, 'homeBox');makeGetRequest('all');\">Home</a></li>";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/clientsDB", "root", "");
            Statement stmt;

            stmt = conn.createStatement();

            String sql = "SELECT DISTINCT foodType FROM menuitems";
            ResultSet rs = stmt.executeQuery(sql);
            boolean itemsExist = false;
            while (rs.next()) {

                itemsExist = true;
                items += "<li id='Section'><a class='tablinks' onclick='visibleSections()' id='" + rs.getString("foodType") + "' href=\"#" + rs.getString("foodType") + "\">" + rs.getString("foodType") + "</a></li>";

            }
            rs.close();

            conn.close();

            if (itemsExist == false) {
                items += "<li><a href='#' class='tablinks' onclick=\"openOption(event, 'addBox')\">Add New Item</a></li>";
            }

            items += piwikURL()
                    + "<li style=\"float:right\"><a class=\"tablinks\" onclick=\"openOption(event, 'serverErrorLogs');\"href=\"#\">Web Server Logs</a>\n"
                    + " <li style=\"float:right\"><a id=\"modifyCommentsTab\" href=\"#\" class=\"tablinks\" onclick=\"openOption(event, 'modifyComments');makeGetRequest('foodTypeRemove');\">moderate reviews</a></li>\n";
            //return sections
            return items;
        } catch (ClassNotFoundException | SQLException e) {
            return "dmb not connect to DB - Error:" + e;
        }
    }

    public String piwikURL() throws MalformedURLException, IOException {

        URL piwikIp = new URL("http://checkip.amazonaws.com");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(piwikIp.openStream()));
            String ip = br.readLine();
            String piwikURL = "href=\"https://" + ip + ":444\">Data Analytics </a>";
            return "<li style=\"float:right\"><a class=\"tablinks\" " + piwikURL + "</li>\n";

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

    }
}

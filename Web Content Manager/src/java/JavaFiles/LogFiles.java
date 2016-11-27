/*
Author: Nickolas Reid
Last date modified: Oct 25th 2016

Purpose: The purpose of this servlet is to gather all of the log files on the local disk that the server is located at. The server
         then sends the log files to the admin in a readable format.
 */
package JavaFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getLogs")
public class LogFiles extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("log") != null) {
            System.out.println(request.getParameter("log") + "************************");
            String log = "C:\\tomcat\\logs\\" + request.getParameter("log");
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(log));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                /*
            grab the lines within the file and read them into a buffer line by line. Note, this method is not perfect.
            If the file size becomes megabytes big it creates significant performance issues. 
                 */
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    //seperate for easy reading.
                    sb.append("<hr>");
                    line = br.readLine();
                }
                String everything = sb.toString();

                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(everything);

            } catch (FileNotFoundException ex) {

                log = "C:\\wamp64\\logs\\" + request.getParameter("log");
                br = new BufferedReader(new FileReader(log));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                /*
            grab the lines within the file and read them into a buffer line by line. Note, this method is not perfect.
            If the file size becomes megabytes big it creates significant performance issues. 
                 */
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    //seperate for easy reading.
                    sb.append("<hr>");
                    line = br.readLine();
                }
                String everything = sb.toString();

                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(everything);

            } catch (IOException ex) {
                Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public String getLogFile(String type) {

        String output = "<h2> Tomcat Logs </h2><hr>";
        File folder = new File("C:\\tomcat\\logs");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                output += "<details><summary onclick=\"getLog('" + listOfFile.getName() + "')\";>" + listOfFile.getName() + "</summary><h5 id='" + listOfFile.getName() + "'></h5></details>";
                System.out.println("File " + listOfFile.getName());
            }
        }

        output += "<hr><h2> Apache Logs </h2><hr>";
        folder = new File("C:\\wamp64\\logs");
        listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                output += "<details><summary onclick=\"getLog('" + listOfFile.getName() + "')\";>" + listOfFile.getName() + "</summary><h5 id='" + listOfFile.getName() + "'></h5></details>";
                System.out.println("File " + listOfFile.getName());
            }
        }
        return output;

    }
}

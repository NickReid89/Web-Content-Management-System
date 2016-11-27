<%-- 
    Document   : index.jsp
    Modified on : October 24, 2016
    Author     : Nickolas
    Purpose     : The purpose of this file is to act as the presentation layer and the portal for the adminstrative portion of the site
                  This one page is able to make seperate calls to the database, which returns appropriate data so that the adminstrator
                  is able to make changes to the client portion of the site in a whim. 
--%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.URL"%>
<%
    /*
    The purpose of this section is to kick out unauthorized users. If a user somehow discovered the url of this page
    or they tried to trick the login by disabling their cookies they would be immediately kicked out and pushed to the 
    login page. 
     */
    Cookie cookie = null;
    Cookie[] cookies = null;
    // Get the array of Cookies
    cookies = request.getCookies();
    boolean valid = false;
    if (cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];
            //Admin validated.
            if (cookie.getName().equals("validated") && cookie.getValue().equals("true")) {
                valid = true;
            }
        }
        //There are no cookies allowing a user access, thus kicking them to the login page.
        if (valid == false) {
            request.getRequestDispatcher("/LoginAdmin").forward(request, response);
        }
        //There are no cookies, therefore the user is not validated.
    } else {
        request.getRequestDispatcher("/LoginAdmin").forward(request, response);
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import ="JavaFiles.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Administrator Panel</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <script type="text/javascript" src="js/tabs.js"></script>
        <script type="text/javascript" src="js/AjaxReviews.js"></script> 
        <script type="text/javascript" src="js/visibleSections.js"></script>
        <script type="text/javascript" src="js/validation.js"></script>
        <script type="text/javascript" src="js/AjaxUpdate.js"></script>
        <script type="text/javascript" src="js/AjaxLogs.js"></script>
    </head>
    <body>
        <!-- 
        Meant for writing out the menu sections(home, about, contact, ect) dynamically.
        -->
        <ul id="menu">
            <%
                out.write(new MenuSections().returnMenuSections());
            %>
        </ul>
        <!--
        This is where the magic happens. For the tag "tab" it is in the class "hideTab" as a way to hide the tab upon page load
        If this wasn't so it could cause an error in the case that a user had no items added yet. In addition, in the tab are add,delete, and modify
        These options open a modal that allow a user to modify the contents in a section in an intuitive manner. 
        -->
        <div class="workSpace" id="workSpace" >

            <ul class="hideTab" id="tab">
                <li class="options"><a href="#" class="tablinks" onclick="openOption(event, 'addBox')">Add</a></li>
                <li class="options"><a href="#" class="tablinks" onclick="openOption(event, 'deleteForm'), makeGetRequest('foodSection')">Delete</a></li>
                <li class="options"><a href="#" class="tablinks" onclick="openOption(event, 'modifyForm'), makeGetRequest('foodType')">Modify</a></li>
                </li>
            </ul>

            <!--
            This is where none items are edited. For example, each section has an image and a summary. This is where the 
            image and summary are input. In addition there is also sales that can be turned on and off.
            -->
            <div id="homeBox" class="tabcontent">
                <details >
                    <summary>Change the specials</summary>
                    <form action="${pageContext.request.contextPath}/specials" onsubmit="return validateSpecials(this)" method="post">
                        <fieldset>
                            <legend>Please check or uncheck which are specials and add price</legend>
                            <div id="specials"></div>
                            <input type="submit" value="Submit!!" />
                        </fieldset>
                    </form>
                </details>
                <script>
                    makeGetRequest('pageType');
                </script>

                <details >
                    <summary> Change the logo for a page</summary>
                    <form action="${pageContext.request.contextPath}/PageImage" enctype="multipart/form-data" id="import" method="post">
                        <fieldset>
                            <legend> Change the description or image of the front page</legend>
                            <div id="imageIndex"></div>
                            <input type="file" name="ImageInput" accept="image/*"></br>
                            <input type="submit" value="Submit!!" />
                        </fieldset>
                    </form>
                </details>
                <details>
                    <summary>Change the front page summary</summary>
                    <form action="${pageContext.request.contextPath}/PageDescription"  method="post">
                        <fieldset>
                            <legend>Change the description on the front page</legend>
                            <div id="descriptionIndex"></div>
                            <textarea rows="4" cols="50" maxlength="500" name="DescriptionInput">Please enter a description for the menu page.
                            </textarea></br>
                            <input type="submit" value="Submit!!" />
                        </fieldset>
                    </form>
                </details>
            </div>
            <!--
            This section allows a user to add an item or new section entirely. However to add a new section
            a user needs to add a new item as a new section without an item it doesn't make sense. In addition
            the form has form validation through JS and then passed to a servlet.
            -->
            <div id="addBox" class="tabcontent">
                <form name="addBox" onsubmit="return validateAddBox()"action="${pageContext.request.contextPath}/add" enctype="multipart/form-data" id="import" method="post">
                    <fieldset>
                        <legend>ADD - Please fill in all fields!</legend>
                        <table>

                            <tr>
                                <td>
                                    <label for="itemTitleAdd">Menu Item: </label>
                                    <input type="text" name="itemTitle" placeholder="Title of Food"/> </td>
                                <td>
                                    <label for="Image">Image: </label>
                                    <input type="file" name="Image" accept="image/*">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="Price">Price: </label>
                                    <input type="number" name="Price" min="1" max="1000" step="any">
                                </td>
                                <td>

                                    <label for="foodType">Menu Category: </label>
                                    <input type="text" placeholder="Pizza" id="foodType" name="menuItems" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">

                                    <label for="Description">Description: </label>
                                    <textarea rows="4" cols="50" maxlength="500" name="Description"></textarea>
                                </td>

                            </tr>
                        </table>
                        <input type="submit" value="Submit!!" />
                    </fieldset>
                </form>
            </div>
            <!--
            In this form the browser presents the administrator with a list of items from the current section they are in.
            From there they can choose to delete items. By deleted an item it also deletes all the comments associated with it.
            If the admin deletes the last item the section also deleted. 
            -->

            <div id="deleteForm" class="tabcontent">
                <form action="${pageContext.request.contextPath}/remove" method="post">
                    <fieldset>
                        <legend>Which menu item do you want to delete?</legend>
                        <div id="choices"></div>
                        <input type="submit" value="Submit" />
                    </fieldset>
                </form>
            </div>

            <!--
            This loads with the various comments of the items. This is not section specific so an administrator can
            quickly scan through all items and modify all comments directly in the modal presented.
            -->

            <div id="modifyComments" class="tabcontent">
                <div id="chooseFoodType"></div>
                <br/>    
                <div id="chooseMenuItem"></div>
                <div id="removeReviews"></div>

            </div> 
            <!--
            This hefty modal/form is used to modify section items. When a section item is added, all fields are able to be edited
            with the exception with the name of the menu item. 
            -->
            <div id="modifyForm" class="tabcontent">
                <fieldset>
                    <legend>EDIT -Check and fill in fields you wish to edit</legend>
                    <form name="modifyForm" onsubmit="return validateModifyBox()" action="${pageContext.request.contextPath}/modify" enctype="multipart/form-data" method="post">
                        <table>

                            <tr>
                                <td>
                                    <div id="menuItemModify"> 
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="checkbox" name="modify" value="Image" id="Image"/><label for="Image">Image</label>
                                    <input type="file" name="Image" id="modifyImage" accept="image/*">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="checkbox" name="modify" value="Price" id="Price"/><label for="Price">Price</label>
                                    <input type="number" name="PriceInput" min="1" max="1000" step="any">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="checkbox" name="modify" value="foodType" id="foodType"/><label for=foodType">Food Category</label>
                                    <div id="foodTypeModify"></div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <input type="checkbox" name="modify" value="Description" id="Description"/><label for="Description">Description</label>
                                    <textarea rows="4" cols="50" maxlength="500" name="DescriptionInput"> 
                                    </textarea>
                                </td>
                            </tr>
                        </table>
                        <input type="submit" value="Submit!!" />
                    </form>
                </fieldset>
            </div>


            <div id="serverErrorLogs" class="tabcontent">
                <%
                    out.write(new LogFiles().getLogFile("DBaccess"));
                %>
            </div>


        </div>
        <!-- this is the preview site for Admin -->
        <div class="siteExample" id="siteExample">
            <%
                URL piwikIp = new URL("http://checkip.amazonaws.com");
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(piwikIp.openStream()));
                    String ip = br.readLine();
                
                    out.write("<iframe class=\"preview\" src=\"https://" + ip + ":8444/Client\" width=\"900\" height=\"900\">");
                    out.write("<p>Your browser does not support iframes or the website is not online.</p>");
                    out.write("</iframe>");
                    
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            %>
        </div>

        <input type="hidden" name="userChoice" id="userChoice" value="Pizza">


    </body>
</html>

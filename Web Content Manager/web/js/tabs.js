/*
 * Author: Nickolas Reid
 * Date last modified: Oct 26 2016
 * 
 * Purpose: The purpose of this file is to reposition the window so that it can 
 *          fit the appropriate data on the screen. In this instance the default
 *          screen is split vertically 50/50 One side for changing items, one side
 *          as a preview screen. When dealing with logs this doesn't make any sense
 *          and compressing log files to a tight place makes no sense. So this file
 *          then hides the preview window, and the work menu and then expands the log
 *          window.
 */

function openOption(evt, option) {

    /*
     * If the window is piwik(data analytics) or a log file window.
     */
    if (option === "piwik" || option === "serverErrorLogs") {
        document.getElementById("workSpace").style.width = '100vw';
        document.getElementById("siteExample").style.visibility = "hidden";
        document.getElementById("siteExample").style.width = '0vw';
        
    } else {
        document.getElementById("workSpace").style.width = '50vw';
        document.getElementById("siteExample").style.visibility = "visible";
        document.getElementById("siteExample").style.width = '50vw';
    }

    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the link that opened the tab
    document.getElementById(option).style.display = "block";
    evt.currentTarget.className += " active";


}
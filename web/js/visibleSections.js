/*
 * Author: Nickolas Reid
 * Date last modified: Oct 26 2016
 * 
 * Purpose: The purpose of this file is to take the input from the header bar and
 *          decide whether to make options visible or not.
 */


//When a user clicks this is called.
document.addEventListener('click', function (e) {

    e = e || window.event;
    var target = e.target || e.srcElement;

//If the user has clicked a section
    if (target.parentNode.id === "Section") {
        document.getElementsByName("userChoice")[0].value = target.id;
        //Make the options add,modify,delete visible.
        document.getElementById("tab").style.visibility = "visible";
        var nodes = document.getElementById("tab").childNodes;

        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].nodeName === "LI") {
                nodes[i].style.visibility = "visible";
            }
        }
        makeGetRequest("foodSection");

        //If it is a home or modify comments tab the user doesn't need the options
        //so hide them
    } else if (target.id === "home" || target.id === "modifyCommentsTab") {
        var nodes = document.getElementById("tab").childNodes;

        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].nodeName === "LI") {
                nodes[i].style.visibility = "hidden";
            }
        }
    }
}, false);


/*
 * Author: Nickolas Reid
 * Date last modified: Oct 26 2016
 * 
 * Purpose: The purpose of this file is to utilize the functionality of AJAX.
 *          The file creates a request object, uses it to send the data to the
 *          appropriate request area and then sends the response back to the 
 *          index.jsp file
 */
function createRequestObject() {
    var tmpXmlHttpObject;

    if (window.XMLHttpRequest) {
        // Other browsers use this
        tmpXmlHttpObject = new XMLHttpRequest();

    } else if (window.ActiveXObject) {
        // Internet Explorer uses this.
        tmpXmlHttpObject = new ActiveXObject("Microsoft.XMLHTTP");
    }

//Return object
    return tmpXmlHttpObject;
}

//call the above function to create the XMLHttpRequest object
var http = createRequestObject();

//Used to memorize the selection for ajax return
var selection = "";

function makeGetRequest(all) {
    //send off request
    selection = all;
    if (all === "all") {
        http.open('post', 'GetMenuItems?id=all');
    } else if (all === "foodType") {
        http.open('post', 'GetMenuItems?id=foodType');
    } else if (all === "foodSection") {
        http.open('post', 'GetMenuItems?id=' + document.getElementsByName("userChoice")[0].value);
    } else if (all === 'pageType') {
        http.open('post', 'GetMenuItems?id=pages');
    } else if (all === 'foodTypeRemove') {
        http.open('post', 'GetMenuItems?id=foodTypeRemove');
    }
//Send the results to processResponse
    http.onreadystatechange = processResponse;

    http.send(null);
}

function processResponse() {
//    //check if the response has been received from the server
    if (http.readyState === 4) {
//        //read and assign the response from the server
        var response = http.responseText;
        if (selection === "all") {
            document.getElementById('specials').innerHTML = response;
        } else if (selection === "foodType") {
            document.getElementById('foodTypeModify').innerHTML = response;
        } else if (selection === "foodSection") {
            document.getElementById('choices').innerHTML = response;
            document.getElementById('menuItemModify').innerHTML = response;
            document.getElementById('chooseMenuItem').innerHTML = response;
        } else if (selection === "pageType") {
            document.getElementById('descriptionIndex').innerHTML = response;
            document.getElementById('imageIndex').innerHTML = response;
        } else if (selection === "foodTypeRemove") {
            document.getElementById('chooseFoodType').innerHTML = response;
        }
    }
}

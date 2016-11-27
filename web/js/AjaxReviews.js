/*
 * Author: Nickolas Reid
 * Date last modifed: Oct 26 2016
 * 
 * Purpose: The purpose of this file is to update the reviews for the modify review section.
 *          Also DO NOT remove the timer functions. These are super important. Since
 *          two ajax calls get made so closely together there must be some time to allow 
 *          one to finish so another can begin. If this does not happen an ajax call
 *          being called before the first has finished is fully ignored with makes
 *          for some big issues.
 */



function getMenuItems(menuItem) {
    document.getElementById("userChoice").value = menuItem;
    document.getElementsByName("userChoice")[0].value = menuItem;
    makeGetRequest('foodSection');
    setTimeout(function () {
        console.log("|");
    }, 100);
}

function getMenuItemReviews(menuItemReviews) {
    setTimeout(function () {
        console.log("|");
    }, 100);
    //Makes an AJAX call.
    GrabReviews(menuItemReviews);
}

/*
 * Here we need an ajax function. The function will send a parameter and fill removeReviews with either some reviews
 * or no reviews. The items themselves will also need another ajax function to remove the item in front of the users eyes.
 */
function createRequestObject() {
    var tmpXmlHttpObject;

    //depending on what the browser supports, use the right way to create the XMLHttpRequest object
    if (window.XMLHttpRequest) {
        // Mozilla, Safari would use this method ...
        tmpXmlHttpObject = new XMLHttpRequest();

    } else if (window.ActiveXObject) {
        // IE would use this method ...
        tmpXmlHttpObject = new ActiveXObject("Microsoft.XMLHTTP");
    }

    return tmpXmlHttpObject;
}

//call the createRequestObject() method in AjaxUpdate.js 
var http = createRequestObject();

function GrabReviews(menuItem) {
    //make a connection to the server ... specifying that you intend to make a GET request 
    //to the server. Specifiy the page name and the URL parameters to send
    http.open('post', 'ReviewsToEdit?menuItem=' + menuItem);


    //assign a handler for the response
    http.onreadystatechange = DisplayReviews;

    //actually send the request to the server
    http.send(null);
}

function DisplayReviews() {
    //check if the response has been received from the server
    if (http.readyState === 4) {

        //read and assign the response from the server
        var response = http.responseText;

        document.getElementById('removeReviews').innerHTML = response;

    }
}

function RemoveReview(reviewID, reviewName) {
    http.open('post', 'RemoveReview?id=' + reviewID + ',' + reviewName);


    //assign a handler for the response
    http.onreadystatechange = DisplayReviews;

    //actually send the request to the server
    http.send(null);

}
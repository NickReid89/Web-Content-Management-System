
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

var elementToReturn;


function getLog(logName) {
    //make a connection to the server ... specifying that you intend to make a GET request 
    //to the server. Specifiy the page name and the URL parameters to send

    elementToReturn = logName;
    http.open('post', 'getLogs?log=' + logName);


    //assign a handler for the response
    http.onreadystatechange = returnLog;

    //actually send the request to the server
    http.send(null);
}

function returnLog() {
    //check if the response has been received from the server
    if (http.readyState === 4) {

        //read and assign the response from the server
        var response = http.responseText;

        document.getElementById(elementToReturn).innerHTML = response;

    }
}
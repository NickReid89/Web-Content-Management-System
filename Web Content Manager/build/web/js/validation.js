/*
 * Author: Nickolas Reid
 * Date last modified: Oct 26 2016
 * 
 * Purpose: To vlaidate all the forms on index.jsp
 */

function validateAddBox() {
    var response = "";
    var invalid = false;
    var x = document.forms["addBox"]["itemTitle"].value;
    if (x === null || x === "") {
        response += " - Please enter a name for the item\n\n";
        invalid = true;
    }
    x = document.forms["addBox"]["Image"].value;
    if (x === null || x === "") {
        response += " - Please upload an image of the item.\n\n";
        invalid = true;
    }
    x = document.forms["addBox"]["Price"].value;
    if (x === null || x === "") {
        response += " - You must enter a price for the item.\n\n";
        invalid = true;
    }
    x = document.forms["addBox"]["menuItems"].value;
    if (x === null || x === "") {
        response += " - You must put the item into a menu.\n\n";
        invalid = true;
    }
    x = document.forms["addBox"]["Description"].value;
    if (x === null || x === "") {
        response += " - You must give the item a description.\n\n";
        invalid = true;
    }
    if (invalid === false) {
        return true;
    } else {
        alert(response);
        return false;
    }
}

function validateModifyBox() {
    var response = "";
    var valid = true;

    var x = document.forms["modifyForm"]["modifyImage"].value;
    if (!(document.getElementById('Image').checked ^ (x === "") || x === null)) {

        response += "Please upload the image or click the check box.\n\n";
        valid = false;
    }
    x = document.forms["modifyForm"]["PriceInput"].value;
    if (!(document.getElementById('Price').checked ^ (x === "") || x === null)) {
        response += "Please enter a price or click the check box.\n\n";
        valid = false;

    }
    x = document.forms["modifyForm"]["DescriptionInput"].value;
    if (!(document.getElementById('Description').checked ^ (x === "") || x === null)) {
        response += "Please enter a description or click the check box.";
        valid = false;
    }

    if (valid === false) {
        alert(response);

        return valid;
    } else {
        return true;
    }

}

function validateSpecials(formToBeValidated) {
    var response = "";
    var valid = false;

    var elements = formToBeValidated.elements;
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].checked === true) {
            valid = true;

        }
    }
    if (valid === false) {
        alert(response);

        return valid;
    } else {
        return valid;
    }
}
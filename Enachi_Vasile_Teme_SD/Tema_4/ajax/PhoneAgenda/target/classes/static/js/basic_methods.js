$( document ).ready(function() {

    var url = window.location;

    // SUBMIT FORM
    $("#customerForm").submit(function(event) {
        // Prevent the form from submitting via the browser.
        event.preventDefault();

        switch($("#operation").val()) {
            case "post":
                ajaxPost();
                break;
            case "update":
                ajaxUpdate();
                break;
            case "delete":
                ajaxDelete();
                break;
            case "get":
                ajaxGet();
                break;
            default:
                break;
            // code block
        }
    });

    function ajaxUpdate() {
        let formData = {
            id : $("#id").val(),
            firstName : $("#firstname").val(),
            lastName :  $("#lastname").val(),
            telephoneNumber :  $("#telephone").val()
        }
        $.ajax({
            type : "PUT",
            contentType : "application/json",
            url : url + "/person/" + formData.id,
            data : JSON.stringify(formData),
            dataType : 'json',
            success : function(result){
                if(result.status == "OK"){
                    $("#postResultDiv").html("Succesfully updated:</br> ID = "+ result.data.id + "</br>FirstName = "
                        + result.data.firstName + "</br>LastName = " + result.data.lastName + "</br>Telephone = " + result.data.telephoneNumber);
                }else{
                    $("#postResultDiv").html("<strong>NOT FOUND</strong>");
                }
                console.log(result);
            },
            error : error_fun
        });
        // Reset FormData after Posting
        resetData();
    }

    function ajaxDelete() {
        let formData = {
            id : $("#id").val(),
        }
        $.ajax({
            type : "DELETE",
            contentType : "application/json",
            url : url + "/person/" + formData.id,
            data : JSON.stringify(formData),
            dataType : 'json',
            success : function(result){
                if(result.status == "OK"){
                    $("#postResultDiv").html("Succesfully deleted:</br> ID = "+ result.data.id + "</br>FirstName = "
                        + result.data.firstName + "</br>LastName = " + result.data.lastName + "</br>Telephone = " + result.data.telephoneNumber);
                }else{
                    $("#postResultDiv").html("<strong>NOT FOUND</strong>");
                }
                console.log(result);
            },
            error : error_fun
        });
        // Reset FormData after Posting
        resetData();
    }

    function ajaxGet() {
        let formData = {
            id : $("#id").val(),
        }
        $.ajax({
            type : "GET",
            contentType : "application/json",
            url : url + "/person/"+formData.id,
            dataType : 'json',
            success : function(result){
                if(result.status == "OK"){
                    $("#postResultDiv").html("FOUND:</br> ID = "+ result.data.id + "</br>FirstName = "
                        + result.data.firstName + "</br>LastName = " + result.data.lastName + "</br>Telephone = " + result.data.telephoneNumber);
                }else{
                    $("#postResultDiv").html("<strong>NOT FOUND</strong>");
                }
                console.log(result);
            },
            error : error_fun
        });
        // Reset FormData after Posting
        resetData();
    }

    function ajaxPost(){
        // PREPARE FORM DATA
        let formData = {
            id : $("#id").val(),
            firstName : $("#firstname").val(),
            lastName :  $("#lastname").val(),
            telephoneNumber :  $("#telephone").val()
        }

        // DO POST
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : url + "/person",
            data : JSON.stringify(formData),
            dataType : 'json',
            success : function(result){
            if(result.status == "OK"){
                $("#postResultDiv").html("Succesfully  created:</br> ID = "+ result.data.id + "</br>FirstName = "
                    + result.data.firstName + "</br>LastName = " + result.data.lastName + "</br>Telephone = " + result.data.telephoneNumber);
            }else{
                $("#postResultDiv").html("<strong>Error</strong>");
            }
            console.log(result);
        },
            error : error_fun
        });
        // Reset FormData after Posting
        resetData();
    }

    function resetData(){
        $("#id").val("");
        $("#firstname").val("");
        $("#lastname").val("");
        $("#telehone").val("");
    }

    function error_fun(e){
        alert("Error!")
        console.log("ERROR: ", e);
    }
})
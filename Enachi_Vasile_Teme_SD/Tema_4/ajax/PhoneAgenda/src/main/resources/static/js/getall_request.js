$( document ).ready(function() {

    var url = window.location;

    // GET REQUEST
    $("#getBtn").click(function(event){
        event.preventDefault();
        ajaxGet();
    });

    // DO GET
    function ajaxGet(){
        let formData = {
            id : $("#id").val(),
            firstName : $("#firstname").val(),
            lastName :  $("#lastname").val(),
            telephoneNumber :  $("#telephone").val()
        }
        $.ajax({
            type : "GET",
            url : url + "/agenda/" + "?firstName=" + formData.firstName
                + "&id=" + formData.id
                + "&lastName=" + formData.lastName
                + "&telephoneNumber=" + formData.telephoneNumber,
            success: function(result){

                if(result.status == "OK"){
                    $('#getResultDiv .list-group tr').remove();
                    var custList = "<tr>" +
                        "                    <th>ID</th>" +
                        "                    <th>Firstname</th>" +
                        "                    <th>Lastname</th>" +
                        "                    <th>Telephone</th></tr>>";
                    $('#getResultDiv .list-group').append(custList)
                    $.each(result.data, function(i, person){
                        var person = "<td>" + person.id + "</td>" +
                        "             <td>" + person.firstName + "</td>" +
                        "             <td>" + person.lastName + "</td>" +
                        "             <td>" + person.telephoneNumber + "</td>";
                        $('#getResultDiv .list-group').append('<tr><h5 class="list-group-item">'+person+'</h5></tr>')
                    });
                    console.log("Success: ", result);
                }else{
                    $("#getResultDiv").html("<strong>NOT FOUND</strong>");
                    console.log("Fail: result : ", result);
                }
            },
            error : function(e) {
                $("#getResultDiv").html("<strong>Error</strong>");
                console.log("ERROR: ", e);
            }
        });
    }
})
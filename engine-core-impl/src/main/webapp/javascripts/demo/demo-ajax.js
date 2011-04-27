$().ready(function() {

    $("#loginButton").click(function(){

        var uuid = $('#loginBox option:selected').attr('value');
        var name = $('#loginBox option:selected').html();
        $.Storage.set("participantUUID", uuid);
        $.Storage.set("participantName", name);
        $(location).attr('href', 'worklist/');
    });

    // GETs all the participants
    $.ajax({
        type: 'GET',
        url: 'api/identity/participants',
        success: function(data) {
            console.log(data);
            var participants = data;
            // add each participant to the loginbox
            $.each(participants, function(i, participant) {
                $("#loginBox").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#participants').html(jqXHR.responseText).addClass('error');
        },
        dataType: "json" // we expect json
    });
});


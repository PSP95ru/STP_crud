$(document).ready(function () {

    $("form").submit(function (event) {
        event.preventDefault();

        ajax_submit();

    });

});

function ajax_submit() {

    var ppvform = {};
    ppvform.name = $("#name").val();
    ppvform.date = $("#date").val();
    ppvform.id = null;
    if ($("#id").val() != null) {
        ppvform.id = $("#id").val();
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: $('form').prop('action'),
        data: JSON.stringify(ppvform),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            if (response.result == 0 ){
                //window.location.replace(response.nextAddr);
                window.location.href = ($("#go_home").attr("href"));
            }
            else {
                alert(response.msg);
            }
        },
        error: function (e) {

            var json = e.responseText;
            alert(json);
        }
    });

}
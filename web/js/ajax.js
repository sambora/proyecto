/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function getAjax(url,formId,callback){
    var h = url+'?'+$('#'+formId).serialize();
    $.ajax({
        type: "GET",
        url: h, 
        dataType: "json",
        success: function(retorno){
            callback(retorno);
//            $("#datos").empty();
//            //$("#datos").append("id=" + source["id"] + "<br/>" + "nombre=" + source["nombre"]);
//            $("#datos").append("id=" + source.id + "<br/>" + "nombre=" + source.nombre);
        },
        error: function(){
            console.log("error");
        }
    })
}

function sendForm(formId,divId,url)
{
    var h = url+'?'+$('#'+formId).serialize();
    ajaxChangeDivId(divId,h);
}
function plainSendForm(formId)
{
    document.forms[formId].submit();
}
function ajaxModal(url) {
    var params = {};
    $.get(url, params,
        function(data) {
            showModal(data);
        }
        );
}
function ajaxCreateDivId(id,url) {
    var params = {};
    $.get(url, params,
        function(data) {
            createDiv(id,data);
        }
        );
}
function ajaxGetJson(url){
    var params = {};
    $.get(url, params,
        function(data) {
            return data;
        }
        ); 
}
function ajaxChangeDivId(id,url) {
    var params = {};
    $.get(url, params,
        function(data) {
            document.getElementById(id).innerHTML = data;
        }
        );
}
function createDiv(id,data) {
    var divContainer = $('<div>').attr({
        id: id
    });
    $('body').append(divContainer);
    $('#'+id).append(data);
}
function showModal(data) {

    data += "<button onclick=\"closeModalWindow()\">Cerrar</button>";

    var bg = $('<div>').attr({
        className: 'curtain',
        id: 'curtain'
    });

    $('#curtain').css("width", $(window).width());
    $('#curtain').css("height", $(window).height());

    var mw = $('<div>').attr({
        className: 'modalwindow',
        id: 'modalwindow'
    });

    $('body').append(bg);
    $('body').append(mw);

    $('#modalwindow').append(data);

    $('#curtain').css("width", $(window).width());
    $('#curtain').css("height", $(window).height());

    //$('#modalwindow').css("width", 800+'px');
    //$('#modalwindow').css("height", 600+'px');

    var wleft = ($(window).width() - $('#modalwindow').width()) / 2;
    var wtop = ($(window).height() - $('#modalwindow').height()) / 2;
    
    $('#modalwindow').css("left", wleft+'px');
    $('#modalwindow').css("top", wtop+'px');
}
function closeModalWindow() {
    $('#modalwindow').fadeOut('slow', function ()
    {
        $('#modalwindow').remove();
    }
    );
    $('#curtain').remove();
}
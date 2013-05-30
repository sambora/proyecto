/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




$(document).ajaxStart(function(){ 
    $("#data").empty(); 
    $("#data").html('<img src="img/ajax-loading.gif" alt="cargando..." />');
});

function createDiv(id,data) {
    var divContainer = $('<div>').attr({
        id: id
    });
    $('body').append(divContainer);
    $('#'+id).append(data);
}

function procesaAjax(direccion,funcion){
    $.ajax({
        url: direccion,
        //data: "nocache=" + Math.random(),
        type: "GET",
        dataType: "json",
        beforeSend: function () {            
        },
        success: function(source){        
            $("#data").empty(); 
            funcion(source);
        },
        error: function(dato){
            $("#data").empty(); 
            $("#data").append("ERROR en la recepción de datos de clientes");
        }
    });  
}

function showObjeto(source){
    $("#data").append("<p>" + source['id'] + " " + source['nombre'] + "</p><hr/>");         
}

function showLista(source){     
    $.each(source['list'], function(index) {
        $("#data").append('<p>1: index: ' + index + ' id: ' + source['list'][index]['id']  + ' nombre: ' + source['list'][index]['nombre'] + '</p><hr/>');
    });
    $.each(source['list'], function(index, value) {
        $("#data").append('<p>2: index: ' + index + ' id: ' + value['id']  + ' nombre: ' + value['nombre'] + '</p><hr/>');
    });
    $.each(source['list'], function(index, value) {
        showObjeto(value);
    });
}

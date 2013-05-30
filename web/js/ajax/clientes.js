/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    createDiv("data","");

    procesaAjax("/facturacionajax01/clientes.json",showLista);					
});
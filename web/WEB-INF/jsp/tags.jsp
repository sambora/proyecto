<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>

<div class="span3">
    <br />
    Ordenar por:<br />
    <select id="ordenCombo" name="selCombo" size=1> 
        <option value="asc">Ascendente</option>
        <option value="desc">Descendente</option>
    </select>

</div>
<div class="span8">
    <div id="datos"><img src="img/ajax-loading.gif" /></div>
</div>

<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Blog</h3>
    </div>
    <div class="modal-body"></div>
    <div class="modal-footer">
        <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Cerrar</button> 
        <div id="result"></div>
    </div>
</div>

<script>
    $(document).ready(function() {
        
        
        if("<%=request.getAttribute("usuario")%>" != "null"){
            iniciar(1, 10, "asc", <%=request.getAttribute("usuario")%>)
        }else{
            iniciar(1, 10, "asc")
        }
        $("#ordenCombo").unbind()
        $("#ordenCombo").bind("change", function(){
            iniciar(1, $('#rpp').val(), $("#ordenCombo").val())
        })
        
    });

    function iniciar(pag, rpp, orden, condicion){
        var promise = ajaxCallASync('/ProyectoBlog2/listarTags/'+pag+'/'+rpp+'/'+orden+'/'+condicion, 'GET', '' );
        promise.success(function (data) {
            listarTags(data, condicion)
        });
        promise.error(function (){
            alert('Error de conexión "listarBlogs()"')
        });
    }
    
    //Realiza el listado de las Entradas
    function listarTags(data, condicion){
        var codigo = "<center><h3>Todos los Tags</h3></center><hr />"
        if(data.list.length == 0){
            codigo = "<hr />No hay tags<hr />"
        }
        for(var i = 0; i < data.list.length; i++){
            codigo = codigo
                + '<a href="entradas/'+data.list[i].id+'">' + data.list[i].tags +'</a>'
            
            codigo = codigo + '<hr />'
            
        }
        $("#datos").empty()
        $("#datos").append(codigo)
        $('#datos').append('<div id="configurarPaginas" class="form-inline">'
            +'Resultados por página: <input type="text" id="rpp" />'
            +'<button id="paginar" class="btn">Paginar</button></div>')
        $('#rpp').val(data.rpp);
        $('#paginar').click(function(){
            if(!(/^\d+$/).test($('#rpp').val())){alert("El rpp debe ser un entero")}else{
                iniciar(1, $('#rpp').val(), $("#ordenCombo").val())
            }
        });
        $("#datos").append(getNeighborhood("", data.currentPage, data.totalPages, 2))
        $(".pagination_link").click(function(){
            event.preventDefault();
            var rpp = 10;
            if((/^\d+$/).test($('#rpp').val())){rpp = $('#rpp').val()}
            iniciar($(this).attr('id'), rpp, $("#ordenCombo").val())
        });
        
    };
    
        
</script>
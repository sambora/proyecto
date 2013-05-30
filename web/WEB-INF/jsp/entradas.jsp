<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>

<div class="span3">
    <br />
    <%
        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("user") != null) {
    %>
    <button id="crearEntrada" class="btn">Crear entrada</button><br /><br />
    <script>
        $(document).ready(function() {
                       
            $("#crearEntrada").click(function(){
                var promise = ajaxCallSync('/ProyectoBlog2/pedirArchivo/entradasForm', 'GET','' );
                promise.success(function (formulario) {
                    $('#myModal .modal-body').empty()     
                    $('#myModal .modal-body').append(formulario)
                });
                promise.error(function () {alert('Error de conexión "mostrarFormulario()"');});
                
                var numBlogs = 0;
                var promise = ajaxCallSync('/ProyectoBlog2/listarBlogs/1/500000/asc/'+"<%=sesion.getAttribute("id")%>", 'GET', '' );
                promise.success(function (data) {
                    numBlogs = data.list.length
                    for(var i = 0; i < data.list.length; i++){
                        $('#comboBlogs').append(new Option(data.list[i].nombre, data.list[i].id));
                    }
                });
                promise.error(function (){
                    alert('Error de conexión "listarBlogs()"')
                });
                
                if(!numBlogs > 0){
                    alert("Primero debes crear un blog!")
                    return;
                }
                
                $("#myModal").modal()
                
                $("#mostrarTags").click(function(){
                    event.preventDefault()
                    var promise = ajaxCallSync('/ProyectoBlog2/listarTags/1/500000', 'GET','' );
                    promise.success(function (datos) {
                        $("#myModalTags .modal-body").empty()
                        var codigo = ""
                        for(var i = 0; i < datos.list.length; i++){
                            codigo = codigo + datos.list[i].tagsEnlaces + ", "
                        }
                        codigo = codigo.slice(0, -2)
                        $("#myModalTags .modal-body").append(codigo)
                        $("#myModalTags .enlacesTags").click(function(){
                            event.preventDefault()
                            if($("#myModal #tags").val() == ""){
                                $("#myModal #tags").val($("#myModal #tags").val() + $(this).text())
                            }else{
                                $("#myModal #tags").val($("#myModal #tags").val() +", "+ $(this).text())
                            }
                        });
                        $("#myModalTags").modal()
                    });
                    promise.error(function () {alert('Error de conexión "mostrarTags()"');});
                });
                
                
                $("#submitEntradaForm").click(function(){
                    event.preventDefault()
                    $('#entradaForm #fecha_creacion').val(fechaActual())
                    var serForm = {json:JSON.stringify($('#entradaForm').serializeObject())};
                    var promise = ajaxCallSync('/ProyectoBlog2/crearEntrada', 'GET', serForm );
                    promise.success(function (datos) {
                        alert('Guardado')
                        $('#myModal').modal("hide")
                        $("#ordenCombo").val("desc")
                        iniciar(1, 10, "desc")
                    });
                    promise.error(function () {alert('Error de conexión "crearBlog"');});
                });
                
            });
        });
    </script>
    <%        }
    %>
    Ordenar por fecha:<br />
    <select id="ordenCombo" name="selCombo" size=1> 
        <option value="desc">Más recientes</option>
        <option value="asc">Más antiguas</option>
    </select>

</div>
<div class="span8">
    <div id="datos"><img src="/ProyectoBlog2/img/ajax-loading.gif" /></div>
</div>

<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Entrada</h3>
    </div>
    <div class="modal-body"></div>
    <div class="modal-footer">
        <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Cerrar</button> 
        <div id="result"></div>
    </div>
</div>

<!-- Modal3 -->
<div id="myModalTags" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-body">
        <h3>Tags existentes</h3>

    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Cerrar</button> 
        <div id="result"></div>
    </div>
</div>

<script>

    $(document).ready(function() {
        if("<%=request.getAttribute("id")%>" != "null"){
            iniciar(1, 10, "desc", "<%=request.getAttribute("id")%>")
        }else{
            iniciar(1, 10, "desc")
        }
    });
    
    function iniciar(pag, rpp, orden, condicion, tag){
        var promise = ajaxCallASync('/ProyectoBlog2/listarEntradas/'+pag+'/'+rpp+'/'+orden+'/'+condicion+'/'+tag , 'GET', '' );
        promise.success(function (data) {
            listadoEntradas(data, condicion, tag)
            
            $(".enlacesTags").click(function(){
                event.preventDefault()
                var tag = $(this).text()
                iniciar(1, $('#rpp').val(), $("#ordenCombo").val(), condicion, tag)
            });
            
            if("<%=request.getAttribute("id")%>" != "null"){
                $("#ordenCombo").unbind()
                $("#ordenCombo").bind("change", function(){
                    iniciar(1, $('#rpp').val(), $("#ordenCombo").val(), "<%=request.getAttribute("id")%>", tag)
                })
            }else{
                $("#ordenCombo").unbind()
                $("#ordenCombo").bind("change", function(){
                    iniciar(1, $('#rpp').val(), $("#ordenCombo").val(), "undefined", tag)
                })
            }
        });
        promise.error(function (){
            alert('Error de conexión al intentar realizar el listado de entradas inicial')
        });
    }
    
    //Realiza el listado de las Entradas
    function listadoEntradas(data, condicion, tag){
        var codigo = ""
        if("<%=request.getAttribute("id")%>" != "all"){
            var promise = ajaxCallSync('/ProyectoBlog2/blog/'+<%=request.getAttribute("id")%>, 'GET','' );
            promise.success(function (datos) {
                codigo = "<h1><center><a href=\"\" id=\"tituloBlog\">"+ datos.nombre+"</a></center></h1>";
            });
            promise.error(function () {codigo = "<h1><center>Error al obtener el nombre del blog</center></h1>";});
        }
        
        if(!data.list.length > 0){
            codigo = codigo + "En este blog aun no se ha publicado nada!";
        }
            
        if(tag != undefined){
            var lecturaFiltro = "Mostrando resutlados con el tag: <span id=\"tagActual\">" + tag + "</span>"
            codigo = codigo + lecturaFiltro
        }
        codigo = codigo + "<hr />"
        var idBlogsUsuario = []
        var promise = ajaxCallSync('/ProyectoBlog2/listarBlogs/1/500000/asc/'+"<%=sesion.getAttribute("id")%>", 'GET', '' );
        promise.success(function (data) {
            for(var i = 0; i < data.list.length; i++){
                idBlogsUsuario.push(data.list[i].id)
            }
        });
        for(var i = 0; i < data.list.length; i++){
            var edicion = ""
            if(data.list[i].fecha_edicion != undefined && data.list[i].fecha_edicion != ""){
                edicion = '<br />Última edición: ' + data.list[i].fecha_edicion
            }
            codigo = codigo
                + '<h3>' + data.list[i].titulo
                + '</h3>' + data.list[i].texto
                + '<br /><br />Por: ' + data.list[i].autor
                + '<br />Creado el: ' + data.list[i].fecha_creacion
                + edicion
                + '<br /></br /><span class="tags">Tags: ' + data.list[i].tagsEnlaces+ '</span>'
            
            promise.error(function (){
                alert('Error de conexión "listarBlogs()"')
            });
            for(var n = 0; n < idBlogsUsuario.length; n++){
                if(data.list[i].id_blog == idBlogsUsuario[n]){
                    codigo = codigo + '<br /><br /><button value="' + data.list[i].id + '" class="editarEntrada btn">Editar</button> '
                        + '<button value="' + data.list[i].id + '" class="borrarEntrada btn">Borrar</button>'
                }
            }
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
                iniciar(1, $('#rpp').val(), $("#ordenCombo").val(), condicion, tag)
            }
        });
        $("#datos").append(getNeighborhood("", data.currentPage, data.totalPages, 2))
        $(".pagination_link").click(function(){
            event.preventDefault();
            var rpp = 10;
            if((/^\d+$/).test($('#rpp').val())){rpp = $('#rpp').val()}
            iniciar($(this).attr('id'), rpp, $("#ordenCombo").val(), condicion, tag)
        })
        $("#tituloBlog").click(function(){
            event.preventDefault()
            iniciar(1, 10, "desc", <%=request.getAttribute("id")%>)
        })
        
        
        $(".borrarEntrada").click(function(){
            var idEntrada = $(this).val()
            var promise = ajaxCallSync('/ProyectoBlog2/pedirArchivo/entradasForm', 'GET','' );
            promise.success(function (formulario) {
                $('#myModal .modal-body').empty()     
                $('#myModal .modal-body').append(formulario)
            });
            promise.error(function () {alert('Error de conexión "mostrarFormulario()"');});

            var promise = ajaxCallSync('/ProyectoBlog2/entrada/' + idEntrada, 'GET','' );
            promise.success(function (datos) {
                $('#titulo').val(datos.titulo)
                $('#texto').val(datos.texto)
                $('#tags').val(datos.tags)
                $('#comboBlogs').append(new Option(datos.nombre_blog, ""));
                $('#titulo').attr("disabled", "")
                $('#texto').attr("disabled", "")
                $('#tags').attr("disabled", "")
                $('#comboBlogs').attr("disabled", "")

                $("#submitEntradaForm").click(function(){
                    event.preventDefault()
                    var promise = ajaxCallSync('/ProyectoBlog2/borrarEntrada/'+ idEntrada, 'GET');
                    promise.success(function (datos) {
                        alert('Borrado')
                        $("#myModal").modal("hide")
                        iniciar(1, 10, "desc")
                    });
                    promise.error(function () {alert('Error de conexión "borrarBlog"');});
                });
            });
            promise.error(function () {alert('Error de conexión "borrarBlog.click()"');});
            $("#myModal").modal()
        })
        
        $(".editarEntrada").click(function(){
            var idEntrada = $(this).val()
            var promise = ajaxCallSync('/ProyectoBlog2/pedirArchivo/entradasForm', 'GET','' );
            promise.success(function (formulario) {
                $('#myModal .modal-body').empty()     
                $('#myModal .modal-body').append(formulario)
            });
            promise.error(function () {alert('Error de conexión "mostrarFormulario()"');});

            $("#mostrarTags").click(function(){
                event.preventDefault()
                var promise = ajaxCallSync('/ProyectoBlog2/listarTags/1/500000', 'GET','' );
                promise.success(function (datos) {
                    $("#myModalTags .modal-body").empty()
                    var codigo = ""
                    for(var i = 0; i < datos.list.length; i++){
                        codigo = codigo + datos.list[i].tagsEnlaces + ", "
                    }
                    codigo = codigo.slice(0, -2)
                    $("#myModalTags .modal-body").append(codigo)
                    $("#myModalTags .enlacesTags").click(function(){
                        event.preventDefault()
                        if($("#myModal #tags").val() == ""){
                            $("#myModal #tags").val($("#myModal #tags").val() + $(this).text())
                        }else{
                            $("#myModal #tags").val($("#myModal #tags").val() +", "+ $(this).text())
                        }
                    });
                    $("#myModalTags").modal()
                });
                promise.error(function () {alert('Error de conexión "mostrarTags()"');});
            });
            
            var promise = ajaxCallSync('/ProyectoBlog2/entrada/' + idEntrada, 'GET','' );
            promise.success(function (datos) {
                
                $('#titulo').val(datos.titulo)
                $('#texto').val(datos.texto)
                $('#tags').val(datos.tags)
                
                var promise = ajaxCallSync('/ProyectoBlog2/listarBlogs/1/500000/asc/'+"<%=sesion.getAttribute("id")%>", 'GET', '' );
                promise.success(function (data) {
                    for(var i = 0; i < data.list.length; i++){
                        $('#comboBlogs').append(new Option(data.list[i].nombre, data.list[i].id));
                    }
                });
                
                $("#submitEntradaForm").click(function(){
                    event.preventDefault()
                    $('#entradaForm #id').val(datos.id)
                    $('#entradaForm #fecha_creacion').val(datos.fecha_creacion)
                    $('#entradaForm #fecha_edicion').val(fechaActual())
                    var serForm = {json:JSON.stringify($('#entradaForm').serializeObject())};
                    var promise = ajaxCallSync('/ProyectoBlog2/crearEntrada', 'GET', serForm);
                    promise.success(function (datos) {
                        alert('Guardado')
                        $("#myModal").modal("hide")
                        iniciar(1, 10, "desc")
                    });
                    promise.error(function () {alert('Error de conexión "editarEntrada"');});
                });
            
                promise.error(function (){
                    alert('Error de conexión "listarBlogs()"')
                });
                
            });
            promise.error(function () {alert('Error de conexión "editarEntrada.click()"');});
            
            
            $("#myModal").modal()
        });
        
    };

</script>

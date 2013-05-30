<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>

<div class="span3">
    <br />
    <%
        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("user") != null) {
    %>
    <button id="misBlogs" class="btn">Mis Blogs</button><br /><br />
    <button id="crearBlog" class="btn">Crear Blog</button><br /><br />
    <script>
        $(document).ready(function() {
            $("#misBlogs").click(function(){
                $("#ordenCombo").val("desc")
                iniciar(1, 10, "desc", "<%=sesion.getAttribute("id")%>")
                $("#ordenCombo").unbind()
                $("#ordenCombo").bind("change", function(){
                    iniciar(1, $('#rpp').val(), $("#ordenCombo").val(), "<%=sesion.getAttribute("id")%>")
                })
            });
            
            $("#crearBlog").click(function(){
                
                $("#myModal").modal()
                var promise = ajaxCallSync('/ProyectoBlog2/pedirArchivo/blogsForm', 'GET','' );
                promise.success(function (formulario) {
                    $('#myModal .modal-body').empty()     
                    $('#myModal .modal-body').append(formulario)
                });
                promise.error(function () {alert('Error de conexión "mostrarFormulario()"');});
                
                $("#submitBlogForm").click(function(){
                    event.preventDefault()
                    $('#blogForm #id_usuario').val("<%=sesion.getAttribute("id")%>")
                    var serForm = {json:JSON.stringify($('#blogForm').serializeObject())};
                    var promise = ajaxCallSync('/ProyectoBlog2/crearBlog', 'GET', serForm );
                    promise.success(function (datos) {
                        alert('Guardado')
                        $('#myModal').modal("hide")
                        $("#ordenCombo").val("desc")
                        iniciar(1, 10, "desc", "<%=sesion.getAttribute("id")%>")
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
        <option value="asc">Más antiguos</option>
    </select>

</div>
<div class="span8">
    <div id="datos"><img src="/ProyectoBlog2/img/ajax-loading.gif" /></div>
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
        
        iniciar(1, 10, "desc", "<%=request.getAttribute("usuario")%>")
        
        $("#ordenCombo").unbind()
        $("#ordenCombo").bind("change", function(){
            iniciar(1, $('#rpp').val(), $("#ordenCombo").val(), "<%=request.getAttribute("usuario")%>")
        })
        
    });

    function iniciar(pag, rpp, orden, condicion){
        var promise = ajaxCallASync('/ProyectoBlog2/listarBlogs/'+pag+'/'+rpp+'/'+orden+'/'+condicion, 'GET', '' );
        promise.success(function (data) {
            listarBlogs(data, condicion)
        });
        promise.error(function (){
            alert('Error de conexión "listarBlogs()"')
        });
    }
    
    //Realiza el listado de las Entradas
    function listarBlogs(data, condicion){
        var codigo = "<center><h3>Todos los Blogs</h3></center><hr />"
        if(condicion == "<%=sesion.getAttribute("id")%>"){
            codigo = "<center><h3>Tus blogs</h3></center><hr />"
        }
        if(data.list.length == 0){
            codigo = "<hr />No hay blogs<hr />"
        }
        else if("<%=request.getAttribute("usuario")%>" != "all"){
            codigo = "<center><h3>Blogs de "+data.list[0].nombre_usuario+"</h3></center><hr />"
        }
        for(var i = 0; i < data.list.length; i++){
            codigo = codigo
                + '<a href="/ProyectoBlog2/entradas/'+data.list[i].id+'">' + data.list[i].nombre +'</a><br />'
                + data.list[i].informacion + '<br />'
                + '<br />Por: ' +data.list[i].nombre_usuario
            
            if (condicion == "<%=sesion.getAttribute("id")%>") {
                codigo = codigo + '<br /><br /><button class="borrarBlog btn" value="'+data.list[i].id +'">Borrar</button>'
                    + ' <button class="editarBlog btn" value="'+data.list[i].id +'">Editar</button>'
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
                iniciar(1, $('#rpp').val(), $("#ordenCombo").val(), "<%=request.getAttribute("usuario")%>")
                
            }
        });
        $("#datos").append(getNeighborhood("", data.currentPage, data.totalPages, 2))
        $(".pagination_link").click(function(){
            event.preventDefault();
            var rpp = 10;
            if((/^\d+$/).test($('#rpp').val())){rpp = $('#rpp').val()}
            iniciar($(this).attr('id'), rpp, $("#ordenCombo").val(), "<%=request.getAttribute("usuario")%>")
        })
        
        $(".borrarBlog").click(function(){
            var idBlog = $(this).val()
            var promise = ajaxCallSync('/ProyectoBlog2/pedirArchivo/blogsForm', 'GET','' );
            promise.success(function (formulario) {
                $('#myModal .modal-body').empty()     
                $('#myModal .modal-body').append(formulario)
            });
            promise.error(function () {alert('Error de conexión "mostrarFormulario()"');});
                
            var promise = ajaxCallSync('/ProyectoBlog2/blog/' + idBlog, 'GET','' );
            promise.success(function (datos) {
                $('#nombre').val(datos.nombre)
                $('#informacion').val(datos.informacion)
                $('#nombre').attr("disabled", "")
                $('#informacion').attr("disabled", "")

                $("#submitBlogForm").click(function(){
                    event.preventDefault()
                    var promise = ajaxCallSync('/ProyectoBlog2/borrarBlog/'+ idBlog, 'GET');
                    promise.success(function (datos) {
                        alert('Borrado')
                        $("#myModal").modal("hide")
                        iniciar(1, 10, "desc", "<%=sesion.getAttribute("id")%>")
                    });
                    promise.error(function () {alert('Error de conexión "borrarBlog"');});
                });
            });
            promise.error(function () {alert('Error de conexión "borrarBlog.click()"');});
            $("#myModal").modal()
        });
        
        $(".editarBlog").click(function(){
            var idBlog = $(this).val()
            var promise = ajaxCallSync('/ProyectoBlog2/pedirArchivo/blogsForm', 'GET','' );
            promise.success(function (formulario) {
                $('#myModal .modal-body').empty()     
                $('#myModal .modal-body').append(formulario)
            });
            promise.error(function () {alert('Error de conexión "mostrarFormulario()"');});

            var promise = ajaxCallSync('/ProyectoBlog2/blog/' + idBlog, 'GET','' );
            promise.success(function (datos) {
                $('#id').val(datos.id)
                $('#id_usuario').val(<%=sesion.getAttribute("id")%>)
                $('#nombre').val(datos.nombre)
                $('#informacion').val(datos.informacion)

                $("#submitBlogForm").click(function(){
                    event.preventDefault()
                    var serForm = {json:JSON.stringify($('#blogForm').serializeObject())};
                    var promise = ajaxCallSync('/ProyectoBlog2/editarBlog', 'GET', serForm);
                    promise.success(function (datos) {
                        alert('Guardado')
                        $("#myModal").modal("hide")
                        iniciar(1, 10, "desc", "<%=sesion.getAttribute("id")%>")
                    });
                    promise.error(function () {alert('Error de conexión "editarBlog"');});
                });
            });
            promise.error(function () {alert('Error de conexión "editarBlog.click()"');});
            $("#myModal").modal()
        })
    };
    
        
</script>
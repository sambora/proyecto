<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>

<div class="span3">
    <br />
    <%
        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("user") != null) {
    %>
    <button id="editarUsuario" class="btn">Editar Usuario</button><br /><br />
    <button id="borrarUsuario" class="btn">Borrar Usuario</button><br /><br />
    <script>
        $(document).ready(function() {
            
            $("#editarUsuario").click(function(){
                var promise = ajaxCallSync('/ProyectoBlog2/usuario', 'GET','' );
                promise.success(function (datos) {
                    $('#id').val(datos.id)
                    $('#nombre').val(datos.nombre)
                    $('#email').val(datos.email)
                    $('#informacion').val(datos.informacion)
                    $('#nombre').removeAttr("disabled")
                    $('#email').removeAttr("disabled")
                    $('#pass').removeAttr("disabled")
                    $('#informacion').removeAttr("disabled")
                    
                    $("#submitUsuarioForm").click(function(){
                        event.preventDefault()
                        if($("#usuarioForm #pass").val() != ""){
                            var serForm = {json:JSON.stringify($('#usuarioForm').serializeObject())};
                            var promise = ajaxCallSync('/ProyectoBlog2/editarUsuario', 'GET', serForm );
                            promise.success(function (datos) {
                                alert('Guardado')
                                $('#myModalForm').modal("hide")
                                iniciar(1, $("#rpp").val(), $("#ordenCombo").val())
                            });
                            promise.error(function () {alert('Error de conexión "crearUsuario"');});
                        }else{
                            alert("Reescribe tu contraseña o cámbiala")
                        }
                    });
                });
                promise.error(function () {alert('Error de conexión "editarUsuario.click()"');});
                $("#myModalForm").modal()
            })
            
            $("#borrarUsuario").click(function(){
                var promise = ajaxCallSync('/ProyectoBlog2/usuario', 'GET','' );
                promise.success(function (datos) {
                    $('#id').val(datos.id)
                    $('#nombre').val(datos.nombre)
                    $('#email').val(datos.email)
                    $('#informacion').val(datos.informacion)
                    $('#nombre').attr("disabled", "")
                    $('#email').attr("disabled", "")
                    $('#pass').attr("disabled", "")
                    $('#informacion').attr("disabled", "")
                    
                    $("#submitUsuarioForm").click(function(){
                        event.preventDefault()
                        var serForm = {json:JSON.stringify($('#usuarioForm').serializeObject())};
                        var promise = ajaxCallSync('/ProyectoBlog2/borrarUsuario/'+ datos.id, 'GET', serForm );
                        promise.success(function (datos) {
                            alert('Borrado')
                            $(location).attr('href',"index");
                        });
                        promise.error(function () {alert('Error de conexión "borrarUsuario"');});
                    });
                });
                promise.error(function () {alert('Error de conexión "editarUsuario.click()"');});
                $("#myModalForm").modal()
            })

        });
    </script>
    <%        } else {
    %>
    ¿No tienes cuenta?<br /><br />
    <button id="crearUsuario" class="btn">Crear usuario</button><br /><br />
    <script>
        $(document).ready(function() {
            $("#crearUsuario").click(function(){
                $('#usuarioForm').each (function(){
                    this.reset();
                });
                $("#myModalForm").modal()
            })
            
            $("#submitUsuarioForm").click(function(){
                event.preventDefault()
                $('#usuarioForm #id').val("0")
                var serForm = {json:JSON.stringify($('#usuarioForm').serializeObject())};
                var promise = ajaxCallSync('/ProyectoBlog2/crearUsuario', 'GET', serForm );
                promise.success(function (datos) {
                    alert('Guardado')
                    $('#myModalForm').modal("hide")
                    iniciar(1, $("#rpp").val(), $("#ordenCombo").val())
                });
                promise.error(function () {alert('Error de conexión "crearUsuario"');});
            });
        });
    </script>
    <%            }
    %>
    Ordenar por:<br />
    <select id="ordenCombo" name="selCombo" size=1> 
        <option value="asc">Ascendente</option>
        <option value="desc">Descendente</option>
    </select>

</div>
<div class="span8">
    <div id="datos"><img src="img/ajax-loading.gif" /></div>
</div>

<jsp:include page='usuariosForm.jsp' />


<script>
    $(document).ready(function() {
        iniciar(1, 10, "asc")
        $("#ordenCombo").change(function(){
            iniciar(1, $('#rpp').val(), $("#ordenCombo").val())
        })
    });

    function iniciar(pag, rpp, orden){
        var promise = ajaxCallASync('/ProyectoBlog2/listarUsuarios/'+pag+'/'+rpp+'/'+orden, 'GET', '' );
        promise.success(function (data) {
            listarUsuarios(data)
        });
        promise.error(function (){
            alert('Error de conexión "listarUsuarios()"')
        });
    }

    //FUNCIONES DE USO GENERAL
    //
    //Realiza el listado de las Entradas
    function listarUsuarios(data){
        var codigo = "<hr />"
        for(var i = 0; i < data.list.length; i++){
            codigo = codigo
                + '<h4><a href="blogs/'+ data.list[i].id +'">' + data.list[i].nombre + '</a></h4>'
                + data.list[i].informacion
                + '<hr />'
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
        })
    };   
    
</script>
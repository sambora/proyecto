<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
    <!--<![endif]-->
    <head>
        <meta charset="charset=ISO-8859-1">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Blog</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="/ProyectoBlog2/css/bootstrap.min.css">
        <style>
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
        </style>
        <link rel="stylesheet" href="/ProyectoBlog2/css/bootstrap-responsive.min.css">
        <link rel="stylesheet" href="/ProyectoBlog2/css/main.css">

        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="/ProyectoBlog2/js/vendor/jquery-1.8.3.min.js"><\/script>')</script>
        <script src="/ProyectoBlog2/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>

    </head>
    <body>
        <!--[if lt IE 7]>
        <p class="chromeframe">You are using an outdated browser. <a href="http://browsehappy.com/">Upgrade your browser today</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to better experience this site.</p>
        <![endif]-->
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <div class="row">
                        <div class="span1"><a class="brand" href="/ProyectoBlog2/index">Inicio</a></div>
                        <div class="span1"><a class="brand" href="/ProyectoBlog2/usuarios">Usuarios</a></div>
                        <div class="span1"><a class="brand" href="/ProyectoBlog2/blogs/all">Blogs</a></div>
                        <div class="span1"><a class="brand" href="/ProyectoBlog2/entradas/all">Entradas</a></div>
                        <div class="span1"><a class="brand" href="/ProyectoBlog2/tags">Tags</a></div>
                        <%
                            HttpSession sesion = request.getSession();
                            if (sesion.getAttribute("user") == null) {
                        %>
                        <div class="offset1 span6">
                            <form id="loginForm" class="form-horizontal" name="loginForm" action="json">
                                <input type="text" id="user" name="user" />
                                <input type="text" id="pass" name="pass" />
                                <button type="submit" id="submitLoginForm" class="btn">Conectarse</button>
                            </form>
                        </div>
                        <script>
                            $(document).ready(function() {
                                cargarEventoLogin()
                            });
                        </script>
                        <%                            } else {%>
                        <div class="offset1 span6 usuario">
                            Conectado con <%=session.getAttribute("user")%> - <a href="/ProyectoBlog2/logout">Desconectarse</a>
                        </div>
                        <%                            }%>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row-fluid">
                <jsp:include page='<%=(String) request.getAttribute("contenido")%>' />
            </div>
        </div>


        <script>
            
            function cargarEventoLogin(){
                $("#submitLoginForm").click(function(){
                    event.preventDefault()
                    var user = $("#user").val()
                    var pass = $("#pass").val()
                    
                    if($("#user").val() != "" && $("#pass").val() != ""){
                        var promise = ajaxCallASync('/ProyectoBlog2/login/'+user+'/'+pass, 'GET', '' );
                        promise.success(function (data) {
                            if(data.list[0] != null){
                                $(location).attr('href',"index");
                            }else{
                                alert("Correo o contraseña incorrectos")
                            }
                        });
                        promise.error(function (){
                            alert('Error en la conexión "cargarEventoLogin()"')
                        });
                    }else{
                        alert("No puedes dejar ningún campo vacío")
                    }
                })
            }
            
            //FUNCIONES DE USO GENERAL
            
            //Llamadas ajax
            function ajaxCallSync(url, type, data) {return $.ajax({type: type,async: false,url: url,data: data});};
            function ajaxCallASync(url, type, data) {return $.ajax({type: type,url: url,data: data});};
        
            //Obtiene e imprime las paginas con la vencidad
            function getNeighborhood(link,  page_number, total_pages, neighborhood) { 
                page_number=parseInt(page_number);
                total_pages=parseInt(total_pages);
                neighborhood=parseInt(neighborhood);
                vector = "<div class=\"pagination\"><ul>";
                if (page_number > 1)
                    vector+=("<li><a class=\"pagination_link\" id=\"" + (page_number - 1) + "\" href=\"" + link + (page_number - 1) + "\">prev</a></li>");
                if (page_number > neighborhood + 1)
                    vector+=("<li><a class=\"pagination_link\" id=\"1\" href=\"" + link + "1\">1</a></li>");
                if (page_number > neighborhood + 2)
                    vector+=("<li>" + "<a href=\"#\">...</a>" + "</li>");
                for (i = (page_number - neighborhood); i <= (page_number + neighborhood); i++){
                    if (i >= 1 && i <= total_pages){
                        if (page_number == i){
                            vector+=("<li class=\"active\"><a class=\"pagination_link\" id=\"" + i + "\" href=\"" + link +     i + "\">" + i + "</a></li>");
                        }
                        else
                            vector+=("<li><a class=\"pagination_link\" id=\"" + i + "\" href=\"" + link + i + "\">" + i + "</a></li>");
                    }
                }
                if (page_number < total_pages - (neighborhood + 1))
                    vector+=("<li>" + "<a href=\"#\">...</a>" + "</li>");
                if (page_number < total_pages - (neighborhood))
                    vector+=("<li><a class=\"pagination_link\" id=\"" + total_pages + "\" href=\"" + link + total_pages + "\">" + total_pages + "</a></li>");
                if (page_number < total_pages)
                    vector+=("<li><a class=\"pagination_link\"  id=\"" + (page_number + 1) + "\" href=\"" + link + (page_number + 1) + "\">next</a></li>");        
                vector += "</ul></div>";
                return vector;
            }
            
            //Serializar formulario
            $.fn.serializeObject = function(){
                var o = {};
                var a = this.serializeArray();
                $.each(a, function() {
                    if (o[this.name] !== undefined) {
                        if (!o[this.name].push) {
                            o[this.name] = [o[this.name]];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
                });
                return o;
            };
            
            //Devuelve la fecha actual en formato dd/mm/yyyy
            function fechaActual(){
                var fullDate = new Date()
                var twoDigitMonth = ((fullDate.getMonth().length+1) === 1)? (fullDate.getMonth()+1) : '0' + (fullDate.getMonth()+1);
                var currentDate = fullDate.getDate() + "/" + twoDigitMonth + "/" + fullDate.getFullYear();
                return currentDate;
            }
        
        </script>

        <!-- /container -->
        <script src="/ProyectoBlog2/js/vendor/bootstrap.min.js"></script>
        <script src="/ProyectoBlog2/js/main.js"></script>
        <script src="/ProyectoBlog2/js/ajax.js"></script>
    </body>
</html>
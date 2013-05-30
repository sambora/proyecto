<%@page import="java.util.List"%>
<%@page import="blog.bean.UsuariosBean"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%
    UsuariosBean usuario = (UsuariosBean) request.getAttribute("usuario");
    String data = new Gson().toJson(usuario);
    out.print(data);
%>
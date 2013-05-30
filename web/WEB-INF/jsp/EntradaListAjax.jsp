<%@page import="blog.bean.EntradasBean"%>
<%@page import="java.util.List"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%
    List<EntradasBean> listaEntradas = (List<EntradasBean>) request.getAttribute("entradas");
    String data = new Gson().toJson(listaEntradas);
    String rpp = new Gson().toJson(request.getAttribute("rpp").toString());
    String paginaActual = new Gson().toJson(request.getAttribute("currentPage").toString());
    String paginasTotal = new Gson().toJson(request.getAttribute("totalPages").toString());
    out.print("{\"list\":" + data + ",\"rpp\":" + rpp + ",\"currentPage\":" + paginaActual + ",\"totalPages\":" + paginasTotal + "}");
%>